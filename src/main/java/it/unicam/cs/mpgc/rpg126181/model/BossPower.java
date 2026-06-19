package it.unicam.cs.mpgc.rpg126181.model;

public enum BossPower {

    NONE("Nessun potere speciale."),

    SHRINK_NOTES("Sotto il 30% di vita rimpicciolisce le note, piu' difficili da colpire."),

    BOMB_NOTES("Sotto il 30% di vita lancia note bomba: non colpirle o subisci danno.");

    public static final double THRESHOLD = 0.30;

    private final String description;

    BossPower(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
