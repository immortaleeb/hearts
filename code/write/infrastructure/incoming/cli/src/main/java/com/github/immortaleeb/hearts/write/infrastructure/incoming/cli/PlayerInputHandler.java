package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.shared.Card;

import java.util.List;

public interface PlayerInputHandler {

    List<Card> chooseCardsToPass(List<Card> hand);

    Card chooseCardToPlay(List<Card> playableCards);

}
