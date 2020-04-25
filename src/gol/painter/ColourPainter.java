package gol.painter;

import java.awt.Color;

import gol.*;

public class ColourPainter implements CellPainter {

    public void prepare(Universe u) {}

    public Color colourFor(Cell cell) {
        Organism org = cell.getOrganism();
        if (org == null) return null;
        return org.my_colour;
    }
}
