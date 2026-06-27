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

}
}
