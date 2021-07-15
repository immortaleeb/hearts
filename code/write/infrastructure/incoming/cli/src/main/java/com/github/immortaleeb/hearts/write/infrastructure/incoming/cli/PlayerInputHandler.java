package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;
import java.util.Map;

public interface PlayerInputHandler {

    void showDealtCards(List<Card> hand);

    void showReceivedCards(PlayerId fromPlayer, List<Card> receivedCards);

    void showPlayedCard(PlayerId playedBy, Card playedCard);

    void showPlayerWonTrick(PlayerId trickWinner);

    void showRoundEnded(Map<PlayerId, Integer> roundScores);

    void showGameEnded(Map<PlayerId, Integer> scores);

    List<Card> chooseCardsToPass(List<Card> hand);

    Card chooseCardToPlay(List<Card> hand, List<Card> playableCards);
}
