package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.shared.Card;

import java.util.List;

public interface PlayerInputHandler {

    List<Card> chooseCardsToPass(List<Card> hand);

    Card chooseCardToPlay(List<Card> playableCards);

}
