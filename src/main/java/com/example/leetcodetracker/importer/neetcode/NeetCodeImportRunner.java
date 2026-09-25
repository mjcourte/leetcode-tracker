package com.example.leetcodetracker.importer.neetcode;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.leetcodetracker.importer.CurriculumDefinition;
import com.example.leetcodetracker.importer.CurriculumImporter;

// "https://neetcode.io/main.f39af0c52a4e9fb5.js"
@Component
public class NeetCodeImportRunner implements ApplicationRunner {

    private final NeetCodeScraper scraper;
    private final CurriculumImporter curriculumImporter;

    public NeetCodeImportRunner(NeetCodeScraper scraper, CurriculumImporter curriculumImporter) {
        this.scraper = scraper;
        this.curriculumImporter = curriculumImporter;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.containsOption("import-neetcode")) {
            return;
        }
        System.out.println("NeetCodeImportRunner.run() called.");

        List<CurriculumDefinition> lcd = scraper.scrape("https://neetcode.io/main.f39af0c52a4e9fb5.js");

        for (CurriculumDefinition cd : lcd) {
            curriculumImporter.importCurriculum(cd);

        }
    }
}
