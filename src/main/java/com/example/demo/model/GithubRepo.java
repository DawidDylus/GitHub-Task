package com.example.demo.model;

import java.util.List;

public class GithubRepo {

    private String name;
    private boolean fork;
    private Owner owner;
    private List<Branch> branches;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public static class Owner {
        private String login;

        // getters and setters

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }

    public static class Branch {
        private String name;
        private String lastCommitSha;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastCommitSha() {
            return lastCommitSha;
        }

        public void setLastCommitSha(String lastCommitSha) {
            this.lastCommitSha = lastCommitSha;
        }
    }
}
