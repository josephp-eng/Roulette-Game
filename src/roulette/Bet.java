package roulette;

/** A wager of some amount on one {@link BetType}. Immutable. */
public final class Bet {
    private final BetType type;
    private final int target;
    private final long amount;

    /**
     * @param type   kind of bet
     * @param target the number for STRAIGHT (0-36, or 37 for "00"), 1-3 for DOZEN/COLUMN, ignored otherwise
     * @param amount dollars wagered, at least 1
     */
    public Bet(BetType type, int target, long amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("A bet must be at least $1.");
        }
        if (type == BetType.STRAIGHT && (target < 0 || target > Pocket.DOUBLE_ZERO)) {
            throw new IllegalArgumentException("Straight-up bets must be on 0, 00, or 1-36.");
        }
        if ((type == BetType.DOZEN || type == BetType.COLUMN) && (target < 1 || target > 3)) {
            throw new IllegalArgumentException(type.getDisplayName() + " must be 1, 2, or 3.");
        }
        this.type = type;
        this.target = target;
        this.amount = amount;
    }

    /** Convenience constructor for bets that don't need a target (red, odd, high, ...). */
    public Bet(BetType type, long amount) {
        this(type, 0, amount);
    }

    public BetType getType() {
        return type;
    }

    public int getTarget() {
        return target;
    }

    public long getAmount() {
        return amount;
    }

    public boolean wins(Pocket pocket) {
        int value = pocket.getValue();
        if (type == BetType.STRAIGHT) {
            return value == target;
        }
        // Every other bet loses on 0 and 00.
        if (pocket.isZero()) {
            return false;
        }
        switch (type) {
            case RED:    return pocket.getColor() == Color.RED;
            case BLACK:  return pocket.getColor() == Color.BLACK;
            case ODD:    return value % 2 == 1;
            case EVEN:   return value % 2 == 0;
            case LOW:    return value <= 18;
            case HIGH:   return value >= 19;
            case DOZEN:  return (value - 1) / 12 + 1 == target;
            case COLUMN: return (value - 1) % 3 + 1 == target;
            default:     throw new IllegalStateException("Unhandled bet type " + type);
        }
    }

    /** Profit if this bet wins (stake not included). */
    public long profit() {
        return amount * type.getPayout();
    }

    /** Chips handed back if this bet wins: the stake plus the profit. */
    public long totalReturn() {
        return amount + profit();
    }

    /** Human-readable description, e.g. "Straight up on 17" or "2nd dozen (13-24)". */
    public String describe() {
        switch (type) {
            case STRAIGHT:
                return "Straight up on " + (target == Pocket.DOUBLE_ZERO ? "00" : String.valueOf(target));
            case DOZEN:
                int low = (target - 1) * 12 + 1;
                return ordinal(target) + " dozen (" + low + "-" + (low + 11) + ")";
            case COLUMN:
                return ordinal(target) + " column";
            default:
                return type.getDisplayName();
        }
    }

    private static String ordinal(int n) {
        switch (n) {
            case 1:  return "1st";
            case 2:  return "2nd";
            default: return "3rd";
        }
    }

    @Override
    public String toString() {
        return "$" + amount + " on " + describe();
    }
}
