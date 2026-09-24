// this is a data model?
package com.example.leetcodetracker.problem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

// what is jakarta? can i combine imports?
// what is "JPA"
@Entity
public class Problem {

    @Id
    private long id;

    private String title;

    @Convert(converter = ProblemDifficultyConverter.class)
    private ProblemDifficulty difficulty;

    private String titleSlug;

    @ManyToMany
    @JoinTable(name = "problem_tag", joinColumns = @JoinColumn(name = "problem_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private List<ProblemSubmission> submissions = new ArrayList<>();

    protected Problem() {
        // required by JPA?
    }

    public Problem(long id, String title, ProblemDifficulty difficulty, String titleSlug) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.titleSlug = titleSlug;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProblemDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(ProblemDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getTitleSlug() {
        return titleSlug;
    }

    public void setTitleSlug(String titleSlug) {
        this.titleSlug = titleSlug;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public List<ProblemSubmission> getSubmissions() {
        return this.submissions;
    }

    // public void addSubmission(ProblemSubmission submission) {
    // submissions.add(submission);
    // }
}