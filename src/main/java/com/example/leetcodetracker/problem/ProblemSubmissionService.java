package com.example.leetcodetracker.problem;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProblemSubmissionService {

    private final ProblemSubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;

    public ProblemSubmissionService(
            ProblemSubmissionRepository submissionRepository,
            ProblemRepository problemRepository) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
    }

    @Transactional
    public ProblemSubmission createSubmission(
            Long problemId,
            SubmissionLanguage language,
            Instant completedAt,
            Integer estimatedMinutes,
            String notes) {

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException(problemId));

        ProblemSubmission submission = new ProblemSubmission(
                problem,
                language,
                completedAt,
                estimatedMinutes,
                notes);

        return submissionRepository.save(submission);
    }

    @Transactional
    public void deleteSubmission(Long problemId, UUID submissionId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException(problemId));

        ProblemSubmission submission = problem.getSubmissions().stream().filter(s -> s.getId().equals(submissionId))
                .findFirst().orElseThrow(() -> new SubmissionNotFoundException(submissionId));

        submissionRepository.delete(submission);
    }
}