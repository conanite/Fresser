package gol.behaviours;

import java.util.*;
import gol.*;

public class Eat extends SimpleBehaviour {
    public static final String        name = "Eat";

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

    public static final Gene eatMore    = new Tweak<SimpleBehaviour>("EatMore"   , Eat.name, DNA.more, probc);
    public static final Gene eatLess    = new Tweak<SimpleBehaviour>("EatLess"   , Eat.name, DNA.less, probc);
    public static final Gene greedy     = new Tweak<SimpleBehaviour>("Greedy"    , Eat.name, DNA.more, nrgc );
    public static final Gene abstemious = new Tweak<SimpleBehaviour>("Abstemious", Eat.name, DNA.less, nrgc );

    public       double                 attackEnergy       = 0.4;

    public Eat(Organism org) {
        super(org);
        prob = 0.5;
    }

    public String toString() {
        return name + "(p=" + nf1.format(prob) + " e=" + nf1.format(energyShare) + ")";
    }

    public void tick() {
        boolean doit = org.random.nextDouble() < prob;
        if (!doit) return;

        eat((Organism)org.blackboard.get("food"));

        // Cell cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());

        // if (cell.organism == null) {
        //     cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());
        // }

        // if (cell.organism == null) {
        //     cell = org.cell.pickANeighbour(org.reachLength, org.random.nextDouble());
        // }

        // if (cell.organism == null) {
        //     return;
        // }

        // eat(cell.organism);
    }

    public void eat(Organism prey) {
        if (prey == org || prey == null || prey.dead) return;

        prey.healthCheck();

        if (prey.dead || prey.energy <= 0) return;

        double max0 = org.energy  * energyShare;
        double max1 = prey.energy;

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
