package it.unicam.cs.mpgc.rpg126181.model;

public class GameCharacter {

    public static final int HP_PER_LEVEL = 20;

    private final String id;
    private final String name;
    private final String power;
    private final String description;
    private final String story;
    private final Ability ability;

    private final int maxHp;
    private final double damageMultiplier;

    public GameCharacter(String id, String name, String power, String description,
                         String story, Ability ability, int maxHp, double damageMultiplier) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.description = description;
        this.story = story;
        this.ability = ability;
        this.maxHp = maxHp;
        this.damageMultiplier = damageMultiplier;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPower() {
        return power;
    }

    public String getDescription() {
        return description;
    }

    public String getStory() {
        return story;
    }

    public Ability getAbility() {
        return ability;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public int effectiveMaxHp(int hpLevel) {
        return maxHp + hpLevel * HP_PER_LEVEL;
    }
}
