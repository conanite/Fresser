package gol;

import java.awt.Color;

public interface CellPainter extends Global {

    void prepare(Universe u);

    Color colourFor(Cell c);
}
