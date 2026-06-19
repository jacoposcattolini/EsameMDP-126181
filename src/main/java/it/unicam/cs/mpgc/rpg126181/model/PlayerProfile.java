package it.unicam.cs.mpgc.rpg126181.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerProfile {

    public enum UpgradeType {HP, ABILITY}

    public static final int MAX_LEVEL = 15;

    private int experience;
    private final Map<String, CharacterUpgrades> upgrades = new HashMap<>();

    public int getExperience() {
        return experience;
    }

    public void addExperience(int amount) {
        if (amount > 0) {
            experience += amount;
        }
    }

    public CharacterUpgrades getUpgrades(String characterId) {
        return upgrades.computeIfAbsent(characterId, k -> new CharacterUpgrades());
    }

    public static int costFor(UpgradeType type, int currentLevel) {
        int step = (type == UpgradeType.HP) ? 10 : 20;
        return step * (currentLevel + 1);
    }

}
