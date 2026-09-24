package com.example.leetcodetracker.problem;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

@Entity
public class ProblemSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Problem problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionLanguage language; // can we make this an enum

    @Column(nullable = false)
    private Instant completedAt;

    private Integer estimatedMinutes;

    @Column(columnDefinition = "TEXT")
    private String notes;

    protected ProblemSubmission() {
    }

    public ProblemSubmission(Problem problem, SubmissionLanguage language, Instant completedAt,
            Integer estimatedMinutes,
            String notes) {
        this.problem = problem;
        this.language = language;
        this.completedAt = completedAt;
        this.estimatedMinutes = estimatedMinutes;
        this.notes = notes;
    }

    public UUID getId() {
        return id;
    }

    public SubmissionLanguage getLanguage() {
        return language;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public String getNotes() {
        return notes;
    }

}
