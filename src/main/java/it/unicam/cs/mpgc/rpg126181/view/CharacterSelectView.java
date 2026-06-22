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

}
