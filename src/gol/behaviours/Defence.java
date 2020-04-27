package gol.behaviours;

import java.util.*;
import gol.*;

public class Defence extends SimpleBehaviour {
    public static final String        name = "Defence";

    public static Defence get(Organism org) {
        return (Defence)org.blackboard.get(Defence.name);
    }

    public static final Gene gene = new Gene() {
            public String name() { return Defence.name; }
            public void install(Organism org) {
                Defence b = Defence.get(org);
                if (b != null) { return; }
                b = new Defence(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    public static final Gene defendMore   = new Tweak<SimpleBehaviour>("DefendMore"   , Defence.name, DNA.more, nrgc);
    public static final Gene defendLess   = new Tweak<SimpleBehaviour>("DefendLess"   , Defence.name, DNA.less, nrgc);

    public double defendEnergy = 0.0;


    public Defence(Organism org) {
        super(org);
        this.energyShare = 0.7;
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
        //     org.addEnergy("contribution to Defence fund", -up);
        // }
    }
}
