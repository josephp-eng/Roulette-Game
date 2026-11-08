package roulette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * An American roulette wheel: 38 pockets (0, 00, and 1-36) in their real order around the wheel.
 */
public final class Wheel {
    /** Pocket order going clockwise around a real American wheel (37 = "00"). */
    private static final int[] ORDER = {
        0, 28, 9, 26, 30, 11, 7, 20, 32, 17, 5, 22, 34, 15, 3, 24, 36, 13, 1,
        Pocket.DOUBLE_ZERO, 27, 10, 25, 29, 12, 8, 19, 31, 18, 6, 21, 33, 16, 4, 23, 35, 14, 2
    };

    /** The 18 red numbers. Every other number from 1 to 36 is black. */
    private static final int[] RED_NUMBERS = {
        1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36
    };

    private final List<Pocket> pockets;
    private final Random random;

    public Wheel(Random random) {
        this.random = random;
        List<Pocket> list = new ArrayList<Pocket>();
        for (int value : ORDER) {
            list.add(new Pocket(value, colorOf(value)));
        }
        this.pockets = Collections.unmodifiableList(list);
    }

    /** Spins the wheel and returns the index (position around the wheel) where the ball lands. */
    public int spinIndex() {
        return random.nextInt(pockets.size());
    }

    public Pocket pocketAt(int index) {
        return pockets.get(index);
    }

    public int size() {
        return pockets.size();
    }

    public List<Pocket> getPockets() {
        return pockets;
    }

    /** Position of a pocket value around the wheel, or -1 if it isn't on the wheel. */
    public static int indexOf(int value) {
        for (int i = 0; i < ORDER.length; i++) {
            if (ORDER[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static Color colorOf(int value) {
        if (value == 0 || value == Pocket.DOUBLE_ZERO) {
            return Color.GREEN;
        }
        for (int red : RED_NUMBERS) {
            if (red == value) {
                return Color.RED;
            }
        }
        return Color.BLACK;
    }
}
