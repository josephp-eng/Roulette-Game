package roulette;

/**
 * A single numbered slot on the wheel.
 * "00" is stored internally as {@link #DOUBLE_ZERO} so every pocket has an int value.
 */
public final class Pocket {
    /** Internal value used to represent the "00" pocket. */
    public static final int DOUBLE_ZERO = 37;

    private final int value;
    private final Color color;

    Pocket(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    /** True for 0 and 00. Zeros make every outside bet lose, which is where the house edge comes from. */
    public boolean isZero() {
        return value == 0 || value == DOUBLE_ZERO;
    }

    /** The number as printed on the wheel: "0", "00", or "1" through "36". */
    public String getLabel() {
        return value == DOUBLE_ZERO ? "00" : String.valueOf(value);
    }

    /** Compact form such as "17B" or "00G". */
    public String getShortLabel() {
        return getLabel() + color.getSymbol();
    }

    @Override
    public String toString() {
        return getLabel() + " " + color.getDisplayName();
    }
}
