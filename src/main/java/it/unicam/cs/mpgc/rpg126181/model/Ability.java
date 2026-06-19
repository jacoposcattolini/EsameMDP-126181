package it.unicam.cs.mpgc.rpg126181.model;

public enum Ability {

    WIDE_HIT("Finestra Ampia",
            "Allarga la finestra di precisione (il puntatore con cui colpisci le note): "
                    + "centrarle diventa piu' facile. +5% di ampiezza per ogni livello di "
                    + "potenziamento."),

    HEAL_ON_PERFECT("Rigenerazione",
            "Ad ogni colpo perfetto la star recupera vita: 1 punto al liv.1, +1 per ogni "
                    + "livello di potenziamento."),

    CRIT_ON_STREAK("Furia Critica",
            "Ogni 3 colpi perfetti consecutivi, il 4 e' un critico: +30% di danno al liv.1, "
                    + "+1% per ogni livello di potenziamento.");

    private final String displayName;
    private final String description;

    Ability(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String effectAt(int abilityLevel) {
        int level = abilityLevel + 1;
        return switch (this) {
            case WIDE_HIT ->
                    "Effetto attuale: finestra di precisione +" + (5 * level) + "%";
            case HEAL_ON_PERFECT ->
                    "Effetto attuale: +" + level + " vita per colpo perfetto";
            case CRIT_ON_STREAK ->
                    "Effetto attuale: critico +" + (30 + abilityLevel) + "% di danno ogni 4 colpo perfetto";
        };
    }
}
