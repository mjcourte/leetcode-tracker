package com.example.leetcodetracker.modules;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleProblemRepository extends JpaRepository<ModuleProblem, UUID> {
    List<ModuleProblem> findByProblemId(Long problemId);
}
