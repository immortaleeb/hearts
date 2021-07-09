package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.application.*;
import com.github.immortaleeb.hearts.write.shared.GameId;

import java.util.ArrayList;
import java.util.List;

public class FakeCommandDispatcher implements CommandDispatcher {

    private final List<Command> dispatchedCommands = new ArrayList<>();

    @Override
    public GameId dispatch(StartGame startGame) {
        dispatchedCommands.add(startGame);
        return GameId.generate();
    }

    @Override
    public void dispatch(PassCards passCards) {
        dispatchedCommands.add(passCards);
    }

    @Override
    public void dispatch(PlayCard playCard) {
        dispatchedCommands.add(playCard);
    }

    public List<Command> dispatchedCommands() {
        return dispatchedCommands;
    }

}
