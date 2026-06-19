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

    private int currentLevel(String characterId, UpgradeType type) {
        CharacterUpgrades u = getUpgrades(characterId);
        return switch (type) {
            case HP -> u.getHpLevel();
            case ABILITY -> u.getAbilityLevel();
        };
    }

    public int costFor(String characterId, UpgradeType type) {
        return costFor(type, currentLevel(characterId, type));
    }

    public boolean isMaxLevel(String characterId, UpgradeType type) {
        return currentLevel(characterId, type) >= MAX_LEVEL - 1;
    }

    public boolean canUpgrade(String characterId, UpgradeType type) {
        return !isMaxLevel(characterId, type) && experience >= costFor(characterId, type);
    }

    public boolean upgrade(String characterId, UpgradeType type) {
        if (isMaxLevel(characterId, type)) {
            return false;
        }
        int cost = costFor(characterId, type);
        if (experience < cost) {
            return false;
        }
        experience -= cost;
        CharacterUpgrades u = getUpgrades(characterId);
        switch (type) {
            case HP -> u.incHp();
            case ABILITY -> u.incAbility();
        }
        return true;
    }
}
