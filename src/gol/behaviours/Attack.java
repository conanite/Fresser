package gol.behaviours;

import java.util.*;
import gol.*;

public class Attack extends SimpleBehaviour {
    public static final String        name = "Attack";

    public static Attack get(Organism org) {
        return (Attack)org.blackboard.get(Attack.name);
    }

    public static final Gene gene = new Gene() {
            public String name() { return Attack.name; }
            public void install(Organism org) {
                Attack b = Attack.get(org);
                if (b != null) { return; }
                b = new Attack(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    public static final Gene attackMore   = new Tweak<SimpleBehaviour>("AttackMore"   , Attack.name, DNA.more, nrgc);
    public static final Gene attackLess   = new Tweak<SimpleBehaviour>("AttackLess"   , Attack.name, DNA.less, nrgc);

    private double attackEnergy = 0.0;
    private Organism attacked;

    public Attack(Organism org) {
        super(org);
        this.energyShare = 0.4;
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
        //     this.attackEnergy += up;
        //     org.addEnergy("contribution to attack fund", -up);
        // }
    }

    public static Organism attack(Organism attacker, Organism defender) {
        Attack  a = Attack.get(attacker);
        Defence d = Defence.get(defender);

        if (d == null) { return defender; } // no defence : free lunch
        if (a == null) { return null;     } // no attack : no lunch

        if (a.attacked == defender) { return defender; } // we've done this already and attacker won

        double ae = attacker.random.nextDouble() * a.energyShare;
        double de = defender.random.nextDouble() * d.energyShare;

        if (de >= ae) { // defence is bigger : wins
            attacker.energy -= ae * attacker.energy;
            defender.energy -= ae * defender.energy;
            a.attacked      = null;

        } else { // attack is bigger : wins
            attacker.energy -= de * attacker.energy;
            defender.energy -= de * defender.energy;
            a.attacked      = defender;
        }

        return a.attacked;
    }
}
