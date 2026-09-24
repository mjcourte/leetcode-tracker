package com.example.leetcodetracker.curriculum;

import com.example.leetcodetracker.modules.ModuleService;
import com.example.leetcodetracker.problem.ProblemService;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/curricula")
public class CurriculumWebController {

    private final CurriculumService curriculumService;
    private final ModuleService moduleService;
    private final ProblemService problemService;

    public CurriculumWebController(CurriculumService curriculumService, ModuleService moduleService,
            ProblemService problemService) {
        this.curriculumService = curriculumService;
        this.moduleService = moduleService;
        this.problemService = problemService;
    }

    @GetMapping
    public String listCurricula(Model model) {
        model.addAttribute("curricula", curriculumService.getAllCurricula());
        return "curricula/list";
    }

    @GetMapping("/{id}")
    public String getCurriculum(@PathVariable UUID id, Model model) {

        model.addAttribute("curriculum", curriculumService.getCurriculum(id));
        model.addAttribute("availableModules", moduleService.getAllModules());
        model.addAttribute("availableProblems", problemService.getAllProblems());
        model.addAttribute("solvedProblemIds", problemService.getSolvedProblemIds());
        return "curricula/detail";
    }

    @PostMapping
    public String createCurriculum(@RequestParam String name) {
        Curriculum curriculum = curriculumService.createCurriculum(name);
        return "redirect:/web/curricula/" + curriculum.getId();
    }

    @PostMapping("/{id}/delete")
    public String deleteCurriculum(@PathVariable UUID id) {
        curriculumService.deleteCurriculum(id);
        return "redirect:/web/curricula";
    }

    // this may need to simultaneously create new module?
    @PostMapping("/{curriculumId}/modules")
    public String addModule(@PathVariable UUID curriculumId, @RequestParam UUID moduleId) {
        curriculumService.addModule(curriculumId, moduleId);
        return "redirect:/web/curricula/{curriculumId}";
    }

    @PostMapping("/{curriculumId}/modules/create")
    public String createModule(@PathVariable UUID curriculumId, @RequestParam String name) {
        curriculumService.createAndAddModule(curriculumId, name);
        return "redirect:/web/curricula/{curriculumId}";
    }

    @PostMapping("/{curriculumId}/modules/{moduleId}/move")
    public String moveModule(
            @PathVariable UUID curriculumId,
            @PathVariable UUID moduleId,
            @RequestParam int position) {

        curriculumService.moveModule(
                curriculumId,
                moduleId,
                position);

        return "redirect:/web/curricula/{curriculumId}";
    }

    @PostMapping("/{curriculumId}/modules/{moduleId}/remove")
    public String removeModule(
            @PathVariable UUID curriculumId,
            @PathVariable UUID moduleId) {

        curriculumService.removeModule(
                curriculumId,
                moduleId);

        return "redirect:/web/curricula/{curriculumId}";
    }

    @PostMapping("/{curriculumId}/problems")
    public String addProblem(@PathVariable UUID curriculumId, @RequestParam Long problemId) {
        curriculumService.addProblem(curriculumId, problemId);
        return "redirect:/web/curricula/{curriculumId}";
    }

    @PostMapping("/{curriculumId}/problems/{problemId}/move")
    public String moveProblem(
            @PathVariable UUID curriculumId,
            @PathVariable UUID problemId,
            @RequestParam int position) {

        curriculumService.moveProblem(
                curriculumId,
                problemId,
                position);

        return "redirect:/web/curricula/{curriculumId}";
    }

    @PostMapping("/{curriculumId}/problems/{problemId}/remove")
    public String removeProblem(
            @PathVariable UUID curriculumId,
            @PathVariable UUID problemId) {

        curriculumService.removeProblem(
                curriculumId,
                problemId);

        return "redirect:/web/curricula/{curriculumId}";
    }

    @PostMapping("/{curriculumId}/problems/{curriculumProblemId}/revisit")
    public String toggleRevisit(
            @PathVariable UUID curriculumId,
            @PathVariable UUID curriculumProblemId) {

        curriculumService.toggleRevisit(curriculumId, curriculumProblemId);

        return "redirect:/web/curricula/{curriculumId}";
    }
}
