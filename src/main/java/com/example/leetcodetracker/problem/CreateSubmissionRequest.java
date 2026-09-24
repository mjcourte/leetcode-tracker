package com.example.leetcodetracker.problem;

import java.time.Instant;

public record CreateSubmissionRequest(
        SubmissionLanguage language,
        Instant completedAt,
        Integer estimatedMinutes,
        String notes) {

}
