package com.example.leetcodetracker.modules;

import com.example.leetcodetracker.problem.OrderedProblem;
import com.example.leetcodetracker.problem.Problem;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class ModuleProblem extends OrderedProblem {

    @ManyToOne(optional = false)
    private Module module;

    private boolean revisit;

    protected ModuleProblem() {
    }

    public ModuleProblem(Module module, Problem problem, int position) {
        super(problem, position);
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public boolean isRevisit() {
        return revisit;
    }

    public void markForRevisit() {
        revisit = true;
    }

    public void clearRevisit() {
        revisit = false;
    }

}
