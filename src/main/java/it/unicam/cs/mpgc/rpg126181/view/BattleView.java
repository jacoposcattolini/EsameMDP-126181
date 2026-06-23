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

        drawEffects(laneWidth);
        g.restore();

        effects.removeIf(fx -> gameTime - fx.start > fx.duration);

        renderHud();
    }

    private void drawEffects(double laneWidth) {
        double gameTime = model.getGameTime();
        for (Fx fx : effects) {
            double age = gameTime - fx.start;
            if (age < 0 || age > fx.duration) {
                continue;
            }
            double p = age / fx.duration;
            double cx = fx.lane * laneWidth + laneWidth / 2;
            Color c = fx.color;

            if (fx.type == FxType.SPARK) {
                double alpha = 1 - p;
                double r = 34 + p * 55 * fx.power;
                g.setStroke(Color.color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
                g.setLineWidth(5 * (1 - p) + 1);
                g.strokeOval(cx - r, HIT_LINE_Y - r, r * 2, r * 2);

                int spikes = 8;
                double inner = 34;
                double outer = 34 + p * 70 * fx.power;
                for (int i = 0; i < spikes; i++) {
                    double ang = Math.PI * 2 * i / spikes;
                    g.strokeLine(
                            cx + Math.cos(ang) * inner, HIT_LINE_Y + Math.sin(ang) * inner,
                            cx + Math.cos(ang) * outer, HIT_LINE_Y + Math.sin(ang) * outer);
                }
            } else {
                double alpha = (1 - p) * 0.85;
                g.setStroke(Color.color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
                g.setLineWidth(14 * (1 - p) + 2);
                g.strokeLine(cx, HIT_LINE_Y, cx, 66);
                g.setFill(Color.color(1, 1, 1, alpha));
                g.fillOval(cx - 8, 58, 16, 16);
            }
        }
    }

    private void renderHud() {
        double gameTime = model.getGameTime();
        boolean arcade = model.isArcade();

        if (gameTime < bossFlashUntil) {
            double a = Math.max(0, (bossFlashUntil - gameTime) / 0.18 * 0.55);
            g.setFill(Color.color(1, 1, 1, a));
            g.fillRect(0, 0, W, 70);
        }
        if (gameTime < playerFlashUntil) {
            double a = Math.max(0, (playerFlashUntil - gameTime) / 0.30 * 0.5);
            g.setFill(Color.color(1, 0.18, 0.3, a));
            g.fillRect(0, H - 130, W, 130);
        }
        if (gameTime < healFlashUntil) {
            double a = Math.max(0, (healFlashUntil - gameTime) / 0.30 * 0.4);
            g.setFill(Color.color(0.1, 1, 0.4, a));
            g.fillRect(0, H - 130, W, 130);
        }

        g.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        g.setTextAlign(TextAlignment.LEFT);
        g.setFill(UiUtils.TEXT);
        g.fillText(model.getBoss().getName(), 20, 30);
        g.setTextAlign(TextAlignment.RIGHT);
        g.setFill(Color.web("#ff8fae"));
        g.fillText((int) Math.ceil(model.getBossHp()) + " / " + model.getBossMaxHp() + " HP", W - 20, 30);
        g.setTextAlign(TextAlignment.LEFT);
        drawBar(20, 42, W - 40, 18, model.getBossHp() / model.getBossMaxHp(), Color.web("#ff2a6d"));

        g.setFill(UiUtils.TEXT);
        if (arcade) {
            g.fillText(model.getCharacter().getName(), 20, H - 100);
        } else {
            g.fillText(model.getCharacter().getName() + "  ❤ " + model.getPlayerHp()
                    + "/" + model.getPlayerMaxHp(), 20, H - 100);
            drawBar(20, H - 88, 360, 16,
                    (double) model.getPlayerHp() / model.getPlayerMaxHp(), Color.web("#05d9e8"));
        }

        g.setTextAlign(TextAlignment.RIGHT);
        g.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        g.setFill(Color.web("#ffd166"));
        g.fillText("Punti: " + model.getScore() + "   Perfect: " + model.getPerfectHits()
                + "   OK: " + model.getGoodHits() + "   Errori: " + model.getErrors(), W - 20, H - 100);
        g.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        g.setFill(Color.web("#05d9e8"));
        g.fillText(arcade ? "Streak: " + model.getStreak()
                : "Streak: " + model.getStreak() + "   XP: +" + model.getXpEarned(), W - 20, H - 54);

        g.setTextAlign(TextAlignment.LEFT);
        g.setFont(Font.font("Verdana", 14));
        g.setFill(Color.web("#9aa"));
        g.fillText("Abilita': " + model.getAbility().getDisplayName(), 20, H - 54);

        if (gameTime < feedbackUntil) {
            g.setTextAlign(TextAlignment.CENTER);
            g.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
            g.setFill(feedbackColor);
            g.fillText(feedback, W / 2, HIT_LINE_Y - 120);
        }

        g.setTextAlign(TextAlignment.CENTER);
        g.setFont(Font.font("Verdana", 14));
        g.setFill(Color.web("#777"));
        g.fillText("ESC: pausa / salva ed esci", W / 2, H - 20);

        if (counting) {
            g.setFill(Color.color(0, 0, 0, 0.55));
            g.fillRect(0, 0, W, H);
            g.setTextAlign(TextAlignment.CENTER);
            g.setFont(Font.font("Verdana", FontWeight.BOLD, 130));
            g.setFill(UiUtils.NEON2);
            g.fillText(String.valueOf((int) Math.ceil(countdownRemaining)), W / 2, H / 2 + 30);
            g.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
            g.setFill(UiUtils.TEXT);
            g.fillText("Si riprende...", W / 2, H / 2 + 90);
        }
    }

    private void drawBattleBackground(double x, double y, double w, double h, boolean veil) {
        if (hasImageBackground) {
            g.clearRect(x, y, w, h);
            if (veil) {
                g.setFill(Color.color(0, 0, 0, 0.45));
                g.fillRect(x, y, w, h);
            }
        } else {
            g.setFill(Color.web("#0a0613"));
            g.fillRect(x, y, w, h);
        }
    }

    private void drawBar(double x, double y, double width, double height, double ratio, Color color) {
        ratio = Math.max(0, Math.min(1, ratio));
        g.setFill(Color.web("#241537"));
        g.fillRoundRect(x, y, width, height, 8, 8);
        g.setFill(color);
        g.fillRoundRect(x, y, width * ratio, height, 8, 8);
        g.setStroke(Color.web("#11111a"));
        g.setLineWidth(1);
        g.strokeRoundRect(x, y, width, height, 8, 8);
    }

    public void clearOverlay() {
        if (overlay != null) {
            root.getChildren().remove(overlay);
            overlay = null;
        }
    }

    public void showIntro(Runnable onStart, Runnable onChangeStar) {
        Boss boss = model.getBoss();
        Label title = UiUtils.title(boss.getName());

        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(34, 44, 34, 44));
        box.setMaxWidth(900);
        box.setStyle(panelStyle("#ff2a6d"));
        UiUtils.glow(box, UiUtils.NEON2, 16);
        box.getChildren().add(title);

        if (model.getState().getMode() == GameMode.STORY) {
            Label chapter = UiUtils.subtitle("Capitolo " + (model.getState().getCurrentBossIndex() + 1)
                    + " di " + GameContent.getBossCount());
            chapter.setTextFill(Color.web("#ffd166"));
            box.getChildren().add(chapter);
        }

        String diffText = "Difficolta': " + boss.getDifficulty().getDisplayName();
        Label who = UiUtils.body(model.isArcade() ? diffText
                : diffText + "   ·   STAR: " + model.getCharacter().getName());
        who.setTextFill(UiUtils.MUTED);
        who.setTextAlignment(TextAlignment.CENTER);
        box.getChildren().add(who);

        if (model.getState().getMode() == GameMode.STORY) {
            Label villainStory = UiUtils.body(boss.getStory());
            villainStory.setMaxWidth(820);
            villainStory.setTextAlignment(TextAlignment.CENTER);
            villainStory.setStyle("-fx-line-spacing: 6;");
            box.getChildren().add(villainStory);
        }

        Label controls = UiUtils.body("Premi i tasti A/S/K/L a tempo per sconfiggere i nemici");
        controls.setTextFill(UiUtils.GREEN);
        controls.setTextAlignment(TextAlignment.CENTER);

        Button start = UiUtils.primaryButton("⚔  Inizia il combattimento");
        start.setOnAction(e -> onStart.run());

        box.getChildren().addAll(controls, start);

        if (!model.isArcade() && onChangeStar != null) {
            Button change = UiUtils.secondaryButton("🔄  Scegli STAR");
            change.setOnAction(e -> onChangeStar.run());
            box.getChildren().add(change);
        }

        setOverlay(box);
    }

    public void showPause(Runnable onResume, Runnable onSave, Runnable onSaveExit,
                          Runnable onMenu, Runnable onQuit) {
        Label title = UiUtils.title("Pausa");
        Button resume = UiUtils.coloredButton("Riprendi", "#9dff00");
        resume.setOnAction(e -> onResume.run());
        Button save = UiUtils.secondaryButton("Salva");
        save.setOnAction(e -> onSave.run());
        Button saveExit = UiUtils.secondaryButton("Salva ed esci");
        saveExit.setOnAction(e -> onSaveExit.run());
        Button session = UiUtils.secondaryButton("Menu");
        session.setOnAction(e -> onMenu.run());
        Button quit = UiUtils.coloredButton("Esci senza salvare", "#ff1e3c");
        quit.setOnAction(e -> onQuit.run());
        setOverlay(overlayBox(title, resume, save, saveExit, session, quit));
    }

}
