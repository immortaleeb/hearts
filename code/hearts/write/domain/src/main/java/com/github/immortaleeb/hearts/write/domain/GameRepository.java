package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.GameId;

public interface GameRepository {

    Game load(GameId gameId);

    void save(Game game);
}
