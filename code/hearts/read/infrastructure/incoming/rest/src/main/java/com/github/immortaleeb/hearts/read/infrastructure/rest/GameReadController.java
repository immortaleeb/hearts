package com.github.immortaleeb.hearts.read.infrastructure.rest;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hears.common.shared.PlayerHand;
import com.github.immortaleeb.hears.common.shared.Rank;
import com.github.immortaleeb.hears.common.shared.Suite;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import javax.websocket.server.PathParam;

@RestController
public class GameReadController {

    private final GameSummaryReadRepository gameSummaryRepository;
    private final PlayerHandReadRepository playerHandReadRepository;

    public GameReadController(GameSummaryReadRepository gameSummaryRepository, PlayerHandReadRepository playerHandReadRepository) {
        this.gameSummaryRepository = gameSummaryRepository;
        this.playerHandReadRepository = playerHandReadRepository;
    }

    @GetMapping("/api/v1/games/{gameId}")
    public GameSummary getGameSummary(@PathParam("gameId") GameId gameId) {
        return gameSummaryRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("Could not find game with id " + gameId));
    }

    @GetMapping("/api/v1/hand")
    public PlayerHandDto getPlayerHand(@RequestHeader("X-Player-Id") PlayerId playerId) {
        PlayerHand playerHand = playerHandReadRepository.findById(playerId)
            .orElseThrow(() -> new IllegalArgumentException("No hand found for player with id " + playerId));

        return new PlayerHandDto(playerHand.player(), playerHand.cards().stream()
            .map(card -> new CardDto(card.suite(), card.rank()))
            .collect(Collectors.toList()));
    }

    record PlayerHandDto(PlayerId playerId, List<CardDto> cards) {
    }

    record CardDto(Suite suite, Rank rank) { }

}
