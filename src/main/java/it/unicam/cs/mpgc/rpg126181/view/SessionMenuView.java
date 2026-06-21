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

}
}
