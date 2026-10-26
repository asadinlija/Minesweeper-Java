# Minesweeper

A classic Minesweeper implementation in Java with a graphical interface (Swing) and a console version. The game logic is separate from the user interface, so both versions share the same code.

## Features

- The first click is always safe (mines are placed only after the first reveal)
- Automatic reveal of empty areas (flood fill, BFS)
- Flagging and a remaining-mines counter
- Chord click: reveals the neighboring cells when the number of flags equals the number on the cell
- Difficulty levels: Easy (9×9, 10 mines), Medium (16×16, 40 mines), Hard (16×30, 99 mines) and Custom
- Input validation: rows and columns in the range [5, 50], mines from min(R, C) up to 80% of the cells

## Project structure

```
src/
├── logika/     Game rules (no UI dependencies)
│   ├── Igra.java          main logic: reveal, flags, chord, win/loss
│   ├── Ploca.java         board, mine placement and adjacent-mine counting
│   ├── Polje.java         a single cell
│   ├── StanjePolja.java   ZATVORENO / OZNACENO / OTVORENO (closed / flagged / open)
│   └── StatusIgre.java    SPREMNA / U_TOKU / POBJEDA / PORAZ (ready / in progress / won / lost)
├── gui/        Swing interface
│   ├── GuiIgra.java       entry point (main)
│   └── ProzorIgre.java    game window
└── konzola/    Console version
    └── KonzolnaIgra.java  entry point (main)
api_doc/        Generated Javadoc (open index.html)
```

## Running

Java 9 or newer is required (the project uses `module-info.java`).

From an Eclipse or IntelliJ project, run `gui.GuiIgra` (graphical version) or `konzola.KonzolnaIgra` (console version).

From the command line, in the project root:

```bash
javac -d bin $(find src -name "*.java")

java -p bin -m minesweeper/gui.GuiIgra
java -p bin -m minesweeper/konzola.KonzolnaIgra
```

On Windows (PowerShell), to compile:

```powershell
javac -d bin (Get-ChildItem src -Recurse -Filter *.java).FullName
```

## Controls

### Graphical version

| Action | Mouse button |
|---|---|
| Reveal a cell | left |
| Place / remove a flag | right |
| Chord (reveal surrounding cells) | middle |

The **Nova igra** (New game) button opens the difficulty selection.

### Console version

| Command | Description |
|---|---|
| `n R C M` | new game (rows, columns, mines) |
| `o r c` | reveal a cell |
| `f r c` | place / remove a flag |
| `c r c` | chord |
| `q` | quit |

Example: `n 9 9 10`, then `o 4 4`.

Board symbols: `#` closed, `F` flag, `.` empty cell, `1`–`8` number of adjacent mines, `*` mine.
