package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeLobbyRepository implements LobbyRepository {

    private List<Lobby.Snapshot> savedSnapshots = new ArrayList<>();

    public void save(Lobby lobby) {
        savedSnapshots.add(lobby.snapshot());
    }

    public Lobby.Snapshot lastSaved() {
        if (savedSnapshots.isEmpty()) {
            throw new IllegalStateException("No snapshots were saved");
        }

        return savedSnapshots.get(savedSnapshots.size() - 1);
    }

}
