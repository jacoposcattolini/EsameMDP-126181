package it.unicam.cs.mpgc.rpg126181.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import it.unicam.cs.mpgc.rpg126181.model.BattleModel;
import it.unicam.cs.mpgc.rpg126181.model.BattleResult;
import it.unicam.cs.mpgc.rpg126181.model.Boss;
import it.unicam.cs.mpgc.rpg126181.model.BossScore;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.GameState;
import it.unicam.cs.mpgc.rpg126181.view.BattleView;

import java.util.Optional;

public class BattleController {

    private static final KeyCode[] LANE_KEYS = {KeyCode.A, KeyCode.S, KeyCode.K, KeyCode.L};

    private final GameController app;
    private final GameState state;
    private final BattleModel model;
    private final BattleView view;

    private AnimationTimer timer;
    private long lastNanos = -1;
    private boolean paused = false;
    private boolean finished = false;
    private boolean started = false;
    private boolean defeatChoicePending = false;
    private boolean counting = false;
    private double countdownRemaining = 0;

    private MediaPlayer musicPlayer;
    private boolean musicStarted = false;

    public BattleController(GameController app, GameState state) {
        this.app = app;
        this.state = state;
        this.model = new BattleModel(state);
        this.view = new BattleView(model);

        initMusic();
        setupInput();
        startLoop();
        view.showIntro(this::startFight, this::changeStar);
    }

    public Parent getRoot() {
        return view.getRoot();
    }

    private void initMusic() {
        Boss boss = model.getBoss();
        if (!boss.hasMusic()) {
            return;
        }
        try {
            var url = getClass().getResource("/" + boss.getMusicResource());
            if (url != null) {
                musicPlayer = new MediaPlayer(new Media(url.toExternalForm()));
                musicPlayer.setOnError(() -> musicPlayer = null);
                musicPlayer.setOnReady(() -> {
                    if (musicStarted) {
                        return;
                    }
                    double dur = musicPlayer.getMedia().getDuration().toSeconds();
                    model.regenerateChartForDuration(dur);
                    prewarmAudio();
                });
                musicPlayer.setOnEndOfMedia(this::onSongEnd);
            }
        } catch (Exception e) {
            musicPlayer = null;
        }
    }

    private void prewarmAudio() {
        if (musicPlayer == null) {
            return;
        }
        try {
            musicPlayer.setMute(true);
            musicPlayer.play();
            musicPlayer.pause();
            musicPlayer.seek(Duration.ZERO);
            musicPlayer.setMute(false);
        } catch (Exception ignored) {
        }
    }

    private void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    private void startMusicIfNeeded() {
        if (musicPlayer != null && !musicStarted && model.getGameTime() >= GameContent.LEAD_IN) {
            musicStarted = true;
            musicPlayer.play();
        }
    }

