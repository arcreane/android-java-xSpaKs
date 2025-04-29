package com.xspaks.filmscan.model;

public class GameObject {
    private int id;
    private String name;
    private boolean validated;

    public GameObject(int id, String name, int validated) {
        this.id = id;
        this.name = name;
        this.validated = validated == 1;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isValidated() {
        return validated;
    }
}
