package com.github.immortaleeb.hearts;

import com.github.immortaleeb.hearts.write.application.CommandDispatcher;
import com.github.immortaleeb.hearts.write.application.PassCards;
import com.github.immortaleeb.hearts.write.application.PlayCard;
import com.github.immortaleeb.hearts.write.application.StartGame;
import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.domain.GameRepository;
import com.github.immortaleeb.hearts.write.domain.GameStarted;
import com.github.immortaleeb.hearts.write.domain.PlayerHasTakenPassedCards;
import com.github.immortaleeb.hearts.write.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.write.domain.RoundEnded;
import com.github.immortaleeb.hearts.write.domain.StartedPlaying;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.infrastructure.EventDispatcher;
import com.github.immortaleeb.hearts.write.infrastructure.EventListenerRegistry;
import com.github.immortaleeb.hearts.write.infrastructure.EventStore;
import com.github.immortaleeb.hearts.write.infrastructure.InMemoryEventStore;
import com.github.immortaleeb.hearts.write.infrastructure.InMemoryGameRepository;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Application {

    private PlayerControllers playerControllers;

    private void run() {
        EventListenerRegistry eventListenerRegistry = new EventListenerRegistry();
        EventDispatcher eventDispatcher = new EventDispatcher(eventListenerRegistry);
        EventStore eventStore = new InMemoryEventStore(eventDispatcher);

        GameRepository gameRepository = new InMemoryGameRepository(eventStore);
        CommandDispatcher commandDispatcher = new CommandDispatcher(gameRepository);

        playerControllers = new PlayerControllers(commandDispatcher);

        eventListenerRegistry.register(GameStarted.class, playerControllers::apply);
        eventListenerRegistry.register(CardsDealt.class, playerControllers::apply);
        eventListenerRegistry.register(PlayerPassedCards.class, playerControllers::apply);
        eventListenerRegistry.register(PlayerHasTakenPassedCards.class, playerControllers::apply);
        eventListenerRegistry.register(StartedPlaying.class, playerControllers::apply);
        eventListenerRegistry.register(CardPlayed.class, playerControllers::apply);
        eventListenerRegistry.register(TrickWon.class, playerControllers::apply);
        eventListenerRegistry.register(RoundEnded.class, System.out::println);

        List<PlayerId> players = Arrays.asList(
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate());

        commandDispatcher.dispatch(new StartGame(players));
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    private static class PlayerControllers {
        private final CommandDispatcher commandDispatcher;
        private final List<PlayerController> controllers = new ArrayList<>();

        private PlayerControllers(CommandDispatcher commandDispatcher) {
            this.commandDispatcher = commandDispatcher;
        }

        public void apply(GameStarted event) {
            System.out.println(event);
            List<PlayerController> controllers = event.players()
                    .stream()
                    .map(playerId -> new PlayerController(commandDispatcher, event.gameId(), playerId))
                    .collect(Collectors.toList());

            this.controllers.addAll(controllers);
        }

        public void apply(GameEvent event) {
            System.out.println(event);
            controllers.forEach(controller -> controller.push(event));
            controllers.forEach(PlayerController::process);
        }

    }

    static class PlayerController {
        private final CommandDispatcher commandDispatcher;

        private final GameId gameId;
        private final PlayerId playerId;
        private final List<Card> hand;
        private final Queue<GameEvent> unprocessedEvents = new LinkedList<>();
        private final List<GameEvent> events = new ArrayList<>();

        public PlayerController(CommandDispatcher commandDispatcher, GameId gameId, PlayerId playerId) {
            this.commandDispatcher = commandDispatcher;
            this.playerId = playerId;
            this.gameId = gameId;
            this.hand = new ArrayList<>();
        }

        public void push(GameEvent event) {
            unprocessedEvents.add(event);
        }

        public void process() {
            System.out.println(playerId + " = " + unprocessedEvents);
            if (!unprocessedEvents.isEmpty()) {
                GameEvent gameEvent = unprocessedEvents.poll();
                apply(gameEvent);
            }
        }

        public void apply(GameEvent event) {
            if (event instanceof CardsDealt cardsDealt) {
                apply(cardsDealt);
            } else if (event instanceof PlayerPassedCards playerPassedCards) {
                apply(playerPassedCards);
            } else if (event instanceof PlayerHasTakenPassedCards playerHasTakenPassedCards) {
                apply(playerHasTakenPassedCards);
            } else if (event instanceof StartedPlaying startedPlaying) {
                apply(startedPlaying);
            } else if (event instanceof CardPlayed cardPlayed) {
                apply(cardPlayed);
            } else if (event instanceof TrickWon trickWon) {
                apply(trickWon);
            } else {
                throw new RuntimeException("Unknown event type " + event.getClass());
            }
        }

        public void apply(CardsDealt event) {
            addEvent(event);
            List<Card> dealtCards = event.playerHands().get(playerId);
            this.hand.addAll(dealtCards);

            passCards();
        }

        private void passCards() {
            List<Card> firstThreeCards = IntStream.range(0, 3).mapToObj(hand::get).collect(Collectors.toList());
            commandDispatcher.dispatch(new PassCards(gameId, playerId, firstThreeCards));
        }

        public void apply(PlayerPassedCards event) {
            addEvent(event);
            if (event.fromPlayer().equals(playerId)) {
                this.hand.removeAll(event.passedCards());
            }
        }

        public void apply(PlayerHasTakenPassedCards event) {
            addEvent(event);
            if (event.toPlayer().equals(playerId)) {
                this.hand.addAll(event.cards());
            }
        }

        public void apply(StartedPlaying event) {
            addEvent(event);
            if (event.leadingPlayer().equals(playerId)) {
                playCard();
            }
        }

        public void apply(CardPlayed event) {
            addEvent(event);
            if (event.playedBy().equals(playerId)) {
                hand.remove(event.card());
            }

            if (event.nextLeadingPlayer().isPresent() && event.nextLeadingPlayer().get().equals(playerId)) {
                playCard();
            }
        }

        public void apply(TrickWon event) {
            addEvent(event);
            if (event.wonBy().equals(playerId) && !hand.isEmpty()) {
                playCard();
            }
        }

        private void addEvent(GameEvent event) {
            events.add(event);
            System.out.println(playerId + ": " + event);
        }

        private void playCard() {
            boolean cardPlayed = false;

            for (int i = 0; i < hand.size(); i++) {
                if (!cardPlayed) {
                    try {
                        commandDispatcher.dispatch(new PlayCard(gameId, playerId, hand.get(i)));
                        cardPlayed = true;
                    } catch (BlaException e) {
                        throw e;
                    } catch (Exception e) {
                    }
                }
            }

            if (!cardPlayed) {
                System.out.println(playerId + " -- " + hand);
                for (int i = 0; i < hand.size() && !cardPlayed; i++) {
                    try {
                        if (!cardPlayed) {
                            commandDispatcher.dispatch(new PlayCard(gameId, playerId, hand.get(i)));
                            cardPlayed = true;
                        }
                    } catch (BlaException e) {
                        throw e;
                    } catch (Exception e) {
                    }
                }
                throw new BlaException("Could not find card to play");
            }
        }

    }

    public static class BlaException extends RuntimeException {
        public BlaException(String message) {
            super(message);
        }
    }

}
