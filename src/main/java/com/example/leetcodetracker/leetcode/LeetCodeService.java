package com.example.leetcodetracker.leetcode;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LeetCodeService {

    private final LeetCodeClient leetCodeClient;

    public LeetCodeService(LeetCodeClient leetCodeClient) {
        this.leetCodeClient = leetCodeClient;
    }

    public List<SubmissionStats> getSubmissionStats(String username) {
        return leetCodeClient.getSubmissionStats(username);
    }

    // get from exact slug
    public LeetCodeProblem getProblem(String titleSlug) {
        return leetCodeClient.getProblem(titleSlug);
    }

    private String slugFromUrl(String input) {
        URI uri = URI.create(input);

        String[] parts = uri.getPath().split("/");

        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals("problems")) {
                return parts[i + 1];
            }
        }

        throw new IllegalArgumentException("Not a LeetCode problem URL: " + input);
    }

    private String toProblemSlug(String input) {
        String value = input.trim();

        if (value.startsWith("http://") || value.startsWith("https://")) {
            return slugFromUrl(value);
        }
        // Strip "1. ", "53. ", etc.
        value = value.replaceFirst("^\\d+\\.\\s*", "");

        return value
                .toLowerCase()
                .replaceAll("\\s+", "-");
    }

    // get via slug normalizer
    public LeetCodeProblem fetchProblem(String input) {
        String slug = toProblemSlug(input);
        return leetCodeClient.getProblem(slug);
    }

}
