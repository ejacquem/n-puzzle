import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import Node.NodeSearch;
import Node.State;

public class AStar {
    PriorityQueue<NodeSearch> open;
    HashMap<Integer, NodeSearch> openMap;
    HashMap<Integer, NodeSearch> close;
    NodeSearch start;
    NodeSearch goal;

    public boolean solutionFound = false;
    public boolean solutionDoesntExist = false;

    public List<NodeSearch> path;

    AStar(NodeSearch start, NodeSearch goal) {
        open = new PriorityQueue<>();
        openMap = new HashMap<>();
        close = new HashMap<>();
        path = new ArrayList<>();

        this.start = start;
        this.goal = goal;

        System.out.println("Staring Astar: ");
        System.out.println("Start Node: ");
        start.printNode();
        System.out.println("Goal Node: ");
        goal.printNode();

        open.add(start);
        openMap.put(start.state.hashCode(), start);
    }

    public void step() {
        if (open.isEmpty()){
            System.out.println("No Path found !");
            solutionDoesntExist = true;
        }
        
        if (open.peek().state.isGoal(goal.state)){
            System.out.println("goal node found !");
            solutionFound = true;
            path = generatePath(open.peek());
        }

        NodeSearch current = open.poll();
        close.put(current.state.hashCode(), current);
        
        // System.out.println("\n=========== Step ===========");
        // System.out.printf("open.size: %3d, openMap.size: %3d, close.size: %3d\n", open.size(), openMap.size(), close.size());
        // System.out.println("Current Node: ");
        // current.printNode();

        List<State> neighbours = current.state.neighbors();
        for (State state : neighbours) {
            if (state == null || close.containsKey(state.hashCode())) {
                continue;
            }

            //if node was already generated, replace the values
            if (openMap.containsKey(state.hashCode())) {
                NodeSearch node = openMap.get(state.hashCode());
                // System.out.println("Node already visited: node.getParent().getG() = " + node.getParent().getG() + ", current.getG() = " + current.getG());
                if (current.getG() < node.getParent().getG()) {
                    node.setParent(current);
                    open.remove(node);
                    open.add(node);
                }
            } else { // if node is new, add it to queue
                NodeSearch node = new NodeSearch(state, goal.state, current);
                open.add(node);
                openMap.put(node.state.hashCode(), node);
            }
        }
    }

    public List<NodeSearch> generatePath(NodeSearch node) {
        List<NodeSearch> list = new ArrayList<>();
        System.out.println("\n\n------------------- backtracking the path from node:");
        int i = 0;
        while (node != null) {
            System.out.println("id: " + i++);
            node.printNode();
            list.add(0, node); // inefficient but idc
            node = node.getParent();
        }
        return list;
    }
}
