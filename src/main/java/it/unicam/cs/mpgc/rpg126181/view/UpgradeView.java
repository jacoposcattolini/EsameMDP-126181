package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import it.unicam.cs.mpgc.rpg126181.model.CharacterUpgrades;
import it.unicam.cs.mpgc.rpg126181.model.GameCharacter;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.PlayerProfile;
import it.unicam.cs.mpgc.rpg126181.model.PlayerProfile.UpgradeType;

import java.util.function.BiConsumer;

public class UpgradeView {

    private final PlayerProfile profile;
    private final BiConsumer<GameCharacter, UpgradeType> onBuy;
    private final VBox root;

    public UpgradeView(PlayerProfile profile, BiConsumer<GameCharacter, UpgradeType> onBuy, Runnable onBack) {
        this.profile = profile;
        this.onBuy = onBuy;
        this.root = new VBox(24);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        UiUtils.backgroundImage(root, "/images/potenziamento.png", UiUtils.BG);
        build(onBack);
    }

    public Parent getRoot() {
        return root;
    }

    private void build(Runnable onBack) {
        Label title = UiUtils.title("POTENZIA LE STAR");

        Label xp = new Label("Esperienza disponibile: " + profile.getExperience() + " XP");
        xp.setFont(Font.font("Consolas", FontWeight.BOLD, 24));
        xp.setTextFill(UiUtils.NEON);

        VBox cards = new VBox(16);
        cards.setAlignment(Pos.TOP_CENTER);
        for (GameCharacter c : GameContent.getCharacters()) {
            cards.getChildren().add(buildCard(c));
        }

        ScrollPane scroll = new ScrollPane(cards);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(440);
        scroll.setMaxWidth(900);
        scroll.setStyle("-fx-background: #07060f; -fx-background-color: transparent;");

        Button back = UiUtils.backButton("← INDIETRO");
        back.setOnAction(e -> onBack.run());

        VBox panel = new VBox(16, title, xp, scroll);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setMaxWidth(940);
        panel.setPadding(new Insets(24, 28, 24, 28));
        panel.setStyle("-fx-background-color: rgba(10,6,19,0.92);"
                + "-fx-background-radius: 12; -fx-border-color: #2a1a44;"
                + "-fx-border-width: 2; -fx-border-radius: 12;");

        root.getChildren().addAll(panel, back);
    }

    private VBox buildCard(GameCharacter c) {
        CharacterUpgrades u = profile.getUpgrades(c.getId());

        Label name = new Label(c.getName() + "  —  " + c.getAbility().getDisplayName());
        name.setFont(Font.font("Consolas", FontWeight.BOLD, 22));
        name.setTextFill(UiUtils.NEON2);

        Label ability = UiUtils.body(c.getAbility().getDescription());
        ability.setTextFill(Color.web("#9aa"));

        Label effect = UiUtils.body(c.getAbility().effectAt(u.getAbilityLevel()));
        effect.setTextFill(UiUtils.NEON);

        HBox stats = new HBox(14,
                upgradeBox(c, UpgradeType.HP,
                        "Vita", c.effectiveMaxHp(u.getHpLevel()) + " HP", u.getHpLevel()),
                upgradeBox(c, UpgradeType.ABILITY,
                        "Abilita'", "Liv. " + (u.getAbilityLevel() + 1), u.getAbilityLevel()));
        stats.setAlignment(Pos.CENTER);

        VBox card = new VBox(12, name, ability, effect, stats);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #140a26; -fx-background-radius: 6;"
                + "-fx-border-color: #2a1a44; -fx-border-width: 1; -fx-border-radius: 6;");
        return card;
    }

    private VBox upgradeBox(GameCharacter c, UpgradeType type, String label, String value, int level) {
        Label l = UiUtils.body(label);
        l.setTextFill(UiUtils.TEXT);

        Label v = new Label(value);
        v.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        v.setTextFill(UiUtils.NEON);

        int cost = profile.costFor(c.getId(), type);
        boolean affordable = profile.canUpgrade(c.getId(), type);
        boolean maxed = profile.isMaxLevel(c.getId(), type);

        Button buy = UiUtils.secondaryButton(maxed ? "MAX" : "+ (" + cost + " XP)");
        buy.setPrefWidth(170);
        buy.setDisable(!affordable);
        buy.setOnAction(e -> onBuy.accept(c, type));

        Label lvl = UiUtils.body("Liv. " + (level + 1));
        lvl.setTextFill(UiUtils.MUTED);

        VBox box = new VBox(6, l, v, lvl, buy);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(230);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #1c1030; -fx-background-radius: 6;");
        return box;
    }
}
