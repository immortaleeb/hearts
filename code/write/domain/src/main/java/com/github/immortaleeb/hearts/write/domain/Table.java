package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Table {

    private final List<PlayerId> players;

    private Trick trick = Trick.empty();

    private final Map<PlayerId, List<Card>> passedCards;
    private final Map<PlayerId, List<Trick>> wonTricks;

    public Table(List<PlayerId> players) {
        this.players = players;

        this.passedCards = new HashMap<>();
        this.wonTricks = clearPlayerMap(new HashMap<>());
    }

    public static Table with(List<PlayerId> players) {
        return new Table(players);
    }

    public void passCardsTo(PlayerId toPlayer, List<Card> cards) {
        this.passedCards.put(toPlayer, cards);
    }

    public boolean hasCardsPassedTo(PlayerId toPlayer) {
        return this.passedCards.containsKey(toPlayer);
    }

    public List<Card> cardsPassedTo(PlayerId toPlayer) {
        return this.passedCards.get(toPlayer);
    }

    public void takeCardsPassedTo(PlayerId toPlayer) {
        this.passedCards.remove(toPlayer);
    }

    public Trick trick() {
        return trick;
    }

    public void clearTrick() {
        wonTricks.compute(trick.winner(), (p, tricks) -> addTo(tricks, trick));
        trick = Trick.empty();
    }

    public Map<PlayerId, List<Trick>> wonTricks() {
        return wonTricks;
    }

    public void clearWonTricks() {
        clearPlayerMap(wonTricks);
    }

    public boolean hasPlayedCard(PlayerId playerId) {
        return trick.hasPlayedCard(playerId);
    }

    public boolean hasPlayedCards() {
        return !trick.isEmpty();
    }

    public int numberOfPlayedCards() {
        return trick.numberOfPlayedCards();
    }

    public void play(Card card, PlayerId playedBy) {
        trick = trick.play(card, playedBy);
    }

    // helper methods

    private <T> Map<PlayerId, List<T>> clearPlayerMap(Map<PlayerId, List<T>> map) {
        for (PlayerId player : players) {
            map.put(player, new ArrayList<>());
        }

        return map;
    }

    private static List<Trick> addTo(List<Trick> list, Trick trick) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(trick);

        return list;
    }

}
