package com.example.leetcodetracker.modules;

import java.util.UUID;

public class ModuleProblemNotFoundException extends RuntimeException {

    public ModuleProblemNotFoundException(String message) {
        super(message);
    }

    public ModuleProblemNotFoundException(UUID id) {
        super("Module not found: ID" + id);
    }

}
