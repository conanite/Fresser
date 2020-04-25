package gol.painter;

import gol.*;

public class GroundEnergyPainter extends LogScalePainter {
    protected Double valueFor(Cell c) {
        return c.energy;
    }
}
