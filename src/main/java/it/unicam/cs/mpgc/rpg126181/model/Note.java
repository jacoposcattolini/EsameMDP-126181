package it.unicam.cs.mpgc.rpg126181.model;

public class Note {

    private final double targetTime;
    private final int lane;

    private final boolean bomb;

    private boolean resolved = false;

    public Note(double targetTime, int lane) {
        this(targetTime, lane, false);
    }

    public Note(double targetTime, int lane, boolean bomb) {
        this.targetTime = targetTime;
        this.lane = lane;
        this.bomb = bomb;
    }

    public boolean isBomb() {
        return bomb;
    }

    public double getTargetTime() {
        return targetTime;
    }

    public int getLane() {
        return lane;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void resolve() {
        this.resolved = true;
    }
}
