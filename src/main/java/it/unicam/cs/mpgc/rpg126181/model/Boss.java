package it.unicam.cs.mpgc.rpg126181.model;

public class Boss {

    private final String name;
    private final String story;
    private final Difficulty difficulty;
    private final int maxHp;
    private final int noteCount;
    private final double noteIntervalSeconds;
    private final int playerDamagePerMiss;

    private final String musicResource;
    private final double bpm;
    private final double musicOffsetSeconds;

    private final BossPower power;

    public Boss(String name, String story, Difficulty difficulty, int maxHp, int noteCount,
                double noteIntervalSeconds, int playerDamagePerMiss) {
        this(name, story, difficulty, maxHp, noteCount, noteIntervalSeconds,
                playerDamagePerMiss, null, 0, 0, BossPower.NONE);
    }

    public Boss(String name, String story, Difficulty difficulty, int maxHp, int noteCount,
                double noteIntervalSeconds, int playerDamagePerMiss,
                String musicResource, double bpm, double musicOffsetSeconds) {
        this(name, story, difficulty, maxHp, noteCount, noteIntervalSeconds,
                playerDamagePerMiss, musicResource, bpm, musicOffsetSeconds, BossPower.NONE);
    }

    public Boss(String name, String story, Difficulty difficulty, int maxHp, int noteCount,
                double noteIntervalSeconds, int playerDamagePerMiss,
                String musicResource, double bpm, double musicOffsetSeconds, BossPower power) {
        this.name = name;
        this.story = story;
        this.difficulty = difficulty;
        this.maxHp = maxHp;
        this.noteCount = noteCount;
        this.noteIntervalSeconds = noteIntervalSeconds;
        this.playerDamagePerMiss = playerDamagePerMiss;
        this.musicResource = musicResource;
        this.bpm = bpm;
        this.musicOffsetSeconds = musicOffsetSeconds;
        this.power = power == null ? BossPower.NONE : power;
    }

    public String getName() {
        return name;
    }

    public String getStory() {
        return story;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public double getNoteIntervalSeconds() {
        return noteIntervalSeconds;
    }

    public int getPlayerDamagePerMiss() {
        return playerDamagePerMiss;
    }

    public String getMusicResource() {
        return musicResource;
    }

    public double getBpm() {
        return bpm;
    }

    public double getMusicOffsetSeconds() {
        return musicOffsetSeconds;
    }

    public boolean hasMusic() {
        return musicResource != null;
    }

    public BossPower getPower() {
        return power;
    }
}
