package it.unicam.cs.mpgc.rpg126181.model;

public enum Difficulty {
    FACILE("Facile", 0.70),
    MEDIO("Medio", 1.00),
    DIFFICILE("Difficile", 1.20);

    private final String displayName;
    private final double densityScale;

    Difficulty(String displayName, double densityScale) {
        this.displayName = displayName;
        this.densityScale = densityScale;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDensityScale() {
        return densityScale;
    }
}
