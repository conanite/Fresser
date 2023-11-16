package gol.painter;

import gol.*;

import gol.behaviours.DefenceB;

public class DefendBPainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        Organism org = c.getOrganism();
        if (org == null) return null;

        DefenceB e = DefenceB.get(org);
        if (e == null) return null;

        return e.energyShare;
    }
}
