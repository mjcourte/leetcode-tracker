package com.example.leetcodetracker.modules;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.leetcodetracker.problem.AddProblemRequest;

import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public List<Module> getModules(@PathVariable UUID curriculumId) {
        return moduleService.getModules(curriculumId);
    }

    @GetMapping("/{id}")
    public Module getModule(@PathVariable UUID id) {
        return moduleService.getModule(id);
    }

    @PostMapping
    public Module createModule(@RequestBody CreateModuleRequest request) {
        return moduleService.createModule(request.name());
    }

    @PutMapping("/{id}")
    public Module updateModule(@PathVariable UUID id, @RequestBody UpdateModuleRequest request) {
        return moduleService.updateModule(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteModule(@PathVariable UUID id) {
        moduleService.deleteModule(id);
    }

    @PostMapping("/{moduleId}/problems")
    public ModuleProblem addProblem(
            @PathVariable UUID moduleId,
            @RequestBody AddProblemRequest request) {
        return moduleService.addProblem(moduleId, request.problemId());
    }

    // TODO List<AddProblemRequest> and extend AddProblemRequest with position int
    @PostMapping("/{moduleId}/problems/batch")
    public void addProblems(
            @PathVariable UUID moduleId,
            @RequestBody List<Long> problemIds) {
        moduleService.addProblems(moduleId, problemIds);
    }

    @PatchMapping("/{moduleId}/problems/{moduleProblemId}")
    public void moveProblem(
            @PathVariable UUID moduleId,
            @PathVariable UUID moduleProblemId,
            @RequestParam UpdateModuleProblemRequest request) {
        moduleService.moveProblem(moduleId, moduleProblemId, request.position());
    }

    @DeleteMapping("/{moduleId}/problems/{moduleProblemId}")
    public void removeProblem(
            @PathVariable UUID moduleId,
            @PathVariable UUID moduleProblemId) {
        moduleService.removeProblem(moduleId, moduleProblemId);
    }
}
