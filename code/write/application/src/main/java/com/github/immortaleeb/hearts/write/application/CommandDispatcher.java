package com.github.immortaleeb.hearts.write.application;

import com.github.immortaleeb.hearts.write.shared.GameId;

public interface CommandDispatcher {

    GameId dispatch(StartGame startGame);

    void dispatch(PassCards passCards);

    void dispatch(PlayCard playCard);

}
