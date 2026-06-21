package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import it.unicam.cs.mpgc.rpg126181.model.BossScore;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.GameState;
import it.unicam.cs.mpgc.rpg126181.model.Score;

public class EndView {

    private final VBox root;

    public EndView(GameState state, Runnable onMenu) {
        this.root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        UiUtils.fillBackground(root, UiUtils.BG);
        build(state, onMenu);
    }

    public Parent getRoot() {
        return root;
    }

}
