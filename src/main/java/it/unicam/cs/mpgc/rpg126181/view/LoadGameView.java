package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.GameMode;
import it.unicam.cs.mpgc.rpg126181.model.GameState;
import it.unicam.cs.mpgc.rpg126181.save.SaveSlot;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class
LoadGameView {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

    private final VBox root;

    public LoadGameView(List<SaveSlot> saves, Consumer<GameState> onContinue,
                        Consumer<SaveSlot> onDelete, Runnable onBack) {
        this.root = new VBox(28);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        UiUtils.backgroundImage(root, "/images/salvataggio.png", UiUtils.BG);
        build(saves, onContinue, onDelete, onBack);
    }

}
