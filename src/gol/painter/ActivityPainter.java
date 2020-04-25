package gol.painter;

import gol.*;

public class ActivityPainter extends LogScalePainter {
    protected double get_log_diff(Universe u) {
        return Math.log(1.0d + u.age - min);
    }

    protected Double valueFor(Cell c) {
        return (double)c.ageLastChange;
    }
}
