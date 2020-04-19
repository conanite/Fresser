package gol.behaviours;

import java.util.*;
import gol.*;

public class GatherCandidatePrey extends Eat implements Behaviour {
    public String toString() {
        return "GatherCandidatePrey";
    }

    public void init(Organism org) {
        org.behaviours.add(this);
    }

    public void tick(Organism org) {
        // if (org.reach.size() < 1) { return; }

        // for(Cell c : org.reach) {
        //     if (c.organism != null) {
        //         org.candidatePrey.add(c.organism);
        //     }
        // }
    }
}
