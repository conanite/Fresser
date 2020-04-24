package gol.behaviours;

import java.util.*;
import gol.*;

public class HuntFood extends Approach implements Behaviour {
    // static int yhunt = 0;
    // static int xhunt = 0;

    public String toString() { return "HuntFood"; }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {

        // if (visible.isEmpty()) return;

        // Collections.shuffle(visible);
        // Collections.sort(visible, new TastinessComparator(org));

        // Organism tastiest = visible.get(0);

        // approach(org, tastiest);
    }
}
