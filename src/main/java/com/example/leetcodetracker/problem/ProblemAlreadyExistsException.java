package com.example.leetcodetracker.problem;

public class ProblemAlreadyExistsException extends RuntimeException {

    private final String title;

    public ProblemAlreadyExistsException(String title) {
        super("Problem already exists: " + title);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
