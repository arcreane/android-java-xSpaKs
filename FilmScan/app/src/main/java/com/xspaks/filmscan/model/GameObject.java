package com.xspaks.filmscan.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class GameObject {
    private int id;
    private String name;
    private boolean validated;
    private long createdAt;
    private long validatedAt;

    public GameObject(int id, String name, int validated, long createdAt, long validatedAt) {
        this.id = id;
        this.name = name;
        this.validated = validated == 1;
        this.createdAt = createdAt;
        this.validatedAt = validatedAt;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public long getValidatedAt() {
        return validatedAt;
    }

    public String getFormattedValidatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH'h'mm", Locale.getDefault());
        String formattedDate = sdf.format(validatedAt);
        return formattedDate;
    }
}
