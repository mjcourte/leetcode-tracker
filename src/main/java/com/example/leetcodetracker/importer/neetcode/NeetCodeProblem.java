package com.example.leetcodetracker.importer.neetcode;

public record NeetCodeProblem(
        String problem,
        String pattern,
        String link,
        String difficulty,
        boolean neetcode150,
        boolean blind75,
        boolean neetcode250) {

}
