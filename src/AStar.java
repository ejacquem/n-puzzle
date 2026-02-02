import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
    PriorityQueue<Node> open;
    HashMap<Integer, Node> openMap;
    HashMap<Integer, Node> close;
    Node start;
    Node end;

    AStar(Node start, Node end, int[][] map) {
        open = new PriorityQueue<>();
        openMap = new HashMap<>();
        close = new HashMap<>();

        this.start = start;
        this.end = end;

        Node.map = map;
        Node.target = end;
        start.h = Node.heuristic(start, end);

        open.add(start);
        openMap.put(start.hash(), start);
    }

    public void step() {
        if (open.isEmpty()){
            System.out.println("No Path found !");
            return;
        }
        
        if (open.peek().getH() == 0){
            System.out.println("End node found !");
            return;
        }

        System.out.println("Step");
        System.out.println("open.size: " + open.size());
        System.out.println("openMap.size: " + openMap.size());
        System.out.println("close.size: " + close.size());

        Node current = open.poll();
        close.put(current.hash(), current);

        List<Node> neighbours = current.generateNeighbour();
        for (Node node : neighbours) {
            if (node == null) {
                System.out.println("Node null");
            } else {
                node.printNode();
            }

            // if node already visited, skip
            if (node == null || close.containsKey(node.hash())) {
                continue;
            }

            //if node was already generated, replace the values
            if (openMap.containsKey(node.hash())) {
                Node n = openMap.get(node.hash());
                if (node.getF() < n.getF()) {
                    openMap.get(node.hash()).parent = current;
                    openMap.get(node.hash()).g = current.g + 1;
                }
            } else { //  if node is new, add it to queue
                open.add(node);
                openMap.put(node.hash(), node);
            }
        }
    }
}
