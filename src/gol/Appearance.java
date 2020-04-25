package gol;

import java.awt.Color;

public class Appearance {
    public static float norm(float f) {
        if (f < 0) return 0;
        if (f > 1) return 1;
        return f;
    }

    public static Color build(float r, float g, float b) {
        r = norm(r);
        g = norm(g);
        b = norm(b);
        return new Color(r,g,b);
    }

    public static Color darkify(Color c, float scale) {
        float r = (float)((c.getRed()   * scale) / 255.0);
        float g = (float)((c.getGreen() * scale) / 255.0);
        float b = (float)((c.getBlue()  * scale) / 255.0);
        return new Color(norm(r),norm(g),norm(b));
    }
}
