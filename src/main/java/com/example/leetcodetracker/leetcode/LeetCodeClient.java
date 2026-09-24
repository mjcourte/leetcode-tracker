package com.example.leetcodetracker.leetcode;

import java.util.List;

public interface LeetCodeClient {
    List<SubmissionStats> getSubmissionStats(String username);

    LeetCodeProblem getProblem(String titleSlug);
}
