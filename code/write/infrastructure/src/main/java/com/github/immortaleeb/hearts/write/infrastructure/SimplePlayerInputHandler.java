package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.shared.Card;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimplePlayerInputHandler implements PlayerInputHandler {

    @Override
    public List<Card> chooseCardsToPass(List<Card> hand) {
        return IntStream.range(0, 3)
                .mapToObj(hand::get)
                .collect(Collectors.toList());
    }

    @Override
    public Card chooseCardToPlay(List<Card> playableCards) {
        return playableCards.get(0);
    }

}
