package roulette;

/** The kinds of bets the table accepts, with their standard American roulette payouts. */
public enum BetType {
    STRAIGHT("Straight up", 35),
    RED("Red", 1),
    BLACK("Black", 1),
    ODD("Odd", 1),
    EVEN("Even", 1),
    LOW("Low (1-18)", 1),
    HIGH("High (19-36)", 1),
    DOZEN("Dozen", 2),
    COLUMN("Column", 2);

    private final String displayName;
    private final int payout;

    BetType(String displayName, int payout) {
        this.displayName = displayName;
        this.payout = payout;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Winnings per $1 bet, not counting the returned stake (e.g. 35 means "35 to 1"). */
    public int getPayout() {
        return payout;
    }
}
