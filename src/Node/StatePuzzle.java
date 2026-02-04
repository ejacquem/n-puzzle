package Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatePuzzle implements State {
    public final int x, y;
    public final int[][] grid;

    public StatePuzzle(int x, int y, int[][] grid) {
        this.x = x;
        this.y = y;
        this.grid = grid;
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

        newGrid[this.y][this.x] = newGrid[y][x];
        newGrid[y][x] = 0;

        list.add(new StatePuzzle(x, y, grid));
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
                // int number = grid[i][j];
                // int x = number % grid[i].length;
                // int y = number / grid.length;
                // score += manhattan(j, i, x, y);
                score += Math.abs(grid[i][j] - g.grid[i][j]);
            }
        }
        return score;
    }

    @Override
    public boolean isGoal(State goal) {
        StatePuzzle g = (StatePuzzle) goal;
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
}
