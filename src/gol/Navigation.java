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
            panel.tgip.scroll(1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN) {
            panel.tgip.scroll(-1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT) {
            panel.tgip.scroll(0, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT) {
            panel.tgip.scroll(0, -1);
        } else if (e.getKeyChar() == 'b') {
            if (panel.tgip.bgColour == "dark") {
                panel.tgip.bgColour = "medium";
            } else if (panel.tgip.bgColour == "medium") {
                panel.tgip.bgColour = "bright";
            } else {
                panel.tgip.bgColour = "dark";
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
            panel.tgip.colourify = panel.tgip.colourify.next();
            o.println("View is " + panel.tgip.colourify);
        } else if (e.getKeyChar() == 'V') {
            panel.tgip.colourify = panel.tgip.colourify.previous();
            o.println("View is " + panel.tgip.colourify);
        } else if (e.getKeyChar() == 'R') {
            panel.universe.requestRestart();
        } else if (e.getKeyChar() == 'z') {
            panel.zoomIn();
        } else if (e.getKeyChar() == 'Z') {
            panel.zoomOut();
        } else {
            // o.println("Going Somewhere: " + e.getKeyChar());
        }

        panel.tgip.requireRedraw();
        info.update();
    }
}
