// Service owns the application logic

package com.example.leetcodetracker.problem;

import com.example.leetcodetracker.curriculum.CurriculumProblem;
import com.example.leetcodetracker.curriculum.CurriculumProblemRepository;
import com.example.leetcodetracker.leetcode.LeetCodeProblem;
import com.example.leetcodetracker.leetcode.LeetCodeService;
import com.example.leetcodetracker.leetcode.LeetCodeTopicTag;
import com.example.leetcodetracker.modules.ModuleProblem;
import com.example.leetcodetracker.modules.ModuleProblemRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;
    private final LeetCodeService leetCodeService;
    private final ModuleProblemRepository moduleProblemRepository;
    private final CurriculumProblemRepository curriculumProblemRepository;
    private final ProblemSubmissionRepository problemSubmissionRepository;

    public ProblemService(ProblemRepository problemRepository, TagRepository tagRepository,
            LeetCodeService leetCodeService, ModuleProblemRepository moduleProblemRepository,
            CurriculumProblemRepository curriculumProblemRepository,
            ProblemSubmissionRepository problemSubmissionRepository) {
        this.problemRepository = problemRepository;
        this.tagRepository = tagRepository;
        this.leetCodeService = leetCodeService;
        this.moduleProblemRepository = moduleProblemRepository;
        this.curriculumProblemRepository = curriculumProblemRepository;
        this.problemSubmissionRepository = problemSubmissionRepository;
    }

    // get batch by Long id
    @Transactional(readOnly = true)
    public List<Problem> getProblems(List<Long> ids) {
        List<Problem> lp = problemRepository.findAllById(ids);

        if (lp.size() != ids.size()) {
            throw new ProblemNotFoundException(ids);
        }
        return lp;
    }

    @Transactional(readOnly = true)
    public List<Problem> getAllProblems() {
        return problemRepository.findAllByOrderByIdAsc();
    }

    // get filtered by difficulty and/or tags
    @Transactional(readOnly = true)
    public List<Problem> getProblems(
            ProblemDifficulty difficulty,
            Collection<String> tagSlugs,
            ProblemStatus status) {

        // if (difficulty != null && !tagSlugs.isEmpty()) {
        // return problemRepository.findByDifficultyAndAllTagSlugsOrderByIdAsc(
        // difficulty,
        // tagSlugs,
        // tagSlugs.size());
        // }

        // if (difficulty != null) {
        // return problemRepository.findByDifficultyOrderByIdAsc(difficulty);
        // }

        // if (!tagSlugs.isEmpty()) {
        // return problemRepository.findByAllTagSlugsOrderByIdAsc(
        // tagSlugs,
        // tagSlugs.size());
        // }

        // return problemRepository.findAllByOrderByIdAsc();
        Set<Long> solvedProblemIds = problemSubmissionRepository
                .findSolvedProblemIds();

        Stream<Problem> problems = problemRepository
                .findAllByOrderByIdAsc()
                .stream();

        if (difficulty != null) {
            problems = problems.filter(
                    p -> p.getDifficulty() == difficulty);
        }

        if (!tagSlugs.isEmpty()) {
            problems = problems.filter(
                    p -> tagSlugs.stream()
                            .allMatch(slug -> p.getTags().stream()
                                    .anyMatch(tag -> tag.getSlug().equals(slug))));
        }

        if (status == ProblemStatus.SOLVED) {
            problems = problems.filter(
                    p -> solvedProblemIds.contains(p.getId()));
        } else if (status == ProblemStatus.UNSOLVED) {
            problems = problems.filter(
                    p -> !solvedProblemIds.contains(p.getId()));
        }

        return problems.toList();
    }

    @Transactional(readOnly = true)
    public Problem getProblem(long id) {
        return problemRepository.findById(id).orElseThrow(() -> new ProblemNotFoundException(id));
    }

    @Transactional
    public Problem postProblem(Problem data) {
        if (problemRepository.findById(data.getId()).isPresent()) {
            throw new ProblemAlreadyExistsException("Problem already exists: ID " + data.getId());
        }
        return problemRepository.save(data);
    }

    @Transactional
    public void deleteProblem(long id) {
        if (problemRepository.findById(id).isEmpty()) {
            throw new ProblemNotFoundException(id);
        }
        problemRepository.deleteById(id);
    }

    // tag helpers
    private Tag findOrCreateTag(LeetCodeTopicTag topicTag) {
        return tagRepository.findBySlug(topicTag.slug())
                .orElseGet(() -> tagRepository.save(new Tag(topicTag.name(), topicTag.slug())));
    }

    private List<Tag> mapTags(List<LeetCodeTopicTag> topicTags) {
        return topicTags.stream().map(this::findOrCreateTag).toList();
    }

    // Mapper from leetcode
    private Problem toProblem(LeetCodeProblem p) {
        Problem np = new Problem(
                Long.parseLong(p.questionFrontendId()),
                p.title(),
                ProblemDifficulty.valueOf(p.difficulty().toUpperCase()),
                p.titleSlug());

        np.setTags(mapTags(p.topicTags()));

        return np;
    }

    @Transactional
    public Problem importProblem(String input) {
        LeetCodeProblem lcp = leetCodeService.fetchProblem(input);

        if (lcp == null) {
            throw new ProblemNotFoundException(input);
        }

        Long problemId = Long.parseLong(lcp.questionFrontendId());

        if (problemRepository.existsById(problemId)) {
            throw new ProblemAlreadyExistsException(lcp.questionFrontendId());
        }

        Problem problem = toProblem(lcp);
        return problemRepository.save(problem);
    }

    @Transactional
    public List<ImportResult> importProblems(List<String> inputs) {
        List<ImportResult> results = new ArrayList<>();

        for (String input : inputs) {
            try {
                Problem problem = importProblem(input);

                results.add(new ImportResult(
                        input,
                        ImportStatus.IMPORTED,
                        "Imported: " + problem.getTitle()));

            } catch (ProblemAlreadyExistsException e) {
                results.add(new ImportResult(
                        input,
                        ImportStatus.ALREADY_EXISTS,
                        "Already imported: " + e.getTitle()));

            } catch (ProblemNotFoundException e) {
                results.add(new ImportResult(
                        input,
                        ImportStatus.NOT_FOUND,
                        "Problem not found:" + input));
            }
        }

        return results;

    }

    public List<ModuleProblem> getModuleOccurrences(Long problemId) {
        return moduleProblemRepository.findByProblemId(problemId);
    }

    public List<CurriculumProblem> getCurriculumOccurrences(Long problemId) {
        return curriculumProblemRepository.findByProblemId(problemId);
    }

    public boolean hasBeenSolved(Long problemId) {
        return problemSubmissionRepository.existsByProblemId(problemId);
    }

    public Optional<ProblemSubmission> getMostRecentSubmission(Long problemId) {
        return problemSubmissionRepository.findFirstByProblemIdAndCompletedAtIsNotNullOrderByCompletedAtDesc(problemId);
    }

    public Set<Long> getSolvedProblemIds() {
        return problemSubmissionRepository.findSolvedProblemIds();
    }

    public Map<Long, Instant> getLatestSolvedDates() {
        return problemSubmissionRepository.findLatestSolvedDates().stream()
                .collect(Collectors.toMap(row -> (Long) row[0], row -> (Instant) row[1]));
    }

    public String getTimeSinceSolved(Instant completedAt) {
        Duration duration = Duration.between(completedAt, Instant.now());

        long days = duration.toDays();

        if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        }

        long hours = duration.toHours();

        if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        }

        return "Today";
    }
}
