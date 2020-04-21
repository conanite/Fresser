package gol.behaviours;

import java.util.*;
import gol.*;

public class EatEverything extends Eat implements Behaviour {
    public String toString() {
        return "EatEverything";
    }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // if (org.leafiness > 0.5) return;
        for(Cell target : org.reach) {
            if (target.organism != null) {
                eat(org, target.organism);
            }
        }
    }
}
