package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimplePlayerInputHandler implements PlayerInputHandler {

    @Override
    public void showDealtCards(List<Card> hand) {
        // nothing to do
    }

    @Override
    public void showReceivedCards(PlayerId fromPlayer, List<Card> receivedCards) {
        // nothing to do
    }

    @Override
    public void showPlayedCard(PlayerId playedBy, Card playedCard) {
        // nothing to do
    }

    @Override
    public void showPlayerWonTrick(PlayerId trickWinner) {
        // nothing to do
    }

    @Override
    public void showRoundEnded(Map<PlayerId, Integer> roundScores) {
        // nothing to do
    }

    @Override
    public void showGameEnded(Map<PlayerId, Integer> scores) {
        // nothing to do
    }

    @Override
    public List<Card> chooseCardsToPass(List<Card> hand) {
        return IntStream.range(0, 3)
                .mapToObj(hand::get)
                .collect(Collectors.toList());
    }

    @Override
    public Card chooseCardToPlay(List<Card> hand, List<Card> playableCards) {
        return playableCards.get(0);
    }

}
