package gol.behaviours;

import java.util.*;
import gol.*;

public class AbsorbSunlight extends AbsorbCellEnergy implements Behaviour {
    public static final Gene gene = new Gene() {
            public void install(Organism org) {
                org.behaviours.add(new AbsorbSunlight(org));
            }
        };

    public AbsorbSunlight(Organism org) {
        super(org);
    }

    public String toString() {
        return "AbsorbSunlight";
    }

    public void init(Organism org) {
        // org.behaviours.add(this);
    }

    public void tick() {
        absorbEnergyFrom(org.cell);
    }
}
