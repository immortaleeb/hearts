package com.github.immortaleeb.lobby.fakes;

import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyEvent;
import com.github.immortaleeb.lobby.domain.LobbyRepository;
import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

public class FakeLobbyRepository implements LobbyRepository {

    private LobbyId lastSavedId = null;
    private final Map<LobbyId, Lobby.Snapshot> savedSnapshots = new LinkedHashMap<>();
    private final List<LobbyEvent> raisedEvents = new ArrayList<>();

    public void givenExisting(Lobby.Snapshot existingLobby) {
        savedSnapshots.put(existingLobby.id(), existingLobby);
    }

    @Override
    public List<Lobby> findAll() {
        return savedSnapshots.values()
                .stream()
                .map(Lobby::restoreFrom)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Lobby> find(LobbyId lobbyId) {
        return Optional.ofNullable(savedSnapshots.get(lobbyId))
                .map(Lobby::restoreFrom);
    }

    public void save(Lobby lobby) {
        savedSnapshots.put(lobby.id(), lobby.snapshot());
        raisedEvents.addAll(lobby.raisedEvents());
        lastSavedId = lobby.id();
    }

    public Lobby.Snapshot lastSaved() {
        if (lastSavedId == null) {
            throw new IllegalStateException("No snapshots were saved");
        }

        return savedSnapshots.get(lastSavedId);
    }

    public List<LobbyEvent> raisedEvents() {
        return unmodifiableList(raisedEvents);
    }

    public LobbyEvent lastRaisedEvent() {
        if (raisedEvents.isEmpty()) {
            throw new IllegalStateException("Expected at least one event to have been raised");
        }

        return raisedEvents.get(raisedEvents.size() - 1);
    }

}