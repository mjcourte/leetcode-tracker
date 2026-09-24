package com.example.leetcodetracker.problem;

import java.util.UUID;

public class SubmissionNotFoundException extends RuntimeException {
    public SubmissionNotFoundException(String message) {
        super(message);
    }

    public SubmissionNotFoundException(UUID id) {
        super("Submission not found: ID " + id);
    }

}
