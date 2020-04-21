package gol.behaviours.fission;

import gol.*;
import gol.behaviours.Fission;

public class DefaultFission implements Fission.FissionBehaviour {
    public static final String        name = "DefaultFission";
    public static final Gene          gene = new Gene() {
            public String name() { return "DefaultFission"; }
            public void install(Organism org) {
                Fission f = (Fission)org.blackboard.get(Fission.name);
                if (f == null) { return; }

                DefaultFission b = new DefaultFission(org);
                f.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    public final Organism org;

    public DefaultFission(Organism org) {
        this.org = org;
    }


    public Fission.Decision decide() {
        return Fission.Decision.yes;
    }

    public double energy(double initial) {
        return initial;
    }
}
