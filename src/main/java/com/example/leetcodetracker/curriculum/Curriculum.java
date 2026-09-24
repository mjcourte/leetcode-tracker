package com.example.leetcodetracker.curriculum;

import com.example.leetcodetracker.problem.Problem;
import com.example.leetcodetracker.modules.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Curriculum {

    @Id
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "curriculum")
    private List<Module> modules = new ArrayList<>();

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurriculumProblem> problems = new ArrayList<>();

    protected Curriculum() {
    }

    public Curriculum(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public Curriculum(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    // "direct" problem associations
    public List<CurriculumProblem> getProblems() {
        return problems;
    }

    public CurriculumProblem addProblem(Problem problem) {
        if (problems.stream().anyMatch(cp -> cp.getProblem().equals(problem))) {
            throw new IllegalArgumentException("Problem is already in Curriculum.");
        }

        return new CurriculumProblem(this, problem);
    }

    public void removeProblem(CurriculumProblem problem) {
        if (!problems.remove(problem)) {
            throw new IllegalArgumentException("Problem is not in Curriculum.");
        }
    }

    // associated modules
    public List<Module> getModules() {
        return this.modules;
    }

    public Module addModule(Module module) {
        if (modules.contains(module)) {
            throw new IllegalArgumentException("Module is already in Curriculum.");
        }

        module.setCurriculum(this);
        // modules.add(module);
        return module;
    }

    public void removeModule(Module module) {
        if (!modules.remove(module)) {
            throw new IllegalArgumentException("Module is not in Curriculum.");
        }
        module.setCurriculum(null);
    }
}
