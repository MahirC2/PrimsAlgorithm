import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author: MahirC2
 */
public class MazeWindow {

    public static void show(char[][] grid, MazePanel mazePanel, JLabel progressLabel, JButton primsButton, JButton aStarButton, JButton dfsButton, JButton bfsButton) {
        JFrame frame = new JFrame("Prim's Algorithm vs Path Finding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("PRIM’S ALGORITHM VS PATH FINDING");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);


        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        PrimsAlgorithm.progressBar = progressBar;

        JLabel subtitle = new JLabel("Maze Generation");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressLabel.setForeground(Color.LIGHT_GRAY);
        progressLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        primsButton.setForeground(Color.WHITE);
        primsButton.setBackground(new Color(30, 30, 30));
        primsButton.setBorderPainted(false);
        primsButton.setFocusPainted(false);
        primsButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        primsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        aStarButton.setForeground(Color.WHITE);
        aStarButton.setBackground(new Color(30, 30, 30));
        aStarButton.setBorderPainted(false);
        aStarButton.setFocusPainted(false);
        aStarButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        aStarButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        dfsButton.setForeground(Color.WHITE);
        dfsButton.setBackground(new Color(30, 30, 30));
        dfsButton.setBorderPainted(false);
        dfsButton.setFocusPainted(false);
        dfsButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dfsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        bfsButton.setForeground(Color.WHITE);
        bfsButton.setBackground(new Color(30, 30, 30));
        bfsButton.setBorderPainted(false);
        bfsButton.setFocusPainted(false);
        bfsButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        bfsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(title);
        topPanel.add(subtitle);
        topPanel.add(progressLabel);
        topPanel.add(primsButton);
        topPanel.add(aStarButton);
        topPanel.add(dfsButton);
        topPanel.add(bfsButton);
        topPanel.add(Box.createVerticalStrut(10));

        JLabel sizeLabel = new JLabel(grid.length + "x" + grid[0].length);
        sizeLabel.setForeground(Color.LIGHT_GRAY);
        sizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(sizeLabel, BorderLayout.EAST);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(30, 30, 30));
        centerWrapper.add(mazePanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        int padding = 40;
        int maxWidth = 800;
        int maxHeight = 800 - padding;

        mazePanel.resizeToFit(maxWidth, maxHeight);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = frame.getContentPane().getSize();
                mazePanel.resizeToFit(size.width, size.height - topPanel.getHeight() - bottomPanel.getHeight());
                frame.revalidate();
                mazePanel.repaint();
            }
        });
    }
}
