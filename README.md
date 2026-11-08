# American Roulette (Java)

A command-line American roulette game. You start with a stack of chips, place as many bets as you like, spin, and the ball rolls around a real 38-pocket wheel (0, 00, 1–36 in the actual wheel order) before it settles.

This started as one method in a freshman intro-to-programming assignment. I rebuilt it as a standalone program with real roulette rules, input validation, and tests.

## Features

- **Real wheel:** the American layout with 0 and 00, the correct red and black numbers, and the real pocket order.
- **Standard bets and payouts:** straight up (35:1), red/black, odd/even, low/high (1:1), dozens and columns (2:1).
- **Multiple bets per spin:** chips come off your stack as you bet. You can `clear` to take them back or `repeat` last spin's bets.
- **Spin animation:** the ball goes around at least one lap and slows down before it stops.
- **Session tracking:** recent results, red/black/green counts, total wagered, biggest win, peak balance, and net.
- **Forgiving input:** bad input gets a helpful message and doesn't crash the game. You can bet `all`.
- **Tests:** they check the wheel, every bet type, the parser, and full scripted games. They also confirm every bet carries the same 5.26% house edge.

## Running it

Requires a JDK (Java 8 or newer).

```bash
javac -d out src/roulette/*.java
java -cp out roulette.Main
```

Options:

```
--balance N   starting chips (default 500)
--seed N      make spins repeatable
--fast        skip the spinning animation
```

Run the tests:

```bash
java -cp out roulette.RouletteTest
```

## How to play

```
[$500 in chips | $0 on the table] > red 25
Placed $25 on Red (pays 1 to 1).

[$475 in chips | $25 on the table] > 17 5
Placed $5 on Straight up on 17 (pays 35 to 1).

[$470 in chips | $30 on the table] > spin
No more bets! The wheel is spinning...
     7R   20B   32R  >17B<   5R   22B   34R

  *** 17 BLACK ***

  WIN   Straight up on 17          +$175
  lose  Red                         -$25

You're up $150 this spin. Balance: $650
```

| Bet | Example | Pays |
|---|---|---|
| Straight up (0, 00, 1–36) | `17 10`, `00 5` | 35 to 1 |
| Red / Black | `red 25` | 1 to 1 |
| Odd / Even | `odd 10` | 1 to 1 |
| Low (1–18) / High (19–36) | `low 20`, `19-36 20` | 1 to 1 |
| Dozen | `dozen 2 30` or `d2 30` | 2 to 1 |
| Column | `column 3 15` or `c3 15` | 2 to 1 |

Commands: `spin` (`s`), `bets`, `clear`, `repeat` (`r`), `table`, `history` (`h`), `stats`, `help`, `quit` (`q`).

## Project layout

| File | Purpose |
|---|---|
| `Wheel.java` | The 38 pockets in wheel order, their colors, and random spins |
| `Pocket.java`, `Color.java` | A single slot on the wheel |
| `BetType.java`, `Bet.java` | Bet kinds, payouts, and whether a bet wins on a given pocket |
| `BetParser.java` | Turns typed input like `dozen 2 30` into a `Bet` |
| `Player.java` | Chip balance and session stats |
| `RouletteGame.java` | The command loop, spinning, payouts, and screens |
| `Main.java` | Command-line options and startup |
| `RouletteTest.java` | Tests that need no external libraries |

The game reads from a `BufferedReader` and writes to a `PrintStream`, and the wheel takes a `Random`. That lets the tests feed in scripted input and force the ball onto a chosen number.
