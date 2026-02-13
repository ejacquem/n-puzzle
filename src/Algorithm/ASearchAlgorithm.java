package Algorithm;

import java.util.List;

import Node.NodeSearch;

public abstract class ASearchAlgorithm {
    protected NodeSearch start;
    protected NodeSearch goal;

    public boolean solutionFound = false; // true if algorithm finished computing and a solution was found
    public boolean solutionNotFound = false; // true if algorithm finished computing and a solution was not found

    public abstract boolean solve(NodeSearch start, NodeSearch goal);
    public abstract List<NodeSearch> getPath();

    public NodeSearch getGoal() {
        return goal;
    }
    public NodeSearch getStart() {
        return start;
    }
}
