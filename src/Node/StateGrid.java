package Node;

import java.util.ArrayList;
import java.util.List;

public class StateGrid implements State {
    public final int x, y;

    public final GridProblem problem;

    public StateGrid(int x, int y, GridProblem problem) {
        this.x = x;
        this.y = y;
        this.problem = problem;
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

    private void addIfValid(List<State> list, int x, int y) {
        if (!problem.inBounds(x, y))return;
        if (!problem.isFree(x, y))return;

        list.add(new StateGrid(x, y, problem));
    }

    @Override
    public int heuristic(State goal) {
        StateGrid g = (StateGrid) goal;
        return Math.abs(this.x - g.x) + Math.abs(this.y - g.y);
    }

    @Override
    public boolean equals(State other) {
        StateGrid g = (StateGrid) other;
        return (this.x == g.x && this.y == g.y);
    }

    public final static class GridProblem {
        public final int[][] map;
    
        public GridProblem(int[][] map) {
            this.map = map;
        }
    
        boolean isFree(int x, int y) {
            return map[y][x] == 0;
        }
    
        boolean inBounds(int x, int y) {
            return y >= 0 && y < map.length &&
                   x >= 0 && x < map[y].length;
        }
    }

    @Override
    public int hashCode() {
        return 255 * x + y;
    }

    @Override
    public void print() {
        System.out.println("State: (" + x + ", " + y + ")");
    }
}
