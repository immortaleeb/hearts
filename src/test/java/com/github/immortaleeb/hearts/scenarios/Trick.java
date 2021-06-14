package com.github.immortaleeb.hearts.scenarios;

import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.TrickWon;

import java.util.List;

public record Trick(List<CardPlayed> cardPlays, TrickWon trickWon) {

    public CardPlayed lastCardPlayed() {
        return cardPlays.get(cardPlays.size() - 1);
    }

}
