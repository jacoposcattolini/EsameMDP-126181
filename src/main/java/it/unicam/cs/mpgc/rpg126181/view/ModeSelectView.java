package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ModeSelectView {

    private final VBox root;

    public ModeSelectView(Runnable onStory, Runnable onArcade, Runnable onBack) {
        this.root = new VBox(28);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        UiUtils.backgroundImage(root, "/images/scelta.png", UiUtils.BG);
        build(onStory, onArcade, onBack);
    }

    public Parent getRoot() {
        return root;
    }

    private void build(Runnable onStory, Runnable onArcade, Runnable onBack) {
        Label title = UiUtils.title("SCEGLI LA MODALITA'");

        Button story = UiUtils.coloredButton("📖  STORIA", "#a020f0");
        story.setOnAction(e -> onStory.run());
        Label storyDesc = UiUtils.body("Affronta i tre VILLAIN in sequenza, con storia e progressione.");
        storyDesc.setTextFill(UiUtils.MUTED);

        Button arcade = UiUtils.secondaryButton("🕹  ARCADE");
        arcade.setOnAction(e -> onArcade.run());
        Label arcadeDesc = UiUtils.body("Scegli liberamente quale VILLAIN affrontare senza "
                + "dover prendere danno e potenziare le STAR");
        arcadeDesc.setTextFill(UiUtils.MUTED);

        Button back = UiUtils.backButton("← INDIETRO");
        back.setOnAction(e -> onBack.run());

        Region gap = new Region();
        gap.setMinHeight(28);
        Region titleGap = new Region();
        titleGap.setMinHeight(22);

        VBox box = new VBox(16, title, titleGap, story, storyDesc, gap, arcade, arcadeDesc,
                new Region(), back);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(720);
        box.setPadding(new Insets(36, 48, 36, 48));
        box.setStyle("-fx-background-color: #000000;"
                + "-fx-background-radius: 12; -fx-border-color: #000000;"
                + "-fx-border-width: 2; -fx-border-radius: 12;");
        root.getChildren().add(box);
    }
}
