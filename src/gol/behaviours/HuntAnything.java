package gol.behaviours;

import java.util.*;
import gol.*;

public class HuntAnything extends Approach implements Behaviour {
    public String toString() { return "HuntAnything"; }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // if (org.random.nextDouble() > org.stockiness || org.reachLength < 1) return;

        // List<Organism> targets = new ArrayList<Organism>();

        // for(Cell c : org.vision) if (c.organism != null && c.organism != org) targets.add(c.organism);

        // if (targets.isEmpty()) return;

        // approach(org, targets.get(org.random.nextInt(targets.size())));
    }
}
