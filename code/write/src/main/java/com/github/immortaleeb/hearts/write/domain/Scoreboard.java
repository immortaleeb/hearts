package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Scoreboard {

    private final List<Score> totalScores;

    private Scoreboard(List<Score> totalScores) {
        this.totalScores = totalScores;
    }

    public Scoreboard add(Map<PlayerId, Integer> roundScores) {
        List<Score> newTotalScores = totalScores.stream()
            .map(score -> addToScore(score, roundScores))
            .collect(Collectors.toList());

        return new Scoreboard(newTotalScores);
    }

    private Score addToScore(Score score, Map<PlayerId, Integer> roundScores) {
        return score.add(roundScores.getOrDefault(score.player(), Score.INITIAL_SCORE));
    }

    public Score largestScore() {
        return totalScores.stream()
            .max(Score.compareByScore())
            .get();
    }

    public static Scoreboard forPlayers(List<PlayerId> players) {
        return new Scoreboard(players.stream()
            .map(Score::forPlayer)
            .collect(Collectors.toList()));
    }

    public Map<PlayerId, Integer> toMap() {
        return totalScores.stream()
            .collect(Collectors.toMap(Score::player, Score::score));
    }
}
