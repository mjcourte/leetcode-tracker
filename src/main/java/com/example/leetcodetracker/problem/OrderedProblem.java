package com.example.leetcodetracker.problem;

import com.example.leetcodetracker.ordering.Positionable;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class OrderedProblem implements Positionable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private Problem problem;

    private int position;

    protected OrderedProblem() {
    };

    protected OrderedProblem(Problem problem) {
        this.problem = problem;
    }

    protected OrderedProblem(Problem problem, int position) {
        this.problem = problem;
        this.position = position;
    }

    public UUID getId() {
        return id;
    }

    public Problem getProblem() {
        return problem;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
