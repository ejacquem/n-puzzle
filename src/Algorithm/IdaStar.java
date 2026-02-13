package Algorithm;
import java.util.List;

import Node.NodeSearch;
import Node.State;

public class IdaStar extends ASearchAlgorithm {

    public IdaStar() {}

    protected void solve() {
        stepCount = 0;
        int threshold = start.getH();

        while (true) {
            int temp = search(start, threshold);

            if (temp == -1) {
                return ;
            }

            if (temp == Integer.MAX_VALUE) {
                solutionNotFound = true;
                return ;
            }

            threshold = temp;
        }
    }

    public int search(NodeSearch node, int threshold) {
        stepCount++;
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
