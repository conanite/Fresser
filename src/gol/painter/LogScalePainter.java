package gol.painter;

import java.awt.Color;
import gol.*;

public abstract class LogScalePainter implements CellPainter {
    public static final Double ZERO = new Double(0.0);
    protected double log_diff;
    protected double max;
    protected double min;

    public static float getLogScaleValue(double min, double v, double log_gap) {
        return (float)(Math.log(1.0 + v - min) / log_gap);
    }

    public static float getLinearScaleValue(double min, double v, double max) {
        return (float)((v - min) / (max - min));
    }

    public static Color grey(float d) {
        if (d < 0) d = 0;
        if (d > 1) d = 1;
        return new Color(d, d, d);
    }

    protected abstract Double valueFor(Cell c);

    protected double get_log_diff(Universe u) {
        return Math.log(1.0d + max - min);
    }

    public void prepare(Universe u) {
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;

        for(Cell cell : u.allCells) {
            Double v = valueFor(cell);
            if (v != null) {
                if (v < min) min = v;
                if (v > max) max = v;
            }
        }

        log_diff = get_log_diff(u);
    }

    public Color colourFor(Cell c) {
        Double v = valueFor(c);
        if (v == null) return null;

        float scale = getLogScaleValue(min, v, log_diff);
        return(grey(scale));
    }
}
