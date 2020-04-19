package gol.behaviours;

import java.util.*;
import gol.*;

public class EjectBaby implements Behaviour {
    public String toString() {
        return "EjectBaby";
    }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // if (org.babies.isEmpty() || org.reachLength < 1) return;

        // eject(org, org.babies.get(0));
    }

}
