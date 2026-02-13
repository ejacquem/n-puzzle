package Algorithm;

import java.util.List;

import Node.NodeSearch;

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
        this.start = start;
        this.goal = goal;
        stepCount = 0;
        long startTime = System.nanoTime();

        solve();

        long duration = System.nanoTime() - startTime;
        System.out.println("Algorithm finished computing");
        System.out.println("Algorithm used: " + getClass().getSimpleName());
        System.out.println("Problem type: " + start.state.getClass().getSimpleName());
        System.out.println("Step Count: " + stepCount);
        System.out.println("Time: " + (duration / 1_000_000.0) + " ms");
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