    private void setupInput() {
        StackPane root = view.getRoot();
        root.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene != null) {
                scene.setOnKeyPressed(e -> handleKey(e.getCode()));
                view.requestFocus();
            }
        });
        root.setFocusTraversable(true);
    }

    private void handleKey(KeyCode code) {
        if (finished || !started || defeatChoicePending || counting) {
            return;
        }
        if (code == KeyCode.ESCAPE) {
            togglePause();
            return;
        }
        if (paused) {
            return;
        }
        for (int lane = 0; lane < LANE_KEYS.length; lane++) {
            if (code == LANE_KEYS[lane]) {
                BattleModel.HitOutcome outcome = model.hitLane(lane);
                view.showHit(outcome);
                if (model.isBossDefeated()) {
                    onBossDefeated();
                } else if (model.isPlayerDead()) {
                    lose();
                }
                return;
            }
        }
    }

    private void startLoop() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastNanos < 0) {
                    lastNanos = now;
                }
                double dt = (now - lastNanos) / 1_000_000_000.0;
                lastNanos = now;
                if (counting && !finished) {
                    countdownRemaining -= dt;
                    if (countdownRemaining <= 0) {
                        counting = false;
                        resume();
                    }
                }
                if (started && !paused && !finished) {
                    BattleModel.AdvanceResult r = model.advance(dt);
                    startMusicIfNeeded();
                    if (r.anyMiss()) {
                        view.showMiss();
                    }
                    if (r.playerDied()) {
                        lose();
                    } else if (r.notesExhaustedLoss()) {
                        lose();
                    }
                }
                view.setCountdown(counting, countdownRemaining);
                view.render();
            }
        };
        timer.start();
    }

    private void startFight() {
        view.clearOverlay();
        started = true;
        lastNanos = -1;
        view.requestFocus();
    }

    private void changeStar() {
        finished = true;
        timer.stop();
        stopMusic();
        app.showChangeCharacter(state, () -> app.startBattle(state));
    }

    private void togglePause() {
        if (paused) {
            startResumeCountdown();
        } else {
            pause();
        }
    }

    private void pause() {
        paused = true;
        if (musicStarted && musicPlayer != null) {
            musicPlayer.pause();
        }
        view.showPause(this::startResumeCountdown, this::saveAndContinue, this::saveAndExit,
                this::goToSessionMenu, this::quitNoSave);
    }

    private void startResumeCountdown() {
        view.clearOverlay();
        counting = true;
        countdownRemaining = 3.0;
        lastNanos = -1;
        view.requestFocus();
    }

    private void resume() {
        paused = false;
        view.clearOverlay();
        if (musicStarted && musicPlayer != null) {
            musicPlayer.play();
        }
        lastNanos = -1;
        view.requestFocus();
    }

    private void goToSessionMenu() {
        finished = true;
        timer.stop();
        stopMusic();
        app.showSessionMenu(state, null, false);
    }

    private void quitNoSave() {
        finished = true;
        timer.stop();
        stopMusic();
        app.showMainMenu();
    }

    private void win(boolean songCompleted) {
        if (finished) {
            return;
        }
        finished = true;
        timer.stop();
        stopMusic();
        model.awardXp(1.0);
        BattleResult result = model.buildResult(songCompleted);
        BossScore bossScore = new BossScore(result.score(), result.timeSeconds());
        view.showResult(result, () -> app.showSessionMenu(state, bossScore, true));
    }

    private void onSongEnd() {
        if (finished) {
            return;
        }
        if (model.isBossDefeated()) {
            win(true);
        } else {
            lose();
        }
    }

    private void lose() {
        if (finished) {
            return;
        }
        finished = true;
        timer.stop();
        stopMusic();
        model.awardXp(0.5);
        view.showGameOver(
                () -> app.retryBattleWithCharacterChoice(state, () -> app.showSessionMenu(state, null, false)),
                () -> app.showSessionMenu(state, null, false));
    }

    private void onBossDefeated() {
        if (finished || defeatChoicePending || model.isKeepPlayingAfterDefeat()) {
            return;
        }
        if (!model.getBoss().hasMusic() || musicPlayer == null) {
            win(true);
            return;
        }
        showDefeatChoice();
    }

    private void showDefeatChoice() {
        defeatChoicePending = true;
        paused = true;
        if (musicStarted && musicPlayer != null) {
            musicPlayer.pause();
        }
        view.showDefeat(() -> win(false), this::continueAfterDefeat);
    }

    private void continueAfterDefeat() {
        model.setKeepPlayingAfterDefeat(true);
        defeatChoicePending = false;
        startResumeCountdown();
    }

    private void saveAndExit() {
        TextInputDialog dialog = new TextInputDialog(defaultSaveName());
        dialog.setTitle("Salva partita");
        dialog.setHeaderText("Salva ed esci");
        dialog.setContentText("Nome del salvataggio:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                app.getSaveManager().save(name, state);
            } catch (Exception ex) {
            }
            finished = true;
            timer.stop();
            stopMusic();
            app.showMainMenu();
        });
    }

    private void saveAndContinue() {
        TextInputDialog dialog = new TextInputDialog(defaultSaveName());
        dialog.setTitle("Salva partita");
        dialog.setHeaderText("Salva");
        dialog.setContentText("Nome del salvataggio:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                app.getSaveManager().save(name, state);
            } catch (Exception ex) {
            }
            startResumeCountdown();
        });
    }

    private String defaultSaveName() {
        return model.getCharacter().getName() + " - VILLAIN " + (state.getCurrentBossIndex() + 1);
    }
}
