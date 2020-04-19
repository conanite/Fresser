package gol.behaviours;

import java.util.*;
import gol.*;

public class HuntStranger extends Approach implements Behaviour {
    public String toString() { return "HuntStranger"; }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // if (org.random.nextDouble() > org.stockiness || org.reachLength < 1) return;

        // double strangeness = -Double.MAX_VALUE; // lower is tastier
        // Organism strangest = null;

        // List<Organism> visible = org.visibleOrganisms();
        // if (visible.isEmpty()) return;

        // Collections.shuffle(visible);
        // Collections.sort(visible, new StrangenessComparator(org));

        // approach(org, strangest);
    }
}
