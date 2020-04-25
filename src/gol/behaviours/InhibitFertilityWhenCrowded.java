package gol.behaviours;

import java.util.*;
import gol.*;

public class InhibitFertilityWhenCrowded implements Behaviour {
    public String toString() {
        return "InhibitFertilityWhenCrowded";
    }

    public void tick(Organism org) {
        if (org.reach.size() < 1) { return; }

        List<Organism> neighbours = new ArrayList<Organism>();

        for(Cell c : org.reach) if (c.getOrganism() != null) neighbours.add(c.getOrganism());

        double inhibition = 1.0 - ((1.0 * neighbours.size()) / (1.0 * org.reach.size()));

        org.fertility *= inhibition;
    }
}
