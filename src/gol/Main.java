package gol;

import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.util.*;
import javax.swing.*;

public class Main implements Global {
    public static void main(String[] args) throws Exception {
        DNA.init();

        final Config           config = new Config();
        final Universe              u = new Universe(config);
        final ToroidalGridPanel panel = new ToroidalGridPanel(u, config.pixel_size());
        final ImageCapture    capture = new ImageCapture(u, panel);
        final Info               info = new Info(u, panel);
        final Controller            c = new Controller(u, config.threads());

        new javax.swing.Timer(1000, new PanelRepainter(panel)).start();

        u.addListener(panel);
        u.addListener(capture);

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame("Main");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocation(800, 0);

                    panel.addKeyListener(new Navigation(panel, info));
                    PanelPointer pointer = new PanelPointer(panel, info);
                    panel.addMouseListener(pointer);
                    panel.addMouseMotionListener(pointer);

                    Box content = Box.createHorizontalBox();
                    info.panel.setAlignmentY(JPanel.TOP_ALIGNMENT);
                    panel.setAlignmentY(JPanel.TOP_ALIGNMENT);

                    content.setOpaque(true);
                    content.setAlignmentY(JPanel.TOP_ALIGNMENT);
                    content.add(info.panel);
                    content.add(panel);
                    content.setBackground(new Color(192,192,192));

                    frame.getContentPane().add(content);
                    frame.pack();
                    frame.setVisible(true);

                    u.restart();
                    new Thread(c).start();
                }
            });

    }
}
