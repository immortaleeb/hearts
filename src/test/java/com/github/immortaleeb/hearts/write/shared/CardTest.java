package com.github.immortaleeb.hearts.write.shared;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

class CardTest {

    @Test
    void of_creates_a_card() {
        Card card = Card.of(Suite.SPADES, Rank.ACE);

        assertThat(card, is(notNullValue()));
        assertThat(card.suite(), is(equalTo(Suite.SPADES)));
        assertThat(card.rank(), is(equalTo(Rank.ACE)));
    }
}