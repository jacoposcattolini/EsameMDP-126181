package it.unicam.cs.mpgc.rpg126181.model;

public class CharacterUpgrades {

    private int hpLevel;
    private int abilityLevel;

    public int getHpLevel() {
        return hpLevel;
    }

    public int getAbilityLevel() {
        return abilityLevel;
    }

    public void incHp() {
        hpLevel++;
    }

    public void incAbility() {
        abilityLevel++;
    }
}
