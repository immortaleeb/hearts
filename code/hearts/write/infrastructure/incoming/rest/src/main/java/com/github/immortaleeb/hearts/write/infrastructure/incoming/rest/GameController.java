package com.github.immortaleeb.hearts.write.infrastructure.incoming.rest;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.Rank;
import com.github.immortaleeb.hears.common.shared.Suite;
import com.github.immortaleeb.hearts.write.application.api.PassCards;
import com.github.immortaleeb.hearts.write.application.api.PlayCard;
import com.github.immortaleeb.hearts.write.application.api.StartGame;
import com.github.immortaleeb.hears.common.shared.GameId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import javax.websocket.server.PathParam;

@RestController
public class GameController {

    private final CommandDispatcher commandDispatcher;

    public GameController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping("/api/v1/games/start")
    public StartGameResponse startGame(@RequestBody StartGameRequest request) {
        GameId gameId = commandDispatcher.dispatch(new StartGame(request.players));
        return new StartGameResponse(gameId);
    }

    @PostMapping("/api/v1/games/{gameId}/pass-cards")
    public void passCards(@RequestHeader("X-Player-Id") PlayerId playerId, @PathParam("gameId") GameId gameId,
        @RequestBody PassCardsRequest request) {
        commandDispatcher.dispatch(new PassCards(gameId, playerId, toCards(request)));
    }

    @PostMapping("/api/v1/games/{gameId}/play-card")
    public void playCard(@RequestHeader("X-Player-Id") PlayerId playerId, @PathParam("gameId") GameId gameId,
        @RequestBody PlayCardRequest request) {
        commandDispatcher.dispatch(new PlayCard(gameId, playerId, request.card.toCard()));
    }

    record StartGameRequest(List<PlayerId> players) { }

    record StartGameResponse(GameId id) { }
    record PassCardsRequest(List<CardDto> cards) { }

    record PlayCardRequest(CardDto card) { }

    record CardDto(Suite suite, Rank rank) {

        Card toCard() {
            return Card.of(suite, rank);
        }
    }

    private List<Card> toCards(PassCardsRequest request) {
        return request.cards.stream().map(CardDto::toCard).collect(Collectors.toList());
    }

}
