package com.example.leetcodetracker.importer;

import java.util.List;

public record CurriculumDefinition(
        String name,
        List<ModuleDefinition> modules) {

}
