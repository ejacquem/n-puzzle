package Algorithm;

import java.util.List;

import Node.NodeSearch;
import Node.StatePuzzle;
import Utils.Ansi;

public abstract class ASearchAlgorithm {
    protected NodeSearch start;
    protected NodeSearch goal;
    public NodeSearch endNode; // equals(goal)

    protected long stepCount = 0;

    public boolean solutionFound = false; // true if algorithm finished computing and a solution was found
    public boolean solutionNotFound = false; // true if algorithm finished computing and a solution was not found

    public boolean solve(NodeSearch start, NodeSearch goal) {
        if (start == null || goal == null) {
            throw new IllegalArgumentException("start and goal cannot be null");
        }
        if (start.state == null || goal.state == null) {
            throw new IllegalArgumentException("start and goal states cannot be null");
        }

        System.out.println("Solving with algorithm " + getClass().getSimpleName() + "...");
        // if (start.state instanceof StatePuzzle) {
        //     System.out.println("Problem type: " + start.state.getClass().getSimpleName());
        //     if (!StatePuzzle.canReach((StatePuzzle) start.state, (StatePuzzle) goal.state)) {
        //         System.out.println("Grid is not solvable");
        //         return false;
        //     }
        // }

        this.start = start;
        this.goal = goal;
        stepCount = 0;
        long startTime = System.nanoTime();

        solve();

        long duration = System.nanoTime() - startTime;
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.println("│           ALGORITHM REPORT               │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("  Algorithm used : " + Ansi.BOLD + getClass().getSimpleName() + Ansi.RESET);
        System.out.println("  Problem type   : " + Ansi.BOLD + start.state.getClass().getSimpleName() + Ansi.RESET);
        System.out.println("  Step Count     : " + Ansi.YELLOW + stepCount + Ansi.RESET);
        System.out.println("  Time           : " + Ansi.YELLOW + (duration / 1_000_000.0) + " ms" + Ansi.RESET);
        System.out.println("  Solution Move  : " + Ansi.YELLOW + endNode.countParent() + Ansi.RESET);
        System.out.println("└──────────────────────────────────────────┘" + Ansi.RESET);
        return solutionFound;
    }
    protected abstract void solve();
    public abstract List<NodeSearch> getPath();

    public NodeSearch getGoal() {
        return goal;
    }
    public NodeSearch getStart() {
        return start;
    }
}
