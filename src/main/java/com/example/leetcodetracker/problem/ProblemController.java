// Controller: routes HTTP calls to service
package com.example.leetcodetracker.problem;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    // @GetMapping
    // public List<Problem> getAllProblems() {
    // return problemService.getAllProblems();
    // }

    @GetMapping
    public List<Problem> getProblems(
            @RequestParam(required = false) ProblemDifficulty difficulty,
            @RequestParam(required = false) List<String> tags) {

        return problemService.getProblems(
                difficulty,
                tags == null ? List.of() : tags);
    }

    @GetMapping("/{id}")
    public Problem getProblem(@PathVariable long id) {
        return problemService.getProblem(id);
    }

    @PostMapping
    public Problem postProblem(@RequestBody Problem data) {
        return problemService.postProblem(data);
    }

    @DeleteMapping("/{id}")
    public void deleteProblem(@PathVariable long id) {
        problemService.deleteProblem(id);
    }

    @PostMapping("/import")
    public Problem importProblem(@RequestBody ImportProblemRequest request) {
        return problemService.importProblem(request.titleSlug());
    }

    @PostMapping("/import/batch")
    public List<ImportResult> importProblems(@RequestBody ImportProblemsRequest request) {
        return problemService.importProblems(request.titleSlugs());
    }

}
