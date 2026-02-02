import java.util.Random;

public class GridGenerator {
    public static int[][] generate(int cols, int rows) {
        int[][] grid = new int[rows][cols];

        Random rand = new Random();

        double obstacleProbability = 0.2; // 20% cells will be obstacles

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if (rand.nextDouble() < obstacleProbability) {
                    grid[x][y] = 1; // obstacle
                } else {
                    grid[x][y] = 0; // empty
                }
            }
        }
        return grid;
    }
}
