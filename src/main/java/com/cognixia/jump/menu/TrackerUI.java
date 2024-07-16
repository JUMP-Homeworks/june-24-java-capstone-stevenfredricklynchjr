package com.cognixia.jump.menu;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

/*
 * Tracker UI Class to generate a simple UI for users
 * 
 * Used to generate a window displaying a graphical 
 * representation of each of the user's trackers, with
 * a back button that closes the window without closing 
 * the application
 */
public class TrackerUI {
    private static List<Map.Entry<String, Double>> progressListWithTitles;
    private static boolean isRunning = true;

    public static void setProgressListWithTitles(List<Map.Entry<String, Double>> progressListWithTitles) {
        TrackerUI.progressListWithTitles = progressListWithTitles;
    }

    public void displayUI() {
        Frame frame = new Frame("Tracker Progress");
        frame.setSize(800, 600); // 

        Panel panel = new Panel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        // iterate over progress entries and add components to the panel
        for (Map.Entry<String, Double> entry : progressListWithTitles) {
            String title = entry.getKey();
            Double progress = entry.getValue();

            Label titleLabel = new Label(title);
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panel.add(titleLabel, gbc);

            // local final variable for progress
            final double progressValue = progress;

            Canvas canvas = new Canvas() {
                private static final long serialVersionUID = 1L;

                @Override
                public void paint(Graphics g) {
                    super.paint(g);
                    drawProgressCircle(g, progressValue);
                }
            };
            canvas.setSize(200, 200); // Adjusted canvas size
            panel.add(canvas, gbc);
        }

        // add back button to close window
        Button backButton = new Button("Back");
        backButton.addActionListener(e -> {
            isRunning = false;
            frame.dispose();
        });
        panel.add(backButton, gbc);

        // allow scrolling
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(panel);

        frame.add(scrollPane);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRunning = false;
            }
        });

        frame.setVisible(true);
    }

    private void drawProgressCircle(Graphics g, double progress) {
        int centerX = 100;
        int centerY = 100;
        int radius = 80;

        // background circle
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // progress loading circle
        g.setColor(Color.BLUE);
        int startAngle = 90;
        int arcAngle = (int) (-progress * 360);
        g.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, arcAngle);

        // progress percentage text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString((int) (progress * 100) + "%", centerX - 25, centerY + 10);
    }

    public static boolean isRunning() {
        return isRunning;
    }
}
