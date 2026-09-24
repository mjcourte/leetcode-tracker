package com.example.leetcodetracker.leetcode;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leetcode")
public class LeetCodeController {

    private final LeetCodeService leetCodeService;

    public LeetCodeController(LeetCodeService leetCodeService) {
        this.leetCodeService = leetCodeService;
    }

    @GetMapping("/stats/{username}")
    public List<SubmissionStats> getLeetCodeStats(
            @PathVariable String username) {
        return leetCodeService.getSubmissionStats(username);
    }

    @GetMapping("/problems/{titleSlug}")
    public LeetCodeProblem getProblem(@PathVariable String titleSlug) {
        return leetCodeService.getProblem(titleSlug);
    }
}
