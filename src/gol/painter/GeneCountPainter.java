package gol.painter;

import gol.*;

public class GeneCountPainter extends LogScalePainter {
    protected Double valueFor(Cell cell) {
        Organism org = cell.getOrganism();
        if (org == null) return null;
        return (double)org.genes.length;
    }
}
