package com.example.leetcodetracker.modules;

import com.example.leetcodetracker.curriculum.Curriculum;
import com.example.leetcodetracker.ordering.Positionable;
import com.example.leetcodetracker.problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class Module implements Positionable {

    @Id
    private UUID id;

    private String name;
    private int position;

    @ManyToOne
    private Curriculum curriculum;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<ModuleProblem> problems = new ArrayList<>();

    protected Module() {
    }

    public Module(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public Module(UUID id, String name, int position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override // positionable
    public int getPosition() {
        return this.position;
    }

    @Override // positionable
    public void setPosition(int position) {
        this.position = position;
    }

    public Curriculum getCurriculum() {
        return this.curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public List<ModuleProblem> getProblems() {
        return problems;
    }

    public ModuleProblem addProblem(Problem problem) {
        if (problems.stream().anyMatch(mp -> mp.getProblem().equals(problem))) {
            throw new IllegalArgumentException("Problem is already in module.");
        }

        ModuleProblem mp = new ModuleProblem(this, problem, 0);
        problems.add(mp);
        return mp;
    }

    public void removeProblem(ModuleProblem problem) {
        if (!problems.remove(problem)) {
            throw new IllegalArgumentException("Problem is not in Module.");
        }
    }

}
