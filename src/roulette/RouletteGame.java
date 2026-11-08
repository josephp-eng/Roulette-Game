package roulette;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The interactive table: reads commands, holds the bets currently on the felt, spins, and pays out.
 * Input and output are passed in so the whole game can be driven by a test.
 */
public final class RouletteGame {
    private static final int HISTORY_SIZE = 12;
    private static final int WINDOW_RADIUS = 3;

    private final Wheel wheel;
    private final Player player;
    private final BufferedReader in;
    private final PrintStream out;
    private final boolean animate;

    private final List<Bet> bets = new ArrayList<Bet>();
    private List<Bet> lastBets = new ArrayList<Bet>();
    private final LinkedList<Pocket> history = new LinkedList<Pocket>();
    private int ballPosition = 0;

    public RouletteGame(Wheel wheel, Player player, BufferedReader in, PrintStream out, boolean animate) {
        this.wheel = wheel;
        this.player = player;
        this.in = in;
        this.out = out;
        this.animate = animate;
    }

    /** Runs the game until the player quits, runs out of chips, or input ends. */
    public void play() throws IOException {
        printWelcome();
        while (true) {
            if (player.getBalance() == 0 && bets.isEmpty()) {
                out.println();
                out.println("You're out of chips. The house always wins eventually!");
                break;
            }
            out.println();
            out.print("[" + money(player.getBalance()) + " in chips | " + money(tableTotal()) + " on the table] > ");
            out.flush();

            String line = in.readLine();
            if (line == null) {
                out.println();
                break;
            }
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (!handle(line)) {
                break;
            }
        }
        refundTable();
        printSummary();
    }

    /** @return false when the player wants to stop playing */
    private boolean handle(String line) {
        String cmd = line.toLowerCase();
        if (cmd.equals("quit") || cmd.equals("q") || cmd.equals("exit") || cmd.equals("cash out")) {
            return false;
        } else if (cmd.equals("help") || cmd.equals("?")) {
            printHelp();
        } else if (cmd.equals("table") || cmd.equals("layout")) {
            printTable();
        } else if (cmd.equals("bets")) {
            printBets();
        } else if (cmd.equals("clear")) {
            clearBets();
        } else if (cmd.equals("spin") || cmd.equals("s")) {
            spin();
        } else if (cmd.equals("repeat") || cmd.equals("r")) {
            repeatLastBets();
        } else if (cmd.equals("history") || cmd.equals("h")) {
            printHistory();
        } else if (cmd.equals("stats")) {
            printStats();
        } else {
            placeBet(line);
        }
        return true;
    }

    // ---------------------------------------------------------------- betting

    private void placeBet(String line) {
        Bet bet;
        try {
            bet = BetParser.parse(line, player.getBalance());
        } catch (IllegalArgumentException e) {
            out.println(e.getMessage());
            return;
        }
        if (bet.getAmount() > player.getBalance()) {
            out.println("You only have " + money(player.getBalance()) + " in chips.");
            return;
        }
        player.withdraw(bet.getAmount());
        bets.add(bet);
        out.println("Placed " + money(bet.getAmount()) + " on " + bet.describe()
                + " (pays " + bet.getType().getPayout() + " to 1).");
    }

    private void repeatLastBets() {
        if (lastBets.isEmpty()) {
            out.println("There are no bets from a previous spin to repeat.");
            return;
        }
        long total = sum(lastBets);
        if (total > player.getBalance()) {
            out.println("Repeating needs " + money(total) + " but you only have " + money(player.getBalance()) + ".");
            return;
        }
        for (Bet bet : lastBets) {
            player.withdraw(bet.getAmount());
            bets.add(bet);
        }
        out.println("Re-placed " + lastBets.size() + " bet(s) totaling " + money(total) + ".");
    }

    private void clearBets() {
        if (bets.isEmpty()) {
            out.println("There are no bets on the table.");
            return;
        }
        long total = tableTotal();
        refundTable();
        out.println("Took " + money(total) + " back off the table.");
    }

