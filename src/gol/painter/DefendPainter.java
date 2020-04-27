package gol.painter;

import gol.*;

import gol.behaviours.Defence;

public class DefendPainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        Organism org = c.getOrganism();
        if (org == null) return null;

        Defence e = Defence.get(org);
        if (e == null) return null;

        return e.energyShare;
    }
}
