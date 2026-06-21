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

    public GameState load(String name) {
        Path file = saveDir.resolve(sanitize(name) + EXTENSION);
        if (!Files.exists(file)) {
            return null;
        }
        try (Reader reader = Files.newBufferedReader(file)) {
            return GSON.fromJson(reader, GameState.class);
        } catch (IOException | JsonParseException e) {
            return null;
        }
    }

    public void delete(String name) throws IOException {
        Path file = saveDir.resolve(sanitize(name) + EXTENSION);
        Files.deleteIfExists(file);
    }

    public List<SaveSlot> listSaves() {
        List<SaveSlot> slots = new ArrayList<>();
        if (!Files.exists(saveDir)) {
            return slots;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(saveDir, "*" + EXTENSION)) {
            for (Path file : stream) {
                String fileName = file.getFileName().toString();
                String name = fileName.substring(0, fileName.length() - EXTENSION.length());
                GameState state = load(name);
                if (state != null) {
                    slots.add(new SaveSlot(name, state));
                }
            }
        } catch (IOException e) {
            return slots;
        }
        slots.sort(Comparator.comparingLong(
                (SaveSlot s) -> s.state().getLastSavedEpochMillis()).reversed());
        return slots;
    }

    private static String sanitize(String name) {
        String trimmed = name == null ? "" : name.trim();
        if (trimmed.isEmpty()) {
            trimmed = "partita";
        }
        return trimmed.replaceAll("[^a-zA-Z0-9 _-]", "_");
    }
}
