package gol;

public interface Gene {
    String name();
    void install(Organism org);

    default double higher(double p, double by) { return 1.0 - ((1.0 - p) * by); }
    default double lower (double p, double by) { return p * by;                 }
}
