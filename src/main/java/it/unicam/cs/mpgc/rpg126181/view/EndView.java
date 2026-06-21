package it.unicam.cs.mpgc.rpg126181.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import it.unicam.cs.mpgc.rpg126181.model.BossScore;
import it.unicam.cs.mpgc.rpg126181.model.GameContent;
import it.unicam.cs.mpgc.rpg126181.model.GameState;
import it.unicam.cs.mpgc.rpg126181.model.Score;

public class EndView {

    private final VBox root;

    public EndView(GameState state, Runnable onMenu) {
        this.root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        UiUtils.fillBackground(root, UiUtils.BG);
        build(state, onMenu);
    }

    public Parent getRoot() {
        return root;
    }

    private void build(GameState state, Runnable onMenu) {
        Label title = UiUtils.title("Hai concluso Beat Fighter! Per ora...");
        Label subtitle = UiUtils.subtitle("La Cittadella del Suono e' salva. L'armonia ti appartiene.");
        subtitle.setTextAlignment(TextAlignment.CENTER);

        HBox scores = new HBox(30);
        scores.setAlignment(Pos.CENTER);
        for (int i = 0; i < state.getEarnedScores().size(); i++) {
            BossScore bs = state.getEarnedScores().get(i);
            VBox col = new VBox(8);
            col.setAlignment(Pos.CENTER);
            Label boss = UiUtils.body(GameContent.getBoss(i).getName());
            Label pts = new Label(String.valueOf(bs.points()));
            pts.setFont(Font.font("Consolas", FontWeight.BOLD, 40));
            pts.setTextFill(UiUtils.NEON);
            Label time = UiUtils.body("Tempo: " + Score.formatTime(bs.timeSeconds()));
            time.setTextFill(UiUtils.MUTED);
            col.getChildren().addAll(pts, time, boss);
            scores.getChildren().add(col);
        }

        Label total = new Label("PUNTEGGIO TOTALE: " + state.getTotalPoints());
        total.setFont(Font.font("Consolas", FontWeight.BOLD, 32));
        total.setTextFill(UiUtils.NEON2);

        Button menu = UiUtils.primaryButton("Torna al menu");
        menu.setOnAction(e -> onMenu.run());

        root.getChildren().addAll(title, subtitle, scores, total, menu);
    }
}
