package com.github.immortaleeb.hearts.write.application.scenarios;

import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;
import java.util.Map;

public interface RoundScenario {

    Map<PlayerId, List<Card>> cardsDealt();

    PassedCards cardsPassed();

    PlayerId leadingPlayer();

    Trick trick(int trickNumber);

    Map<PlayerId, Integer> roundScore();

}
