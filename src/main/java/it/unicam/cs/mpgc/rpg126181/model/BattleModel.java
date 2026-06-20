package it.unicam.cs.mpgc.rpg126181.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleModel {

    public enum HitType {WRONG, BOMB, GOOD, PERFECT, CRIT}

    public record HitOutcome(HitType type, int lane, boolean healed) {
    }

    public record AdvanceResult(boolean anyMiss, boolean playerDied, boolean notesExhaustedLoss) {
    }

    public static final double APPROACH_TIME = 1.9;

    private static final int XP_GOOD = 1;
    private static final int XP_PERFECT = 5;
    private static final double ERROR_XP_PENALTY = XP_GOOD / 2.0;

    private static final int HP_PER_NOTE = 75;
    private static final double PERFECT_DAMAGE = 100;
    private static final double GOOD_DAMAGE = 50;

    private static final double BOMB_INTERVAL = 1.4;
    private static final double BOMB_CLEARANCE = 0.35;

    private final GameState state;
    private final GameCharacter character;
    private final Boss boss;
    private final boolean arcade;

    private List<Note> notes;
    private int totalNotes;

    private double gameTime = 0;

    private int playerHp;
    private final int playerMaxHp;
    private double bossHp;
    private int bossMaxHp;
    private int perfectHits = 0;
    private int goodHits = 0;
    private int errors = 0;
    private int resolvedCount = 0;
    private int score = 0;

    private final double goodDamage = GOOD_DAMAGE;
    private final double perfectDamage = PERFECT_DAMAGE;

    private final double perfectWindow;
    private final double goodWindow;

    private final PlayerProfile profile;
    private final Ability ability;
    private final int healPerPerfect;
    private final double critMultiplier;
    private int perfectStreak = 0;
    private int streak = 0;
    private int bestStreak = 0;
    private double xpEarned = 0;
    private boolean xpAwarded = false;
    private final Random rng = new Random();

    private boolean bombsActive = false;
    private double nextBombTime = 0;

    private boolean keepPlayingAfterDefeat = false;

    public BattleModel(GameState state) {
        this.state = state;
        this.arcade = state.getMode() == GameMode.ARCADE;
        this.character = state.getCharacter();
        this.boss = GameContent.getBoss(state.getCurrentBossIndex());

        this.profile = state.getProfile();
        CharacterUpgrades upgrades = profile.getUpgrades(character.getId());
        this.ability = character.getAbility();
        int abilityLevel = upgrades.getAbilityLevel();

        this.playerMaxHp = character.effectiveMaxHp(upgrades.getHpLevel());
        this.playerHp = playerMaxHp;

        int abilityDisplayLevel = abilityLevel + 1;

        double windowMult = (ability == Ability.WIDE_HIT)
                ? 1.0 + 0.05 * abilityDisplayLevel : 1.0;
        this.perfectWindow = 0.06 * windowMult;
        this.goodWindow = 0.14 * windowMult;

        this.healPerPerfect = (ability == Ability.HEAL_ON_PERFECT) ? abilityDisplayLevel : 0;
        this.critMultiplier = (ability == Ability.CRIT_ON_STREAK)
                ? 1.0 + (0.30 + 0.01 * (abilityDisplayLevel - 1)) : 0;

        this.notes = GameContent.generateChart(state.getCurrentBossIndex());
        this.totalNotes = notes.size();
        this.bossMaxHp = totalNotes * HP_PER_NOTE;
        this.bossHp = bossMaxHp;
    }

    public void regenerateChartForDuration(double durationSeconds) {
        if (durationSeconds > 0 && Double.isFinite(durationSeconds)) {
            notes = GameContent.generateMusicChart(boss, durationSeconds);
            totalNotes = notes.size();
            double ratio = bossMaxHp > 0 ? bossHp / bossMaxHp : 1.0;
            bossMaxHp = totalNotes * HP_PER_NOTE;
            bossHp = bossMaxHp * ratio;
        }
    }

    public HitOutcome hitLane(int lane) {
        Note best = null;
        double bestDelta = Double.MAX_VALUE;
        for (Note note : notes) {
            if (note.isResolved() || note.getLane() != lane) {
                continue;
            }
            double delta = Math.abs(note.getTargetTime() - gameTime);
            if (delta < bestDelta) {
                bestDelta = delta;
                best = note;
            }
        }
        if (best != null && bestDelta <= goodWindow) {
            if (best.isBomb()) {
                best.resolve();
                streak = 0;
                perfectStreak = 0;
                penalizeXp();
                damagePlayer();
                return new HitOutcome(HitType.BOMB, lane, false);
            }
            best.resolve();
            resolvedCount++;
            streak++;
            bestStreak = Math.max(bestStreak, streak);
            boolean perfect = bestDelta <= perfectWindow;

            double dmg = perfect ? perfectDamage : goodDamage;
            boolean crit = false;
            if (ability == Ability.CRIT_ON_STREAK) {
                if (perfect) {
                    perfectStreak++;
                    if (perfectStreak >= 4) {
                        crit = true;
                        perfectStreak = 0;
                    }
                } else {
                    perfectStreak = 0;
                }
            }
            if (crit) {
                dmg = perfectDamage * critMultiplier;
            }

            boolean healed = false;
            if (perfect) {
                perfectHits++;
                xpEarned += XP_PERFECT;
                score += Score.PERFECT_POINTS;
                if (ability == Ability.HEAL_ON_PERFECT && healPerPerfect > 0) {
                    heal(healPerPerfect);
                    healed = true;
                }
            } else {
                goodHits++;
                xpEarned += XP_GOOD;
                score += Score.GOOD_POINTS;
            }
            if (crit) {
                score += Score.CRIT_BONUS;
            }
            damageBoss(dmg);

            if (crit) {
                return new HitOutcome(HitType.CRIT, lane, false);
            } else if (perfect) {
                return new HitOutcome(HitType.PERFECT, lane, healed);
            } else {
                return new HitOutcome(HitType.GOOD, lane, false);
            }
        }
        errors++;
        streak = 0;
        perfectStreak = 0;
        penalizeXp();
        damagePlayer();
        return new HitOutcome(HitType.WRONG, lane, false);
    }

    public AdvanceResult advance(double dt) {
        gameTime += dt;

        if (bombsActive && gameTime >= nextBombTime) {
            double targetTime = gameTime + APPROACH_TIME;
            int lane = pickFreeBombLane(targetTime);
            if (lane >= 0) {
                notes.add(new Note(targetTime, lane, true));
                nextBombTime = gameTime + BOMB_INTERVAL;
            } else {
                nextBombTime = gameTime + 0.2;
            }
        }

        boolean anyMiss = false;
        boolean playerDied = false;
        for (Note note : notes) {
            if (!note.isResolved() && note.getTargetTime() < gameTime - goodWindow) {
                note.resolve();
                if (note.isBomb()) {
                    continue;
                }
                resolvedCount++;
                errors++;
                streak = 0;
                perfectStreak = 0;
                penalizeXp();
                damagePlayer();
                anyMiss = true;
                if (isPlayerDead()) {
                    playerDied = true;
                    break;
                }
            }
        }
        boolean notesExhaustedLoss = !playerDied && !boss.hasMusic()
                && resolvedCount >= totalNotes && bossHp > 0;
        return new AdvanceResult(anyMiss, playerDied, notesExhaustedLoss);
    }

    private void heal(int amount) {
        playerHp = Math.min(playerMaxHp, playerHp + amount);
    }

    private void penalizeXp() {
        xpEarned = Math.max(0, xpEarned - ERROR_XP_PENALTY);
    }

    private void damageBoss(double amount) {
        bossHp = Math.max(0, bossHp - amount);
        if (boss.getPower() == BossPower.BOMB_NOTES && !bombsActive
                && bossHp <= bossMaxHp * BossPower.THRESHOLD) {
            bombsActive = true;
            nextBombTime = gameTime + 0.6;
        }
    }

    private void damagePlayer() {
        if (arcade || keepPlayingAfterDefeat) {
            return;
        }
        playerHp = Math.max(0, playerHp - boss.getPlayerDamagePerMiss());
    }

    private int pickFreeBombLane(double targetTime) {
        List<Integer> free = new ArrayList<>();
        for (int lane = 0; lane < GameContent.LANES; lane++) {
            boolean busy = false;
            for (Note n : notes) {
                if (n.isResolved() || n.getLane() != lane) {
                    continue;
                }
                if (Math.abs(n.getTargetTime() - targetTime) < BOMB_CLEARANCE) {
                    busy = true;
                    break;
                }
            }
            if (!busy) {
                free.add(lane);
            }
        }
        return free.isEmpty() ? -1 : free.get(rng.nextInt(free.size()));
    }

    public boolean notesShrunk() {
        return boss.getPower() == BossPower.SHRINK_NOTES
                && bossHp <= bossMaxHp * BossPower.THRESHOLD;
    }

}
