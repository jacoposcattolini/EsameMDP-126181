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

        String base = slot.name() + "  —  " + charName
                + "   (" + progressText + ")   Punti: " + state.getTotalPoints();
        Label info = UiUtils.body(arcade ? base
                : base + "   XP: " + state.getProfile().getExperience());
        info.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label date = UiUtils.body(when);
        date.setTextFill(UiUtils.MUTED);

        Button load = continueButton(slot, onContinue);
        Button delete = deleteButton(slot, onDelete);

        HBox row = new HBox(14, info, date, load, delete);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-background-color: #140a26; -fx-background-radius: 6;"
                + "-fx-border-color: #2a1a44; -fx-border-width: 1; -fx-border-radius: 6;");
        return row;
    }

    private Button continueButton(SaveSlot slot, Consumer<GameState> onContinue) {
        Button play = new Button("▶");
        play.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        play.setTextFill(Color.web("#00ff9f"));
        play.setPrefSize(44, 44);
        play.setStyle("-fx-background-color: rgba(0,255,159,0.10);"
                + "-fx-border-color: #00ff9f; -fx-border-width: 2;"
                + "-fx-background-radius: 4; -fx-border-radius: 4; -fx-cursor: hand;");
        play.setOnAction(e -> onContinue.accept(slot.state()));
        return play;
    }

    private Button deleteButton(SaveSlot slot, Consumer<SaveSlot> onDelete) {
        Button delete = new Button("✕");
        delete.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        delete.setTextFill(Color.web("#ff2a6d"));
        delete.setPrefSize(44, 44);
        delete.setStyle("-fx-background-color: rgba(255,42,109,0.10);"
                + "-fx-border-color: #ff2a6d; -fx-border-width: 2;"
                + "-fx-background-radius: 4; -fx-border-radius: 4; -fx-cursor: hand;");
        delete.setOnAction(e -> onDelete.accept(slot));
        return delete;
    }
}
