// package Node;
// import java.util.ArrayList;
// import java.util.List;

// public class Node implements Comparable<Node> {
//     int x;
//     int y;
//     private int g; // g cost: distance from starting position
//     private int h; // h cost: distance from end node (heuristic)
//     private List<Node> neighbour;
//     private Node parent;

//     public static int map[][];
//     public static Node target;

//     private Node(int x, int y, Node parent) {
//         this.x = x;
//         this.y = y;
//         this.g = (parent == null) ? 0 : parent.g + 1;
//         this.h = (target == null) ? 0 : heuristic(this, target);
//         this.parent = parent;
//     }

//     public int getF() {
//         return g + h;
//     }

//     public int getH() {
//         return h;
//     }

//     public int getG() {
//         return g;
//     }

//     public Node getParent() {
//         return parent;
//     }

//     public void setParent(Node parent) {
//         this.parent = parent;
//         this.g = parent.g + 1;
//     }

//     public Integer hash() {
//         return x + y * 10000;
//     }

//     public List<Node> generateNeighbour() {
//         neighbour = new ArrayList<>();
//         neighbour.add(Node.create(x, y - 1, this));
//         neighbour.add(Node.create(x, y + 1, this));
//         neighbour.add(Node.create(x + 1, y, this));
//         neighbour.add(Node.create(x - 1, y, this));
//         return neighbour;
//     }

//     public static int heuristic(Node A, Node B) {
//         return Math.abs(A.x - B.x) + Math.abs(A.y - B.y);
//     }

//     public void computeHeuristic() {
//         this.h = heuristic(this, target);
//     }

//     @Override
//     public int compareTo(Node other) {
//         if (this.getF() == other.getF()) {
//             return Integer.compare(this.h, other.h); 
//         }
//         return Integer.compare(this.getF(), other.getF());
//     }

//     // factory returns null if invalid
//     public static Node create(int x, int y, Node parent) {
//         if (x < 0 || y < 0 || (map != null && (y >= map.length || x >= map[y].length))) return null; // invalid position
//         if (map != null && map[y][x] == 1) return null;
//         return new Node(x, y, parent);
//     }

//     public void printNode() {
//         System.out.print("Node: (" + x + ", " + y + "), h = " + h + ", g = " + g + ", ");
//         if (parent != null) {
//             System.out.println("Parent: (" + parent.x + ", " + parent.y + ")");
//         } else {
//             System.out.println("Parent: null");
//         }
//     }
// }