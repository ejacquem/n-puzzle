import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import Algorithm.IdaStar;
import Algorithm.ASearchAlgorithm;
import Algorithm.AStar;
import Node.NodeSearch;
import Node.StateGrid;
import Node.StatePuzzle;
import Utils.Ansi;

public class Main extends Application {

    private static final int WIDTH = 1300;
    private static final int HEIGHT = 800;

    private AStarRenderer renderer;
    private ASearchAlgorithm algo;
    int solutionPathIndex = 0;

    public List<NodeSearch> path;

    private NodeSearch start;
    private NodeSearch goal;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        int gridX = 20;
        int gridY = 15;

        // int map[][] = GridGenerator.generate(gridX, gridY);
        int map[][] = new int[gridY][gridX];

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        renderer = new AStarRenderer(canvas, null);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                stage.close();
            }
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                if (algo == null) return;
                if (path != null) {
                    if (solutionPathIndex < path.size()) {
                        NodeSearch node = path.get(solutionPathIndex);
                        solutionPathIndex++;
                        renderer.drawBackGround();
                        renderer.drawPuzzleNode(node, 0, 0);
                    }
                } else {
                    // astar.step();
                    // renderer.draw();
                }
            }
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                launchAStar(map);
                // astar.step();
                // astar.solve();
                algo = new IdaStar();
                algo.solve(start, goal);
                // new AStar().solve(start, goal);
                path = algo.getPath();
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
        
        NodeSearch goalNode;
        NodeSearch startNode;

        boolean grid = true;
        if (grid) {
            StateGrid.GridProblem problem = new StateGrid.GridProblem(map);
            StateGrid start = new StateGrid(0, 0, problem);
            StateGrid goal = new StateGrid(map[0].length - 1, map.length - 1, problem);
            goalNode = new NodeSearch(goal, goal, null);
            startNode = new NodeSearch(start, goal, null);
            map[start.y][start.x] = 0;
            map[goal.y][goal.x] = 0;
        } else {
            // int[][] gridStart = {{1,2,3}, {4,5,6}, {7,8,0}};
            // int[][] gridStart = {{3,4,7}, {6,8,5}, {1,2,0}};
            // int[][] gridStart = {{1,4,2}, {3,0,5}, {6,7,8}}; // 2 move
            
            // int[][] gridStart = {
            //     {8,7,6}, 
            //     {5,4,3}, 
            //     {2,1,0}};
            // int[][] gridGoal = {{0,1,2}, {3,4,5}, {6,7,8}};

            // int[][] gridStart = {
            //     { 0,  1,  2,  3},
            //     { 4,  5,  6,  7},
            //     { 8,  9, 10, 11},
            //     {12, 15, 13, 14}
            // };

            // int[][] gridGoal = {
            //     { 0,  1,  2,  3},
            //     { 4,  5,  6,  7},
            //     { 8,  9, 10, 11},
            //     {12, 13, 14, 15}
            // };
            
            
            // int[][] gridStart = {
            //     { 5,  1,  2,  3},
            //     { 9,  6,  7,  4},
            //     {13, 10, 11,  8},
            //     { 0, 14, 15, 12}
            // };
            // int[][] gridGoal = {
            //     { 5,  2,  0,  3},
            //     { 9,  1, 11,  4},
            //     {13,  7,  6,  8},
            //     {14, 10, 15, 12}
            // };

            int[][] gridStart = {
                {14, 15, 13, 12},
                {11, 10,  9,  8},
                { 7,  6,  5,  4},
                { 3,  2,  1,  0}
            };
            int[][] gridGoal = {
                { 0,  1,  2,  3},
                { 4,  5,  6,  7},
                { 8,  9, 10, 11},
                {12, 13, 14, 15}
            };
                    
            StatePuzzle puzzleStart = new StatePuzzle(gridStart); 
            StatePuzzle puzzleGoal = new StatePuzzle(gridGoal);
            puzzleGoal.setGoalPosition();
            goalNode = new NodeSearch(puzzleGoal, puzzleGoal, null);
            startNode = new NodeSearch(puzzleStart, puzzleGoal, null);
        }

        // astar = new AStar(startNode, endNode);
        // idastar = new IdaStar();
        this.start = startNode;
        this.goal = goalNode;

        // renderer.setAStar(astar);
        renderer.draw();
    }

    public static void main(String[] args) {
        // testStatePuzzleCanReach();
        launch(args);
    }

    public static void testStatePuzzleCanReach() {
        int test = 0;
        int[][] grid1 = {
            {0,1,2}, 
            {3,4,5}, 
            {6,7,8}};
        int[][] grid2 = {
            {1,0,2}, 
            {3,4,5}, 
            {6,7,8}};
        int[][] grid3 = {
            {3,1,2}, 
            {0,4,5}, 
            {6,7,8}};
        int[][] grid4 = {
            {0,2,5}, 
            {1,4,8}, 
            {3,6,7}};
        int[][] grid5 = { // false
            {0,2,5}, 
            {1,4,8}, 
            {3,7,6}};
        int[][] grid6 = { // false
            {0,2,5}, 
            {1,4,7}, 
            {3,6,8}};
        int[][] grid7 = { // false
            {0,1,2}, 
            {3,4,5}, 
            {6,8,7}};
        int[][] grid8 = { // true
            {0,1,2}, 
            {3,4,5}, 
            {8,6,7}};
        int[][] gridGoal = {{0,1,2}, {3,4,5}, {6,7,8}};
        StatePuzzle goal = new StatePuzzle(gridGoal);
        test(new StatePuzzle(grid1), goal, true, test++);
        test(new StatePuzzle(grid2), goal, true, test++);
        test(new StatePuzzle(grid3), goal, true, test++);
        test(new StatePuzzle(grid4), goal, true, test++);
        test(new StatePuzzle(grid5), goal, false, test++);
        test(new StatePuzzle(grid6), goal, false, test++);
        test(new StatePuzzle(grid7), goal, false, test++);
        test(new StatePuzzle(grid8), goal, true, test++);
        

        int[][] grid4_1 = {
            { 0,  1,  2,  3},
            { 4,  5,  6,  7},
            { 8,  9, 10, 11},
            {12, 13, 14, 15}
        };
        int[][] grid4_2 = {
            { 1,  0,  2,  3},
            { 4,  5,  6,  7},
            { 8,  9, 10, 11},
            {12, 13, 14, 15}
        };
        int[][] grid4_3 = { //false
            { 0,  1,  2,  3},
            { 4,  5,  6,  7},
            { 8,  9, 10, 11},
            {12, 13, 15, 14}
        };
        int[][] grid4_4 = { // true
            { 0,  1,  2,  3},
            { 4,  5,  6,  7},
            { 8,  9, 10, 11},
            {12, 15, 13, 14}
        };
        int[][] grid4_5 = { // ?
            {15, 14, 13, 12},
            {11, 10,  9,  8},
            { 7,  6,  5,  4},
            { 3,  2,  1,  0}
        };
        int[][] grid4_6 = { // ?
            {15, 14, 13, 12},
            {11, 10,  9,  8},
            { 7,  6,  5,  4},
            { 3,  2,  0,  1}
        };
        int[][] grid4_7 = { // true
            {14, 15, 13, 12},
            {11, 10,  9,  8},
            { 7,  6,  5,  4},
            { 3,  2,  1,  0}
        };
        int[][] grid4_8 = { // false
            {15, 14, 13, 12},
            {11, 10,  9,  8},
            { 7,  6,  5,  4},
            { 3,  2,  1,  0}
        };
        int[][] grid4Goal = {
            { 0,  1,  2,  3},
            { 4,  5,  6,  7},
            { 8,  9, 10, 11},
            {12, 13, 14, 15}
        };
        
        StatePuzzle goal4 = new StatePuzzle(grid4Goal);
        test(new StatePuzzle(grid4_1), goal4, true, test++);
        test(new StatePuzzle(grid4_2), goal4, true, test++);
        test(new StatePuzzle(grid4_3), goal4, false, test++);
        test(new StatePuzzle(grid4_4), goal4, true, test++);
        test(new StatePuzzle(grid4_5), goal4, true, test++);
        test(new StatePuzzle(grid4_6), goal4, true, test++);
        test(new StatePuzzle(grid4_7), goal4, true, test++);
        test(new StatePuzzle(grid4_8), goal4, false, test++);
    }

    public static void test(StatePuzzle start, StatePuzzle goal, boolean expected, int testnb) {
        final String ok = Ansi.GREEN + "OK" + Ansi.RESET;
        final String fail = Ansi.RED + "Fail" + Ansi.RESET;
        boolean canreach = StatePuzzle.canReach(start, goal);
        System.out.println("isSolvable(start.grid) :" + isSolvable(start.grid));
        System.out.println("isSolvable(goal.grid) :" + isSolvable(goal.grid));
        System.out.println("isSolvable(start.grid) == isSolvable(goal.grid):" + (isSolvable(start.grid) == isSolvable(goal.grid)));
        // System.out.printf("canreach == (isSolvable(start.grid) == isSolvable(goal.grid)): %b\n", canreach == (isSolvable(start.grid) == isSolvable(goal.grid)));
        System.out.printf("test %3d:, solvable: %b, expected: %b, %s\n", testnb, canreach, expected, canreach == expected ? ok : fail);
    }

    public static boolean isSolvable(int[][] puzzle)
    {
        // Count inversions in given puzzle
        int N = puzzle.length;
        int[] arr = new int[N * N];
        int k = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                arr[k++] = puzzle[i][j];

        int invCount = getInvCount(arr, N);

        // If grid is odd, return true if inversion
        // count is even.
        if (N % 2 == 1)
            return invCount % 2 == 0;
        else // grid is even
        {
            int pos = findXPosition(puzzle);
            if (pos % 2 == 1)
                return invCount % 2 == 0;
            else
                return invCount % 2 == 1;
        }
    }

    static int getInvCount(int[] arr, int N)
    {
        int inv_count = 0;
        for (int i = 0; i < N * N - 1; i++) {
            for (int j = i + 1; j < N * N; j++) {
                // count pairs(arr[i], arr[j]) such that
                // i < j but arr[i] > arr[j]
                if (arr[j] != 0 && arr[i] != 0
                    && arr[i] > arr[j])
                    inv_count++;
            }
        }
        return inv_count;
    }

    // find Position of blank from bottom
    static int findXPosition(int[][] puzzle)
    {
        int N = puzzle.length;
        // start from bottom-right corner of matrix
        for (int i = N - 1; i >= 0; i--)
            for (int j = N - 1; j >= 0; j--)
                if (puzzle[i][j] == 0)
                    return N - i;
        return -1;
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