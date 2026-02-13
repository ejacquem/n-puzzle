import java.util.Map;

import Algorithm.AStar;
import Node.NodeSearch;
import Node.State;
import Node.StateGrid;
import Node.StatePuzzle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AStarRenderer {
    private Canvas canvas;
    private GraphicsContext gc;

    public AStar astar;

    public static final int TILE_SIZE = 50;
    public static final double gridWidth = 1;

    public AStarRenderer(Canvas canvas, AStar astar) {
        this.canvas = canvas;
        this.astar = astar;

        gc = canvas.getGraphicsContext2D();
    }

    public void setAStar(AStar astar) {
        this.astar = astar;
    }

    public void draw() {
        drawBackGround();

        if (astar == null || astar.getStart() == null) return;
        if (astar.getStart().state instanceof StateGrid) {
            drawAStarGrid();
        } else {
            drawAStarPuzzle();
        }
    }

    public void drawBackGround() {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawAStarGrid() {
        drawMap(((StateGrid)astar.getStart().state).problem.map);
        drawStartGoal((StateGrid)astar.getStart().state, (StateGrid)astar.getGoal().state);
        drawOpen();
        drawClose();
        drawPath();
        drawGrid(((StateGrid)astar.getStart().state).problem.map);
    }


    public void drawAStarPuzzle() {
        if (astar == null || !(astar.getStart().state instanceof StatePuzzle)) return;
        // drawPuzzle(((StatePuzzle)astar.open.peek().state).grid);
        
        NodeSearch currentNode = astar.open.peek();
        StatePuzzle currentState = ((StatePuzzle)astar.open.peek().state);
        drawPuzzleNode(currentNode, 0, 0);
        
        int i = 0;
        for (State state : currentNode.state.neighbors()) {
            NodeSearch n = new NodeSearch(state, astar.getGoal().state, currentNode);
            drawPuzzleNode(n, TILE_SIZE * (currentState.grid.length + 1), TILE_SIZE * (currentState.grid.length + 1) * i);
            i++;
        }
    }

    private void drawStartGoal(StateGrid start, StateGrid goal) {
        gc.setFill(Color.BLUE);
        gc.fillRect(start.x * TILE_SIZE, start.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        gc.setFill(Color.RED);
        gc.fillRect(goal.x * TILE_SIZE, goal.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void drawTileNode(NodeSearch node, Color color) {
        gc.setFill(color);
        StateGrid state = (StateGrid) node.state;
        gc.fillRect(state.x * TILE_SIZE, state.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        drawTileNodeInfo(node);
    }

    private void drawTileNodeInfo(NodeSearch node) {
        if (Settings.showNodeInfo == false) {
            return;
        }
        float y = 3;
        float x = 0;
        StateGrid state = (StateGrid) node.state;
        drawStringAt(gc, state.x, state.y, x, y * -1, "g=" + node.getG(), Color.BLACK);
        drawStringAt(gc, state.x, state.y, x, y * 0, "h=" + node.getH(), Color.BLACK);
        drawStringAt(gc, state.x, state.y, x, y * 1, "f=" + node.getF(), Color.BLACK);
    }

    private void drawOpen() {
        for (NodeSearch node : astar.open) {
            drawTileNode(node, Color.rgb(250, 250, 150));
        }
    }

    private void drawClose() {
        for (Map.Entry<State, NodeSearch> entry : astar.close.entrySet()) {
            NodeSearch node = entry.getValue();
            drawTileNode(node, Color.rgb(250, 200, 200));
        }
    }

    private void drawPath() {
        NodeSearch n = astar.open.peek();
        if (n != null) {
            drawTileNode(n, Color.rgb(0, 255, 255));
            n = n.getParent();
        } 
        while (n != null) {
            drawTileNode(n, Color.rgb(0, 255, 0));
            n = n.getParent();
        }
    }

    public void drawGrid(int[][] map) {
        int cols = map.length;
        int rows = map[0].length;
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(gridWidth);
        double w = gridWidth / 2;
        double frac = w - Math.floor(w);
        for (int i = 0; i <= rows; i++)
        {
            double start = i * TILE_SIZE + frac;
            double length = (cols) * TILE_SIZE;
            gc.strokeLine(start, frac, start, length);
        }
        for (int i = 0; i <= cols; i++)
        {
            double start = i * TILE_SIZE + frac;
            double length = (rows) * TILE_SIZE;
            gc.strokeLine(frac, start, length, start);
        }
    }

    public void drawMap(int map[][]) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 0) {
                    gc.setFill(Color.WHITE);
                } else {
                    gc.setFill(Color.BLACK);
                }
                gc.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public void drawPuzzle(int map[][]) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                float size = map.length;
                int color = (int) (125f + 125f * ((map[y][x]) / (size * size)));
                // if (map[y][x] == 0) {
                //     color = 255;
                // }
                gc.setFill(Color.rgb(color, color, color));
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                if (map[y][x] != 0) {
                    drawStringAt(gc, x, y, 0f, 0f, Integer.toString(map[y][x]), Color.BLACK);
                }
            }
        }
        drawGrid(map);
    }

    public void drawPuzzleNode(NodeSearch node, int x, int y) {
        if (node == null || !(node.state instanceof StatePuzzle)) return;
        gc.translate(x, y);

        StatePuzzle state = (StatePuzzle) node.state;
        drawPuzzle(state.grid);
        drawStringAt(gc, state.grid.length, 0, 0, 0, "g=" + node.getG(), Color.BLACK);
        drawStringAt(gc, state.grid.length, 1, 0, 0, "h=" + node.getH(), Color.BLACK);
        drawStringAt(gc, state.grid.length, 2, 0, 0, "f=" + node.getF(), Color.BLACK);
     
        gc.translate(-x, -y);
    }

    private void drawStringAt(GraphicsContext gc, int x, int y, float anchorx, float anchory, String str, Color color) {
        final int w = 8, h = 10; // size of a letter
        gc.setFill(color);
        double halfHeight = h / 2;
        double halfWidth = w * str.length() / 2;
        gc.fillText(str, 
        TILE_SIZE / 2 + x * TILE_SIZE - halfWidth + anchorx * halfWidth, 
        TILE_SIZE / 2 + y * TILE_SIZE + halfHeight + anchory * halfHeight
        );
    }
}
