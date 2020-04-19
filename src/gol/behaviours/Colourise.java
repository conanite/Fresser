package gol.behaviours;

import java.util.*;
import gol.*;

public class Colourise implements Behaviour {
    public final String who;
    public final double r;
    public final double g;
    public final double b;

    public Colourise(String who, double r, double g, double b) {
        this.who    = who;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public String toString() {
        return who + "/" + r + "," + g + "," + b;
    }

    public void init(Organism org) {
    }

    public void tick(Organism org) {
    }
}
