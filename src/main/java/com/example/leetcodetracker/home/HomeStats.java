package com.example.leetcodetracker.home;

public record HomeStats(
        long problemsImported,
        long problemsCompleted,
        long curricula,
        long modules) {
    public long problemsRemaining() {
        return problemsImported - problemsCompleted;
    }
}
