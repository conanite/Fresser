package gol.behaviours.fission;

import gol.*;
import gol.behaviours.Fission;

public class EnthusiasticFission {
    public static final Gene gene = new Gene() {
            public void install(Organism org) {
                Fission f = (Fission)org.blackboard.get(Fission.name);
                if (f == null) { return; }

                double p = 1.0 - f.fissionProbability;

                f.fissionProbability = 1.0 - (p * 0.8);
            }
        };
}
