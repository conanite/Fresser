package gol.behaviours;

import java.util.*;
import java.text.DecimalFormat;
import java.awt.Color;

import gol.*;
import gol.behaviours.fission.*;

public class Fission implements Behaviour {
    public static final String        name = "Fission";
    public static final DecimalFormat nf   = new DecimalFormat("#.##");
    public static final Gene          gene = new Gene() {
            public String name() { return "Fission"; }
            public void install(Organism org) {
                Fission b = new Fission(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    public enum Decision { yes, no, whatever }

    public interface FissionBehaviour {
        Decision decide();
        double  energy(double initial);
    }

    public final Organism               org;
    public final List<FissionBehaviour> behaviours         = new ArrayList<FissionBehaviour>();
    public       double                 fissionProbability = 0.1;
    public       double                 energyShare        = 0.1;
    public       int                    fertilityCycle     = 1;

    public Fission(Organism org) {
        this.org = org;
    }

    public String toString() {
        // String s = name + "\n";
        // for(FissionBehaviour b : behaviours) {
        //     s += "    " + b.toString() + "\n";
        // }
        // return s;
        return name + "(p=" + nf.format(fissionProbability) + " e=" + nf.format(energyShare) + ")";
    }

    public void tick() {
        // Decision d = Decision.whatever;

        // for (FissionBehaviour b : behaviours) {
        //     d = b.decide();
        //     if (d == Decision.yes) { break;  }
        //     if (d == Decision.no ) { return; }
        // }

        // if (d != Decision.yes) { return; }

        Cell cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());

        if (cell.organism != null) {
            cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());
        }

        if (cell.organism != null) {
            cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());
        }

        if (cell.organism != null) {
            return;
        }

        boolean doit = org.random.nextDouble() < fissionProbability;
        if (!doit) return;

        doit = org.age % fertilityCycle == 0;
        if (!doit) return;

        Organism baby        = new Organism(org.universe, null, DNA.mutate(org.random, org.genes), org.random);
        baby.food_colour     = DNA.mutate(org.random, org.food_colour);
        baby.my_colour       = DNA.mutate(org.random, org.my_colour);
        baby.predator_colour = DNA.mutate(org.random, org.predator_colour);

        double energy = org.energy * energyShare;
        // for (FissionBehaviour b : behaviours) {
        //     energy = b.energy(energy);
        // }

        org.addEnergy("conception as parent", energy * -1.0);
        baby.addEnergy("conception as baby", energy * 0.8);

        org.addEnergy("cost of ejecting baby " + baby + " to " + cell.coordinate, -cell.distanceSq(org.cell));
        cell.organism = baby;
        baby.cell     = cell;

        // try { org.universe.addOrganism(baby); } catch (InterruptedException e) {}
        org.universe.addBaby(baby);

        // if (org.watching ) org.addHistory(org.toString()   + " gave birth to " + baby.toString());
        // if (baby.watching) baby.addHistory(baby.toString() + " was born of "   + org.toString() + " with energy " + baby.energy);
    }

}
