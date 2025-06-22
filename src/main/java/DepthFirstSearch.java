import javax.swing.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

/**
 * @author: MahirC2
 */
public class DepthFirstSearch
{
    private final char[][] grid;
    private final int[] start;
    private final int[] end;
    private final int initialDistance;

    private final Stack<Node> stack = new Stack<>();
    private final MazePanel mazePanel;
    private final JLabel progressLabel;

    private final Set<Node> visitedNodes = new HashSet<>();

    DepthFirstSearch(MazePanel mazePanel, char[][] grid, int[] start, int[] end, JLabel progressLabel)
    {
        this.mazePanel = mazePanel;
        this.progressLabel = progressLabel;
        this.grid = grid;
        this.start = start;
        this.end = end;
        this.initialDistance = Math.abs(start[0] - end[0]) + Math.abs(start[1] - end[1]);
    }

    public void startDFS() throws InterruptedException {
        Node node = new Node(start, null);
        if (grid[start[0]][start[1]] != 'E' && grid[start[0]][start[1]] != 'S') {
            grid[start[0]][start[1]] = 'V';
        }
        stack.push(node);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            int currentDistance = Math.abs(currentNode.getStartX() - end[0]) + Math.abs(currentNode.getStartY() - end[1]);
            int progress = 100 * (initialDistance - currentDistance) / initialDistance;

            SwingUtilities.invokeLater(() -> progressLabel.setText("Progress: " + progress + "%"));

            if (currentNode.getStartX() == end[0] && currentNode.getStartY() == end[1]) {
                reconstructPath(currentNode);
                break;
            }
            searchNeighbours(currentNode);
        }
    }

    private void searchNeighbours(Node node) throws InterruptedException {
        boolean hasUnvisitedNeighbour = false;

        for (int[] dir : Utility.directions) {
            int newX = node.getStartX() + dir[0];
            int newY = node.getStartY() + dir[1];

            if (!Utility.inBounds(newX, newY, grid)) continue;
            if (grid[newX][newY] == 'W' || grid[newX][newY] == 'V' || grid[newX][newY] == 'P') continue;

            hasUnvisitedNeighbour = true;

            Node newNode = new Node(new int[]{newX, newY}, node);
            if (visitedNodes.contains(newNode)) continue;
            visitedNodes.add(newNode);
            stack.push(newNode);

            if (grid[newX][newY] != 'S' && grid[newX][newY] != 'E') {
                grid[newX][newY] = 'V';
            }

            SwingUtilities.invokeLater(mazePanel::repaint);
            Thread.sleep(50);
        }

        if (!hasUnvisitedNeighbour && grid[node.getStartX()][node.getStartY()] != 'S' && grid[node.getStartX()][node.getStartY()] != 'E') {
            grid[node.getStartX()][node.getStartY()] = 'B';
            SwingUtilities.invokeLater(mazePanel::repaint);
            Thread.sleep(50);
        }
    }

    private void reconstructPath(Node node) throws InterruptedException {
       Node current = node;
        while (current != null) {
            int x = current.getStartX();
            int y = current.getStartY();

            if (grid[x][y] != 'S' && grid[x][y] != 'E') {
                grid[x][y] = 'O';
                SwingUtilities.invokeLater(mazePanel::repaint);
                Thread.sleep(5);
            }
            current = current.getParent();
        }
    }

    private static class Node
    {
        private final int startX;
        private final int startY;
        private final Node parent;

        Node(int[] start, Node parentNode)
        {
            this.startX = start[0];
            this.startY = start[1];
            this.parent = parentNode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return startX == node.startX && startY == node.startY;
        }

        @Override
        public int hashCode() {
            return Objects.hash(startX, startY);
        }

        public int getStartY() {
            return startY;
        }

        public int getStartX() {
            return startX;
        }

        public Node getParent() {
            return parent;
        }
    }
}
