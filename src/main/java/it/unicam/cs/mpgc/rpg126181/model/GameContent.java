package it.unicam.cs.mpgc.rpg126181.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GameContent {

    public static final int LANES = 4;

    public static final double LEAD_IN = 3.0;

    private static final String ROMANCE_MUSIC = "RomancePlanet.mp3";
    private static final double ROMANCE_BPM = 149.33;
    private static final double ROMANCE_MUSIC_OFFSET = 0.163;
    private static final double[][] ROMANCE_SECTIONS = {
            {0.0, 8.0, 0.6},
            {8.0, 20.0, 1.0},
            {20.0, 32.0, 1.25},
            {32.0, 44.0, 1.5},
    };

    private static final String DUTTY_MUSIC = "Dutty.mp3";
    private static final double DUTTY_BPM = 153.04;
    private static final double DUTTY_MUSIC_OFFSET = 0.076;
    private static final double[][] DUTTY_SECTIONS = {
            {0.0, 8.0, 0.5},
            {8.0, 18.0, 1.0},
            {18.0, 28.0, 1.5},
            {28.0, 36.0, 1.0},
    };

    private static final String STRONGER_MUSIC = "Stronger.mp3";
    private static final double STRONGER_BPM = 104.0;
    private static final double STRONGER_MUSIC_OFFSET = 0.241;
    private static final double[][] STRONGER_SECTIONS = {
            {0.0, 12.0, 0.5},
            {12.0, 28.0, 1.0},
            {28.0, 44.0, 1.5},
            {44.0, 60.0, 1.0},
            {60.0, 80.0, 2.0},
    };

    private static final int BASE_HP = 100;
    private static final double BASE_DAMAGE = 1.0;

    private static final List<GameCharacter> CHARACTERS = List.of(
            new GameCharacter(
                    "guerriero", "LUCY", "Backup",
                    "Stesse statistiche degli altri. Abilita': recupera vita coi colpi perfetti.",
                    "Lucy e' una ghost-runner, specializzata nel rubare segreti corporativi "
                            + "fisici. Non ruba per arricchirsi, ma per sopravvivere e per esporre "
                            + "la corruzione.",
                    Ability.HEAL_ON_PERFECT, BASE_HP, BASE_DAMAGE),
            new GameCharacter(
                    "berserker", "MAINE", "Overdrive",
                    "Stesse statistiche degli altri. Abilita': critici dopo una serie di colpi giusti.",
                    "Ha servito trent'anni nella Polizia. Ora, in pensione, offre rifugio, "
                            + "informazioni e consigli ai disperati che frequentano il suo bar. "
                            + "Anche se dice di aver chiuso con la giustizia, il suo istinto da "
                            + "poliziotto e' ancora vivo.",
                    Ability.CRIT_ON_STREAK, BASE_HP, BASE_DAMAGE),
            new GameCharacter(
                    "bardo", "DAVID", "Aimbot",
                    "Stesse statistiche degli altri. Abilita': finestra di precisione piu' ampia.",
                    "David e' cresciuto nella poverta' estrema. Conosce Lucy fin da bambino e la "
                            + "ammira per la sua audacia. Cerca disperatamente di crearsi una nuova "
                            + "vita come tecnico di droni autodidatta, riparando vecchi modelli e "
                            + "sognando di entrare in un'accademia corporativa.",
                    Ability.WIDE_HIT, BASE_HP, BASE_DAMAGE)
    );

}
