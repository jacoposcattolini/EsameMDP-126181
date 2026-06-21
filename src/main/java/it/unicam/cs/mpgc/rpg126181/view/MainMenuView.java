package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainMenuView {

    private final VBox root;

    public MainMenuView(Runnable onNewGame, Runnable onContinue, Runnable onExit) {
        this.root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        UiUtils.backgroundImage(root, "/images/schermata_home.png", UiUtils.BG);
        build(onNewGame, onContinue, onExit);
    }

    public Parent getRoot() {
        return root;
    }

    private void build(Runnable onNewGame, Runnable onContinue, Runnable onExit) {
        Label title = UiUtils.waveTitle("BEAT FIGHTER");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, 72));
        title.setTextFill(UiUtils.RED);

        Button newGame = UiUtils.coloredButton("▶  NUOVA PARTITA", "#f9f002");
        newGame.setOnAction(e -> onNewGame.run());

        Button continueGame = UiUtils.coloredButton("⏯  CONTINUA", "#00ff9f");
        continueGame.setOnAction(e -> onContinue.run());

        Button exit = UiUtils.coloredButton("⏻  ESCI DAL GIOCO", "#ff1e3c");
        exit.setOnAction(e -> onExit.run());

        VBox center = new VBox(18, title, newGame, continueGame, exit);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(640);
        center.setPadding(new Insets(40, 48, 40, 48));
        center.setStyle("-fx-background-color: #000000;"
                + "-fx-background-radius: 16;"
                + "-fx-border-color: #000000;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 16;");

        root.getChildren().add(center);
    }
}
