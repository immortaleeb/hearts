package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyGameId;
import com.github.immortaleeb.lobby.shared.LobbyFull;
import com.github.immortaleeb.lobby.shared.LobbyId;
import com.github.immortaleeb.lobby.shared.PlayerAlreadyJoinedLobby;

import java.util.ArrayList;
import java.util.List;

import static com.github.immortaleeb.lobby.domain.LobbyState.*;
import static java.util.Collections.unmodifiableList;

public class Lobby {

    private static final int LOBBY_SIZE = 4;

    private final LobbyId id;
    private LobbyState state;
    private final String name;
    private final PlayerId createdBy;
    private final List<PlayerId> players;
    private LobbyGameId game;

    private final List<LobbyEvent> raisedEvents = new ArrayList<>();

    private Lobby(LobbyId id, LobbyState state, String name, PlayerId createdBy, List<PlayerId> players) {
        this.id = id;
        this.state = state;
        this.name = name;
        this.createdBy = createdBy;
        this.players = new ArrayList<>(players);
    }

    public LobbyId id() {
        return id;
    }

    public void join(PlayerId player) {
        if (isFull()) {
            throw new LobbyFull("Lobby %s is full".formatted(name));
        }

        if (players.contains(player)) {
            throw new PlayerAlreadyJoinedLobby("Player %s is already present in lobby '%s'".formatted(player, name));
        }

        players.add(player);

        if (isFull()) {
            state = READY_TO_PLAY;
        }

        raisedEvents.add(new PlayerJoinedLobby(id, player));
    }

    public void startGame(GameStarter gameStarter) {
        game = gameStarter.startGame(players);
        state = PLAYING;
        raisedEvents.add(new GameStarted(id, game));
    }

    private boolean isFull() {
        return players.size() == LOBBY_SIZE;
    }

    public List<LobbyEvent> raisedEvents() {
        return unmodifiableList(raisedEvents);
    }

    public static Lobby create(String name, PlayerId createdBy) {
        Lobby lobby = new Lobby(LobbyId.generate(), WAITING_FOR_PLAYERS, name, createdBy, List.of(createdBy));
        lobby.raisedEvents.add(new LobbyCreated(lobby.id, lobby.name, lobby.createdBy));
        return lobby;
    }

    public static Lobby restoreFrom(Snapshot snapshot) {
        return new Lobby(snapshot.id, snapshot.state, snapshot.name, snapshot.createdBy, snapshot.players);
    }

    public record Snapshot(LobbyId id, LobbyState state, String name, PlayerId createdBy, List<PlayerId> players, LobbyGameId game) {}

    public Snapshot snapshot() {
        return new Snapshot(id, state, name, createdBy, unmodifiableList(players), game);
    }

}
