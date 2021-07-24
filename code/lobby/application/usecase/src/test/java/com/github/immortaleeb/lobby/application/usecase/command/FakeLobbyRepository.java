package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyEvent;
import com.github.immortaleeb.lobby.domain.LobbyRepository;
import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

public class FakeLobbyRepository implements LobbyRepository {

    private final List<Lobby.Snapshot> savedSnapshots = new ArrayList<>();
    private final List<LobbyEvent> raisedEvents = new ArrayList<>();

    public void givenExisting(Lobby.Snapshot existingLobby) {
        savedSnapshots.add(existingLobby);
    }

    @Override
    public Optional<Lobby> find(LobbyId lobbyId) {
        return savedSnapshots.stream()
                .filter(snapshot -> snapshot.id().equals(lobbyId))
                .findFirst()
                .map(Lobby::restoreFrom);
    }

    public void save(Lobby lobby) {
        savedSnapshots.add(lobby.snapshot());
        raisedEvents.addAll(lobby.raisedEvents());
    }

    public Lobby.Snapshot lastSaved() {
        if (savedSnapshots.isEmpty()) {
            throw new IllegalStateException("No snapshots were saved");
        }

        return savedSnapshots.get(savedSnapshots.size() - 1);
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
