package gol.painter;

import gol.*;

public class EnergyPainter extends LogScalePainter {
    protected Double valueFor(Cell cell) {
        Organism org = cell.getOrganism();
        if (org == null) return null;
        return org.energy;
    }
}
