package com.example.leetcodetracker.problem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findBySlug(String slug);

    @Query("""
            SELECT t
            FROM Problem p
            JOIN p.tags t
            GROUP BY t
            HAVING COUNT(p) >= 2
            ORDER BY t.name
            """)
    List<Tag> findTagsUsedByMultipleProblems();

    // this is an awful query I know sigh THE DATABASE SHALL REMAIN SMALL
    @Query("""
            SELECT DISTINCT p
            FROM Problem p
            LEFT JOIN FETCH p.tags
            ORDER BY p.id ASC
            """)
    List<Problem> findAllWithTagsOrderByIdAsc();
}
