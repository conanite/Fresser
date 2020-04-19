package gol.behaviours;

import java.util.*;
import gol.*;

public class AvoidEatingFamily implements Behaviour {
    public String toString() {
        return "AvoidEatingFamily";
    }

    public void tick(Organism org) {
        // double dmin = 0.2;
        // Iterator<Organism> i = org.candidatePrey.iterator();
        // while(i.hasNext()) {
        //     Organism prey = i.next();
        //     if (org.visualDistanceSq(prey) < dmin) {
        //         i.remove();
        //     }
        // }
    }
}
