package com.github.immortaleeb.hearts.shared;

import java.util.Objects;

public class Card {

    private final Suite suite;
    private final Rank rank;

    public Card(Suite suite, Rank rank) {
        this.suite = suite;
        this.rank = rank;
    }

    public Suite suite() {
        return suite;
    }

    public Rank rank() {
        return rank;
    }

    public static Card of(Suite suite, Rank rank) {
        return new Card(suite, rank);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return suite == card.suite && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suite, rank);
    }

    @Override
    public String toString() {
        return rank + " of " + suite;
    }
}
