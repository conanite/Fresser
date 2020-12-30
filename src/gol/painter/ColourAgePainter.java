package gol.painter;

import java.awt.Color;
import gol.*;

public class ColourAgePainter extends Age {
    public Color colourFor(Cell cell) {
        Double v = valueFor(cell);
        if (v == null) return null;

        Organism org = cell.getOrganism();

        // float scale = getLogScaleValue(min, org.age, log_diff);
        // float scale = getLinearSquaredScaleValue(min, org.age, max);
        float scale = getScaled(min, v, max, log_diff);
        return(Appearance.darkify(org.my_colour, scale));
    }
}
