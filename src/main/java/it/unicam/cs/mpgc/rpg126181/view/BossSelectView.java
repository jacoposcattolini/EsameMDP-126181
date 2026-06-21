package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import it.unicam.cs.mpgc.rpg126181.model.Boss;
import it.unicam.cs.mpgc.rpg126181.model.BossPower;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;

import java.util.function.IntConsumer;

public class BossSelectView {

    private final VBox root;

    public BossSelectView(IntConsumer onChoose, Runnable onBack) {
        this.root = new VBox(28);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        UiUtils.backgroundImage(root, "/images/villain.png", UiUtils.BG);
        build(onChoose, onBack);
    }

    public Parent getRoot() {
        return root;
    }

    private void build(IntConsumer onChoose, Runnable onBack) {
        Label title = UiUtils.title("SCEGLI IL VILLAIN");
        title.setTextFill(Color.web("#f9f002"));

}
}
