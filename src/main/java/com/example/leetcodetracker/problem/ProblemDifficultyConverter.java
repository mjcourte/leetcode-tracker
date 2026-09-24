package com.example.leetcodetracker.problem;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProblemDifficultyConverter implements AttributeConverter<ProblemDifficulty, String> {

    @Override
    public String convertToDatabaseColumn(ProblemDifficulty difficulty) {
        return difficulty == null ? null : difficulty.getDisplayName();
    }

    @Override
    public ProblemDifficulty convertToEntityAttribute(String value) {
        return value == null ? null : switch (value) {
            case "Easy" -> ProblemDifficulty.EASY;
            case "Medium" -> ProblemDifficulty.MEDIUM;
            case "Hard" -> ProblemDifficulty.HARD;
            default -> throw new IllegalArgumentException(
                    "Unknown difficulty: " + value);
        };
    }
}
