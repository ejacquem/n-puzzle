import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Node.NodeSearch;
import Node.StateGrid;
import Node.StatePuzzle;

public class Main extends Application {

    private static final int WIDTH = 1300;
    private static final int HEIGHT = 800;

    private AStarRenderer renderer;
    private AStar aStar;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        int gridX = 20;
        int gridY = 15;

        // int map[][] = GridGenerator.generate(gridX, gridY);
        int map[][] = new int[gridY][gridX];

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        renderer = new AStarRenderer(canvas, aStar);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                stage.close();
            }
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                if (aStar == null) return;
                aStar.step();
                renderer.draw();
            }
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                launchAStar(map);
                renderer.draw();
            }
        });

        scene.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown() || event.isSecondaryButtonDown()) { 
                double x = event.getSceneX();
                double y = event.getSceneY();
                
                int tx = (int)x / AStarRenderer.TILE_SIZE;
                int ty = (int)y / AStarRenderer.TILE_SIZE;
                if (ty >= 0 && ty < map.length && tx >= 0 && tx < map[ty].length) {
                    map[ty][tx] = event.isPrimaryButtonDown() ? 1 : 0;
                    renderer.drawMap(map);
                    renderer.drawGrid(map);
                }
            }
        });

        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) { 
                double x = event.getSceneX();
                double y = event.getSceneY();
                
                int tx = (int)x / AStarRenderer.TILE_SIZE;
                int ty = (int)y / AStarRenderer.TILE_SIZE;
                map[ty][tx] = event.getButton() == MouseButton.PRIMARY ? 1 : 0;
                renderer.drawMap(map);
                renderer.drawGrid(map);
            }
        });

        // scene.setOnMouseMoved(event -> {
        //     renderer.drawMap(map);
        //     renderer.drawGrid(map);
        // });

        renderer.drawMap(map);
        renderer.drawGrid(map);

        stage.setTitle("A* Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    public void launchAStar(int[][] map) {
        StateGrid.GridProblem problem = new StateGrid.GridProblem(map);

        StateGrid start = new StateGrid(0, 0, problem);
        StateGrid goal = new StateGrid(map[0].length - 1, map.length - 1, problem);
        NodeSearch endNode = new NodeSearch(goal, goal, null);
        NodeSearch startNode = new NodeSearch(start, goal, null);

        //TODO
        // int[3][3] = {{1,2,3}, {1,2,3}, {1,2,3}, };//TODO
        // StatePuzzle start = new StatePuzzle(WIDTH, HEIGHT, map); 

        map[start.y][start.x] = 0;
        map[goal.y][goal.x] = 0;
        aStar = new AStar(startNode, endNode);
        renderer.setAStar(aStar);
        renderer.draw();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String readFile(String filename) {
        String line;
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return content.toString();
    }
}