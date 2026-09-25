package com.example.leetcodetracker.importer;

import java.util.List;

public record ModuleDefinition(
        String name,
        List<String> problemSlugs) {

}
