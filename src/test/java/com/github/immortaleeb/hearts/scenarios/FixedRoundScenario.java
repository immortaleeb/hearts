package com.github.immortaleeb.hearts.scenarios;

import com.github.immortaleeb.hearts.util.Events;

abstract class FixedRoundScenario implements RoundScenario {

    @Override
    public Events trick(int trickNumber) {
        return switch (trickNumber) {
            case 1 -> trick1();
            case 2 -> trick2();
            case 3 -> trick3();
            case 4 -> trick4();
            case 5 -> trick5();
            case 6 -> trick6();
            case 7 -> trick7();
            case 8 -> trick8();
            case 9 -> trick9();
            case 10 -> trick10();
            case 11 -> trick11();
            case 12 -> trick12();
            case 13 -> trick13();
            default -> Events.none();
        };
    }

    protected abstract Events trick1();

    protected abstract Events trick2();

    protected abstract Events trick3();

    protected abstract Events trick4();

    protected abstract Events trick5();

    protected abstract Events trick6();

    protected abstract Events trick7();

    protected abstract Events trick8();

    protected abstract Events trick9();

    protected abstract Events trick10();

    protected abstract Events trick11();

    protected abstract Events trick12();

    protected abstract Events trick13();

}
