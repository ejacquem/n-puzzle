import java.util.List;

import Node.NodeSearch;
import Node.State;

public class IdaStar {
    NodeSearch start;
    NodeSearch goal;

    // private int threshold;

    public boolean solutionFound = false;
    public boolean solutionDoesntExist = false;

    public List<NodeSearch> path;
    public NodeSearch endNode;

    IdaStar(NodeSearch start, NodeSearch goal) {
        this.start = start;
        this.goal = goal;
    }

    public void solve() {
        int threshold = start.getH();

        while (true) {
            int temp = search(start, threshold);

            if (temp == -1) {
                solutionDoesntExist = true;
                return;
            }

            if (temp == Integer.MAX_VALUE) {
                solutionDoesntExist = true;
                return;
            }

            threshold = temp;
        }
    }

    public int search(NodeSearch node, int threshold) {
        if (node.getF() > threshold) {
            return node.getF();
        }
        if (node.state.equals(goal.state)) {
            solutionFound = true;
            endNode = node;
            return -1;
        }
        int min = Integer.MAX_VALUE;
        for (State state : node.state.neighbors()) {
            if (node.getParent() != null && state.equals(node.getParent().state)) continue; // skip going back to parent
            int t = search(new NodeSearch(state, goal.state, node), threshold);
            if (t == -1) return -1;
            if (t < min) min = t;
        }
        return min;
    }
}
