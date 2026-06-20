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

    private static final List<Boss> BOSSES = List.of(
            new Boss(
                    "SABURO ARASAKA",
                    "Nato nei vicoli inferiori, e' stato addestrato alla lealta' assoluta. Non "
                            + "cerca il potere, ma l'equilibrio della tradizione in una citta' "
                            + "caotica. E' un'arma che non discute l'ordine, solo l'esecuzione.",
                    Difficulty.FACILE, 100, 28, 0.85, 5,
                    ROMANCE_MUSIC, ROMANCE_BPM, ROMANCE_MUSIC_OFFSET),
            new Boss(
                    "MELISSA RORY",
                    "Non controlla i vicoli, controlla i server e i flussi di dati. Gestisce il "
                            + "sindacato come un'azienda: fredda, spietata e ossessionata "
                            + "dall'efficienza. Ha ereditato l'impero criminale del padre, ma lo ha "
                            + "digitalizzato. Il suo potere non viene dalla violenza fisica, ma dalla "
                            + "ricchezza sintetica e dalla capacita' di cancellare chiunque dal "
                            + "database della citta' con un semplice gesto.",
                    Difficulty.MEDIO, 160, 40, 0.55, 10,
                    DUTTY_MUSIC, DUTTY_BPM, DUTTY_MUSIC_OFFSET, BossPower.SHRINK_NOTES),
            new Boss(
                    "FARADAY",
                    "Non e' solo un cantante; e' un profeta del caos. La sua musica e' un'arma che "
                            + "incita alla rivolta contro le corporazioni. La sua gang, i "
                            + "Synth-Rioters, usa i suoi concerti come copertura per sabotaggi e "
                            + "furti di dati. Crede sinceramente nell'anarchia, ma la sua fama lo ha "
                            + "reso dipendente dall'adrenalina e dal potere che esercita sulle masse "
                            + "oppresse.",
                    Difficulty.DIFFICILE, 220, 56, 0.40, 15,
                    STRONGER_MUSIC, STRONGER_BPM, STRONGER_MUSIC_OFFSET, BossPower.BOMB_NOTES)
    );

    private GameContent() {
    }

    public static List<GameCharacter> getCharacters() {
        return CHARACTERS;
    }

    public static GameCharacter getCharacterById(String id) {
        return CHARACTERS.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(CHARACTERS.get(0));
    }

    public static int getBossCount() {
        return BOSSES.size();
    }

    public static Boss getBoss(int index) {
        return BOSSES.get(index);
    }

}
