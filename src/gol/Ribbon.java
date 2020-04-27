package gol;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Ribbon implements Runnable, Global {
    public final int        id;
    public final Cell[]     cells;
    public final Universe   universe;
    public       String     state        = "initial";
    public       int        births       = 0;
    public       int        deaths       = 0;
    public       int        total        = 0;

    public Ribbon(int id, Universe u, int columns) {
        this.id         = id;
        this.universe   = u;

        int start   = id * columns;
        int finish  = (id + 1) * columns;
        int i       = 0;

        this.cells = new Cell[columns * u.edge];

        for (int y = start; y < finish; y++) {
            for (int x = 0; x < u.edge; x++) {
                cells[i]        = u.cells[y][x];
                cells[i].ribbon = this;
                i++;
            }
        }
    }

    public void reset() {
        births       = 0;
        deaths       = 0;
        total        = 0;
    }

    public synchronized void run() {
        try {
            while(true) {
                while(state != "tick") { wait(); }

                for (Cell cell : cells) {
                    cell.energy /= 2.0;
                    cell.energy += universe.tick_energy;
                }

                for (Cell cell : cells) {
                    cell.tick();
                    if (cell.alive()) { total++; }
                }

                state = "tick-done";
                notifyAll();
            }
        } catch (InterruptedException e) {
            throw new Error("interrupted");
        }
    }
}
