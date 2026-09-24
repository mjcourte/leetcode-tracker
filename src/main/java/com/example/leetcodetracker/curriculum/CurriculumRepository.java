package com.example.leetcodetracker.curriculum;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumRepository extends JpaRepository<Curriculum, UUID> {
}
