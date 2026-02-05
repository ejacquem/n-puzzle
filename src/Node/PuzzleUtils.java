package Node;
public class PuzzleUtils {
    public static void print2DArray(int grid[][], int spacing) {
        System.out.println("-----------------");
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.printf("%" + spacing + "d ", grid[y][x]);
            }
            System.out.println();
        }
    }
}
