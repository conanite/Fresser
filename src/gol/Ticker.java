package gol;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Ticker implements Runnable {
    public final Universe u;
    public final JPanel panel;
    public final Info info;

    public Ticker(Universe u, JPanel panel, Info info) {
        this.u        = u;
        this.panel    = panel;
        this.info     = info;
    }

    public void run() {
        try {
            while(true) {
                for (int i = 0 ; i < u.organisms.size() ; i++) {
                    while (u.paused) Thread.sleep(50);

                    Organism item = u.organisms.get(i);
                    if (item != null) { item.pretick(); }
                }

                for (int i = 0 ; i < u.organisms.size() ; i++) {
                    while (u.paused) Thread.sleep(50);

                    Organism item = u.organisms.get(i);
                    if (item != null) { item.tick(); }
                }

                for (int i = 0 ; i < u.organisms.size() ; i++) {
                    while (u.paused) Thread.sleep(50);

                    Organism item = u.organisms.get(i);
                    if (item != null) { item.posttick(); }
                }
                u.nextGeneration();
            }
        } catch (InterruptedException e) { }
    }
}
