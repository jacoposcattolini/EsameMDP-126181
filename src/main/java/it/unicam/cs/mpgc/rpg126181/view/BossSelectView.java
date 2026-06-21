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

        HBox cards = new HBox(24);
        cards.setAlignment(Pos.CENTER);
        for (int i = 0; i < GameContent.getBossCount(); i++) {
            cards.getChildren().add(buildCard(i, onChoose));
        }

        Button back = UiUtils.backButton("← INDIETRO");
        back.setOnAction(e -> onBack.run());

        root.getChildren().addAll(title, cards, back);
    }

    private VBox buildCard(int index, IntConsumer onChoose) {
        Boss boss = GameContent.getBoss(index);

        Label name = new Label(boss.getName());
        name.setFont(Font.font("Consolas", FontWeight.BOLD, 22));
        name.setTextFill(UiUtils.NEON2);
        name.setWrapText(true);

        Label info = UiUtils.body("Difficolta': " + boss.getDifficulty().getDisplayName());
        info.setTextFill(UiUtils.MUTED);

        String powerText = boss.getPower() == BossPower.NONE
                ? boss.getPower().getDescription()
                : "Potere: " + boss.getPower().getDescription();
        Label power = UiUtils.body(powerText);
        power.setMaxWidth(240);
        power.setTextFill(UiUtils.GREEN);

        Button fight = UiUtils.primaryButton("Scegli");
        fight.setPrefWidth(220);
        fight.setOnAction(e -> onChoose.accept(index));

        VBox card = new VBox(14, name, info, power, fight);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(22));
        card.setPrefWidth(280);
        card.setPrefHeight(300);
        card.setStyle("-fx-background-color: #140a26; -fx-background-radius: 6;"
                + "-fx-border-color: #05d9e8; -fx-border-width: 2; -fx-border-radius: 6;");
        UiUtils.glow(card, UiUtils.NEON, 10);
        return card;
    }
}
