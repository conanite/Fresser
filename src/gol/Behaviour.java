package gol;

import java.text.DecimalFormat;

public interface Behaviour {
    public static final DecimalFormat nf2 = new DecimalFormat("#.##");
    public static final DecimalFormat nf1 = new DecimalFormat("#.#" );
    default void tick()     { };
}
