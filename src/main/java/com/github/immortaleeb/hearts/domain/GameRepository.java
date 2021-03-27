package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.GameId;

public interface GameRepository {

    Game load(GameId gameId);

    void save(Game game);
}
