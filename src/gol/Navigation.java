package gol;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Navigation extends KeyAdapter implements Global {
    private ToroidalGridPanel panel;
    private Info info;

    public Navigation(ToroidalGridPanel panel, Info info) {
        this.panel = panel;
        this.info  = info;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP) {
            panel.scroll(1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN) {
            panel.scroll(-1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT) {
            panel.scroll(0, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT) {
            panel.scroll(0, -1);
        } else if (e.getKeyChar() == 'b') {
            if (panel.bgColour == "dark") {
                panel.bgColour = "medium";
            } else if (panel.bgColour == "medium") {
                panel.bgColour = "bright";
            } else {
                panel.bgColour = "dark";
            }
        } else if (e.getKeyChar() == ' ') {
            panel.universe.stepped = true;
        } else if (e.getKeyChar() == 'g') {
            o.println(MapToString.toString(DNA.stats()));
        } else if (e.getKeyChar() == 'G') {
            // for (Organism org : panel.universe.organisms) {
            //     o.println(org.behaviours);
            // }
        } else if (e.getKeyChar() == 'm') {
            panel.universe.requestStats();
        } else if (e.getKeyChar() == 'o') {
            o.println(panel.universe.population());
        } else if (e.getKeyChar() == 's') {
            panel.universe.stopped    = !panel.universe.stopped;
        } else if (e.getKeyChar() == 'v') {
            panel.colourify = panel.colourify.next();
            o.println("View is " + panel.colourify);
        } else if (e.getKeyChar() == 'V') {
            panel.colourify = panel.colourify.previous();
            o.println("View is " + panel.colourify);
        } else if (e.getKeyChar() == 'R') {
            panel.universe.requestRestart();
        } else if (e.getKeyChar() == 'z') {
            panel.zoomIn();
        } else if (e.getKeyChar() == 'Z') {
            panel.zoomOut();
        } else {
            // o.println("Going Somewhere: " + e.getKeyChar());
        }

        panel.requireRedraw();
        info.update();
    }
}
