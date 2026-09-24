package com.example.leetcodetracker.curriculum;

import java.util.UUID;

public class CurriculumNotFoundException extends RuntimeException {
    public CurriculumNotFoundException(String message) {
        super(message);
    }

    public CurriculumNotFoundException(UUID id) {
        super("Curriculum not found: ID" + id);
    }

}
