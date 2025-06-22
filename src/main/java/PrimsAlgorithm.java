import javax.swing.*;
import java.util.*;

public class PrimsAlgorithm
{
    // Only odd numbers for grid size.
    private static final char[][] grid = new char[55][55];
    private static final MazePanel mazePanel = new MazePanel(grid);
    private static final JLabel progressLabel = new JLabel("Progress: 0%");
    private static final JButton primsButton = new JButton("Prim's");
    private static final JButton aStarButton = new JButton("A*");
    private static final JButton dfsButton = new JButton("DFS");
    private static final JButton bfsButton = new JButton("BFS");
    private static final Random random = new Random();
    private static final List<int[]> wallsList = new ArrayList<>();
    private static final List<int[]> oppositeWallsList = new ArrayList<>();

    private static int visitedCount = 1;
    private static boolean activePrim = false;
    private static boolean activeAStar = false;
    private static boolean activeDfs = false;
    private static boolean activeBfs = false;
    private static boolean finishedPrim = false;
    private static AStar aStarAlgorithm = null;
    private static DepthFirstSearch dfsAlgorithm = null;
    private static BreadthFirstSearch bfsAlgorithm = null;

    public static JProgressBar progressBar;

    public static void main(String[] args) {
        initializeGrid();
        SwingUtilities.invokeLater(() -> MazeWindow.show(grid, mazePanel, progressLabel, primsButton, aStarButton, dfsButton, bfsButton));

        primsButton.addActionListener(e -> {
            if (!activePrim) {
                new Thread(PrimsAlgorithm::primsAlgorithm).start();
                activePrim = true;
            }
        });

        aStarButton.addActionListener(e ->
        {
            if(finishedPrim && !activeDfs && !activeAStar && !activeBfs)
            {
                new Thread(() -> {
                    try {
                        activeAStar = true;
                        aStarAlgorithm.runAStar();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
            }
        });

        dfsButton.addActionListener(e ->
        {
            if(finishedPrim && !activeAStar && !activeDfs && !activeBfs)
            {
                new Thread(() -> {
                    try {
                        activeDfs = true;
                        dfsAlgorithm.startDFS();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
            }
        });

        bfsButton.addActionListener(e ->
        {
            if(finishedPrim && !activeAStar && !activeDfs && !activeBfs)
            {
                new Thread(() -> {
                    try {
                        activeBfs = true;
                        bfsAlgorithm.startBFS();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
            }
        });
    }

    private static void initializeGrid()
    {
        for (char[] chars : grid) {
            Arrays.fill(chars, 'W');
        }
    }

    private static void primsAlgorithm() {
        System.out.println("Running primsAlgorithm...");

        int startX = getRandomOdd(grid.length - 2);
        int startY = getRandomOdd(grid[0].length - 2);
        grid[startX][startY] = ' ';
        surrounding(startX, startY);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<int[]> paths = getAllPathCells();
        int[] startCell = paths.get(random.nextInt(paths.size()));
        int[] endCell;
        do {
            endCell = paths.get(random.nextInt(paths.size()));
        } while (Arrays.equals(startCell, endCell));


        grid[startCell[0]][startCell[1]] = 'S';
        grid[endCell[0]][endCell[1]] = 'E';
        int[] gridEndCell = endCell;
        SwingUtilities.invokeLater(mazePanel::repaint);
        finishedPrim = true;
        aStarAlgorithm = new AStar(mazePanel, grid, startCell, gridEndCell, progressLabel);
        dfsAlgorithm = new DepthFirstSearch(mazePanel, grid, startCell, endCell, progressLabel);
        bfsAlgorithm = new BreadthFirstSearch(mazePanel, grid, startCell, endCell, progressLabel);
    }

    private static int getRandomOdd(int max) {
        return random.nextInt((max - 1) / 2 + 1) * 2 + 1;
    }

    private static void surrounding(int startX, int startY)
    {
        grid[startX][startY] = ' ';
        addNeighbours(startX, startY);
        while(!wallsList.isEmpty())
        {
            int randomInt = random.nextInt(wallsList.size());
            convertWallToPath(randomInt);
        }
    }

    private static void addNeighbours(int startX, int startY)
    {
        for(int[] dir : Utility.directions)
        {
            int wallX = startX + dir[0];
            int wallY = startY + dir[1];
            int oppositeX = startX + (2 * dir[0]);
            int oppositeY = startY + (2 * dir[1]);

            if (!Utility.inBounds(oppositeX, oppositeY, grid) || Utility.isOnBorder(oppositeX, oppositeY, grid)) continue;

            if (grid[oppositeX][oppositeY] == 'W'){
                wallsList.add(new int[] { wallX, wallY });
                oppositeWallsList.add(new int[] {oppositeX, oppositeY});
            }
        }
    }

    public static List<int[]> getAllPathCells() {
        List<int[]> pathCells = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == ' ') {
                    pathCells.add(new int[]{row, col});
                }
            }
        }
        return pathCells;
    }

    private static void convertWallToPath(int chosenWallIndex)
    {
        int[] wall = wallsList.get(chosenWallIndex);
        int[] oppositeWall = oppositeWallsList.get(chosenWallIndex);
        int wallX = wall[0];
        int wallY = wall[1];
        int oppositeWallX = oppositeWall[0];
        int oppositeWallY = oppositeWall[1];


        if(grid[wallX][wallY] == 'W' && grid[oppositeWallX][oppositeWallY] == 'W')
        {
            grid[wallX][wallY] = ' ' ;
            grid[oppositeWallX][oppositeWallY] = ' ';
            visitedCount += 2;
            mazePanel.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            addNeighbours(oppositeWallX, oppositeWallY);
        }
        wallsList.remove(chosenWallIndex);
        oppositeWallsList.remove(chosenWallIndex);

        int totalExpected = ((grid.length - 1) / 2) * ((grid[0].length - 1) / 2);
        int progress = (int)((visitedCount / (double)(totalExpected * 2 - 1)) * 100);

        SwingUtilities.invokeLater(() -> {
                progressLabel.setText("Progress: " + progress + "%");
            if (progressBar != null) progressBar.setValue(progress);});

    }
}
