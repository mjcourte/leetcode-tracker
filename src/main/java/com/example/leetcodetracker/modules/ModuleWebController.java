package com.example.leetcodetracker.modules;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.leetcodetracker.curriculum.CurriculumService;
import com.example.leetcodetracker.problem.ProblemService;

@Controller
@RequestMapping("/web/modules") // should we nest under curricula?
public class ModuleWebController {

    private final ModuleService moduleService;
    private final ProblemService problemService;
    private final CurriculumService curriculumService;

    public ModuleWebController(ModuleService moduleService, ProblemService problemService,
            CurriculumService curriculumService) {
        this.moduleService = moduleService;
        this.problemService = problemService;
        this.curriculumService = curriculumService;
    }

    // this is list ALL not all under curriculum
    @GetMapping
    public String listModules(Model model) {
        model.addAttribute("modules", moduleService.getAllModules());
        model.addAttribute("curricula", curriculumService.getAllCurricula());
        return "modules/list";
    }

    @GetMapping("/{id}")
    public String getModule(@PathVariable UUID id, Model model) {
        model.addAttribute("module", moduleService.getModule(id));
        model.addAttribute("availableProblems", problemService.getAllProblems());
        model.addAttribute("curricula", curriculumService.getAllCurricula());
        model.addAttribute("solvedProblemIds", problemService.getSolvedProblemIds());
        return "modules/detail";
    }

    @PostMapping
    public String createModule(@RequestParam String name) {
        Module module = moduleService.createModule(name);
        return "redirect:/web/modules/" + module.getId();
    }

    @PostMapping("/{id}/edit")
    public String updateModule(
            @PathVariable UUID id,
            @RequestParam UpdateModuleRequest request) {

        moduleService.updateModule(id, request);
        return "redirect:/web/modules/{id}";
    }

    @PostMapping("/{id}/delete")
    public String deleteModule(@PathVariable UUID id) {
        moduleService.deleteModule(id);
        return "redirect:/web/modules";
    }

    @PostMapping("/{moduleId}/problems")
    public String addProblem(
            @PathVariable UUID moduleId,
            @RequestParam Long problemId) {

        moduleService.addProblem(moduleId, problemId);

        return "redirect:/web/modules/{moduleId}";
    }

    @PostMapping("/{moduleId}/problems/{moduleProblemId}/move")
    public String moveProblem(
            @PathVariable UUID moduleId,
            @PathVariable UUID moduleProblemId,
            @RequestParam int position) {

        moduleService.moveProblem(
                moduleId,
                moduleProblemId,
                position);

        return "redirect:/web/modules/{moduleId}";
    }

    @PostMapping("/{moduleId}/problems/{moduleProblemId}/remove")
    public String removeProblem(
            @PathVariable UUID moduleId,
            @PathVariable UUID moduleProblemId) {

        moduleService.removeProblem(
                moduleId,
                moduleProblemId);

        return "redirect:/web/modules/{moduleId}";
    }

    @PostMapping("/{moduleId}/problems/{moduleProblemId}/revisit")
    public String toggleRevisit(
            @PathVariable UUID moduleId,
            @PathVariable UUID moduleProblemId) {

        moduleService.toggleRevisit(moduleId, moduleProblemId);

        return "redirect:/web/modules/{moduleId}";
    }
}
