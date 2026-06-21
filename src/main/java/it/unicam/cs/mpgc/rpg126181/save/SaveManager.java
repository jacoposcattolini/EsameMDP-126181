package it.unicam.cs.mpgc.rpg126181.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import it.unicam.cs.mpgc.rpg126181.model.GameState;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SaveManager {

    private static final String EXTENSION = ".json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path saveDir;

    public SaveManager() {
        this(Paths.get(System.getProperty("user.home"), ".beatfighter", "saves"));
    }

    public SaveManager(Path saveDir) {
        this.saveDir = saveDir;
    }

    private void ensureDir() throws IOException {
        if (!Files.exists(saveDir)) {
            Files.createDirectories(saveDir);
        }
    }

    public void save(String name, GameState state) throws IOException {
        ensureDir();
        state.setLastSavedEpochMillis(System.currentTimeMillis());
        Path file = saveDir.resolve(sanitize(name) + EXTENSION);
        try (Writer writer = Files.newBufferedWriter(file)) {
            GSON.toJson(state, writer);
        }
    }

}
