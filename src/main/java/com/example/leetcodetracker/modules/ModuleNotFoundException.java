package com.example.leetcodetracker.modules;

import java.util.UUID;

public class ModuleNotFoundException extends RuntimeException {

    public ModuleNotFoundException(String message) {
        super(message);
    }

    public ModuleNotFoundException(UUID id) {
        super("Module not found: ID" + id);
    }

}
