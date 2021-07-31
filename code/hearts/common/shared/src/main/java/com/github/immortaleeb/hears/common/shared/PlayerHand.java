package com.github.immortaleeb.hears.common.shared;

import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;

public record PlayerHand(PlayerId player, List<Card> cards) {
}
