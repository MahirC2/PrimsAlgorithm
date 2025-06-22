import javax.swing.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * @author: MahirC2
 */
public class AStar
{
    private final int[][] directions = new int[][]{{0,-1}, {-1,0}, {1,0}, {0,1}};

    private final MazePanel mazePanel;
    private final JLabel progressLabel;
    private final char[][] grid;
    private final int[] start;
    private final int[] end;
    private final PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
    private final Set<Node> visitedNodeSet = new HashSet<>();
    private final int initialDistance;

    AStar(MazePanel mazePanel, char[][] grid, int[] start, int[] end, JLabel progressLabel)
    {
        this.mazePanel = mazePanel;
        this.progressLabel = progressLabel;
        this.grid = grid;
        this.start = start;
        this.end = end;
        this.initialDistance = Math.abs(start[0] - end[0]) + Math.abs(start[1] - end[1]);
    }

    void runAStar() throws InterruptedException {
        boolean pathFound = false;
        Node startNode = new Node(start, end, null);
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty() && !pathFound)
        {
            Node currentNode = priorityQueue.poll();

            int currentDistance = Math.abs(currentNode.getStartX() - end[0]) + Math.abs(currentNode.getStartY() - end[1]);
            int progress = 100 * (initialDistance - currentDistance) / initialDistance;
            SwingUtilities.invokeLater(() -> progressLabel.setText("Progress: " + progress + "%"));

            if (grid[currentNode.getStartX()][currentNode.getStartY()] != 'S' &&
                    grid[currentNode.getStartX()][currentNode.getStartY()] != 'E') {
                grid[currentNode.getStartX()][currentNode.getStartY()] = 'B';
                SwingUtilities.invokeLater(mazePanel::repaint);
                Thread.sleep(50);
            }

            if (currentNode.getStartX() == end[0] && currentNode.getStartY() == end[1]) {
                pathFound = true;
                reconstructPath(currentNode);
            }

            visitedNodeSet.add(currentNode);
            searchNeighbours(currentNode);
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
                Thread.sleep(50);
            }

            current = current.getParent();
        }
    }

    private void searchNeighbours(Node node) throws InterruptedException {
        for(int[] dir : directions)
        {
            int newNodeX = node.getStartX() + dir[0];
            int newNodeY = node.getStartY() + dir[1];

            if (!Utility.inBounds(newNodeX, newNodeY, grid)) continue;
            if (grid[newNodeX][newNodeY] == 'W') continue;

            Node newNode = new Node(new int[]{newNodeX, newNodeY}, end, node);
            if(visitedNodeSet.contains(newNode)) continue;
            priorityQueue.add(newNode);

            if (grid[newNodeX][newNodeY] != 'S' && grid[newNodeX][newNodeY] != 'E') {
                grid[newNodeX][newNodeY] = 'P';
            }
            SwingUtilities.invokeLater(mazePanel::repaint);
            Thread.sleep(5);
        }
    }

    private static class Node implements Comparable<Node>
    {
        private final int startX;
        private final int startY;
        private final Node parent;

        private int g = 0;
        private int h = 0;
        private int f = 0;

        Node(int[] start, int[] finish, Node parentNode)
        {
            this.startX = start[0];
            this.startY = start[1];
            this.parent = parentNode;
            calculateF(finish);
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

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.getFCost(), o.getFCost());
        }

        public void calculateF(int[] finish)
        {
            if(parent != null)
            {
                g = parent.getGCost() + 1;
            }
            h = Math.abs(startX - finish[0]) + Math.abs(startY - finish[1]);
            f = getGCost() + getHCost();
        }

        public int getFCost() {
            return f;
        }

        public int getHCost() {
            return h;
        }

        public int getGCost() {
            return g;
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
