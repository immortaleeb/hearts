package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;
import java.util.stream.Collectors;

class Player {

    private final PlayerId id;
    private final Hand hand;
    private boolean passedCards;

    private Player(PlayerId id) {
        this.id = id;
        this.hand = Hand.empty();
    }

    public PlayerId id() {
        return id;
    }

    public Hand hand() {
        return hand;
    }

    public void takeDealtCards(List<Card> cards) {
        hand.receive(cards);
        passedCards = false;
    }

    public boolean hasPassedCards() {
        return passedCards;
    }

    public void takePassedCardsFrom(Table table) {
        List<Card> passedCards = table.cardsPassedTo(id);

        table.takeCardsPassedTo(id);
        hand.receive(passedCards);
    }

    public void passCardsTo(PlayerId toPlayer, List<Card> cards, Table table) {
        hand.take(cards);
        table.passCardsTo(toPlayer, cards);
        passedCards = true;
    }

    public static List<Player> listOf(List<PlayerId> playerIds) {
        return playerIds.stream()
                .map(Player::new)
                .collect(Collectors.toList());
    }

    public void play(Card card, Table table) {
        hand.take(card);
        table.play(card, this.id);
    }

}
