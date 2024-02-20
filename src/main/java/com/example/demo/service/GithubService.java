package com.example.demo.service;

import com.example.demo.exception.BranchNotFoundException;
import com.example.demo.exception.RateLimitExceededException;
import com.example.demo.exception.RepoNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.GithubRepo;
import com.example.demo.model.GithubUser;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GithubService {

    private final RestTemplate restTemplate;

    public GithubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GithubUser getGithubUser(String username) throws UserNotFoundException {
        try {
            String url = "https://api.github.com/users/" + username;
            return this.restTemplate.getForObject(url, GithubUser.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new UserNotFoundException("GitHub user not found: " + username, ex);
        }
    }

    public List<GithubRepo> getNonForkedGithubReposForUser(String username) throws BranchNotFoundException, RepoNotFoundException, RateLimitExceededException {
        try {
            String url = "https://api.github.com/users/" + username + "/repos";
            GithubRepo[] repos = Objects.requireNonNull(this.restTemplate.getForObject(url, GithubRepo[].class));

            List<GithubRepo> nonForkedRepos = new ArrayList<>();
            for (GithubRepo repo : repos) {
                if (!repo.isFork()) {
                    repo.setBranches(getBranchesForRepo(username, repo.getName()));
                    nonForkedRepos.add(repo);
                }
            }
            return nonForkedRepos;
        } catch (HttpClientErrorException.Forbidden ex) {
            throw new RateLimitExceededException("GitHub API rate limit exceeded", ex);
        }
    }

    private List<GithubRepo.Branch> getBranchesForRepo(String username, String repoName) throws BranchNotFoundException, RepoNotFoundException {
        try {
            String url = "https://api.github.com/repos/" + username + "/" + repoName + "/branches";
            GithubRepo.Branch[] branches = restTemplate.getForObject(url, GithubRepo.Branch[].class);

            if (branches == null) {
                return Collections.emptyList();
            }
            List<GithubRepo.Branch> branchList = new ArrayList<>();
            for (GithubRepo.Branch branch : branches) {
                branch.setLastCommitSha(getLastCommitSha(username, repoName, branch.getName()));
                branchList.add(branch);
            }
            return branchList;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new RepoNotFoundException("Repository not found: " + repoName, ex);
        }
    }

    private String getLastCommitSha(String username, String repoName, String branchName) throws BranchNotFoundException {
        try {
            String url = "https://api.github.com/repos/" + username + "/" + repoName + "/commits/" + branchName;
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
            JsonNode commit = response.getBody();
            return commit != null ? commit.get("sha").asText() : null;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BranchNotFoundException("Branch not found: " + branchName, ex);
        }
    }
}


