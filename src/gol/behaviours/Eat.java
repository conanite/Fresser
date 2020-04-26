package gol.behaviours;

import java.util.*;
import gol.*;

public class Eat extends SimpleBehaviour {
    public static final String        name = "Eat";

    public static Eat get(Organism org) {
        return (Eat)org.blackboard.get(Eat.name);
    }

    public static final Gene gene = new Gene() {
            public String name() { return "Eat"; }
            public void install(Organism org) {
                Eat b = Eat.get(org);
                if (b != null) { return; }
                b = new Eat(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);

                Gene ff = (Gene)org.blackboard.get(FindFood.tmp);
                if (ff != null) { ff.install(org); }
            }
        };

    public static final Gene eatMore    = new Tweak<SimpleBehaviour>("EatMore"   , Eat.name, DNA.more, probc);
    public static final Gene eatLess    = new Tweak<SimpleBehaviour>("EatLess"   , Eat.name, DNA.less, probc);
    public static final Gene greedy     = new Tweak<SimpleBehaviour>("Greedy"    , Eat.name, DNA.more, nrgc );
    public static final Gene abstemious = new Tweak<SimpleBehaviour>("Abstemious", Eat.name, DNA.less, nrgc );

    public Organism food;
    public FindFood finder;

    public Eat(Organism org) {
        super(org);
        prob = 0.66;
        energyShare = 0.66;
    }

    public double amount() {
        if (finder == null) {
            return 0.0;
        } else {
            return super.amount() * (1.0 - finder.familyThreshold);
        }
    }

    public String toString() {
        return name + "(p=" + nf1.format(prob) + " e=" + nf1.format(energyShare) + ")";
    }

    public String inspect() {
        if (food != null) {
            return name + "(p=" + nf1.format(prob) + " e=" + nf1.format(energyShare) + " food=" + food + " food address=" + food.cell + ")";
        } else {
            return name + "(p=" + nf1.format(prob) + " e=" + nf1.format(energyShare) + " food=" + food + ")";
        }
    }

    public void tick() {
        boolean doit = org.random.nextDouble() < prob;
        if (!doit) return;

        if (food == org || food == null || food.dead || food.cell == null) {
            food = null;
            return;
        }

        eat(food);
    }

    public void eat(Organism prey) {

        prey = Attack.attack(org, prey);
        if (prey == null) return;

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
