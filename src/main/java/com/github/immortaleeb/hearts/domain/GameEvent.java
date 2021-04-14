package com.github.immortaleeb.hearts.domain;

sealed public interface GameEvent permits GameStarted, CardsDealt, PlayerPassedCards, PlayerHasTakenPassedCards, StartedPlaying, CardPlayed, TrickWon, RoundEnded  {
}
