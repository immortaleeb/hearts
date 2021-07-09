package hearts.scenarios;

abstract class FixedRoundScenario implements RoundScenario {

    @Override
    public Trick trick(int trickNumber) {
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
            default -> throw new RuntimeException("Invalid trick number");
        };
    }

    protected abstract Trick trick1();

    protected abstract Trick trick2();

    protected abstract Trick trick3();

    protected abstract Trick trick4();

    protected abstract Trick trick5();

    protected abstract Trick trick6();

    protected abstract Trick trick7();

    protected abstract Trick trick8();

    protected abstract Trick trick9();

    protected abstract Trick trick10();

    protected abstract Trick trick11();

    protected abstract Trick trick12();

    protected abstract Trick trick13();

}
