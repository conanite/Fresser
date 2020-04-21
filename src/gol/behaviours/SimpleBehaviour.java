package gol.behaviours;

import java.util.*;
import gol.*;

public abstract class SimpleBehaviour implements Behaviour {
    public static final Gene          gene = new Gene() {
            public String name() { return "Fission"; }
            public void install(Organism org) {
                Fission b = (Fission)org.blackboard.get(Fission.name);
                if (b != null) { return; }
                b = new Fission(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    static final PropertyChanger<SimpleBehaviour> probc = (b, changer) -> b.prob        = changer.change(b.prob       , 0.8);
    static final PropertyChanger<SimpleBehaviour> nrgc  = (b, changer) -> b.energyShare = changer.change(b.energyShare, 0.8);

    public final Organism               org;
    public       double                 prob           = 0.1;
    public       double                 energyShare    = 0.1;

    public SimpleBehaviour(Organism org) {
        this.org = org;
    }
}
