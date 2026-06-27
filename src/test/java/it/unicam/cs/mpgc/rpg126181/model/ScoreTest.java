package it.unicam.cs.mpgc.rpg126181.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreTest {

    @Test
    void puntiSommanoPerfectEGood() {
        assertEquals(1250, Score.points(10, 5));
    }

    @Test
    void nessunColpoDaZeroPunti() {
        assertEquals(0, Score.points(0, 0));
    }

    @Test
    void formatTimeProduceMinutiSecondi() {
        assertEquals("1:05", Score.formatTime(65));
        assertEquals("0:09", Score.formatTime(9.4));
        assertEquals("2:00", Score.formatTime(120));
    }
}
