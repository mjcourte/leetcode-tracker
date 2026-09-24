package com.example.leetcodetracker.problem;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/problems")
public class ProblemWebController {

    private final ProblemService problemService;
    private final ProblemSubmissionService problemSubmissionService;
    private final TagRepository tagRepository;

    public ProblemWebController(ProblemService problemService, ProblemSubmissionService problemSubmissionService,
            TagRepository tagRepository) {
        this.problemService = problemService;
        this.problemSubmissionService = problemSubmissionService;
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public String listProblems(
            @RequestParam(required = false) ProblemDifficulty difficulty,
            @RequestParam(required = false) List<String> tags,
            Model model) {

        List<String> tagSlugs = tags == null ? List.of() : tags;

        List<Tag> allTags = tagRepository.findAll();
        Map<String, List<String>> tagsWithAddition = new HashMap<>();

        for (Tag tag : allTags) {
            List<String> newTags = new ArrayList<>(tagSlugs);

            if (!newTags.contains(tag.getSlug())) {
                newTags.add(tag.getSlug());
            }

            tagsWithAddition.put(tag.getSlug(), newTags);
        }

        model.addAttribute("tagsWithAddition", tagsWithAddition);

        model.addAttribute(
                "problems",
                problemService.getProblems(difficulty, tagSlugs));

        model.addAttribute("difficulties", ProblemDifficulty.values());
        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute(
                "solvedProblemIds",
                problemService.getSolvedProblemIds());

        model.addAttribute("selectedDifficulty", difficulty);
        model.addAttribute("selectedTags", tagSlugs);
        model.addAttribute(
                "latestSolvedDates",
                problemService.getLatestSolvedDates());
        // model.addAttribute("problems", problemService.getAllProblems());
        return "problems/list";
    }

    @GetMapping("/{id}")
    public String getProblem(@PathVariable Long id, Model model) {
        model.addAttribute("problem", problemService.getProblem(id));
        model.addAttribute("moduleOccurrences", problemService.getModuleOccurrences(id));
        model.addAttribute("curriculumOccurrences", problemService.getCurriculumOccurrences(id));
        model.addAttribute(
                "everSolved",
                problemService.hasBeenSolved(id));
        model.addAttribute(
                "mostRecentSubmission",
                problemService.getMostRecentSubmission(id));
        return "problems/detail";
    }

    @PostMapping("/{id}/submissions")
    public String createSubmission(@PathVariable Long id, @RequestParam SubmissionLanguage language,
            @RequestParam LocalDateTime completedAt, @RequestParam(required = false) Integer estimatedMinutes,
            @RequestParam(required = false) String notes) {

        Instant completedAtInstant = completedAt.atZone(ZoneId.of("America/Vancouver")).toInstant();
        problemSubmissionService.createSubmission(id, language, completedAtInstant, estimatedMinutes, notes);
        return "redirect:/web/problems/" + id;
    }

    @PostMapping("/{problemId}/submissions/{submissionId}/delete")
    public String deleteSubmission(
            @PathVariable Long problemId,
            @PathVariable UUID submissionId) {

        problemSubmissionService.deleteSubmission(problemId, submissionId);
        return "redirect:/web/problems/" + problemId;
    }

    @GetMapping("/import")
    public String importPage() {
        return "problems/import";
    }

    @PostMapping("/import")
    public String importProblem(@RequestParam String input) {
        Problem problem = problemService.importProblem(input);
        return "redirect:/web/problems/" + problem.getId();
    }

    @PostMapping("/import/batch")
    public String importProblems(@RequestParam String inputs, Model model) {
        List<String> problemInputs = inputs.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();

        model.addAttribute(
                "results",
                problemService.importProblems(problemInputs));

        return "problems/import";
    }
}
