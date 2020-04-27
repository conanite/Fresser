package gol.painter;

import gol.*;

import gol.behaviours.Fission;

public class FissionPainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        Organism org = c.getOrganism();
        if (org == null) return null;

        Fission e = Fission.get(org);
        if (e == null) return ZERO;

        return e.amount();
    }
}
