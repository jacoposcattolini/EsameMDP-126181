package it.unicam.cs.mpgc.rpg126181.model;

public enum GameMode {
    STORY("Storia"),
    ARCADE("Arcade");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
