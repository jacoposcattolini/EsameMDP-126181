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

}
