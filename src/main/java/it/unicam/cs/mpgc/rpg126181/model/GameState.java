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

    public PlayerProfile getProfile() {
        if (profile == null) {
            profile = new PlayerProfile();
        }
        return profile;
    }

    public int getCurrentBossIndex() {
        return currentBossIndex;
    }

    public void setCurrentBossIndex(int index) {
        this.currentBossIndex = index;
    }

    public boolean isFinished() {
        return currentBossIndex >= GameContent.getBossCount();
    }

    public List<BossScore> getEarnedScores() {
        if (earnedScores == null) {
            earnedScores = new ArrayList<>();
        }
        return earnedScores;
    }

    public int getTotalPoints() {
        return getEarnedScores().stream().mapToInt(BossScore::points).sum();
    }

    public void completeCurrentBoss(BossScore score) {
        getEarnedScores().add(score);
        currentBossIndex++;
    }

    public long getLastSavedEpochMillis() {
        return lastSavedEpochMillis;
    }

    public void setLastSavedEpochMillis(long lastSavedEpochMillis) {
        this.lastSavedEpochMillis = lastSavedEpochMillis;
    }
}
