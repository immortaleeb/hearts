package hearts.scenarios;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;
import java.util.Map;

public interface RoundScenario {

    Map<PlayerId, List<Card>> cardsDealt();

    PassedCards cardsPassed();

    PlayerId leadingPlayer();

    Trick trick(int trickNumber);

    Map<PlayerId, Integer> roundScore();

}
