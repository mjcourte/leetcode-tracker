package com.example.leetcodetracker.curriculum;

import com.example.leetcodetracker.problem.Problem;
import com.example.leetcodetracker.problem.ProblemNotFoundException;
import com.example.leetcodetracker.ordering.Ordering;
import com.example.leetcodetracker.modules.Module;
import com.example.leetcodetracker.modules.ModuleNotFoundException;
import com.example.leetcodetracker.modules.ModuleProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.leetcodetracker.modules.ModuleRepository;
import com.example.leetcodetracker.modules.ModuleService;
import com.example.leetcodetracker.problem.ProblemRepository;
import com.example.leetcodetracker.problem.ProblemService;

@Service
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleService moduleService;
    private final ProblemRepository problemRepository;
    private final ProblemService problemService;
    private final Ordering<Module> moduleOrdering = new Ordering<>();
    private final Ordering<CurriculumProblem> problemOrdering = new Ordering<>();

    public CurriculumService(CurriculumRepository curriculumRepository, ModuleRepository moduleRepository,
            ModuleService moduleService,
            ProblemRepository problemRepository, ProblemService problemService) {
        this.curriculumRepository = curriculumRepository;
        this.moduleRepository = moduleRepository;
        this.moduleService = moduleService;
        this.problemRepository = problemRepository;
        this.problemService = problemService;
    }

    // keep for pagination?
    @Transactional(readOnly = true)
    public List<Curriculum> getCurricula() {
        return curriculumRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Curriculum> getAllCurricula() {
        return curriculumRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Curriculum getCurriculum(UUID id) {
        return curriculumRepository.findById(id).orElseThrow(() -> new CurriculumNotFoundException(id));
    }

    @Transactional
    public Curriculum createCurriculum(String name) {
        Curriculum newCurriculum = new Curriculum(name);
        return curriculumRepository.save(newCurriculum);
    }

    @Transactional
    public Curriculum updateCurriculum(UUID id, Curriculum data) {
        if (curriculumRepository.findById(id).isEmpty()) {
            throw new CurriculumNotFoundException(id);
        }
        Curriculum updated = new Curriculum(
                id, data.getName());
        return curriculumRepository.save(updated);
    }

    @Transactional
    public void deleteCurriculum(UUID id) {
        Curriculum c = curriculumRepository.findById(id).orElseThrow(() -> new CurriculumNotFoundException(id));

        if (!c.getModules().isEmpty()) {
            throw new CurriculumHasModulesException(id);
        }

        curriculumRepository.delete(c);
    }

    // Direct CurriculumProblems
    // add problem to pos -1
    @Transactional
    public CurriculumProblem addProblem(UUID curriculumId, Long problemId) {
        Curriculum c = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));
        Problem p = problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException(problemId));

        CurriculumProblem cp = c.addProblem(p);
        problemOrdering.insert(c.getProblems(), cp, -1);
        return cp;
    }

    // add problem to pos specified
    @Transactional
    public void addProblem(UUID curriculumId, Long problemId, int position) {
        Curriculum c = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));
        Problem p = problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException(problemId));

        CurriculumProblem cp = c.addProblem(p);
        problemOrdering.insert(c.getProblems(), cp, position);
    }

    // add batch of problems
    @Transactional
    public List<CurriculumProblem> addProblems(UUID curriculumId, List<Long> problemIds) {
        Curriculum c = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));

        List<Problem> ps = problemService.getProblems(problemIds);
        List<CurriculumProblem> cps = new ArrayList<>();

        for (Problem p : ps) {
            CurriculumProblem cp = c.addProblem(p);
            problemOrdering.insert(c.getProblems(), cp, -1);
            cps.add(cp);
        }
        return cps;
    }

    @Transactional
    public void moveProblem(UUID curriculumId, UUID problemId, int position) {
        Curriculum curriculum = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));
        CurriculumProblem curriculumProblem = curriculum.getProblems().stream().filter(x -> x.getId().equals(problemId))
                .findFirst()
                .orElseThrow(() -> new ProblemNotFoundException(problemId));

        problemOrdering.move(curriculum.getProblems(), curriculumProblem, position);
    }

    // remove problem
    @Transactional
    public void removeProblem(UUID curriculumId, UUID curriculumProblemId) {
        Curriculum c = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));
        CurriculumProblem cp = c.getProblems().stream().filter(i -> i.getId().equals(curriculumProblemId)).findFirst()
                .orElseThrow(() -> new CurriculumProblemNotFoundException(curriculumProblemId));

        problemOrdering.remove(c.getProblems(), cp);
    }

    // add module
    // calls moduleService
    @Transactional
    public void addModule(UUID curriculumId, UUID moduleId) {
        Curriculum curriculum = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        if (module.getCurriculum() != null) {
            throw new IllegalArgumentException("Module already belongs to a Curriculum.");
        }

        // module.setCurriculum(curriculum);
        curriculum.addModule(module);

        moduleOrdering.insert(
                curriculum.getModules(),
                module,
                -1);
    }

    @Transactional
    public Module createAndAddModule(UUID curriculumId, String name) {
        Curriculum curriculum = getCurriculum(curriculumId);

        Module module = moduleService.createModule(name);

        curriculum.addModule(module);
        moduleOrdering.insert(curriculum.getModules(), module, -1);

        return module;
    }

    @Transactional
    public void moveModule(UUID curriculumId, UUID moduleId, int position) {
        Curriculum curriculum = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));
        Module module = curriculum.getModules().stream().filter(x -> x.getId().equals(moduleId))
                .findFirst()

                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        moduleOrdering.move(curriculum.getModules(), module, position);
    }

    // remove module
    // calls moduleService
    @Transactional
    public void removeModule(UUID curriculumId, UUID moduleId) {
        Curriculum curriculum = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        if (module.getCurriculum() != curriculum) {
            throw new IllegalArgumentException("Module is not in Curriculum.");
        }

        moduleOrdering.remove(
                curriculum.getModules(),
                module);

        module.setCurriculum(null);
    }

    @Transactional
    public void toggleRevisit(
            UUID curriculumId,
            UUID curriculumProblemId) {

        Curriculum curriculum = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new CurriculumNotFoundException(curriculumId));

        CurriculumProblem curriculumProblem = curriculum.getProblems().stream()
                .filter(cp -> cp.getId().equals(curriculumProblemId))
                .findFirst()
                .orElseThrow(() -> new CurriculumProblemNotFoundException(curriculumProblemId));

        if (curriculumProblem.isRevisit()) {
            curriculumProblem.clearRevisit();
        } else {
            curriculumProblem.markForRevisit();
        }
    }

    public CurriculumStatus getStatus(
            Curriculum curriculum,
            Set<Long> solvedProblemIds) {

        int modules = curriculum.getModules().size();

        int moduleProblems = 0;
        int completedModuleProblems = 0;
        int moduleProblemsToRevisit = 0;

        for (Module module : curriculum.getModules()) {
            for (ModuleProblem mp : module.getProblems()) {
                moduleProblems++;

                if (solvedProblemIds.contains(mp.getProblem().getId())) {
                    completedModuleProblems++;
                }

                if (mp.isRevisit()) {
                    moduleProblemsToRevisit++;
                }
            }
        }

        int curriculumProblems = curriculum.getProblems().size();

        int completedCurriculumProblems = 0;
        int curriculumProblemsToRevisit = 0;

        for (CurriculumProblem cp : curriculum.getProblems()) {
            if (solvedProblemIds.contains(cp.getProblem().getId())) {
                completedCurriculumProblems++;
            }

            if (cp.isRevisit()) {
                curriculumProblemsToRevisit++;
            }
        }

        return new CurriculumStatus(
                modules,
                moduleProblems,
                completedModuleProblems,
                moduleProblemsToRevisit,
                curriculumProblems,
                completedCurriculumProblems,
                curriculumProblemsToRevisit);
    }
}
