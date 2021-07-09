package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.application.Command;
import com.github.immortaleeb.hearts.write.application.PassCards;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.fixtures.GameFixtures;
import com.github.immortaleeb.hearts.write.fixtures.PlayerIdFixtures;
import com.github.immortaleeb.hearts.write.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

class PlayerControllerTest {

    private final PlayerId playerId = PlayerIdFixtures.players().get(0);
    private final GameId gameId = GameId.generate();

    private FakeCommandDispatcher commandDispatcher;
    private PlayerController controller;

    @BeforeEach
    void setUp() {
        commandDispatcher = new FakeCommandDispatcher();
        controller = new PlayerController(playerId, commandDispatcher);
    }

    @Test
    void passes_first_3_cards_for_player_when_cards_have_been_dealt() {
        // given
        Map<PlayerId, List<Card>> dealtCards = GameFixtures.fixedPlayerHands(PlayerIdFixtures.players());

        // when
        controller.process(new CardsDealt(gameId, dealtCards));

        // then
        List<Command> dispatchedCommands = commandDispatcher.dispatchedCommands();

        assertThat(dispatchedCommands, hasSize(1));

        List<Card> expectedCards = List.of(
                Card.of(Suite.HEARTS, Rank.TWO),
                Card.of(Suite.HEARTS, Rank.THREE),
                Card.of(Suite.HEARTS, Rank.FOUR)
        );
        assertThat(dispatchedCommands, hasItems(new PassCards(gameId, playerId, expectedCards)));
    }
}