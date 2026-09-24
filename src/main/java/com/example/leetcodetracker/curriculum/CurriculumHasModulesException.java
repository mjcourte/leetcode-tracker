package com.example.leetcodetracker.curriculum;

import java.util.UUID;

public class CurriculumHasModulesException extends RuntimeException {
    public CurriculumHasModulesException(UUID id) {
        super("Cannot delete Curriculum " + id + ", it has modules!");
    }

}
