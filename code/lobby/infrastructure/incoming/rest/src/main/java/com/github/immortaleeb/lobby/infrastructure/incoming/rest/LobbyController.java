package com.github.immortaleeb.lobby.infrastructure.incoming.rest;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.application.api.command.CreateLobby;
import com.github.immortaleeb.lobby.application.api.command.JoinLobby;
import com.github.immortaleeb.lobby.application.api.command.StartGame;
import com.github.immortaleeb.lobby.application.api.query.GetLobbyDetails;
import com.github.immortaleeb.lobby.application.api.query.ListLobbies;
import com.github.immortaleeb.lobby.shared.GameId;
import com.github.immortaleeb.lobby.shared.LobbyId;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
class LobbyController {

    private final CommandDispatcher commandDispatcher;
    private final ListLobbies listLobbies;
    private final GetLobbyDetails getLobbyDetails;

    LobbyController(CommandDispatcher commandDispatcher, ListLobbies listLobbies, GetLobbyDetails getLobbyDetails) {
        this.commandDispatcher = commandDispatcher;
        this.listLobbies = listLobbies;
        this.getLobbyDetails = getLobbyDetails;
    }

    @GetMapping("/api/v1/lobbies")
    public List<ListLobbies.LobbySummary> listLobbies() {
        return listLobbies.listLobbies();
    }

    @GetMapping("/api/v1/lobbies/{lobbyId}")
    public GetLobbyDetails.LobbyDetails getLobbyDetails(@PathParam("lobbyId") LobbyId lobbyId) {
        return getLobbyDetails.getDetails(lobbyId);
    }

    @PostMapping("/api/v1/lobbies")
    public CreateLobbyResponse createLobby(@RequestHeader("X-Player-Id") PlayerId playerId, @RequestBody CreateLobbyRequest request) {
        LobbyId lobbyId = commandDispatcher.dispatch(new CreateLobby(request.name, playerId));
        return new CreateLobbyResponse(lobbyId, request.name, playerId);
    }

    @PostMapping("/api/v1/lobbies/{lobbyId}/join")
    public void joinLobby(@RequestHeader("X-Player-Id") PlayerId playerId, @PathParam("lobbyId") LobbyId lobbyId) {
        commandDispatcher.dispatch(new JoinLobby(lobbyId, playerId));
    }

    @PostMapping("/api/v1/lobbies/{lobbyId}/start")
    public StartGameResponse startGame(@PathParam("lobbyId") LobbyId lobbyId) {
        GameId gameId = commandDispatcher.dispatch(new StartGame(lobbyId));
        return new StartGameResponse(gameId);
    }

    record CreateLobbyRequest(String name) {}
    record CreateLobbyResponse(LobbyId id, String name, PlayerId createdBy) {}
    record StartGameResponse(GameId id) { }

}
