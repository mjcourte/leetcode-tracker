package com.example.leetcodetracker.home;

import org.springframework.stereotype.Service;

import com.example.leetcodetracker.curriculum.CurriculumRepository;
import com.example.leetcodetracker.modules.ModuleRepository;
import com.example.leetcodetracker.problem.ProblemRepository;
import com.example.leetcodetracker.problem.ProblemSubmissionRepository;

@Service
public class HomeService {
    private final ProblemRepository problemRepository;
    private final ProblemSubmissionRepository problemSubmissionRepository;
    private final CurriculumRepository curriculumRepository;
    private final ModuleRepository moduleRepository;

    public HomeService(
            ProblemRepository problemRepository,
            ProblemSubmissionRepository problemSubmissionRepository,
            CurriculumRepository curriculumRepository,
            ModuleRepository moduleRepository) {
        this.problemRepository = problemRepository;
        this.problemSubmissionRepository = problemSubmissionRepository;
        this.curriculumRepository = curriculumRepository;
        this.moduleRepository = moduleRepository;
    }

    public HomeStats getStats() {
        long problemsImported = problemRepository.count();

        long problemsCompleted = problemSubmissionRepository
                .findSolvedProblemIds()
                .size();

        long curricula = curriculumRepository.count();
        long modules = moduleRepository.count();

        return new HomeStats(
                problemsImported,
                problemsCompleted,
                curricula,
                modules);
    }
}
