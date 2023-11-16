package gol.painter;

import gol.*;

import gol.behaviours.AttackB;

public class AttackBPainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        Organism org = c.getOrganism();
        if (org == null) return null;

        AttackB e = AttackB.get(org);
        if (e == null) return null;

        return e.energyShare;
    }
}
