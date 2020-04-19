package gol.behaviours;

import java.util.*;
import gol.*;

public class EatSelectively extends Eat implements Behaviour {
    public String toString() {
        return "EatSelectively";
    }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // if (org.leafiness > 0.5) return;
        // for (Organism prey : org.candidatePrey) {
        //     eat(org, prey);
        // }
    }
}
