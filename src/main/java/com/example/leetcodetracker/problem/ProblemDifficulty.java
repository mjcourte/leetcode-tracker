package com.example.leetcodetracker.problem;

public enum ProblemDifficulty {
    EASY,
    MEDIUM,
    HARD;

    public String getDisplayName() {
        return switch (this) {
            case EASY -> "Easy";
            case MEDIUM -> "Medium";
            case HARD -> "Hard";
        };
    }

    public String getBadgeClass() {
        return switch (this) {
            case EASY -> "text-bg-success";
            case MEDIUM -> "text-bg-warning";
            case HARD -> "text-bg-danger";
        };
    }
}
