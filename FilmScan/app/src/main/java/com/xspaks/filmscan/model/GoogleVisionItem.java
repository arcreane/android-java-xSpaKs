package com.xspaks.filmscan.model;

public class GoogleVisionItem {
    private String description;
    private double score;

    public GoogleVisionItem(String description, double score) {
        this.description = description;
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public double getScore() {
        return score;
    }
}
