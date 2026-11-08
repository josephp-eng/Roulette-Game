package roulette;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Self-contained tests (no JUnit needed). Run with:
 * <pre>  java -cp out roulette.RouletteTest</pre>
 */
public final class RouletteTest {
    private static int passed;
    private static int failed;

    public static void main(String[] args) throws IOException {
        wheelHas38UniquePockets();
        wheelHasCorrectColors();
        outsideBetsLoseOnZeros();
        dozensAndColumnsCoverTheRightNumbers();
        everyBetHasTheSameHouseEdge();
        parserAcceptsValidBets();
        parserRejectsInvalidBets();
        gamePaysOutCorrectly();
        gameRefundsBetsOnQuit();
        gameEndsWhenBroke();

        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ------------------------------------------------------------------ wheel

    private static void wheelHas38UniquePockets() {
        Wheel wheel = new Wheel(new Random());
        Set<Integer> values = new HashSet<Integer>();
        for (Pocket p : wheel.getPockets()) {
            values.add(p.getValue());
        }
        check(wheel.size() == 38, "wheel has 38 pockets");
        check(values.size() == 38, "all pocket values are unique");
        for (int v = 0; v <= Pocket.DOUBLE_ZERO; v++) {
            check(values.contains(v), "wheel contains " + v);
        }
    }

    private static void wheelHasCorrectColors() {
        int red = 0;
        int black = 0;
        int green = 0;
        for (Pocket p : new Wheel(new Random()).getPockets()) {
            if (p.getColor() == Color.RED) red++;
            if (p.getColor() == Color.BLACK) black++;
            if (p.getColor() == Color.GREEN) green++;
        }
        check(red == 18 && black == 18 && green == 2, "18 red, 18 black, 2 green");
        check(Wheel.colorOf(1) == Color.RED, "1 is red");
        check(Wheel.colorOf(2) == Color.BLACK, "2 is black");
        check(Wheel.colorOf(10) == Color.BLACK, "10 is black");
        check(Wheel.colorOf(19) == Color.RED, "19 is red");
        check(Wheel.colorOf(Pocket.DOUBLE_ZERO) == Color.GREEN, "00 is green");
    }

    // ------------------------------------------------------------------ bets

    private static void outsideBetsLoseOnZeros() {
        Pocket zero = pocket(0);
        Pocket doubleZero = pocket(Pocket.DOUBLE_ZERO);
        BetType[] outside = {BetType.RED, BetType.BLACK, BetType.ODD, BetType.EVEN, BetType.LOW, BetType.HIGH};
        for (BetType type : outside) {
            check(!new Bet(type, 10).wins(zero), type + " loses on 0");
            check(!new Bet(type, 10).wins(doubleZero), type + " loses on 00");
        }
        check(!new Bet(BetType.DOZEN, 1, 10).wins(zero), "dozen loses on 0");
        check(!new Bet(BetType.COLUMN, 3, 10).wins(doubleZero), "column loses on 00");
        check(new Bet(BetType.STRAIGHT, 0, 10).wins(zero), "straight 0 wins on 0");
        check(!new Bet(BetType.STRAIGHT, 0, 10).wins(doubleZero), "straight 0 loses on 00");
        check(new Bet(BetType.STRAIGHT, Pocket.DOUBLE_ZERO, 10).wins(doubleZero), "straight 00 wins on 00");
    }

    private static void dozensAndColumnsCoverTheRightNumbers() {
        check(new Bet(BetType.DOZEN, 1, 1).wins(pocket(12)), "12 is in 1st dozen");
        check(new Bet(BetType.DOZEN, 2, 1).wins(pocket(13)), "13 is in 2nd dozen");
        check(new Bet(BetType.DOZEN, 3, 1).wins(pocket(36)), "36 is in 3rd dozen");
        check(!new Bet(BetType.DOZEN, 1, 1).wins(pocket(13)), "13 is not in 1st dozen");
        check(new Bet(BetType.COLUMN, 1, 1).wins(pocket(34)), "34 is in column 1");
        check(new Bet(BetType.COLUMN, 2, 1).wins(pocket(35)), "35 is in column 2");
        check(new Bet(BetType.COLUMN, 3, 1).wins(pocket(36)), "36 is in column 3");
        check(!new Bet(BetType.COLUMN, 1, 1).wins(pocket(3)), "3 is not in column 1");
        check(new Bet(BetType.LOW, 1).wins(pocket(18)) && !new Bet(BetType.LOW, 1).wins(pocket(19)), "low is 1-18");
    }

    /**
     * On an American wheel every bet returns $36 per $38 wagered across all outcomes,
     * i.e. the house keeps 2/38 = 5.26% no matter what you bet on.
     */
    private static void everyBetHasTheSameHouseEdge() {
        Wheel wheel = new Wheel(new Random());
        Bet[] samples = {
            new Bet(BetType.STRAIGHT, 17, 1), new Bet(BetType.STRAIGHT, Pocket.DOUBLE_ZERO, 1),
            new Bet(BetType.RED, 1), new Bet(BetType.BLACK, 1), new Bet(BetType.ODD, 1), new Bet(BetType.EVEN, 1),
            new Bet(BetType.LOW, 1), new Bet(BetType.HIGH, 1),
            new Bet(BetType.DOZEN, 2, 1), new Bet(BetType.COLUMN, 3, 1),
        };
        for (Bet bet : samples) {
            long returned = 0;
            for (Pocket p : wheel.getPockets()) {
                if (bet.wins(p)) {
                    returned += bet.totalReturn();
                }
            }
            check(returned == 36, bet.describe() + " returns $36 over all 38 pockets (got " + returned + ")");
        }
    }

    // ------------------------------------------------------------------ parser

    private static void parserAcceptsValidBets() {
        checkParse("red 25", BetType.RED, 0, 25);
        checkParse("BLACK $10", BetType.BLACK, 0, 10);
        checkParse("17 5", BetType.STRAIGHT, 17, 5);
        checkParse("0 5", BetType.STRAIGHT, 0, 5);
        checkParse("00 5", BetType.STRAIGHT, Pocket.DOUBLE_ZERO, 5);
        checkParse("straight 36 1", BetType.STRAIGHT, 36, 1);
        checkParse("1-18 20", BetType.LOW, 0, 20);
        checkParse("high 20", BetType.HIGH, 0, 20);
        checkParse("dozen 2 30", BetType.DOZEN, 2, 30);
        checkParse("d3 30", BetType.DOZEN, 3, 30);
        checkParse("column 1st 15", BetType.COLUMN, 1, 15);
        checkParse("c2 15", BetType.COLUMN, 2, 15);
        checkParse("  odd   all ", BetType.ODD, 0, 500);
    }

    private static void parserRejectsInvalidBets() {
        String[] bad = {"red", "blue 10", "37 10", "100 10", "red 0", "red -5", "red ten", "red 1.5",
                        "dozen 4 10", "column 0 10", "green 10", "red 10 extra words", "spinn"};
        for (String input : bad) {
            boolean threw = false;
            try {
                BetParser.parse(input, 500);
            } catch (IllegalArgumentException e) {
                threw = e.getMessage() != null && !e.getMessage().isEmpty();
            }
            check(threw, "rejects '" + input + "' with a message");
        }
    }

    // ------------------------------------------------------------------ full game

    private static void gamePaysOutCorrectly() throws IOException {
        // Ball lands on 17 (black). Red loses $10, straight 17 wins 35 x $5 = $175.
        Player player = runGame(17, 500, "red 10\n17 5\nspin\nquit\n");
        check(player.getBalance() == 500 - 10 + 175, "balance after win is $665 (got " + player.getBalance() + ")");
        check(player.getSpins() == 1 && player.getWinningSpins() == 1, "one winning spin recorded");
        check(player.getBiggestWin() == 165, "biggest net win is $165 (got " + player.getBiggestWin() + ")");

        // Repeat bets and lose on 00.
        player = runGame(Pocket.DOUBLE_ZERO, 500, "red 10\nodd 10\nspin\nr\nspin\nquit\n");
        check(player.getBalance() == 460, "two losing spins of $20 leave $460 (got " + player.getBalance() + ")");
        check(player.getTotalWagered() == 40, "total wagered is $40");
    }

    private static void gameRefundsBetsOnQuit() throws IOException {
        Player player = runGame(1, 500, "red 100\nblack 50\nquit\n");
        check(player.getBalance() == 500, "unspun bets are refunded on quit");

        player = runGame(1, 500, "red 100\nclear\nquit\n");
        check(player.getBalance() == 500, "clear refunds bets");

        player = runGame(1, 100, "red 80\nblack 80\nquit\n");
        check(player.getBalance() == 100, "can't bet more than your chips");
    }

    private static void gameEndsWhenBroke() throws IOException {
        // Lose everything on a black spin; the game should end without needing 'quit'.
        Player player = runGame(2, 50, "red all\nspin\n");
        check(player.getBalance() == 0, "going all-in on red and landing on black leaves $0");
    }

    // ------------------------------------------------------------------ helpers

    /** Plays a scripted game where every spin lands on {@code landOn}, and returns the player afterward. */
    private static Player runGame(int landOn, long balance, String input) throws IOException {
        Player player = new Player(balance);
        Wheel wheel = new Wheel(new FixedRandom(Wheel.indexOf(landOn)));
        BufferedReader in = new BufferedReader(new StringReader(input));
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        new RouletteGame(wheel, player, in, out, false).play();
        return player;
    }

    private static Pocket pocket(int value) {
        return new Pocket(value, Wheel.colorOf(value));
    }

    private static void checkParse(String input, BetType type, int target, long amount) {
        try {
            Bet bet = BetParser.parse(input, 500);
            boolean targetOk = type != BetType.STRAIGHT && type != BetType.DOZEN && type != BetType.COLUMN
                    || bet.getTarget() == target;
            check(bet.getType() == type && targetOk && bet.getAmount() == amount, "parses '" + input + "'");
        } catch (IllegalArgumentException e) {
            check(false, "parses '" + input + "' (threw: " + e.getMessage() + ")");
        }
    }

    private static void check(boolean condition, String name) {
        if (condition) {
            passed++;
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    /** A Random that always returns the same index, so tests control where the ball lands. */
    private static final class FixedRandom extends Random {
        private static final long serialVersionUID = 1L;
        private final int value;

        FixedRandom(int value) {
            this.value = value;
        }

        @Override
        public int nextInt(int bound) {
            return value;
        }
    }
}
