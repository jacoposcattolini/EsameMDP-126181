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

    public static Label waveTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(TITLE_FONT, FontWeight.BOLD, 64));
        label.setTextFill(GOLD);
        return label;
    }

    public static Label subtitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(TITLE_FONT, FontWeight.BOLD, 28));
        label.setTextFill(NEON);
        return label;
    }

    public static Label body(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Verdana", 18));
        label.setTextFill(TEXT);
        label.setWrapText(true);
        return label;
    }

    public static Button primaryButton(String text) {
        return neonButton(text, "#ff2a6d");
    }

    public static Button secondaryButton(String text) {
        return neonButton(text, "#05d9e8");
    }

    public static Button coloredButton(String text, String hex) {
        return neonButton(text, hex);
    }

    public static Button backButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font(TITLE_FONT, FontWeight.BOLD, 20));
        button.setTextFill(RED);
        button.setPrefWidth(380);
        button.setStyle(backStyle());
        button.setOnMouseEntered(e -> button.setTextFill(Color.WHITE));
        button.setOnMouseExited(e -> button.setTextFill(RED));
        return button;
    }

    private static String backStyle() {
        return "-fx-background-color: #000000;"
                + "-fx-border-color: #ff1e3c;"
                + "-fx-border-width: 2;"
                + "-fx-background-radius: 4;"
                + "-fx-border-radius: 4;"
                + "-fx-padding: 12 24 12 24;"
                + "-fx-cursor: hand;";
    }

    private static Button neonButton(String text, String hex) {
        Button button = new Button(text);
        button.setFont(Font.font(TITLE_FONT, FontWeight.BOLD, 20));
        button.setTextFill(Color.web(hex));
        button.setPrefWidth(380);
        button.setStyle(neonStyle(hex, 0.10));
        button.setOnMouseEntered(e -> {
            button.setStyle(neonStyle(hex, 0.28));
            button.setTextFill(Color.WHITE);
        });
        button.setOnMouseExited(e -> {
            button.setStyle(neonStyle(hex, 0.10));
            button.setTextFill(Color.web(hex));
        });
        return button;
    }

    private static String neonStyle(String hex, double fillAlpha) {
        Color c = Color.web(hex);
        String rgba = String.format("rgba(%d,%d,%d,%.2f)",
                (int) (c.getRed() * 255), (int) (c.getGreen() * 255), (int) (c.getBlue() * 255), fillAlpha);
        return "-fx-background-color: " + rgba + ";"
                + "-fx-border-color: " + hex + ";"
                + "-fx-border-width: 2;"
                + "-fx-background-radius: 4;"
                + "-fx-border-radius: 4;"
                + "-fx-padding: 12 24 12 24;"
                + "-fx-cursor: hand;";
    }

    public static void fillBackground(Region region, Color color) {
        region.setBackground(backgroundOf(color));
    }

    public static void glassPanel(Region region, Color accent) {
        CornerRadii radii = new CornerRadii(16);
        region.setBackground(new Background(new BackgroundFill(
                Color.color(0.02, 0.01, 0.06, 0.74), radii, null)));
        region.setBorder(new Border(new BorderStroke(accent,
                BorderStrokeStyle.SOLID, radii, new BorderWidths(2))));
        glow(region, accent, 22);
    }

    public static void backgroundImage(Region region, String resource, Color fallback) {
        backgroundImage(region, resource, fallback, BackgroundPosition.CENTER);
    }

    public static void backgroundImage(Region region, String resource, Color fallback,
                                       BackgroundPosition position) {
        var url = UiUtils.class.getResource(resource);
        if (url == null) {
            fillBackground(region, fallback);
            return;
        }
        Image image = new Image(url.toExternalForm());
        BackgroundSize cover = new BackgroundSize(
                BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);
        BackgroundImage bg = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                position, cover);
        region.setBackground(new Background(bg));
    }
}
