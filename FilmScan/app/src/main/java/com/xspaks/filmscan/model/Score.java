package com.xspaks.filmscan.model;

public class Score {
    private int id;
    private String username;
    private int points;

    public Score(int id, String username, int points) {
        this.id = id;
        this.username = username;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }
}
