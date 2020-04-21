package gol.behaviours;

import java.text.DecimalFormat;

import java.util.*;
import gol.*;

public class Eat implements Behaviour {
    public static final String        name = "Eat";
    public static final DecimalFormat nf   = new DecimalFormat("#.##");

    public static final Gene gene = new Gene() {
            public String name() { return "Eat"; }
            public void install(Organism org) {
                Eat b = (Eat)org.blackboard.get(Eat.name);
                if (b != null) { return; }
                b = new Eat(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    static final Changes more = (p, by) -> 1.0 - ((1.0 - p) * by);
    static final Changes less = (p, by) -> p * by;

    static final PropertyChanger<Eat> probc = (eat, changer) -> eat.prob        = changer.change(eat.prob       , 0.8);
    static final PropertyChanger<Eat> nrgc  = (eat, changer) -> eat.energyShare = changer.change(eat.energyShare, 0.8);

    public static final Gene eatMore    = new Tweak<Eat>("EatMore"   , Eat.name, more, probc);
    public static final Gene eatLess    = new Tweak<Eat>("EatLess"   , Eat.name, less, probc);
    public static final Gene greedy     = new Tweak<Eat>("Greedy"    , Eat.name, more, nrgc );
    public static final Gene abstemious = new Tweak<Eat>("Abstemious", Eat.name, less, nrgc );

    public final Organism               org;
    public       double                 prob               = 0.2;
    public       double                 energyShare        = 0.2;
    public       double                 attackEnergy       = 0.2;

    public Eat(Organism org) {
        this.org = org;
    }

    public double getProb()                 { return prob; }
    public void   setProb(double p)         { prob = p;    }
    public double getEnergyShare()          { return energyShare; }
    public void   setEnergyShare(double p)  { energyShare = p;    }

    public String toString() {
        return name + "(p=" + nf.format(prob) + " e=" + nf.format(energyShare) + ")";
    }

    public void tick() {
        boolean doit = org.random.nextDouble() < prob;
        if (!doit) return;

        Cell cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());

        if (cell.organism == null) {
            cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());
        }

        if (cell.organism == null) {
            cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());
        }

        if (cell.organism == null) {
            return;
        }

        eat(cell.organism);
    }

    public void eat(Organism prey) {
        if (prey == org || prey == null || prey.dead) return;

        prey.healthCheck();

        if (prey.dead || prey.energy <= 0) return;

        double max0 = org.energy  * energyShare;
        double max1 = prey.energy * energyShare;

        double taking = (max0 < max1) ? max0 : max1; // take the smaller of me vs you

        if (taking <= 0) return;

        double conversionCost = 0.9;

        prey.addEnergy("eaten by " + org, -taking);
        org.addEnergy("eating " + prey, taking * conversionCost);

        if (Double.isNaN(org.energy)) {
            throw new Error("wrong energy " + org.energy);
        }

        prey.healthCheck();
    }
}
