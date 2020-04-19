package gol.behaviours;

import java.util.*;
import gol.*;

public class RandomMovement implements Behaviour {
    public String toString() {
        return "RandomMovement";
    }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // double mobility = org.stockiness * org.stockiness;

        // if (org.random.nextDouble() > mobility || org.reachLength < 1) return;

        // Cell         dest = org.reach.get(org.random.nextInt(1, org.reach.size()));
        // double          d = dest.distanceSq(org.cell);
        // double energyCost = d;

        // if (energyCost > org.energy) { return; }

        // if (dest.organism == null) {
        //     org.addEnergy("cost of movement to " + dest.coordinate, -energyCost);
        //     dest.organism     = org;
        //     org.cell.organism = null;
        //     org.cell          = dest;
        // }
    }
}
