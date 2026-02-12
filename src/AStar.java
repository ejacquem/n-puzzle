import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import Node.NodeSearch;
import Node.State;

public class AStar {
    PriorityQueue<NodeSearch> open;
    HashMap<Node.State, NodeSearch> openMap;
    HashMap<Node.State, NodeSearch> close;
    NodeSearch start;
    NodeSearch goal;

    public boolean solutionFound = false;
    public boolean solutionDoesntExist = false;

    public List<NodeSearch> path;
    public NodeSearch endNode;

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
        openMap.put(start.state, start);
    }

    public void solve() {
        while(solutionFound == false && solutionDoesntExist == false){
            step();
        }
    }

    public void step() {
        if (open.isEmpty()){
            System.out.println("No Path found !");
            solutionDoesntExist = true;
        }
        
        NodeSearch current = open.poll();
        openMap.remove(current.state);
        if (close.containsKey(current.state)) { // this happens when the node was already visited and has been "replaced" in the priority queue
            return;
        }
        close.put(current.state, current);
        
        if (current.state.equals(goal.state)){
            solutionFound = true;
            path = generatePath(current);
            endNode = current;
            return;
        }


        List<State> neighbours = current.state.neighbors();
        for (State state : neighbours) {
            if (state == null || close.containsKey(state)) {
                continue;
            }

            //if node was already generated, replace the values
            if (openMap.containsKey(state)) {
                NodeSearch node = openMap.get(state);
                // System.out.println("Node already visited: node.getParent().getG() = " + node.getParent().getG() + ", current.getG() = " + current.getG());
                if (current.getG() < node.getG()) {
                    node.setParent(current);
                    open.add(node); // add node in priority queue (might twice in the queue but it's ok)
                }
            } else { // if node is new, add it to queue
                NodeSearch node = new NodeSearch(state, goal.state, current);
                open.add(node);
                openMap.put(node.state, node);
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
