package gol.behaviours.fission;

import gol.*;
import gol.behaviours.Fission;

public class ReluctantFission {
    public static final Gene gene = new Gene() {
            public void install(Organism org) {
                Fission f = (Fission)org.blackboard.get(Fission.name);
                if (f == null) { return; }

                double p = f.fissionProbability;

                f.fissionProbability = p * 0.8;
            }
        };
}
