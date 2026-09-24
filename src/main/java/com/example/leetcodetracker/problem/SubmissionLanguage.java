package com.example.leetcodetracker.problem;

// Define whatever you want in here...
public enum SubmissionLanguage {
    PYTHON("Python"),
    JAVA("Java"),
    RUST("Rust"),
    CPP("C++"),
    JAVASCRIPT("JavaSCript"),
    TYPESCRIPT("TypeScript");

    private final String displayName;

    SubmissionLanguage(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
