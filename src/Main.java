import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

        Node start = Node.create(0, 0, null);
        Node end = Node.create(gridX - 1, gridY - 1, null);

        int map[][] = GridGenerator.generate(gridX, gridY);
        map[start.y][start.x] = 0;
        map[end.y][end.x] = 0;
        aStar = new AStar(start, end, map);
        renderer = new AStarRenderer(canvas, aStar);
        renderer.draw();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                stage.close();
            }
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                aStar.step();
                renderer.draw();
            }
        });

        stage.setTitle("A* Visualizer");
        stage.setScene(scene);
        stage.show();
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