import java.util.Map;

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

    public void draw() {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawMap(Node.map);
        drawStartEnd();
        drawOpen();
        drawClose();
        drawPath();
        drawGrid();
    }

    private void drawStartEnd() {
        gc.setFill(Color.BLUE);
        gc.fillRect(astar.start.x * TILE_SIZE, astar.start.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        gc.setFill(Color.RED);
        gc.fillRect(astar.end.x * TILE_SIZE, astar.end.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void drawTileNode(Node node, Color color) {
        gc.setFill(color);
        gc.fillRect(node.x * TILE_SIZE, node.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        drawTileNodeInfo(node);
    }

    private void drawTileNodeInfo(Node node) {
        if (Settings.showNodeInfo == false) {
            return;
        }
        float y = 3;
        float x = 0;
        drawStringAt(gc, node.x, node.y, x, y * -1, "g=" + node.g, Color.BLACK);
        drawStringAt(gc, node.x, node.y, x, y * 0, "h=" + node.h, Color.BLACK);
        drawStringAt(gc, node.x, node.y, x, y * 1, "f=" + node.getF(), Color.BLACK);
    }

    private void drawOpen() {
        for (Node node : astar.open) {
            drawTileNode(node, Color.rgb(250, 250, 150));
        }
    }

    private void drawClose() {
        for (Map.Entry<Integer, Node> entry : astar.close.entrySet()) {
            Node node = entry.getValue();
            drawTileNode(node, Color.rgb(250, 200, 200));
        }
    }

    private void drawPath() {
        Node n = astar.open.peek();
        while (n != null) {
            drawTileNode(n, Color.rgb(0, 255, 0));
            n = n.parent;
        }
    }

    private void drawGrid() {
        int cols = Node.map.length;
        int rows = Node.map[0].length;
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

    private void drawMap(int map[][]) {
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
