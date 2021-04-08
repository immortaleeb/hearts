package com.github.immortaleeb.hearts.domain;

sealed public interface GameEvent permits GameStarted, CardsDealt, PlayerPassedCards, PlayerReceivedCards, StartedPlaying, CardPlayed, TrickWon, RoundEnded  {
}
