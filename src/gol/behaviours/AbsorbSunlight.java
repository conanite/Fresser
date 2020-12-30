package gol.behaviours;

import java.util.*;
import gol.*;

public class AbsorbSunlight extends SimpleBehaviour {
    public static final String        name = "AbsorbSunlight";

    public static AbsorbSunlight get(Organism org) {
        return (AbsorbSunlight)org.blackboard.get(AbsorbSunlight.name);
    }

    public static final Gene gene = new Gene() {
            public String name() { return "AbsorbSunlight"; }
            public void install(Organism org) {
                AbsorbSunlight b = AbsorbSunlight.get(org);
                if (b != null) { return; }
                b = new AbsorbSunlight(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    public static final Gene growMore   = new Tweak<SimpleBehaviour>("GrowMore"   , AbsorbSunlight.name, DNA.more, probc);
    public static final Gene growLess   = new Tweak<SimpleBehaviour>("GrowLess"   , AbsorbSunlight.name, DNA.less, probc);
    public static final Gene growFaster = new Tweak<SimpleBehaviour>("GrowFaster" , AbsorbSunlight.name, DNA.more, nrgc );
    public static final Gene growSlower = new Tweak<SimpleBehaviour>("GrowSlower" , AbsorbSunlight.name, DNA.less, nrgc );


    public AbsorbSunlight(Organism org) {
        super(org);
    }

    public String toString() {
        return name + "(p=" + nf1.format(prob) + " e=" + nf1.format(energyShare) + ")";
    }

    public String inspect() {
        return name + "(p=" + nf2.format(prob) + " e=" + nf2.format(energyShare) + ")";
    }

    public void tick() {
        Cell cell = org.cell;

        if (cell.energy < 1.0) return;

        boolean doit = org.rand() < prob;
        if (!doit) return;

        double avail   = cell.energy * energyShare;
        double already = org.energy * energyShare;

        double taking = (avail < already) ? avail : already; // take the smaller of me vs you

        cell.energy         -= taking;

        org.addEnergy("absorb energy from " + cell.coordinate, taking);
    }
}
