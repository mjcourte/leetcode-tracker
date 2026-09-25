package com.example.leetcodetracker.importer.neetcode;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.example.leetcodetracker.importer.CurriculumDefinition;
import com.example.leetcodetracker.importer.ModuleDefinition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class NeetCodeScraper {

    public List<CurriculumDefinition> scrape(String url) throws IOException {
        try {
            System.out.println("Before connect...");

            String javascript = Jsoup.connect(url).timeout(10_000).ignoreContentType(true).execute().body();
            System.out.println("After connect...");

            int start = javascript.indexOf("O=[");
            int end = javascript.indexOf("],", start);

            String problemData = javascript.substring(start + 2, end + 1);
            List<NeetCodeProblem> problems = parseProblems(problemData);

            // long neetcode150 = problems.stream()
            // .filter(NeetCodeProblem::neetcode150)
            // .count();

            // long blind75 = problems.stream()
            // .filter(NeetCodeProblem::blind75)
            // .count();

            // long neetcode250 = problems.stream()
            // .filter(NeetCodeProblem::neetcode250)
            // .count();

            // System.out.println("NC150: " + neetcode150);
            // System.out.println("Blind75: " + blind75);
            // System.out.println("NC250: " + neetcode250);

            CurriculumDefinition b75 = buildCurriculum(problems, "Blind 75", NeetCodeProblem::blind75);
            CurriculumDefinition nc150 = buildCurriculum(problems, "NeetCode 150", NeetCodeProblem::neetcode150);
            CurriculumDefinition nc250 = buildCurriculum(problems, "NeetCode 250", NeetCodeProblem::neetcode250);

            return List.of(b75, nc150, nc250);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<NeetCodeProblem> parseProblems(String problemData) {
        List<NeetCodeProblem> problems = new ArrayList<>();

        String[] objects = problemData
                .substring(1, problemData.length() - 1)
                .split("\\},\\{");

        Pattern pattern = Pattern.compile(
                "problem:\"([^\"]+)\".*?"
                        + "pattern:\"([^\"]+)\".*?"
                        + "link:\"([^\"]+)\".*?"
                        + "difficulty:\"([^\"]+)\"");

        for (String object : objects) {
            Matcher matcher = pattern.matcher(object);

            if (!matcher.find()) {
                throw new IllegalStateException("Could not parse: " + object);
            }

            problems.add(new NeetCodeProblem(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4),
                    object.contains("neetcode150:!0"),
                    object.contains("blind75:!0"),
                    object.contains("neetcode250:!0")));
        }

        return problems;
    }

    private CurriculumDefinition buildCurriculum(
            List<NeetCodeProblem> problems,
            String name,
            Predicate<NeetCodeProblem> membership) {
        Map<String, List<NeetCodeProblem>> byPattern = problems.stream()
                .filter(membership)
                .collect(Collectors.groupingBy(
                        NeetCodeProblem::pattern,
                        LinkedHashMap::new,
                        Collectors.toList()));

        List<ModuleDefinition> modules = byPattern.entrySet()
                .stream()
                .map(entry -> new ModuleDefinition(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(NeetCodeProblem::link)
                                .toList()))
                .toList();

        return new CurriculumDefinition(name, modules);
    }

}
