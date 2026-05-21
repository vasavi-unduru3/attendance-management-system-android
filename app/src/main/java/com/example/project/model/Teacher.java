package com.example.project.model;

public class Teacher {
    private String username;
    private String password;
    private String section;

    public Teacher() {
        // Default constructor required for Firebase
    }

    public Teacher(String username, String password, String section) {
        this.username = username;
        this.password = password;
        this.section = section;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
