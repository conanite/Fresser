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

    public static float getSemiLinearScale(double min, double v, double max) {
        return (float)(0.25 + (0.75 * getLinearScaleValue(min, v, max)));
    }

    public static float getLinearSquaredScaleValue(double min, double v, double max) {
        v = getLinearScaleValue(min, v, max);
        return (float)(v * v);
    }

    public static float getChairScale(double min, double v, double max, double log_max_min) {
        // (0.5 + ((((x-11)/9)^2)/2)) * (ln(x-1) / 2.89037)
        double midpoint = (max + min) / 2.0;
        double radius   = (max - min) / 2.0;
        double d_midp   = (v - midpoint) / radius;
        double parabola = 0.75 + ((d_midp * d_midp) / 4.0); // range 0.5 - 1.0, min -> 1.0, max -> 1.0, midpoint -> 0.5
        double log      = getLogScaleValue(min, v, log_max_min);
        return (float)(parabola * log);
    }

    public static float getScaled(double min, double v, double max, double log_max_min) {
        // return getChairScale(min, v, max, log_max_min);
        // return getSemiLinearScale(min, v, max); π
        return getLogScaleValue(min, v, log_max_min);
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

        // float scale = getLogScaleValue(min, v, log_diff);
        // float scale = getLinearSquaredScaleValue(min, v, max);
        float scale = getScaled(min, v, max, log_diff);
        return(grey(scale));
    }
}
