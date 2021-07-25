package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.application.api.CommandHandler;
import com.github.immortaleeb.lobby.application.api.command.StartGame;
import com.github.immortaleeb.lobby.domain.GameStarter;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyRepository;
import com.github.immortaleeb.lobby.shared.LobbyGameId;
import com.github.immortaleeb.lobby.shared.LobbyNotFound;

public class StartGameCommandHandler implements CommandHandler<LobbyGameId, StartGame> {

    private final LobbyRepository lobbyRepository;
    private final GameStarter gameStarter;

    public StartGameCommandHandler(LobbyRepository lobbyRepository, GameStarter gameStarter) {
        this.lobbyRepository = lobbyRepository;
        this.gameStarter = gameStarter;
    }

    @Override
    public LobbyGameId handle(StartGame command) {
        Lobby lobby = lobbyRepository.find(command.lobbyId())
                .orElseThrow(() -> new LobbyNotFound("Could not find lobby with id " + command.lobbyId()));
        return startGame(lobby);
    }

    private LobbyGameId startGame(Lobby existingLobby) {
        existingLobby.startGame(gameStarter);
        lobbyRepository.save(existingLobby);
        return existingLobby.snapshot().game();
    }

}
