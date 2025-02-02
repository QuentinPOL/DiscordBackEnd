package com.example.discordbackend.model;

public enum Status {
    ONLINE("En ligne"),
    IDLE("Inactif"),
    DO_NOT_DISTURB("Ne pas déranger"),
    INVISIBLE("Invisible");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
