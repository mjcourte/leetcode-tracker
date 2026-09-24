package com.example.leetcodetracker.leetcode;

import java.util.List;

public record LeetCodeProblem(
        String questionFrontendId,
        String title,
        String titleSlug,
        String difficulty,
        boolean isPaidOnly,
        List<LeetCodeTopicTag> topicTags) {

}
