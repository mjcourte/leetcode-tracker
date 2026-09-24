package com.example.leetcodetracker.problem;

import java.util.List;
import java.util.UUID;

public class ProblemNotFoundException extends RuntimeException {
    public ProblemNotFoundException(String message) {
        super(message);
    }

    public ProblemNotFoundException(long id) {
        super("Problem not found: ID " + id);
    }

    public ProblemNotFoundException(UUID id) {
        super("Problem not found: ID " + id);
    }

    public ProblemNotFoundException(List<Long> ids) {
        super("Problem(s) not found: IDs " + ids);
    }
}
