package gol.painter;

import gol.*;

import gol.behaviours.Attack;

public class AttackPainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        Organism org = c.getOrganism();
        if (org == null) return null;

        Attack e = Attack.get(org);
        if (e == null) return null;

        return e.energyShare;
    }
}
