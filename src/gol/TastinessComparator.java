package gol;

import java.util.*;

public class TastinessComparator implements Comparator<Organism> {
    public Organism eater;

    public TastinessComparator(Organism eater) {
        this.eater = eater;
    }

    public int compare(Organism a, Organism b) {
        double ta = eater.tastiness(a);
        double tb = eater.tastiness(b);
        return Double.compare(ta, tb);
    }
}
