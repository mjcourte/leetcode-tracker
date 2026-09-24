package com.example.leetcodetracker.curriculum;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumProblemRepository extends JpaRepository<CurriculumProblem, UUID> {
    List<CurriculumProblem> findByProblemId(Long problemId);
}
