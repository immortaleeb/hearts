package com.github.immortaleeb.hearts.write.shared;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

class GameIdTest {

    @Test
    void generate_generates_a_GameId() {
        GameId gameId = GameId.generate();
        assertThat(gameId, is(notNullValue()));
    }
}
