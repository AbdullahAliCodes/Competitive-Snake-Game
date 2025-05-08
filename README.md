
# 🐍 Snake AI Bot – Wits University Multiplayer Snake Competition

This project implements a smart, competitive AI for the **Wits Snake Tournament** hosted at [snake.wits.ai](https://snake.wits.ai). Built in Java, the bot makes use of **A\*** pathfinding and survival heuristics to compete for apples, avoid traps, and outperform opponents on the grid.

---

## 🧠 Project Overview

- **Language**: Java
- **Focus**: Pathfinding (A*), tail chasing, and survival logic
- **Input**: Game state including apples, board, and snake positions
- **Output**: Integer move (0 = up, 1 = down, 2 = left, 3 = right)

---

## 📂 File Breakdown

| File         | Description |
|--------------|-------------|
| `Snake.java` | Handles snake state, board drawing, direction detection, and visibility logic |
| `PathFind.java` | Main decision engine: chooses move via A\*, fallback to tail following or survival |
| `Node.java`  | Data structure for A\* search nodes, supports Manhattan heuristics |

---

## 🚀 Key Features

### ✅ A* Pathfinding
- Plans optimal route from head to apple
- Avoids collisions using grid masking
- Uses Manhattan distance as heuristic

### ✅ Tail-Chasing Logic
- If apple path fails, attempts to path to own tail (marked temporarily walkable)

### ✅ Survival Instincts
- If tail also unreachable, chooses safest adjacent move based on surroundings
- Simulates "zig-zag" for delay/survival

---

## 📈 Game API Notes

- Your bot will be given:
  - An `int[][] board`
  - An array of `Snake` objects
  - Your own snake index
  - Coordinates of the apple
- The main method to call is:

```java
PathFind pathFinder = new PathFind(appleCoords, snakes, mySnakeIndex, board);
int move = pathFinder.calculateMove();
```

---

## 🧪 How to Use

1. Import the Java files into your IDE (e.g., IntelliJ, Eclipse)
2. Implement the `Main.java` file that integrates with the `snake.wits.ai` backend
3. Compile:
```bash
javac *.java
```
4. Ensure your bot returns a valid direction:  
   - `0 = up`, `1 = down`, `2 = left`, `3 = right`

---

## 📚 Reference

- Competition Rules: [snake.wits.ai/docs](https://snake.wits.ai/docs)
- Game Engine: Java server connects bots via stdin/stdout communication
- Sample moves and grid visuals are available via [snake.wits.ai](https://snake.wits.ai)

---

## 🏆 Author

- **Institution**: University of the Witwatersrand
- **Course**: Introduction to Algorithms & Programming (COMS1018A/1022A)
- **Project**: Snake Game AI – Tournament Submission

---

## ⚠️ Disclaimer

This AI agent is developed for educational purposes as part of a university competition. Use respectfully and do not plagiarize.
