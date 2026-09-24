package com.example.leetcodetracker.modules;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, UUID> {

    Optional<Module> findFirstByCurriculumIdOrderByPositionDesc(UUID curriculumId);

    List<Module> findByCurriculumIdAndPositionBetween(
            UUID curriculumId,
            int lower,
            int upper);
}
