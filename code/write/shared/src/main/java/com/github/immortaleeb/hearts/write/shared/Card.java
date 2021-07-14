package com.github.immortaleeb.hearts.write.shared;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

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

    public boolean matchesSuite(Suite suite) {
        return this.suite.equals(suite);
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

    public static Predicate<Card> matchingSuite(Suite suite) {
        return card -> card.matchesSuite(suite);
    }

    public static Comparator<Card> compareBySuite() {
        return Comparator.comparingInt(card -> card.suite.ordinal());
    }

    public static Comparator<Card> compareByRank() {
        return Comparator.comparingInt(card -> card.rank().ordinal());
    }

    public static Comparator<Card> compareBySuiteAndRank() {
        return compareBySuite().thenComparing(compareByRank());
    }

}
