package gol.painter;

import gol.*;
import gol.behaviours.AbsorbSunlight;

public class AbsorbSunlightPainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        Organism org = c.getOrganism();
        if (org == null) return null;

        AbsorbSunlight e = AbsorbSunlight.get(org);
        if (e == null) return null;

        return e.amount();
    }
}
