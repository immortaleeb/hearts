package com.github.immortaleeb.hearts.write.shared;

import com.github.immortaleeb.common.shared.PlayerId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

class PlayerIdTest {

    @Test
    void generate_creates_a_player_id() {
        PlayerId playerId = PlayerId.generate();

        assertThat(playerId, is(notNullValue()));
        assertThat(playerId.asUuid(), is(notNullValue()));
    }

    @Test
    void asUuid_returns_same_uuid_for_generated_player_id() {
        PlayerId playerId = PlayerId.generate();
        UUID uuid = playerId.asUuid();

        assertThat(playerId.asUuid(), is(equalTo(uuid)));
    }

    @Test
    void generate_generates_random_player_ids() {
        PlayerId playerId1 = PlayerId.generate();
        PlayerId playerId2 = PlayerId.generate();

        assertThat(playerId1.asUuid(), is(not(equalTo(playerId2.asUuid()))));
    }

    @Test
    void of_creates_a_player_id_with_the_given_uuid() {
        String uuid = "57841f18-8ab1-4083-9e19-afdc37c23e6b";
        PlayerId playerId = PlayerId.of(uuid);

        assertThat(playerId.asString(), is(equalTo(uuid)));
    }
}