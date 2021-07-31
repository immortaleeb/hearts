package com.github.immortaleeb.hears.common.shared;

import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;
import java.util.Map;

public record GameSummary(GameId id, GameState state, List<PlayerId> players, Map<PlayerId, Integer> cardsInHand, Table table) {

    public record Table(Map<PlayerId, Card> playedCards) { }

    public enum GameState {
        GAME_STARTED,
        CARDS_DEALT,
        PASSING_CARDS,
        PLAYING_CARDS,
        ROUND_ENDED,
        GAME_ENDED,
        ;
    }

    public Builder toBuilder() {
        return newBuilder()
            .withGameId(id)
            .withState(state)
            .withPlayers(players)
            .withCardsInHand(cardsInHand)
            .withTable(table);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private GameId gameId;
        private GameState state;
        private List<PlayerId> players;
        private Map<PlayerId, Integer> cardsInHand;
        private Table table;

        public Builder withGameId(GameId gameId) {
            this.gameId = gameId;
            return this;
        }

        public Builder withState(GameState state) {
            this.state = state;
            return this;
        }

        public Builder withPlayers(List<PlayerId> players) {
            this.players = players;
            return this;
        }

        public Builder withCardsInHand(Map<PlayerId, Integer> cardsInHand) {
            this.cardsInHand = cardsInHand;
            return this;
        }

        public Builder withTable(Table table) {
            this.table = table;
            return this;
        }

        public GameSummary build() {
            return new GameSummary(gameId, state, players, cardsInHand, table);
        }
    }

}
