package it.unicam.cs.mpgc.rpg126181.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private String characterId;
    private GameMode mode;
    private int currentBossIndex;
    private List<BossScore> earnedScores = new ArrayList<>();

    private PlayerProfile profile;

    private long lastSavedEpochMillis;

    public GameState(String characterId) {
        this(characterId, GameMode.STORY);
    }

    public GameState(String characterId, GameMode mode) {
        this.characterId = characterId;
        this.mode = mode;
        this.currentBossIndex = 0;
        this.profile = new PlayerProfile();
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public GameCharacter getCharacter() {
        return GameContent.getCharacterById(characterId);
    }

    public GameMode getMode() {
        return mode == null ? GameMode.STORY : mode;
    }

}
