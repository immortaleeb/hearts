package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.common.shared.PlayerId;

import java.util.Comparator;

record Score(PlayerId player, int score) {

    public static final int INITIAL_SCORE = 0;

    public Score add(int addend) {
        return new Score(player, score + addend);
    }

    public static Score forPlayer(PlayerId player) {
        return new Score(player, INITIAL_SCORE);
    }

    public static Comparator<Score> compareByScore() {
        return Comparator.comparingInt(Score::score);
    }
}
