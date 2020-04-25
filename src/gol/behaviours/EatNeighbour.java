package gol.behaviours;

import java.util.*;
import gol.*;

public class EatNeighbour extends Eat implements Behaviour {
    public String toString() {
        return "EatNeighbour";
    }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // if (org.leafiness > 0.5) return;

        // org.addEnergy("cost of " + this, (org.energy * -0.1));

        // if (org.reach_length < 2) return;

        // o.println("reachLength is " + org.reachLength);
        // o.println("reach.size() is " + org.reach.size());

        Cell dest = org.cell.pickANeighbour(org.universe.reach_length, org.random.nextDouble());

        eat(org, dest.organism);
    }
}
