package com.example.leetcodetracker.curriculum;

import com.example.leetcodetracker.problem.OrderedProblem;
import com.example.leetcodetracker.problem.Problem;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class CurriculumProblem extends OrderedProblem {

    @ManyToOne(optional = false)
    private Curriculum curriculum;

    private boolean revisit;

    protected CurriculumProblem() {
    }

    public CurriculumProblem(Curriculum curriculum, Problem problem) {
        super(problem);
        this.curriculum = curriculum;
    }

    public CurriculumProblem(Curriculum curriculum, Problem problem, int position) {
        super(problem, position);
        this.curriculum = curriculum;
    }

    public Curriculum getCurriculum() {
        return curriculum;
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
