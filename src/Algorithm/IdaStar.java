package Algorithm;
import java.util.List;

import Node.NodeSearch;
import Node.State;

public class IdaStar extends ASearchAlgorithm {
    private NodeSearch goal;

    public NodeSearch endNode;

    public IdaStar() {}

    @Override
    public boolean solve(NodeSearch start, NodeSearch goal) {
        this.goal = goal;
        int threshold = start.getH();

        while (true) {
            int temp = search(start, threshold);

            if (temp == -1) {
                return true;
            }

            if (temp == Integer.MAX_VALUE) {
                solutionNotFound = true;
                return false;
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

    @Override
    public List<NodeSearch> getPath() {
        return endNode.generatePath();
    }
}
