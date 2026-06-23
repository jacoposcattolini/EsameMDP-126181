package it.unicam.cs.mpgc.rpg126181.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import it.unicam.cs.mpgc.rpg126181.model.BattleModel;
import it.unicam.cs.mpgc.rpg126181.model.BattleResult;
import it.unicam.cs.mpgc.rpg126181.model.Boss;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.GameMode;
import it.unicam.cs.mpgc.rpg126181.model.Note;
import it.unicam.cs.mpgc.rpg126181.model.Score;

import java.util.ArrayList;
import java.util.List;

public class BattleView {

    private static final double W = 760;
    private static final double H = 860;
    private static final double HIT_LINE_Y = H - 180;

    private static final String[] LANE_LABELS = {"A", "S", "K", "L"};
    private static final Color[] LANE_COLORS = {
            Color.web("#05d9e8"), Color.web("#ff2a6d"),
            Color.web("#f9f002"), Color.web("#a020f0")
    };

    private final BattleModel model;
    private final StackPane root;
    private final Canvas canvas;
    private final GraphicsContext g;
    private final boolean hasImageBackground;

    private final List<Fx> effects = new ArrayList<>();
    private String feedback = "";
    private Color feedbackColor = Color.WHITE;
    private double feedbackUntil = 0;
    private double bossFlashUntil = 0;
    private double playerFlashUntil = 0;
    private double healFlashUntil = 0;
    private double shakeUntil = 0;

    private boolean counting = false;
    private double countdownRemaining = 0;

    private VBox overlay;

    public BattleView(BattleModel model) {
        this.model = model;
        this.canvas = new Canvas(W, H);
        this.g = canvas.getGraphicsContext2D();
        this.root = new StackPane(canvas);
        StackPane.setAlignment(canvas, Pos.CENTER);

        String bgRes = battleBackgroundResource();
        this.hasImageBackground = bgRes != null;
        if (hasImageBackground) {
            UiUtils.backgroundImage(root, bgRes, UiUtils.BG);
        } else {
            UiUtils.fillBackground(root, UiUtils.BG);
        }
    }

    public StackPane getRoot() {
        return root;
    }

    public void requestFocus() {
        Platform.runLater(root::requestFocus);
    }

    private String battleBackgroundResource() {
        int index = model.getState().getCurrentBossIndex();
        String resource = null;
        if (index == 0) {
            resource = "/images/yakuza.png";
        } else if (index == 1) {
            resource = "/images/melissa.png";
        } else if (index == GameContent.getBossCount() - 1) {
            resource = "/images/kanye.png";
        }
        if (resource == null || getClass().getResource(resource) == null) {
            return null;
        }
        return resource;
    }

    public void showHit(BattleModel.HitOutcome outcome) {
        int lane = outcome.lane();
        switch (outcome.type()) {
            case WRONG -> {
                playerHit();
                showFeedback("SBAGLIATO", Color.web("#ff4d6d"));
            }
            case BOMB -> {
                playerHit();
                showFeedback("BOMBA!", Color.web("#ff7b00"));
            }
            case GOOD -> {
                spawnHitFx(lane, false);
                showFeedback("OK", Color.web("#06d6a0"));
            }
            case PERFECT -> {
                spawnHitFx(lane, true);
                if (outcome.healed()) {
                    healFlash();
                }
                showFeedback("PERFECT!", Color.web("#ffd166"));
            }
            case CRIT -> {
                spawnHitFx(lane, true);
                showFeedback("CRITICO!", Color.web("#ff7b00"));
            }
        }
    }

}
