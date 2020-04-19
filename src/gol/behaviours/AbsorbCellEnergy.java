package gol.behaviours;

import java.util.*;
import gol.*;

public class AbsorbCellEnergy {
    protected final Organism org;

    public AbsorbCellEnergy(Organism org) {
        this.org = org;
    }

    void absorbEnergyFrom(Cell cell) {
        if (cell.energy < 1.0) return;

        Coordinate     relco = cell.relative(org.cell);
        double          dist = relco.lenSq;
        double        taking = 1.0 / (dist < 1.0 ? 1.0 : dist);
        cell.energy         -= taking;

        org.addEnergy("absorb energy from " + cell.coordinate, taking);
    }
}
