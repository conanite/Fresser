package gol.behaviours;

import java.util.*;
import gol.*;

public class DefenceB extends SimpleBehaviour {
    public static final String        name = "DefenceB";

    public static DefenceB get(Organism org) {
        return (DefenceB)org.blackboard.get(DefenceB.name);
    }

    public static final Gene gene = new Gene() {
            public String name() { return DefenceB.name; }
            public void install(Organism org) {
                DefenceB b = DefenceB.get(org);
                if (b != null) { return; }
                b = new DefenceB(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    public static final Gene defendMore   = new Tweak<SimpleBehaviour>("DefendMore"   , DefenceB.name, DNA.more, nrgc);
    public static final Gene defendLess   = new Tweak<SimpleBehaviour>("DefendLess"   , DefenceB.name, DNA.less, nrgc);

    public double defendEnergy = 0.0;


    public DefenceB(Organism org) {
        super(org);
        this.energyShare = 0.6;
    }

    public String toString() {
        return name + "(e=" + nf1.format(energyShare) + ")";
    }

    public String inspect() {
        return name + "(e=" + nf2.format(energyShare) + ")";
    }

    public void tick() {
        // double newenergy = org.newenergy;
        // double up = newenergy * energyShare;
        // if (org.energy > up && up > 0) {
        //     this.defendEnergy += up;
        //     org.addEnergy("contribution to DefenceB fund", -up);
        // }
    }
}
