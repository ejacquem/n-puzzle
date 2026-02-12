package Node;

import java.util.ArrayList;
import java.util.List;

public class NodeSearch implements Comparable<NodeSearch> {
    public final State state;
    private int g; // g cost: distance from starting position
    private int h; // h cost: distance from end node (heuristic)
    private int f;
    private NodeSearch parent;

    public NodeSearch(State state, State goal, NodeSearch parent) {
        this.state = state;
        this.setParent(parent);
        this.computeHeuristic(goal);
        this.f = g + h;
    }

    public int getF() {
        return f;
        // return g + h;
    }

    public int getH() {
        return h;
    }

    public int getG() {
        return g;
    }

    public void computeHeuristic(State goal) {
        this.h = state.heuristic(goal);
    }

    public NodeSearch getParent() {
        return parent;
    }

    public void setParent(NodeSearch parent) {
        this.parent = parent;
        this.g = (parent == null) ? 0 : parent.getG() + 1;
        this.f = g + h;
    }

    @Override
    public int compareTo(NodeSearch other) {
        if (this.getF() == other.getF()) {
            return Integer.compare(this.getH(), other.getH()); 
        }
        return Integer.compare(this.getF(), other.getF());
    }

    public void printNode() {
        state.print();
        System.out.println("h = " + h + ", g = " + g + ", ");
        // System.out.print("Parent: ");
        // if (parent != null) {
        //     parent.state.print();
        // } else {
        //     System.out.println("null");
        // }
    }

    public static List<NodeSearch> generatePath(NodeSearch node) {
        List<NodeSearch> list = new ArrayList<>();
        while (node != null) {
            node.printNode();
            list.add(0, node); // inefficient but idc
            node = node.getParent();
        }
        return list;
    }
}