package roulette;

/** The three pocket colors on a roulette wheel. */
public enum Color {
    RED("Red", 'R'),
    BLACK("Black", 'B'),
    GREEN("Green", 'G');

    private final String displayName;
    private final char symbol;

    Color(String displayName, char symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** One-letter abbreviation used in compact displays like "17B". */
    public char getSymbol() {
        return symbol;
    }
}
