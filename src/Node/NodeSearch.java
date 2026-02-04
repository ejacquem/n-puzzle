package Node;

public class NodeSearch implements Comparable<NodeSearch> {
    public final State state;
    private int g; // g cost: distance from starting position
    private int h; // h cost: distance from end node (heuristic)
    private NodeSearch parent;

    public NodeSearch(State state, State goal, NodeSearch parent) {
        this.state = state;
        this.setParent(parent);
        this.computeHeuristic(goal);
    }

    public int getF() {
        return g + h;
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
    }

    public void setMinParent(NodeSearch parent) {
        int ng = (parent == null) ? 0 : parent.getG() + 1; //new g value based on parent value
        if (ng > g) return; // only assign new g from the smaller parent
        this.parent = parent;
        this.g = Math.min(this.g, ng); 
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
        System.out.print("h = " + h + ", g = " + g + ", ");
        System.out.print("Parent: ");
        if (parent != null) {
            parent.state.print();
        } else {
            System.out.println("null");
        }
    }
}