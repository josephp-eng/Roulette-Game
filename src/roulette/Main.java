package roulette;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Entry point.
 *
 * <pre>
 *   java -cp out roulette.Main [--balance N] [--seed N] [--fast]
 * </pre>
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) throws IOException {
        long balance = 500;
        Long seed = null;
        // Animate only when a person is at the keyboard, not when input is piped in.
        boolean animate = System.console() != null;

        try {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equals("--balance")) {
                    balance = Long.parseLong(args[++i]);
                } else if (arg.equals("--seed")) {
                    seed = Long.valueOf(args[++i]);
                } else if (arg.equals("--fast")) {
                    animate = false;
                } else if (arg.equals("--help") || arg.equals("-h")) {
                    printUsage();
                    return;
                } else {
                    throw new IllegalArgumentException("Unknown option: " + arg);
                }
            }
            if (balance < 1) {
                throw new IllegalArgumentException("--balance must be at least 1");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: an option is missing its value");
            printUsage();
            System.exit(1);
            return;
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
            System.exit(1);
            return;
        }

        Random random = seed == null ? new Random() : new Random(seed);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        new RouletteGame(new Wheel(random), new Player(balance), in, System.out, animate).play();
    }

    private static void printUsage() {
        System.out.println("Usage: java -cp out roulette.Main [--balance N] [--seed N] [--fast]");
        System.out.println("  --balance N  starting chips (default 500)");
        System.out.println("  --seed N     make spins repeatable");
        System.out.println("  --fast       skip the spinning animation");
    }
}
