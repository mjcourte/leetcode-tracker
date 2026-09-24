package com.example.leetcodetracker.leetcode;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LeetCodeGraphQLClient implements LeetCodeClient {

    private final RestClient restClient;

    public LeetCodeGraphQLClient() {
        this.restClient = RestClient.builder().baseUrl("https://leetcode.com/graphql").build();
    }

    // nested objects that represent the response shape from leetcode
    private record StatsGraphQLResponse(
            StatsGraphQLData data) {
    }

    private record StatsGraphQLData(
            MatchedUser matchedUser) {
    }

    private record MatchedUser(
            String username,
            SubmitStats submitStats) {
    }

    private record SubmitStats(
            List<SubmissionStats> acSubmissionNum) {
    }

    @Override
    public List<SubmissionStats> getSubmissionStats(String username) {
        String query = """
                query getUserStats($username: String!) {}
                  matchedUser(username: $username) {
                    username
                    submitStats: submitStatsGlobal {
                      acSubmissionNum {
                        difficulty
                        count
                        submissions
                      }
                    }
                  }
                }
                }
                            """;
        Map<String, Object> variables = Map.of("username", username);
        GraphQLRequest request = new GraphQLRequest(query, variables);
        StatsGraphQLResponse response = restClient.post().body(request).retrieve().body(StatsGraphQLResponse.class);
        return response.data().matchedUser().submitStats().acSubmissionNum();
    }

    private record ProblemGraphQLData(
            LeetCodeProblem question) {
    }

    private record ProblemGraphQLResponse(
            ProblemGraphQLData data) {
    }

    @Override
    public LeetCodeProblem getProblem(String titleSlug) {

        System.out.println("titleSlug = [" + titleSlug + "]");
        String query = """
                query getQuestion($titleSlug: String!) {
                  question(titleSlug: $titleSlug) {
                    questionFrontendId
                    title
                    titleSlug
                    difficulty
                    isPaidOnly
                    topicTags {
                        name
                        slug
                    }
                  }
                }
                """;
        Map<String, Object> variables = Map.of("titleSlug", titleSlug);
        GraphQLRequest request = new GraphQLRequest(query, variables);
        System.out.println(request);

        ProblemGraphQLResponse response = restClient.post().header("Content-Type",
                "application/json")
                .header("Referer", "https://leetcode.com/").body(request)
                .retrieve().body(ProblemGraphQLResponse.class);

        System.out.println(response);
        // System.out.println(response.data());
        // System.out.println(response.data().question());

        return response.data().question();
        // return null;
    }
}

// topicTags {
// name
// slug
// }