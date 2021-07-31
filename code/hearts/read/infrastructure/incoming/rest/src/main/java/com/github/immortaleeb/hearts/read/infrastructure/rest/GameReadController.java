package com.github.immortaleeb.hearts.read.infrastructure.rest;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
public class GameReadController {

    private final GameSummaryReadRepository gameSummaryRepository;

    public GameReadController(GameSummaryReadRepository gameSummaryRepository) {
        this.gameSummaryRepository = gameSummaryRepository;
    }

    @GetMapping("/api/v1/games/{gameId}")
    public GameSummary getGameSummary(@PathParam("gameId") GameId gameId) {
        return gameSummaryRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("Could not find game with id " + gameId));
    }

}
