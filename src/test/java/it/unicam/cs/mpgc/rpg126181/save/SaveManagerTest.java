package it.unicam.cs.mpgc.rpg126181.save;

import it.unicam.cs.mpgc.rpg126181.model.BossScore;
import it.unicam.cs.mpgc.rpg126181.model.GameMode;
import it.unicam.cs.mpgc.rpg126181.model.GameState;
import it.unicam.cs.mpgc.rpg126181.model.PlayerProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveManagerTest {

    @Test
    void salvataggioVieneScrittoInJson(@TempDir Path dir) throws IOException {
        SaveManager manager = new SaveManager(dir);
        manager.save("partita uno", new GameState("guerriero", GameMode.ARCADE));

        Path file = dir.resolve("partita uno.json");
        assertTrue(Files.exists(file));
        assertTrue(Files.readString(file).trim().startsWith("{"));
    }

    @Test
    void statoSopravviveAlRoundTripJson(@TempDir Path dir) throws IOException {
        SaveManager manager = new SaveManager(dir);
        GameState state = new GameState("guerriero", GameMode.STORY);
        state.setCurrentBossIndex(2);
        state.completeCurrentBoss(new BossScore(1500, 42.5));
        PlayerProfile profile = state.getProfile();
        profile.addExperience(100);
        profile.upgrade("guerriero", PlayerProfile.UpgradeType.HP);

        manager.save("slot", state);
        GameState loaded = manager.load("slot");

        assertNotNull(loaded);
        assertEquals("guerriero", loaded.getCharacterId());
        assertEquals(GameMode.STORY, loaded.getMode());
        assertEquals(3, loaded.getCurrentBossIndex());
        assertEquals(1, loaded.getEarnedScores().size());
        assertEquals(1500, loaded.getEarnedScores().get(0).points());
        assertEquals(42.5, loaded.getEarnedScores().get(0).timeSeconds());
        assertEquals(profile.getExperience(), loaded.getProfile().getExperience());
        assertEquals(1, loaded.getProfile().getUpgrades("guerriero").getHpLevel());
    }

    @Test
    void listaSalvataggiOrdinataDalPiuRecente(@TempDir Path dir) throws IOException, InterruptedException {
        SaveManager manager = new SaveManager(dir);
        manager.save("vecchio", new GameState("guerriero"));
        Thread.sleep(5);
        manager.save("nuovo", new GameState("bardo"));

        var slots = manager.listSaves();
        assertEquals(2, slots.size());
        assertEquals("nuovo", slots.get(0).name());
    }
}
