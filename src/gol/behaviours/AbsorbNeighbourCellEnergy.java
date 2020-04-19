package gol.behaviours;

import java.util.*;
import gol.*;

public class AbsorbNeighbourCellEnergy extends AbsorbCellEnergy implements Behaviour {
    public String toString() {
        return "AbsorbNeighbourCellEnergy";
    }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // if (org.random.nextDouble() > org.photosynth) return;

        // for (Cell target : org.neighbours()) {
        //     absorbEnergyFrom(org, target);
        // }
    }
}
