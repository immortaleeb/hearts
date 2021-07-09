package com.github.immortaleeb.hearts.scenarios;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;

public record CardPass(PlayerId fromPlayer, PlayerId toPlayer, List<Card> cards) { }
