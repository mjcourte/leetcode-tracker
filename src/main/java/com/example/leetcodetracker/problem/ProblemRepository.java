package com.example.leetcodetracker.problem;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
        // jpa repo provides impls for findAll and findByI
        List<Problem> findAllByOrderByIdAsc();

        List<Problem> findByDifficultyOrderByIdAsc(ProblemDifficulty difficulty);

        @Query("""
                        SELECT p
                        FROM Problem p
                        JOIN p.tags t
                        WHERE t.slug IN :slugs
                        GROUP BY p
                        HAVING COUNT(DISTINCT t.slug) = :tagCount
                        """)
        List<Problem> findByAllTagSlugsOrderByIdAsc(
                        @Param("slugs") Collection<String> slugs,
                        @Param("tagCount") long tagCount);

        @Query("""
                        SELECT p
                        FROM Problem p
                        JOIN p.tags t
                        WHERE p.difficulty = :difficulty AND t.slug IN :slugs
                        GROUP BY p
                        HAVING COUNT(DISTINCT t.slug) = :tagCount
                        """)
        List<Problem> findByDifficultyAndAllTagSlugsOrderByIdAsc(
                        @Param("difficulty") ProblemDifficulty difficulty,
                        @Param("slugs") Collection<String> slugs,
                        @Param("tagCount") long TagCount);
}
