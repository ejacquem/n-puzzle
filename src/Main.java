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
    private AStar astar;
    int solutionPathIndex = 0;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        int gridX = 20;
        int gridY = 15;

        // int map[][] = GridGenerator.generate(gridX, gridY);
        int map[][] = new int[gridY][gridX];

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        renderer = new AStarRenderer(canvas, astar);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                stage.close();
            }
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                if (astar == null) return;
                if (astar.solutionFound) {
                    if (solutionPathIndex < astar.path.size()) {
                        NodeSearch node = astar.path.get(solutionPathIndex);
                        solutionPathIndex++;
                        renderer.drawBackGround();
                        renderer.drawPuzzleNode(node, 0, 0);
                    }
                } else {
                    astar.step();
                    renderer.draw();
                }
            }
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                launchAStar(map);
                while(astar.solutionFound == false && astar.solutionDoesntExist == false){
                    astar.step();
                }
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
                if (ty >= 0 && ty < map.length && tx >= 0 && tx < map[ty].length) {
                    map[ty][tx] = event.getButton() == MouseButton.PRIMARY ? 1 : 0;
                    renderer.drawMap(map);
                    renderer.drawGrid(map);
                }
            }
        });

        // scene.setOnMouseMoved(event -> {
        //     renderer.drawMap(map);
        //     renderer.drawGrid(map);
        // });

        // renderer.drawMap(map);
        // renderer.drawGrid(map);

        stage.setTitle("A* Visualizer");
        stage.setScene(scene);
        stage.show();
    }
    

    public void launchAStar(int[][] map) {
        solutionPathIndex = 0;
        
        NodeSearch endNode;
        NodeSearch startNode;

        boolean truc = false;
        if (truc) {
            StateGrid.GridProblem problem = new StateGrid.GridProblem(map);
            StateGrid start = new StateGrid(0, 0, problem);
            StateGrid goal = new StateGrid(map[0].length - 1, map.length - 1, problem);
            endNode = new NodeSearch(goal, goal, null);
            startNode = new NodeSearch(start, goal, null);
            map[start.y][start.x] = 0;
            map[goal.y][goal.x] = 0;
        } else {
            // int[][] gridStart = {{1,2,3}, {4,5,6}, {7,8,0}};
            // int[][] gridStart = {{3,4,7}, {6,8,5}, {1,2,0}};
            // int[][] gridStart = {{1,4,2}, {3,0,5}, {6,7,8}}; // 2 move
            
            int[][] gridStart = {
                {8,7,6}, 
                {5,4,3}, 
                {2,1,0}};
            int[][] gridGoal = {{0,1,2}, {3,4,5}, {6,7,8}};
            
            // int[][] gridStart = {
            //     {15, 14, 13, 12},
            //     {11, 10,  9,  8},
            //     { 7,  6,  5,  4},
            //     { 3,  2,  1,  0}
            // };
            // int[][] gridGoal = {
            //     { 0,  1,  2,  3},
            //     { 4,  5,  6,  7},
            //     { 8,  9, 10, 11},
            //     {12, 13, 14, 15}
            // };
                    
            StatePuzzle puzzleStart = new StatePuzzle(gridStart); 
            StatePuzzle puzzleGoal = new StatePuzzle(gridGoal); 
            endNode = new NodeSearch(puzzleGoal, puzzleGoal, null);
            startNode = new NodeSearch(puzzleStart, puzzleGoal, null);
        }

        astar = new AStar(startNode, endNode);

        renderer.setAStar(astar);
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