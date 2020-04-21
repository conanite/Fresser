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

        if (org.reachLength < 2) return;

        // System.out.println("reachLength is " + org.reachLength);
        // System.out.println("reach.size() is " + org.reach.size());

        Cell dest = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());

        eat(org, dest.organism);
    }
}
