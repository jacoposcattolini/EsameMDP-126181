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

    public void showMiss() {
        playerHit();
        showFeedback("MISS", Color.web("#ff4d6d"));
    }

    public void setCountdown(boolean counting, double remaining) {
        this.counting = counting;
        this.countdownRemaining = remaining;
    }

    private void spawnHitFx(int lane, boolean perfect) {
        double t = model.getGameTime();
        Color c = LANE_COLORS[lane];
        double power = perfect ? 1.4 : 1.0;
        effects.add(new Fx(FxType.SPARK, t, 0.35, lane, c, power));
        effects.add(new Fx(FxType.BEAM, t, 0.22, lane, c, power));
        bossFlashUntil = t + 0.18;
    }

    private void playerHit() {
        double t = model.getGameTime();
        playerFlashUntil = t + 0.30;
        shakeUntil = t + 0.25;
    }

    private void healFlash() {
        healFlashUntil = model.getGameTime() + 0.3;
    }

    private void showFeedback(String text, Color color) {
        feedback = text;
        feedbackColor = color;
        feedbackUntil = model.getGameTime() + 0.5;
    }

    public void render() {
        double gameTime = model.getGameTime();

        drawBattleBackground(0, 0, W, H, false);

        double laneWidth = W / GameContent.LANES;
        double pixelsPerSecond = HIT_LINE_Y / BattleModel.APPROACH_TIME;

        double dx = 0;
        double dy = 0;
        if (gameTime < shakeUntil) {
            double k = Math.max(0, (shakeUntil - gameTime) / 0.25);
            double mag = 9 * k;
            dx = (Math.random() - 0.5) * 2 * mag;
            dy = (Math.random() - 0.5) * 2 * mag;
        }

        g.save();
        g.translate(dx, dy);
        drawBattleBackground(-24, -24, W + 48, H + 48, true);

        for (int lane = 0; lane < GameContent.LANES; lane++) {
            double x = lane * laneWidth;
            g.setStroke(Color.web("#241537"));
            g.setLineWidth(2);
            g.strokeLine(x, 0, x, HIT_LINE_Y + 40);
        }
        g.setStroke(Color.BLACK);
        g.setLineWidth(3);
        g.strokeLine(0, HIT_LINE_Y, W, HIT_LINE_Y);

        for (int lane = 0; lane < GameContent.LANES; lane++) {
            double cx = lane * laneWidth + laneWidth / 2;
            g.setStroke(LANE_COLORS[lane]);
            g.setLineWidth(3);
            g.strokeOval(cx - 34, HIT_LINE_Y - 34, 68, 68);
            g.setFill(LANE_COLORS[lane]);
            g.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
            g.setTextAlign(TextAlignment.CENTER);
            g.fillText(LANE_LABELS[lane], cx, HIT_LINE_Y + 9);
        }

        boolean shrink = model.notesShrunk();
        for (Note note : model.getNotes()) {
            if (note.isResolved()) {
                continue;
            }
            double y = HIT_LINE_Y - (note.getTargetTime() - gameTime) * pixelsPerSecond;
            if (y < -40 || y > HIT_LINE_Y + 40) {
                continue;
            }
            double cx = note.getLane() * laneWidth + laneWidth / 2;
            if (note.isBomb()) {
                double r = 26;
                g.setFill(Color.web("#1a0608"));
                g.fillOval(cx - r, y - r, r * 2, r * 2);
                g.setStroke(Color.web("#ff3b3b"));
                g.setLineWidth(3);
                g.strokeOval(cx - r, y - r, r * 2, r * 2);
                g.strokeLine(cx - 11, y - 11, cx + 11, y + 11);
                g.strokeLine(cx - 11, y + 11, cx + 11, y - 11);
                continue;
            }
            double r = shrink ? 15 : 28;
            g.setFill(LANE_COLORS[note.getLane()]);
            g.fillOval(cx - r, y - r, r * 2, r * 2);
            g.setStroke(Color.WHITE);
            g.setLineWidth(2);
            g.strokeOval(cx - r, y - r, r * 2, r * 2);
        }

}
}
