package it.unicam.cs.mpgc.rpg126181.controller;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import it.unicam.cs.mpgc.rpg126181.model.BossScore;
import it.unicam.cs.mpgc.rpg126181.model.GameCharacter;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.GameMode;
import it.unicam.cs.mpgc.rpg126181.model.GameState;
import it.unicam.cs.mpgc.rpg126181.model.PlayerProfile;
import it.unicam.cs.mpgc.rpg126181.save.SaveManager;
import it.unicam.cs.mpgc.rpg126181.save.SaveSlot;
import it.unicam.cs.mpgc.rpg126181.view.BossSelectView;
import it.unicam.cs.mpgc.rpg126181.view.CharacterSelectView;
import it.unicam.cs.mpgc.rpg126181.view.EndView;
import it.unicam.cs.mpgc.rpg126181.view.LoadGameView;
import it.unicam.cs.mpgc.rpg126181.view.MainMenuView;
import it.unicam.cs.mpgc.rpg126181.view.ModeSelectView;
import it.unicam.cs.mpgc.rpg126181.view.SessionMenuView;
import it.unicam.cs.mpgc.rpg126181.view.UpgradeView;

import java.util.List;
import java.util.Optional;

public class GameController {

    private final Stage stage;
    private final SaveManager saveManager = new SaveManager();

    public GameController(Stage stage) {
        this.stage = stage;
    }

    public void start() {
        stage.setTitle("Beat Fighter");
        stage.setScene(new Scene(new StackPane(), 1280, 720));
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        showMainMenu();
        stage.show();
    }

    public void setRoot(Parent root) {
        stage.getScene().setRoot(root);
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public void showMainMenu() {
        setRoot(new MainMenuView(this::startNewGame, this::showLoadGame, this::confirmExit).getRoot());
    }

    public void showLoadGame() {
        List<SaveSlot> saves = saveManager.listSaves();
        setRoot(new LoadGameView(saves, this::continueGame, this::confirmDelete, this::showMainMenu).getRoot());
    }

    public void exitGame() {
        Platform.exit();
    }

    private void confirmExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Esci dal gioco");
        alert.setHeaderText("Vuoi davvero uscire dal gioco?");
        alert.setContentText("I progressi non salvati andranno persi.");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.YES) {
            exitGame();
        }
    }

}