    private void refundTable() {
        player.deposit(tableTotal());
        bets.clear();
    }

    // ---------------------------------------------------------------- spinning

    private void spin() {
        if (bets.isEmpty()) {
            out.println("Place at least one bet before spinning. Try 'red 10' or 'help'.");
            return;
        }
        out.println("No more bets! The wheel is spinning...");
        int landing = wheel.spinIndex();
        rollBallTo(landing);
        Pocket pocket = wheel.pocketAt(landing);

        out.println();
        out.println("  *** " + pocket.getLabel() + " " + pocket.getColor().getDisplayName().toUpperCase() + " ***");
        out.println();

        long staked = tableTotal();
        long returned = 0;
        for (Bet bet : bets) {
            if (bet.wins(pocket)) {
                returned += bet.totalReturn();
                out.println(String.format("  WIN   %-24s %8s", bet.describe(), "+" + money(bet.profit())));
            } else {
                out.println(String.format("  lose  %-24s %8s", bet.describe(), "-" + money(bet.getAmount())));
            }
        }

        player.deposit(returned);
        player.recordSpin(staked, returned);
        lastBets = new ArrayList<Bet>(bets);
        bets.clear();

        history.addFirst(pocket);
        if (history.size() > HISTORY_SIZE) {
            history.removeLast();
        }

        long net = returned - staked;
        out.println();
        if (net > 0) {
            out.println("You're up " + money(net) + " this spin. Balance: " + money(player.getBalance()));
        } else if (net == 0) {
            out.println("You broke even this spin. Balance: " + money(player.getBalance()));
        } else {
            out.println("You're down " + money(-net) + " this spin. Balance: " + money(player.getBalance()));
        }
        out.println("Type 'r' to repeat those bets, or place new ones.");
    }

    /** Moves the ball around the wheel at least one full lap, slowing down until it stops on the target. */
    private void rollBallTo(int target) {
        int size = wheel.size();
        if (animate) {
            int distance = size + Math.floorMod(target - ballPosition, size);
            for (int step = 1; step <= distance; step++) {
                int pos = (ballPosition + step) % size;
                out.print("\r  " + windowAround(pos));
                out.flush();
                double progress = step / (double) distance;
                pause(12 + (long) (230 * Math.pow(progress, 4)));
            }
            out.println();
        }
        ballPosition = target;
    }

