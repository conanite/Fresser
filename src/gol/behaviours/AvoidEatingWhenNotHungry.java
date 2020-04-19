package gol.behaviours;

import java.util.*;
import gol.*;

public class AvoidEatingWhenNotHungry implements Behaviour {
    public String toString() {
        return "AvoidEatingWhenNotHungry";
    }

    public void tick(Organism org) {
        // if (org.hungry) { return; }

        // Iterator<Organism> i = org.candidatePrey.iterator();
        // while(i.hasNext()) {
        //     Organism prey = i.next();
        //     if (org.random.nextDouble() < 0.1) { // remove 10% of potential prey
        //         i.remove();
        //     }
        // }
    }
}
