package gol.behaviours;

import java.util.*;
import gol.*;

public class Approach {
    public void approach(Organism from, Organism towards) {
        if (towards == null || towards == from || towards.dead || from.cell == null || towards.cell == null) return;

        double bestDistance = Double.MAX_VALUE;
        List<Cell> candidates = new ArrayList<Cell>();
        for (Cell c : from.reach) {
            double d = towards.cell.distanceSq(c);
            if ((c.organism == null) && (d <= bestDistance)) {
                if (d < bestDistance) candidates.clear();
                candidates.add(c);
                bestDistance = d;
            }
        }

        if (candidates.isEmpty()) return;
        Collections.shuffle(candidates);

        Cell closest = candidates.get(0);

        double d = closest.distanceSq(from.cell);
        double energyCost = d;

        if (energyCost > from.energy) return;

        from.addEnergy(toString() + " moving to " + closest.coordinate, -energyCost);

        closest.organism  = from;
        from.cell.organism = null;
        from.cell          = closest;
    }
}
