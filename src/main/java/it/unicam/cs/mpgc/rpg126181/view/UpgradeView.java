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

}
}
