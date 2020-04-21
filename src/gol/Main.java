package gol;

import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.util.*;
import javax.swing.*;

public class Main {
    static class Pointer extends MouseAdapter {
        public final ToroidalGridPanel panel;
        public final Info              info;
        public       Organism          watching;

        public Pointer(ToroidalGridPanel panel, Info info) {
            this.panel = panel;
            this.info = info;
        }

        public void mouseMoved(MouseEvent e) {
            panel.pointingAt(e.getPoint());
            info.update();
            panel.requireRedraw();
        }

        public void mouseClicked(MouseEvent e) {
            if (watching != null) {
                watching.watching = false;
                watching = null;
            }
            Coordinate co = panel.getUniverseCoordinate(e.getPoint());
            Cell     cell = panel.universe.getCell(co);
            Organism  org = cell.organism;

            System.out.println(cell);

            if (org != null) {
                org.watching = true;
                watching     = org;
                System.out.println(MapToString.toString(org.status()));
                Map<String, Integer> geneStats = DNA.stats(new HashMap<String, Integer>(), org.allBehaviours);
                System.out.println(MapToString.toString(geneStats));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DNA.init();
        Config           config = new Config();
        Universe              u = new Universe(config);
        ToroidalGridPanel panel = new ToroidalGridPanel(u, config.pixel_size());
        JLabel            label = new JLabel();
        Info               info = new Info(label, u, panel);
        // u.stopped = true;

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame("Main");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocation(800, 0);

                    panel.addKeyListener(new Navigation(panel, info));
                    Pointer pointer = new Pointer(panel, info);
                    panel.addMouseListener(pointer);
                    panel.addMouseMotionListener(pointer);

                    frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
                    frame.getContentPane().add(info.label);
                    frame.getContentPane().add(panel);
                    frame.pack();
                    frame.setVisible(true);
                }
            });

        // int pool = config.threads();
        // int pool = 2;
        // for (int i = 0; i < pool; i++) {
        //     new Thread().start();
        // }
        new Ticker(u, panel, info).run();
    }
}
