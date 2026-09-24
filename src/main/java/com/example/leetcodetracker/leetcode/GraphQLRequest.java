package com.example.leetcodetracker.leetcode;

import java.util.Map;

public record GraphQLRequest(
                String query, Map<String, Object> variables) {
}
