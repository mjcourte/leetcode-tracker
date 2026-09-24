package com.example.leetcodetracker.curriculum;

import java.util.UUID;

public class CurriculumProblemNotFoundException extends RuntimeException {
    public CurriculumProblemNotFoundException(String message) {
        super(message);
    }

    public CurriculumProblemNotFoundException(UUID id) {
        super("Curriculum not found: ID" + id);
    }

}
