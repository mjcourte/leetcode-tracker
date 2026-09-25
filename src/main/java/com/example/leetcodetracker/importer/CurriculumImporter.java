package com.example.leetcodetracker.importer;

import org.springframework.stereotype.Service;

import com.example.leetcodetracker.curriculum.Curriculum;
import com.example.leetcodetracker.curriculum.CurriculumService;
import com.example.leetcodetracker.modules.ModuleService;
import com.example.leetcodetracker.problem.Problem;
import com.example.leetcodetracker.problem.ProblemService;

import jakarta.transaction.Transactional;

import com.example.leetcodetracker.modules.Module;

@Service
public class CurriculumImporter {

    private final CurriculumService curriculumService;
    private final ModuleService moduleService;
    private final ProblemService problemService;

    public CurriculumImporter(CurriculumService curriculumService, ModuleService moduleService,
            ProblemService problemService) {
        this.curriculumService = curriculumService;
        this.moduleService = moduleService;
        this.problemService = problemService;
    }

    @Transactional
    public Curriculum importCurriculum(CurriculumDefinition definition) {

        Curriculum curriculum = curriculumService.createCurriculum(definition.name());
        for (ModuleDefinition moduleDefinition : definition.modules()) {
            Module module = curriculumService.createAndAddModule(curriculum.getId(), moduleDefinition.name());
            for (String slug : moduleDefinition.problemSlugs()) {
                // import the problem. this step is fallable.
                Problem problem = problemService.getOrImportProblem(slug);
                // get the Long problemId
                moduleService.addProblem(module.getId(), problem.getId());

            }
        }
        return curriculum;
    }
}
