package roulette;

/**
 * Turns what the player types into a {@link Bet}.
 *
 * <p>Format: {@code <what to bet on> <amount>}. Examples:
 * <pre>
 *   red 25        black 10       odd 5        even 5
 *   low 20        high 20        1-18 20      19-36 20
 *   17 10         0 5            00 5
 *   dozen 2 30    d2 30          column 3 15  c3 15
 *   red all       (bets every chip you have)
 * </pre>
 */
public final class BetParser {

    private BetParser() {
    }

    /**
     * @param input     the line the player typed
     * @param available chips the player has, used when the amount is "all"
     * @throws IllegalArgumentException with a friendly message if the input isn't a valid bet
     */
    public static Bet parse(String input, long available) {
        String[] tokens = input.trim().toLowerCase().split("\\s+");
        if (tokens.length < 2 || tokens[0].isEmpty()) {
            throw new IllegalArgumentException(
                "'" + input.trim() + "' isn't a command or a bet. Bets look like 'red 25' or '17 10'.");
        }

        long amount = parseAmount(tokens[tokens.length - 1], available);

        if (tokens.length == 2) {
            return parseSingleWordBet(tokens[0], amount);
        }
        if (tokens.length == 3) {
            String kind = tokens[0];
            if (kind.equals("dozen") || kind.equals("doz")) {
                return new Bet(BetType.DOZEN, parseOneToThree(tokens[1], "Dozen"), amount);
            }
            if (kind.equals("column") || kind.equals("col")) {
                return new Bet(BetType.COLUMN, parseOneToThree(tokens[1], "Column"), amount);
            }
            if (kind.equals("straight") || kind.equals("number")) {
                return new Bet(BetType.STRAIGHT, parseNumber(tokens[1]), amount);
            }
        }
        throw new IllegalArgumentException("I don't recognize the bet '" + input.trim() + "'. Type 'help' for examples.");
    }

    private static Bet parseSingleWordBet(String word, long amount) {
        if (word.equals("red"))                          return new Bet(BetType.RED, amount);
        if (word.equals("black"))                        return new Bet(BetType.BLACK, amount);
        if (word.equals("odd"))                          return new Bet(BetType.ODD, amount);
        if (word.equals("even"))                         return new Bet(BetType.EVEN, amount);
        if (word.equals("low") || word.equals("1-18"))   return new Bet(BetType.LOW, amount);
        if (word.equals("high") || word.equals("19-36")) return new Bet(BetType.HIGH, amount);
        if (word.matches("d[1-3]"))  return new Bet(BetType.DOZEN, word.charAt(1) - '0', amount);
        if (word.matches("c[1-3]"))  return new Bet(BetType.COLUMN, word.charAt(1) - '0', amount);
        if (word.equals("green")) {
            throw new IllegalArgumentException("There's no 'green' bet. Bet on 0 or 00 directly, e.g. '0 5' or '00 5'.");
        }
        if (word.matches("\\d+")) {
            return new Bet(BetType.STRAIGHT, parseNumber(word), amount);
        }
        throw new IllegalArgumentException("I don't recognize the bet '" + word + "'. Type 'help' for examples.");
    }

    private static int parseNumber(String word) {
        if (word.equals("00")) {
            return Pocket.DOUBLE_ZERO;
        }
        if (word.matches("\\d{1,2}")) {
            int n = Integer.parseInt(word);
            if (n <= 36) {
                return n;
            }
        }
        throw new IllegalArgumentException("'" + word + "' isn't on the wheel. Pick 0, 00, or 1-36.");
    }

    private static int parseOneToThree(String word, String what) {
        if (word.equals("1") || word.equals("1st") || word.equals("first"))  return 1;
        if (word.equals("2") || word.equals("2nd") || word.equals("second")) return 2;
        if (word.equals("3") || word.equals("3rd") || word.equals("third"))  return 3;
        throw new IllegalArgumentException(what + " must be 1, 2, or 3.");
    }

    private static long parseAmount(String word, long available) {
        if (word.equals("all") || word.equals("max")) {
            if (available < 1) {
                throw new IllegalArgumentException("You don't have any chips left to bet.");
            }
            return available;
        }
        String digits = word.startsWith("$") ? word.substring(1) : word;
        if (!digits.matches("\\d{1,12}")) {
            throw new IllegalArgumentException("'" + word + "' isn't a valid amount. Use a whole number of dollars, like 25.");
        }
        long amount = Long.parseLong(digits);
        if (amount < 1) {
            throw new IllegalArgumentException("A bet must be at least $1.");
        }
        return amount;
    }
}
