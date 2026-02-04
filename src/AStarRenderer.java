import java.util.Map;

import Node.NodeSearch;
import Node.StateGrid;
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
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawAStarGrid();
    }

    public void drawAStarGrid() {
        if (astar == null) return;
        drawMap(((StateGrid)astar.start.state).problem.map);
        drawStartGoal((StateGrid)astar.start.state, (StateGrid)astar.goal.state);
        drawOpen();
        drawClose();
        drawPath();
        drawGrid(((StateGrid)astar.start.state).problem.map);
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
        for (Map.Entry<Integer, NodeSearch> entry : astar.close.entrySet()) {
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
