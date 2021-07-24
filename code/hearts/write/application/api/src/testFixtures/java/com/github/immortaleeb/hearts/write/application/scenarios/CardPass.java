package com.github.immortaleeb.hearts.write.application.scenarios;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;

public record CardPass(PlayerId fromPlayer, PlayerId toPlayer, List<Card> cards) { }
