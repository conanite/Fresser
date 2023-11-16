package gol.behaviours;

import java.util.*;
import gol.*;

public class AttackB extends SimpleBehaviour {
    public static final String        name = "AttackB";

    public static AttackB get(Organism org) {
        return (AttackB)org.blackboard.get(AttackB.name);
    }

    public static final Gene gene = new Gene() {
            public String name() { return AttackB.name; }
            public void install(Organism org) {
                AttackB b = AttackB.get(org);
                if (b != null) { return; }
                b = new AttackB(org);
                org.behaviours.add(b);
                org.blackboard.put(b.name, b);
            }
        };

    public static final Gene attackMore   = new Tweak<SimpleBehaviour>("AttackBMore"   , AttackB.name, DNA.more, nrgc);
    public static final Gene attackLess   = new Tweak<SimpleBehaviour>("AttackBLess"   , AttackB.name, DNA.less, nrgc);

    private double attackEnergy = 0.0;
    private Organism attacked;

    public AttackB(Organism org) {
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
        AttackB  a = AttackB.get(attacker);
        DefenceB d = DefenceB.get(defender);

        if (d == null) { return defender; } // no defence : free lunch
        if (a == null) { return null;     } // no attack : no lunch

        if (a.attacked == defender) { return defender; } // we've done this already and attacker won

        double ae = attacker.rand() * a.energyShare;
        double de = defender.rand() * d.energyShare;

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
