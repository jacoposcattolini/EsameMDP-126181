package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import it.unicam.cs.mpgc.rpg126181.model.CharacterUpgrades;
import it.unicam.cs.mpgc.rpg126181.model.GameCharacter;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.PlayerProfile;

import java.util.function.Consumer;

public class CharacterSelectView {

    private final String headerText;
    private final PlayerProfile profile;
    private final boolean showStories;
    private final Consumer<GameCharacter> onChoose;
    private final Runnable onBack;
    private final VBox root;

    public CharacterSelectView(String headerText, PlayerProfile profile, boolean showStories,
                               Consumer<GameCharacter> onChoose, Runnable onBack) {
        this.headerText = headerText;
        this.profile = profile;
        this.showStories = showStories;
        this.onChoose = onChoose;
        this.onBack = onBack;
        this.root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(36, 50, showStories ? 14 : 40, 50));
        UiUtils.backgroundImage(root, "/images/personaggi.png", UiUtils.BG);
        build();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        Label title = UiUtils.title(headerText);
        title.setStyle("-fx-background-radius: 8; -fx-padding: 10 28 10 28;");

        HBox cards = new HBox(30);
        cards.setAlignment(Pos.BOTTOM_CENTER);
        for (GameCharacter character : GameContent.getCharacters()) {
            cards.getChildren().add(buildCard(character));
        }

        Button back = UiUtils.backButton("← Indietro");
        back.setOnAction(e -> onBack.run());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(title, spacer, cards, back);
    }

    private VBox buildCard(GameCharacter character) {
        Label name = new Label(character.getName());
        name.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        name.setTextFill(UiUtils.ACCENT);

        Label power = UiUtils.body("Potere: " + character.getPower());
        power.setTextFill(Color.web("#ffd166"));

        int hpLevel = 0;
        int abilityLevel = 0;
        if (profile != null) {
            CharacterUpgrades u = profile.getUpgrades(character.getId());
            hpLevel = u.getHpLevel();
            abilityLevel = u.getAbilityLevel();
        }

        double textWidth = showStories ? 312 : 280;

        Label powerDesc = UiUtils.body(character.getAbility().getDescription()
                + "  (Abilita' liv. " + (abilityLevel + 1) + ")");
        powerDesc.setWrapText(true);
        powerDesc.setMaxWidth(textWidth);
        powerDesc.setTextAlignment(TextAlignment.CENTER);
        powerDesc.setTextFill(UiUtils.NEON);

        Label stats = UiUtils.body("❤ HP: " + character.effectiveMaxHp(hpLevel)
                + "  (Vita liv. " + (hpLevel + 1) + ")");

        Button choose = UiUtils.primaryButton("Scegli");
        choose.setPrefWidth(220);
        choose.setOnAction(e -> onChoose.accept(character));

        VBox card = new VBox(12, name, power, powerDesc, stats);

        if (showStories) {
            Label story = UiUtils.body(character.getStory());
            story.setWrapText(true);
            story.setMaxWidth(textWidth);
            story.setTextAlignment(TextAlignment.CENTER);
            story.setTextFill(UiUtils.MUTED);
            story.setStyle("-fx-font-size: 13;");
            card.getChildren().add(story);
        }

        card.getChildren().add(choose);

        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(showStories ? 372 : 330);
        card.setStyle("-fx-background-color: #140a26; -fx-background-radius: 6;"
                + "-fx-border-color: #05d9e8; -fx-border-width: 2; -fx-border-radius: 6;");
        UiUtils.glow(card, UiUtils.NEON, 10);
        return card;
    }
}
