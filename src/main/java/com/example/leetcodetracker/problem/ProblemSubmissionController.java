package com.example.leetcodetracker.problem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/problems/{problemId}/submissions")
public class ProblemSubmissionController {

    private final ProblemSubmissionService submissionService;

    public ProblemSubmissionController(
            ProblemSubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ProblemSubmission createSubmission(
            @PathVariable Long problemId,
            @RequestBody CreateSubmissionRequest request) {

        return submissionService.createSubmission(
                problemId,
                request.language(),
                request.completedAt(),
                request.estimatedMinutes(),
                request.notes());
    }
}