package com.example.leetcodetracker.modules;

import com.example.leetcodetracker.curriculum.Curriculum;
import com.example.leetcodetracker.curriculum.CurriculumNotFoundException;
import com.example.leetcodetracker.curriculum.CurriculumRepository;
import com.example.leetcodetracker.problem.ProblemNotFoundException;
import com.example.leetcodetracker.problem.ProblemRepository;
import com.example.leetcodetracker.problem.ProblemService;
import com.example.leetcodetracker.problem.Problem;
import com.example.leetcodetracker.ordering.Ordering;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CurriculumRepository curriculumRepository;
    private final ProblemRepository problemRepository;
    private final ProblemService problemService;
    private final Ordering<ModuleProblem> problemOrdering = new Ordering<>();

    public ModuleService(ModuleRepository moduleRepository, CurriculumRepository curriculumRepository,
            ProblemRepository problemRepository, ProblemService problemService) {
        this.moduleRepository = moduleRepository;
        this.curriculumRepository = curriculumRepository;
        this.problemRepository = problemRepository;
        this.problemService = problemService;
    }

    @Transactional(readOnly = true)
    public Module getModule(UUID moduleId) {
        Module existing = moduleRepository.findById(moduleId).orElseThrow(() -> new ModuleNotFoundException(moduleId));
        return existing;
    }

    @Transactional(readOnly = true)
    public List<Module> getModules(UUID curriculumId) {

        Curriculum curriculum = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));

        return curriculum.getModules();
    }

    @Transactional(readOnly = true)
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    @Transactional
    public Module createModule(String name) {
        Module module = new Module(name);
        return moduleRepository.save(module);
    }

    @Transactional
    public Module updateModule(UUID moduleId, UpdateModuleRequest request) {
        Module existing = moduleRepository.findById(moduleId).orElseThrow(() -> new ModuleNotFoundException(moduleId));
        // allowed settables
        existing.setName(request.name());
        return existing;
    }

    @Transactional
    public void deleteModule(UUID moduleId) {
        Module existing = moduleRepository.findById(moduleId).orElseThrow(() -> new ModuleNotFoundException(moduleId));
        moduleRepository.delete(existing);
    }

    // add problem to pos -1
    @Transactional
    public ModuleProblem addProblem(UUID moduleId, Long problemId) {
        Module m = moduleRepository.findById(moduleId).orElseThrow(() -> new ModuleNotFoundException(moduleId));
        Problem p = problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException(problemId));

        return m.addProblem(p);
    }

    // add batch of problems
    @Transactional
    public void addProblems(UUID moduleId, List<Long> problemIds) {
        Module m = moduleRepository.findById(moduleId).orElseThrow(() -> new ModuleNotFoundException(moduleId));

        List<Problem> ps = problemService.getProblems(problemIds);

        for (Problem p : ps) {
            ModuleProblem mp = m.addProblem(p);
            problemOrdering.insert(m.getProblems(), mp, -1);
        }
    }

    // add problem to pos specified
    @Transactional
    public void addProblem(UUID moduleId, Long problemId, int position) {
        Module m = moduleRepository.findById(moduleId).orElseThrow(() -> new ModuleNotFoundException(moduleId));
        Problem p = problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException(problemId));

        ModuleProblem mp = m.addProblem(p);
        problemOrdering.insert(m.getProblems(), mp, position);
    }

    @Transactional
    public void moveProblem(UUID moduleId, UUID problemId, int position) {
        Module module = moduleRepository.findById(moduleId).orElseThrow(() -> new ModuleNotFoundException(moduleId));
        ModuleProblem moduleProblem = module.getProblems().stream().filter(x -> x.getId().equals(problemId)).findFirst()
                .orElseThrow(() -> new ProblemNotFoundException(problemId));

        problemOrdering.move(module.getProblems(), moduleProblem, position);
    }

    // remove problem
    @Transactional
    public void removeProblem(UUID moduleId, UUID moduleProblemId) {
        Module m = moduleRepository.findById(moduleId).orElseThrow(() -> new ModuleNotFoundException(moduleId));
        ModuleProblem mp = m.getProblems().stream().filter(i -> i.getId().equals(moduleProblemId)).findFirst()
                .orElseThrow(() -> new ModuleProblemNotFoundException(moduleProblemId));

        problemOrdering.remove(m.getProblems(), mp);
    }

    @Transactional
    public void toggleRevisit(UUID moduleId, UUID moduleProblemId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        ModuleProblem moduleProblem = module.getProblems().stream()
                .filter(mp -> mp.getId().equals(moduleProblemId))
                .findFirst()
                .orElseThrow(() -> new ModuleProblemNotFoundException(moduleProblemId));

        if (moduleProblem.isRevisit()) {
            moduleProblem.clearRevisit();
        } else {
            moduleProblem.markForRevisit();
        }
    }

    public int getSolvedCount(Module module, Set<Long> solvedProblemIds) {
        return (int) module.getProblems().stream()
                .filter(mp -> solvedProblemIds.contains(mp.getProblem().getId()))
                .count();
    }

    public int getRevisitCount(Module module) {
        return (int) module.getProblems().stream()
                .filter(ModuleProblem::isRevisit)
                .count();
    }
}
