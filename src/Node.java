import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node implements Comparable<Node> {
    int x;
    int y;
    int g; // g cost: distance from starting position
    int h; // h cost: distance from end node (heuristic)
    Node N;
    Node S;
    Node E;
    Node W;
    Node parent;

    public static int map[][];
    public static Node target;

    private Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.g = (parent == null) ? 0 : parent.g + 1;
        this.h = (target == null) ? 0 : heuristic(this, target);
        this.parent = parent;
    }

    public int getF() {
        return g + h;
    }

    public int getH() {
        return h;
    }

    public Integer hash() {
        return x + y * 10000;
    }

    public List<Node> generateNeighbour() {
        N = Node.create(x, y - 1, this);
        S = Node.create(x, y + 1, this);
        E = Node.create(x + 1, y, this);
        W = Node.create(x - 1, y, this);
        return new ArrayList<>(Arrays.asList(N, S, E, W));
    }

    public static int heuristic(Node A, Node B) {
        return Math.abs(A.x - B.x) + Math.abs(A.y - B.y);
    }

    @Override
    public int compareTo(Node other) {
        if (this.getF() == other.getF()) {
            return Integer.compare(this.h, other.h); 
        }
        return Integer.compare(this.getF(), other.getF());
    }

    // factory returns null if invalid
    public static Node create(int x, int y, Node parent) {
        if (x < 0 || y < 0 || (map != null && (y >= map.length || x >= map[y].length))) return null; // invalid position
        if (map != null && map[y][x] == 1) return null;
        return new Node(x, y, parent);
    }

    public void printNode() {
        System.out.print("Node: (" + x + ", " + y + "), h = " + h + ", g = " + g + ", ");
        if (parent != null) {
            System.out.println("Parent: (" + parent.x + ", " + parent.y + ")");
        } else {
            System.out.println("Parent: null");
        }
    }
}