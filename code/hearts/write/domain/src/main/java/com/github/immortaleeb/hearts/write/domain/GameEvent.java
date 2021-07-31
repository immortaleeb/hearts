package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hears.common.shared.GameId;

sealed public interface GameEvent permits GameStarted, CardsDealt, PlayerPassedCards, PlayerHasTakenPassedCards, StartedPlaying, CardPlayed, TrickWon,
    RoundEnded, GameEnded  {

    GameId gameId();

}

