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

    public Parent getRoot() {
        return root;
    }

    private void build(List<SaveSlot> saves, Consumer<GameState> onContinue,
                       Consumer<SaveSlot> onDelete, Runnable onBack) {
        Label title = UiUtils.title("SALVATAGGI");

        VBox list = new VBox(14);
        list.setAlignment(Pos.TOP_CENTER);
        list.setPadding(new Insets(10));

        if (saves.isEmpty()) {
            Label empty = UiUtils.body("Nessun salvataggio disponibile.");
            empty.setTextFill(UiUtils.MUTED);
            list.getChildren().add(empty);
        } else {
            for (SaveSlot slot : saves) {
                list.getChildren().add(buildSaveRow(slot, onContinue, onDelete));
            }
        }

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(360);
        scroll.setMaxWidth(820);
        scroll.setStyle("-fx-background: #07060f; -fx-background-color: transparent;");

        Button back = UiUtils.backButton("← INDIETRO");
        back.setOnAction(e -> onBack.run());

        root.getChildren().addAll(title, scroll, back);
    }

    private HBox buildSaveRow(SaveSlot slot, Consumer<GameState> onContinue, Consumer<SaveSlot> onDelete) {
        GameState state = slot.state();
        String charName = state.getCharacter().getName();
        int progress = state.getCurrentBossIndex();
        int total = GameContent.getBossCount();
        String when = state.getLastSavedEpochMillis() > 0
                ? FORMATTER.format(Instant.ofEpochMilli(state.getLastSavedEpochMillis()))
                : "";
        boolean arcade = state.getMode() == GameMode.ARCADE;
        String progressText = arcade
                ? "Arcade"
                : "Storia · VILLAIN " + Math.min(progress + 1, total) + "/" + total;

}
}
