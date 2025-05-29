package com.xspaks.filmscan.enums;

import com.xspaks.filmscan.R;

public enum GameDifficulty {
    EASY("Easy", 3, R.color.easy_color),
    MEDIUM("Medium", 6, R.color.medium_color),
    HARD("Hard", 10, R.color.hard_color);

    private final String label;
    private final int numberOfObjects;
    private final int color;
    GameDifficulty(String label, int numberOfObjects, int color) {
        this.label = label;
        this.numberOfObjects = numberOfObjects;
        this.color = color;
    }

    public static GameDifficulty getDifficultyFromNumberOfObjects(int count) {
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            if (difficulty.getNumberOfObjects() == count) {
                return difficulty;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }

    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    public int getColor() {
        return color;
    }
}