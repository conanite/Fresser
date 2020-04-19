package gol;

interface UniverseListener {
    default void universeRestarted() {};
    default void universeTicked()    {};
}
