package roulette;

/** Tracks the player's chips and lifetime stats for the session. */
public final class Player {
    private final long startingBalance;
    private long balance;
    private long peakBalance;
    private int spins;
    private int winningSpins;
    private long totalWagered;
    private long biggestWin;

    public Player(long startingBalance) {
        if (startingBalance < 1) {
            throw new IllegalArgumentException("Starting balance must be at least $1.");
        }
        this.startingBalance = startingBalance;
        this.balance = startingBalance;
        this.peakBalance = startingBalance;
    }

    /** Takes chips off the player's stack (when a bet is placed on the table). */
    public void withdraw(long amount) {
        if (amount < 0 || amount > balance) {
            throw new IllegalArgumentException("Can't take $" + amount + " from a balance of $" + balance + ".");
        }
        balance -= amount;
    }

    /** Gives chips back to the player (winnings, or bets taken back off the table). */
    public void deposit(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Can't deposit a negative amount.");
        }
        balance += amount;
        peakBalance = Math.max(peakBalance, balance);
    }

    /** Records the outcome of one spin for the stats screen. */
    public void recordSpin(long staked, long returned) {
        spins++;
        totalWagered += staked;
        long net = returned - staked;
        if (net > 0) {
            winningSpins++;
            biggestWin = Math.max(biggestWin, net);
        }
    }

    public long getBalance()         { return balance; }
    public long getStartingBalance() { return startingBalance; }
    public long getPeakBalance()     { return peakBalance; }
    public int getSpins()            { return spins; }
    public int getWinningSpins()     { return winningSpins; }
    public long getTotalWagered()    { return totalWagered; }
    public long getBiggestWin()      { return biggestWin; }
}