    /** A strip of the wheel centered on the ball, e.g. "  20R  32R  17B >  5R<  22B  34R  15B". */
    private String windowAround(int pos) {
        int size = wheel.size();
        StringBuilder sb = new StringBuilder();
        for (int offset = -WINDOW_RADIUS; offset <= WINDOW_RADIUS; offset++) {
            Pocket p = wheel.pocketAt(Math.floorMod(pos + offset, size));
            String cell = padLeft(p.getShortLabel(), 3);
            sb.append(offset == 0 ? " >" + cell + "<" : "  " + cell + " ");
        }
        return sb.toString();
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ---------------------------------------------------------------- screens

    private void printWelcome() {
        out.println("==============================================");
        out.println("          AMERICAN ROULETTE  (0 / 00)");
        out.println("==============================================");
        out.println("You start with " + money(player.getBalance()) + " in chips.");
        out.println("Place as many bets as you like, then type 'spin'.");
        out.println("Type 'help' for every bet and command, or 'table' to see the layout.");
    }

    private void printHelp() {
        out.println("BETS  (type what to bet on, then the amount; 'all' bets every chip)");
        out.println("  17 10        Straight up on one number (0, 00, 1-36)   pays 35 to 1");
        out.println("  red 25       Red / black                               pays  1 to 1");
        out.println("  odd 5        Odd / even                                pays  1 to 1");
        out.println("  low 20       Low (1-18) / high (19-36)                 pays  1 to 1");
        out.println("  dozen 2 30   1st, 2nd or 3rd dozen (also: d2 30)       pays  2 to 1");
        out.println("  column 3 15  1st, 2nd or 3rd column (also: c3 15)      pays  2 to 1");
        out.println("  0 and 00 make every bet lose except a straight-up bet on them.");
        out.println();
        out.println("COMMANDS");
        out.println("  spin (s)     Spin the wheel");
        out.println("  bets         Show the bets on the table");
        out.println("  clear        Take all your bets back");
        out.println("  repeat (r)   Place the same bets as last spin");
        out.println("  table        Show the betting layout");
        out.println("  history (h)  Recent results");
        out.println("  stats        Your session stats");
        out.println("  quit (q)     Cash out and leave");
    }

    private void printTable() {
        // Each number cell is 4 characters wide; 12 cells + 1 trailing space = 49 between the borders.
        out.println("          +-------------------------------------------------+");
        for (int column = 3; column >= 1; column--) {
            StringBuilder row = new StringBuilder(column == 2 ? "   0 / 00 |" : "          |");
            for (int n = column; n <= 36; n += 3) {
                row.append(padLeft(n + "" + Wheel.colorOf(n).getSymbol(), 4));
            }
            row.append(" | column ").append(column);
            out.println(row);
        }
        out.println("          +----------------+----------------+---------------+");
        out.println("          |   1st dozen    |   2nd dozen    |   3rd dozen   |");
        out.println("          +----------+------+-----+-------+-----+-----------+");
        out.println("          | low 1-18 | even | red | black | odd |19-36 high |");
        out.println("          +-------------------------------------------------+");
    }

    private void printBets() {
        if (bets.isEmpty()) {
            out.println("There are no bets on the table.");
            return;
        }
        out.println("On the table:");
        for (Bet bet : bets) {
            out.println(String.format("  %8s on %-24s (wins %s)", money(bet.getAmount()), bet.describe(), money(bet.profit())));
        }
        out.println("  Total: " + money(tableTotal()));
    }

    private void printHistory() {
        if (history.isEmpty()) {
            out.println("No spins yet.");
            return;
        }
        int red = 0;
        int black = 0;
        int green = 0;
        StringBuilder sb = new StringBuilder("Last " + history.size() + " (newest first): ");
        for (Pocket p : history) {
            sb.append(p.getShortLabel()).append(' ');
            if (p.getColor() == Color.RED) {
                red++;
            } else if (p.getColor() == Color.BLACK) {
                black++;
            } else {
                green++;
            }
        }
        out.println(sb.toString().trim());
        out.println("Red " + red + "  |  Black " + black + "  |  Green " + green);
        out.println("(Every spin is independent; past results don't change the odds.)");
    }

    private void printStats() {
        out.println("Spins played:   " + player.getSpins());
        out.println("Winning spins:  " + player.getWinningSpins());
        out.println("Total wagered:  " + money(player.getTotalWagered()));
        out.println("Biggest win:    " + money(player.getBiggestWin()));
        out.println("Peak balance:   " + money(player.getPeakBalance()));
        out.println("Net so far:     " + signedMoney(player.getBalance() + tableTotal() - player.getStartingBalance()));
    }

    private void printSummary() {
        out.println();
        out.println("=============== CASHING OUT ===============");
        printStats();
        out.println("Final balance:  " + money(player.getBalance()));
        out.println("Thanks for playing!");
    }

    // ---------------------------------------------------------------- helpers

    private long tableTotal() {
        return sum(bets);
    }

    private static long sum(List<Bet> list) {
        long total = 0;
        for (Bet bet : list) {
            total += bet.getAmount();
        }
        return total;
    }

    static String money(long amount) {
        return String.format("$%,d", amount);
    }

    static String signedMoney(long amount) {
        return (amount < 0 ? "-" : "+") + money(Math.abs(amount));
    }

    private static String padLeft(String s, int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < width; i++) {
            sb.append(' ');
        }
        return sb.append(s).toString();
    }
}
