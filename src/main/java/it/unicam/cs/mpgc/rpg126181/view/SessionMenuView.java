package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.GameMode;
import it.unicam.cs.mpgc.rpg126181.model.GameState;

public class SessionMenuView {

    private final VBox root;

    public SessionMenuView(GameState state, boolean won, Runnable onUpgrade, Runnable onRetry,
                           Runnable onProceed, Runnable onSave, Runnable onSaveExit,
                           Runnable onExitNoSave) {
        this.root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        UiUtils.backgroundImage(root, "/images/sessione.png", UiUtils.BG);
        build(state, won, onUpgrade, onRetry, onProceed, onSave, onSaveExit, onExitNoSave);
    }

    public Parent getRoot() {
        return root;
    }

    private void build(GameState state, boolean won, Runnable onUpgrade, Runnable onRetry,
                       Runnable onProceed, Runnable onSave, Runnable onSaveExit,
                       Runnable onExitNoSave) {
        Label title = UiUtils.waveTitle("Menu");
        title.setTextFill(UiUtils.RED);
        boolean arcade = state.getMode() == GameMode.ARCADE;

        VBox buttons = new VBox(14);
        buttons.setAlignment(Pos.CENTER);
        buttons.setMaxWidth(520);

        if (!arcade) {
            Button upgrade = UiUtils.coloredButton("⚡  POTENZIA LE STAR", "#f9f002");
            upgrade.setOnAction(e -> onUpgrade.run());
            buttons.getChildren().add(upgrade);
        }

        Button retry = UiUtils.coloredButton("↻  RIPROVA", "#a020f0");
        retry.setOnAction(e -> onRetry.run());
        buttons.getChildren().add(retry);

        if (arcade) {
            Button other = UiUtils.secondaryButton("🦹  SCEGLI UN ALTRO VILLAIN");
            other.setOnAction(e -> onProceed.run());
            buttons.getChildren().add(other);
        } else if (won) {
            boolean lastBoss = state.getCurrentBossIndex() >= GameContent.getBossCount() - 1;
            Button next = UiUtils.coloredButton(lastBoss ? "🏁  VEDI IL FINALE" : "▶  PROSSIMO VILLAIN", "#9dff00");
            next.setOnAction(e -> onProceed.run());
            buttons.getChildren().add(next);
        }

        Button save = UiUtils.coloredButton("💾  SALVA", "#05d9e8");
        save.setOnAction(e -> onSave.run());
        buttons.getChildren().add(save);

        Button saveExit = UiUtils.coloredButton("💾  SALVA ED ESCI", "#05d9e8");
        saveExit.setOnAction(e -> onSaveExit.run());
        buttons.getChildren().add(saveExit);

        Button exitNoSave = UiUtils.coloredButton("✖  ESCI SENZA SALVARE", "#ff1e3c");
        exitNoSave.setOnAction(e -> onExitNoSave.run());
        buttons.getChildren().add(exitNoSave);

        VBox panel;
        if (arcade) {
            panel = new VBox(18, title, buttons);
        } else {
            Label xp = new Label("XP della partita: " + state.getProfile().getExperience());
            xp.setFont(Font.font("Consolas", FontWeight.BOLD, 22));
            xp.setTextFill(UiUtils.GREEN);
            panel = new VBox(18, title, xp, buttons);
        }
        panel.setAlignment(Pos.CENTER);
        panel.setMaxWidth(620);
        panel.setPadding(new Insets(36, 48, 36, 48));
        panel.setStyle("-fx-background-color: #000000;"
                + "-fx-background-radius: 16; -fx-border-color: #000000;"
                + "-fx-border-width: 2; -fx-border-radius: 16;");

        root.getChildren().add(panel);
    }
}
