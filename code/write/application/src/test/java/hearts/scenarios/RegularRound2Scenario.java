package hearts.scenarios;

import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;
import hearts.CardFixtures;
import hearts.GameFixtures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegularRound2Scenario extends FixedRoundScenario {

    private final List<PlayerId> players;

    public RegularRound2Scenario(List<PlayerId> players) {
        this.players = players;
    }

    @Override
    public Map<PlayerId, List<Card>> cardsDealt() {
        return GameFixtures.fixedPlayerHands(players);
    }

    @Override
    public PassedCards cardsPassed() {
        return PassedCards.none()
            .pass(players.get(0), players.get(3), CardFixtures.threeCardsOfSuite(Suite.HEARTS))
            .pass(players.get(1), players.get(0), CardFixtures.threeCardsOfSuite(Suite.CLUBS))
            .pass(players.get(2), players.get(1), CardFixtures.threeCardsOfSuite(Suite.DIAMONDS))
            .pass(players.get(3), players.get(2), CardFixtures.threeCardsOfSuite(Suite.SPADES));
    }

    @Override
    public PlayerId leadingPlayer() {
        return players.get(1);
    }

    @Override
    public Map<PlayerId, Integer> roundScore() {
        return new HashMap<>() {{
            put(players.get(0), 25);
            put(players.get(1), 1);
            put(players.get(2), 0);
            put(players.get(3), 0);
        }};
    }

    protected Trick trick1() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.TWO), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.SPADES, Rank.ACE), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.TWO), players.get(0)),
                new CardPlayed(players.get(0), Card.of(Suite.CLUBS, Rank.ACE), players.get(1))
            ),
            new TrickWon(players.get(0))
        );
    }

    protected Trick trick2() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.CLUBS, Rank.JACK), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.KING), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.SPADES, Rank.JACK), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.HEARTS, Rank.ACE), players.get(0))
            ),
            new TrickWon(players.get(1))
        );

    }

    protected Trick trick3() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.THREE), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.SPADES, Rank.TEN), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.THREE), players.get(0)),
                new CardPlayed(players.get(0), Card.of(Suite.CLUBS, Rank.TEN), players.get(1))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick4() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.KING), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.DIAMONDS, Rank.JACK), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.KING), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.HEARTS, Rank.JACK), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick5() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.QUEEN), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.DIAMONDS, Rank.TEN), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.QUEEN), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.HEARTS, Rank.TEN), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick6() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.NINE), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.DIAMONDS, Rank.ACE), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.NINE), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.KING), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick7() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.EIGHT), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.QUEEN), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.EIGHT), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.QUEEN), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick8() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.SEVEN), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.NINE), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.SEVEN), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.NINE), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick9() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.SIX), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.EIGHT), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.SIX), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.EIGHT), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick10() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.FIVE), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.SEVEN), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.FIVE), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.SEVEN), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick11() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.FOUR), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.SIX), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.FOUR), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.SIX), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick12() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.THREE), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.FIVE), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.THREE), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.FIVE), players.get(0))
            ),
            new TrickWon(players.get(0))
        );

    }

    protected Trick trick13() {
        return new Trick(
            List.of(
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.TWO), players.get(1)),
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.FOUR), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.TWO), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.FOUR), players.get(0))
            ),
            new TrickWon(players.get(0))
        );
    }

}
