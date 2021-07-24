package com.github.immortaleeb.hearts.write.application.fixtures;

import com.github.immortaleeb.common.shared.PlayerId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerIdFixtures {

    public static List<PlayerId> players() {
        return Arrays.asList(
                PlayerId.of("7b90a86b-e4c0-49f0-aaf3-ce79fa765eab"),
                PlayerId.of("f6df0d25-46bb-4664-b7bf-9de53bfe7efd"),
                PlayerId.of("0972e66e-d6a2-428c-8867-dca4c1216ae1"),
                PlayerId.of("b88a9529-3a1f-423b-a6f9-650d0b2bc7ea"));
    }

    public static List<PlayerId> generatePlayers(int numberOfPlayers) {
        return IntStream.range(0, numberOfPlayers)
                .mapToObj(i -> PlayerId.generate())
                .collect(Collectors.toList());
    }

}
