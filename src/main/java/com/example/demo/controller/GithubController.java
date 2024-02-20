package com.example.demo.controller;


import com.example.demo.exception.BranchNotFoundException;
import com.example.demo.exception.RateLimitExceededException;
import com.example.demo.exception.RepoNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.GithubRepo;
import com.example.demo.model.GithubUser;
import com.example.demo.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/github")
public class GithubController {

    private final GithubService githubService;

    @Autowired
    public GithubController(GithubService gitHubService) {
        this.githubService = gitHubService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getGithubUser(@PathVariable String username) {
        try {
            GithubUser user = githubService.getGithubUser(username);
            List<GithubRepo> repos = githubService.getNonForkedGithubReposForUser(user.getLogin());
            return ResponseEntity.ok(repos);
        } catch (UserNotFoundException | RepoNotFoundException |
                 BranchNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", HttpStatus.NOT_FOUND.value(),
                    "message", ex.getMessage()
            ));
        } catch (RateLimitExceededException ex) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of(
                    "status", HttpStatus.TOO_MANY_REQUESTS.value(),
                    "message", ex.getMessage()
            ));
        }
    }



}
