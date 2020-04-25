package gol.painter;

import gol.*;

public class InverseAgePainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        Organism org = c.getOrganism();
        if (org == null) return null;

        return (double)(org.universe.age - org.age);
    }
}
