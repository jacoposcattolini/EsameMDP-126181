package it.unicam.cs.mpgc.rpg126181.model;

public record BattleResult(int totalNotes, int perfectHits, int goodHits,
                           int missedNotes, double timeSeconds, int score,
                           boolean songCompleted) {
}
