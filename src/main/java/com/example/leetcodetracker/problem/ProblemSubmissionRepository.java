package com.example.leetcodetracker.problem;

import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProblemSubmissionRepository extends JpaRepository<ProblemSubmission, UUID> {
    boolean existsByProblemId(Long problemId);

    Optional<ProblemSubmission> findFirstByProblemIdAndCompletedAtIsNotNullOrderByCompletedAtDesc(Long problemId);

    @Query("""
            SELECT DISTINCT ps.problem.id
            FROM ProblemSubmission ps
            """)
    Set<Long> findSolvedProblemIds();

    @Query("""
            SELECT ps.problem.id, MAX(ps.completedAt)
            FROM ProblemSubmission ps
            WHERE ps.completedAt IS NOT NULL
            GROUP BY ps.problem.id
            """)
    List<Object[]> findLatestSolvedDates();
}
