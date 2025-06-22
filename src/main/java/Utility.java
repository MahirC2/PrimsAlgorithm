import java.util.Objects;

public class Utility
{
    static final int[][] directions = new int[][]{{0,-1}, {-1,0}, {1,0}, {0,1}};

    public static boolean inBounds(int x, int y, char[][] grid)
    {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    public static boolean isOnBorder(int x, int y, char[][] grid) {
        return x == 0 || y == 0 || x == grid.length - 1 || y == grid[0].length - 1;
    }
}
