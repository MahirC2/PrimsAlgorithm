import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private final char[][] grid;

    public MazePanel(char[][] grid) {
        this.grid = grid;
        setBackground(new Color(155, 30, 30));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int cellWidth = getWidth() / grid[0].length;
        int cellHeight = getHeight() / grid.length;
        int cellSize = Math.min(cellWidth, cellHeight);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(1f));
        for (int row = 0; row <= grid.length; row++) {
            g2.drawLine(0, row * cellSize, grid[0].length * cellSize, row * cellSize);
        }
        for (int col = 0; col <= grid[0].length; col++) {
            g2.drawLine(col * cellSize, 0, col * cellSize, grid.length * cellSize);
        }

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1f));

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                if (grid[row][col] == 'W') continue;

                int x = col * cellSize;
                int y = row * cellSize;

                if (row > 0 && grid[row - 1][col] != 'W') {
                    g2.drawLine(x, y, x + cellSize, y);
                }

                if (col > 0 && grid[row][col - 1] != 'W') {
                    g2.drawLine(x, y, x, y + cellSize);
                }

                if (row < grid.length - 1 && grid[row + 1][col] != 'W') {
                    g2.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                }

                if (col < grid[0].length - 1 && grid[row][col + 1] != 'W') {
                    g2.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
                }
            }
        }

        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[0].length; col++)
            {
                int x = col * cellSize;
                int y = row * cellSize;

                if (grid[row][col] == 'B') {
                    g2.setColor(new Color(66, 84,245));
                    g2.fillRect(x, y, cellSize, cellSize);
                }

                if (grid[row][col] == 'O') {
                    g2.setColor(Color.PINK);
                    g2.fillRect(x, y, cellSize, cellSize);
                }

                if (grid[row][col] == 'P') {
                    g2.setColor(new Color(48, 0, 130));
                    g2.fillRect(x, y, cellSize, cellSize);
                }

                if (grid[row][col] == 'H') {
                    g2.setColor(new Color(100, 20, 40));
                    g2.fillRect(x, y, cellSize, cellSize);
                }

                if (grid[row][col] == 'V') {
                    g2.setColor(new Color(177, 124, 15));
                    g2.fillRect(x, y, cellSize, cellSize);
                }

                if (grid[row][col] == 'S') {
                    g2.setColor(Color.GREEN);
                    g2.fillOval(x + 3, y + 3, cellSize - 6, cellSize - 6);
                }
                else if (grid[row][col] == 'E')
                {
                    g2.setColor(Color.RED);
                    g2.fillOval(x + 3, y + 3, cellSize - 6, cellSize - 6);
                }
            }
        }
    }

    public void resizeToFit(int maxWidth, int maxHeight) {
        int rows = grid.length;
        int cols = grid[0].length;

        int cellSize = Math.min(maxWidth / cols, maxHeight / rows);

        int width = cols * cellSize;
        int height = rows * cellSize;

        setPreferredSize(new Dimension(width, height));
        revalidate();
    }
}