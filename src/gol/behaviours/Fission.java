package gol.behaviours;

import java.util.*;

import gol.*;

public class Fission extends SimpleBehaviour {
    public static final String        name = "Fission";
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

    public static final Gene fissionMore    = new Tweak<SimpleBehaviour>("FissionMore"   , Fission.name, DNA.more, probc);
    public static final Gene fissionLess    = new Tweak<SimpleBehaviour>("FissionLess"   , Fission.name, DNA.less, probc);
    public static final Gene fissionBigger  = new Tweak<SimpleBehaviour>("FissionBigger" , Fission.name, DNA.more, nrgc );
    public static final Gene fissionSmaller = new Tweak<SimpleBehaviour>("FissionSmaller", Fission.name, DNA.less, nrgc );

    public       int                    fertilityCycle = 1;

    public Fission(Organism org) {
        super(org);
    }

    public String toString() {
        return name + "(p=" + nf1.format(prob) + " e=" + nf1.format(energyShare) + ")";
    }

    public void tick() {
        boolean doit = org.random.nextDouble() < prob;
        if (!doit) return;

        doit = org.age % fertilityCycle == 0;
        if (!doit) return;

        Cell cell = org.cell.pickANeighbour(org.universe.reach_length, org.random.nextDouble());

        if (cell.organism != null) {
            cell = org.cell.pickANeighbour(org.universe.reach_length, org.random.nextDouble());
        }

        if (cell.organism != null) {
            cell = org.cell.pickANeighbour(org.universe.reach_length, org.random.nextDouble());
        }

        if (cell.organism != null) {
            return;
        }

        Organism baby        = new Organism(org.universe, null, DNA.mutate(org.random, org.genes), org.random);
        baby.food_colour     = DNA.mutate(org.random, org.food_colour);
        baby.my_colour       = DNA.mutate(org.random, org.my_colour);
        baby.predator_colour = DNA.mutate(org.random, org.predator_colour);

        double energy = org.energy * energyShare;

        org.addEnergy("conception as parent", energy * -1.0);
        baby.addEnergy("conception as baby", energy * 0.6); // not the full share, this is the cost of doing business

        org.addEnergy("cost of ejecting baby " + baby + " to " + cell.coordinate, -cell.distanceSq(org.cell));
        cell.organism = baby;
        baby.cell     = cell;

        org.universe.addBaby(baby);
    }

}
