package gol.painter;

import gol.*;

import gol.behaviours.Eat;

public class EatPainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        Organism org = c.getOrganism();
        if (org == null) return null;

        Eat e = Eat.get(org);
        if (e == null) return null;

        return e.amount();
    }
}
