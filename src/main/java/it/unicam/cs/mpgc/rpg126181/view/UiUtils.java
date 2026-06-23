package it.unicam.cs.mpgc.rpg126181.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public final class UiUtils {

    public static final Color BG = Color.web("#07060f");
    public static final Color PANEL = Color.web("#140a26");
    public static final Color NEON = Color.web("#05d9e8");
    public static final Color NEON2 = Color.web("#ff2a6d");
    public static final Color GREEN = Color.web("#9dff00");
    public static final Color GOLD = Color.web("#f9f002");
    public static final Color RED = Color.web("#ff1e3c");
    public static final Color PURPLE = Color.web("#a020f0");
    public static final Color ACCENT = NEON2;
    public static final Color TEXT = Color.web("#eaf6ff");
    public static final Color MUTED = Color.web("#b8a8d8");

    private static final String TITLE_FONT = "Consolas";

    private UiUtils() {
    }

    public static Background backgroundOf(Color color) {
        return new Background(new BackgroundFill(color, CornerRadii.EMPTY, null));
    }

    public static void glow(Node node, Color color, double radius) {
        DropShadow ds = new DropShadow(radius, color);
        ds.setSpread(0.4);
        node.setEffect(ds);
    }

    public static Label title(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(TITLE_FONT, FontWeight.BOLD, 58));
        label.setTextFill(NEON2);
        return label;
    }

}
