package com.xspaks.filmscan.model;

import com.xspaks.filmscan.enums.GameDifficulty;

public class Score {
    private int id;
    private String username;
    private int points;
    private GameDifficulty difficulty;
    private long createdAt;

    public Score(int id, String username, int points, GameDifficulty difficuty, long createdAt) {
        this.id = id;
        this.username = username;
        this.points = points;
        this.difficulty = difficulty;
        this.createdAt = createdAt;
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

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
