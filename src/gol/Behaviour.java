package gol;

import java.text.DecimalFormat;

public interface Behaviour extends Global {
    default void tick()     { };
    default String inspect() { return toString(); }
}
