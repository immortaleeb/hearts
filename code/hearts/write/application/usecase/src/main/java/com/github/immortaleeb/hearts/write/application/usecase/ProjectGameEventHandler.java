package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.common.application.api.NoResultCommandHandler;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hearts.write.application.api.ProjectGameEvent;
import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.GameEnded;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.domain.GameStarted;
import com.github.immortaleeb.hearts.write.domain.GameSummaryWriteRepository;
import com.github.immortaleeb.hearts.write.domain.PlayerHasTakenPassedCards;
import com.github.immortaleeb.hearts.write.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.write.domain.RoundEnded;
import com.github.immortaleeb.hearts.write.domain.StartedPlaying;
import com.github.immortaleeb.hearts.write.domain.TrickWon;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProjectGameEventHandler implements NoResultCommandHandler<ProjectGameEvent> {

    private final GameSummaryWriteRepository repository;

    public ProjectGameEventHandler(GameSummaryWriteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handleNoResult(ProjectGameEvent command) {
        GameSummary summary = repository.findById(command.gameEvent().gameId()).orElse(null);

        GameSummary newSummary = project(summary, command.gameEvent());

        repository.save(newSummary);
    }

    private GameSummary project(GameSummary summary, GameEvent event) {
        if (event instanceof GameStarted gameStarted) {
            Map<PlayerId, Integer> cardsInHand = gameStarted.players()
                .stream()
                .collect(Collectors.toMap(Function.identity(), player -> 0));

            return new GameSummary(gameStarted.gameId(), gameStarted.players(), cardsInHand, clearedTable());
        } else if (event instanceof CardsDealt cardsDealt) {
            Map<PlayerId, Integer> cardsInHand = cardsDealt.playerHands()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));

            return summary.toBuilder()
                .withCardsInHand(cardsInHand)
                .build();
        } else if (event instanceof PlayerPassedCards playerPassedCards) {
            return summary.toBuilder()
                .withCardsInHand(incrementCards(summary.cardsInHand(), playerPassedCards.fromPlayer(), -playerPassedCards.passedCards().size()))
                .build();
        } else if (event instanceof PlayerHasTakenPassedCards playerHasTakenPassedCards) {
            return summary.toBuilder()
                .withCardsInHand(incrementCards(summary.cardsInHand(), playerHasTakenPassedCards.toPlayer(), playerHasTakenPassedCards.cards().size()))
                .build();
        } else if (event instanceof StartedPlaying startedPlaying) {
            // nothing to do
            return summary;
        } else if (event instanceof CardPlayed cardPlayed) {
            return summary.toBuilder()
                .withTable(addCardToTable(summary.table(), cardPlayed.playedBy(), cardPlayed.card()))
                .withCardsInHand(incrementCards(summary.cardsInHand(), cardPlayed.playedBy(), -1))
                .build();
        } else if (event instanceof TrickWon trickWon) {
            return summary.toBuilder()
                .withTable(clearedTable())
                .build();
        } else if (event instanceof RoundEnded roundEnded) {
            return summary.toBuilder()
                .withTable(clearedTable())
                .build();
        } else if (event instanceof GameEnded gameEnded) {
            // nothing to
            return summary;
        }

        return summary;
    }

    private Map<PlayerId, Integer> incrementCards(Map<PlayerId, Integer> cardsInHand, PlayerId forPlayer, int increment) {
        Map<PlayerId, Integer> newCardsInHand = new HashMap<>(cardsInHand);
        newCardsInHand.computeIfPresent(forPlayer, (player, cards) -> cards - increment);
        return newCardsInHand;
    }

    private GameSummary.Table addCardToTable(GameSummary.Table table, PlayerId playedBy, Card card) {
        HashMap<PlayerId, Card> playedCards = new HashMap<>(table.playedCards());
        playedCards.put(playedBy, card);
        return new GameSummary.Table(playedCards);
    }

    private GameSummary.Table clearedTable() {
        return new GameSummary.Table(new HashMap<>());
    }

}
