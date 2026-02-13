package Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatePuzzle implements State {
    public final int x, y;
    public final int[][] grid;
    public Position[] goalPositions;


    public StatePuzzle(int x, int y, int[][] grid) {
        if (grid == null) {
            throw new IllegalArgumentException("grid cannot be null");
        }

        this.x = x;
        this.y = y;
        this.grid = grid;
    }

    public StatePuzzle(int[][] grid) {
        if (grid == null) {
            throw new IllegalArgumentException("grid cannot be null");
        }

        int x = -1, y = -1;
        outer:
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 0){
                    x = j;
                    y = i;
                    break outer;
                }
            }
        }

        if (x == -1) {
            throw new IllegalArgumentException("grid must contain a zero tile");
        }

        this.x = x;
        this.y = y;
        this.grid = grid;
    }

    public void setGoalPosition() {
        goalPositions = new Position[grid.length * grid[y].length];

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                int value = grid[y][x];
                goalPositions[value] = new Position(x, y);
            }
        }
    }

    @Override
    public List<State> neighbors() {
        List<State> neighbour = new ArrayList<>();

        addIfValid(neighbour, x, y - 1);
        addIfValid(neighbour, x, y + 1);
        addIfValid(neighbour, x + 1, y);
        addIfValid(neighbour, x - 1, y);

        return neighbour;
    }

    boolean inBounds(int x, int y, final int[][] map) {
        return y >= 0 && y < map.length &&
               x >= 0 && x < map[y].length;
    }

    private void addIfValid(List<State> list, int x, int y) {
        if (!inBounds(x, y, grid)) return;

        int[][] newGrid = gridDeepCopy(grid);

        // PuzzleUtils.print2DArray(newGrid, 3);
        
        newGrid[this.y][this.x] = newGrid[y][x];
        newGrid[y][x] = 0;

        // PuzzleUtils.print2DArray(newGrid, 3);

        list.add(new StatePuzzle(x, y, newGrid));
    }

    int manhattan(int ax, int ay, int bx, int by) {
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    @Override
    public int heuristic(State goal) {
        int score = 0;
        StatePuzzle g = (StatePuzzle) goal;
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int number = grid[i][j];
                if (number == 0) continue;
                Position goalPos = g.goalPositions[number];
                score += manhattan(j, i, goalPos.x, goalPos.y);
            }
        }
        return score;
    }

    @Override
    public boolean equals(Object other) {
        StatePuzzle g = (StatePuzzle) other;
        if (this.x != g.x || this.y != g.y) {
            return false;
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != g.grid[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    @Override
    public void print() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.printf("%3d ", grid[y][x]);
            }
            System.out.println();
        }
    }

    public static int[][] gridDeepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    // ----------------- Calculating if solvable -----------------
    
    public static boolean canReach(StatePuzzle start, StatePuzzle goal) {
        if (start.grid.length != goal.grid.length) {
            throw new IllegalArgumentException("Start and Goal grid length doens't match!");
        }
        int N = start.grid.length;
        int invStart = countInversions(start.grid);
        int invGoal  = countInversions(goal.grid);

        System.out.println("start:");
        start.print();
        System.out.println("goal:");
        goal.print();

        System.out.println("\ninvStart: " + invStart);
        System.out.println("invGoal: " + invGoal);
    
        int blankRowStart = N - start.y;
        int blankRowGoal  = N - goal.y;

        System.out.println("blankRowStart: " + blankRowStart);
        System.out.println("blankRowGoal: " + blankRowGoal);
    
        if (N % 2 == 1) {
            return invStart % 2 == invGoal % 2;
        } else {
            return (invStart + blankRowStart) % 2 == (invGoal + blankRowGoal) % 2;
        }
    }
    
    private static int countInversions(int[][] grid) {
        int N = grid.length;
        int[] arr = new int[N*N];
        int k = 0;
        for (int[] row : grid) {
            for (int v : row) if (v != 0) arr[k++] = v;
        }

        for (int l : arr) {
            System.out.printf("%d ", l);
        }
        System.out.println();
    
        int inversions = 0;
        for (int i = 0; i < k; i++) {
            for (int j = i+1; j < k; j++) {
                if (arr[i] > arr[j]) inversions++;
            }
        }
        return inversions;
    }

    public class Position {
        public final int x;
        public final int y;
    
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
}
