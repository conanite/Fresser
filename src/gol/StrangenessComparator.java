package gol;

import java.util.*;

public class StrangenessComparator implements Comparator<Organism> {
    public Organism eater;

    public StrangenessComparator(Organism eater) {
        this.eater = eater;
    }

    public int compare(Organism a, Organism b) {
        double sa = eater.visualDistanceSq(a);
        double sb = eater.visualDistanceSq(b);
        return Double.compare(sb, sa); // reverse items because further away = more stranger
    }
}
