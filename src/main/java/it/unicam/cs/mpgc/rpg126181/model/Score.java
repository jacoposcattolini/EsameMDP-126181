package it.unicam.cs.mpgc.rpg126181.model;

public final class Score {

    public static final int PERFECT_POINTS = 100;
    public static final int GOOD_POINTS = 50;
    public static final int CRIT_BONUS = 50;

    private Score() {
    }

    public static int points(int perfectHits, int goodHits) {
        return perfectHits * PERFECT_POINTS + goodHits * GOOD_POINTS;
    }

    public static String formatTime(double seconds) {
        int total = (int) Math.round(seconds);
        return String.format("%d:%02d", total / 60, total % 60);
    }
}
