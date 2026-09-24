package com.example.leetcodetracker.curriculum;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.leetcodetracker.problem.AddProblemRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/curricula")
public class CurriculumController {

    private final CurriculumService curriculumService;

    public CurriculumController(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @GetMapping
    public List<Curriculum> getCurricula() {
        return curriculumService.getCurricula();
    }

    @PostMapping
    public Curriculum createCurriculum(@RequestBody String name) {
        return curriculumService.createCurriculum(name);
    }

    @GetMapping("/{id}")
    public Curriculum getCurriculum(@PathVariable UUID id) {
        return curriculumService.getCurriculum(id);
    }

    @PutMapping("/{id}")
    public Curriculum updateCurriculum(@PathVariable UUID id, @RequestBody Curriculum data) {
        return curriculumService.updateCurriculum(id, data);
    }

    @DeleteMapping("/{id}")
    public void deleteCurriculum(@PathVariable UUID id) {
        curriculumService.deleteCurriculum(id);
    }

    @PostMapping("/{curriculumId}/modules")
    public void addModule(@PathVariable UUID curriculumId, @RequestBody AddModuleRequest request) {
        curriculumService.addModule(curriculumId, request.moduleId());
    }

    @PatchMapping("/{curriculumId}/modules/{moduleId}")
    public void moveModule(@PathVariable UUID curriculumId, @PathVariable UUID moduleId,
            @RequestBody UpdateCurriculumModuleRequest request) {
        // the only thing this method should care about is the ordering
        // any other mutations would call /modules/... instead of /curricula/...
        curriculumService.moveModule(curriculumId, moduleId, request.position());

    }

    @DeleteMapping("/{curriculumId}/modules/{moduleId}")
    public void removeModule(@PathVariable UUID curriculumId, @PathVariable UUID moduleId) {
        curriculumService.removeModule(curriculumId, moduleId);
    }

    @PostMapping("/{curriculumId}/problems")
    public CurriculumProblem addProblem(@PathVariable UUID curriculumId, @RequestBody AddProblemRequest request) {
        return curriculumService.addProblem(curriculumId, request.problemId());
    }

    // TODO List<AddProblemRequest> and extend with positions
    @PostMapping("/{curriculumId}/problems/batch")
    public void addProblems(@PathVariable UUID curriculumId, @RequestBody List<Long> problemIds) {
        curriculumService.addProblems(curriculumId, problemIds);
    }

    @PatchMapping("/{curriculumId}/problems/{curriculumProblemId}")
    public void moveProblem(@PathVariable UUID curriculumId, @PathVariable UUID curriculumProblemId,
            @RequestParam UpdateCurriculumProblemRequest request) {
        curriculumService.moveProblem(curriculumId, curriculumProblemId, request.position());
    }

    @DeleteMapping("/{curriculumId}/problems/{curriculumProblemId}")
    public void removeProblem(@PathVariable UUID curriculumId, @PathVariable UUID curriculumProblemId) {
        curriculumService.removeProblem(curriculumId, curriculumProblemId);
    }
}
