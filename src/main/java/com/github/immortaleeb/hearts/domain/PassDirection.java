package com.github.immortaleeb.hearts.domain;

public enum PassDirection {

    LEFT(1, -1),
    RIGHT(-1, 1),
    DIAGONAL(2, 2),
    ;

    private final int toOffset;
    private final int fromOffset;

    PassDirection(int toOffset, int fromOffset) {
        this.toOffset = toOffset;
        this.fromOffset = fromOffset;
    }

    public int toOffset() {
        return toOffset;
    }

    public int fromOffset() {
        return fromOffset;
    }

}
