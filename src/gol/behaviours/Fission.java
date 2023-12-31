package gol.behaviours;

import java.util.*;

import gol.*;

public class Fission extends SimpleBehaviour {
    public static final String        name = "Fission";

    public static Fission get(Organism org) {
        return (Fission)org.blackboard.get(Fission.name);
    }

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

    public String inspect() {
        return name + "(p=" + nf2.format(prob) + " e=" + nf2.format(energyShare) + ")";
    }

    public void tick() {
        boolean doit = org.rand() < prob;
        if (!doit) return;

        doit = org.age % fertilityCycle == 0;
        if (!doit) return;

        Cell cell = org.cell.pickANeighbour(org.universe.reach_length, org.rand());

        if (cell.getOrganism() != null) {
            cell = org.cell.pickANeighbour(org.universe.reach_length, org.rand());
            if (cell.getOrganism() != null) {
                cell = org.cell.pickANeighbour(org.universe.reach_length, org.rand());
                if (cell.getOrganism() != null) {
                    return;
                }
            }

        }


        Organism baby        = new Organism(org.universe, cell, DNA.mutate(cell.random, org.genes));
        baby.food_colour     = DNA.mutate(cell.random, org.food_colour);
        baby.my_colour       = DNA.mutate(cell.random, org.my_colour);
        baby.predator_colour = DNA.mutate(cell.random, org.predator_colour);

        double energy = org.energy * energyShare;

        org.addEnergy("conception as parent", energy * -1.0);
        baby.addEnergy("conception as baby", energy * 0.6); // not the full share, this is the cost of doing business
        baby.newenergy = baby.energy;

        org.addEnergy("cost of ejecting baby " + baby + " to " + cell.coordinate, -cell.distanceSq(org.cell));
        cell.setOrganism(baby);
        baby.cell     = cell;

        // org.universe.addBaby(baby);
    }
}
