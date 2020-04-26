package gol.behaviours;

import java.util.*;
import gol.*;

public class FindFood extends SimpleBehaviour {
    public static final String        name = "FindFood";
    public static final String        tmp  = "FindFoodGeneInWaiting";

    public static FindFood get(Organism org) {
        return (FindFood)org.blackboard.get(FindFood.name);
    }

    public static final Gene gene = new Gene() {
            public String name() { return FindFood.name; }
            public void install(Organism org) {
                Eat eat = Eat.get(org);
                if (eat == null) {
                    org.blackboard.put(FindFood.tmp, this);
                    return;
                }

                FindFood b = FindFood.get(org);
                if (b != null) { return; }
                b = new FindFood(org, eat);
                org.behaviours.add(b);
                org.blackboard.put(name(), b);
                eat.finder = b;
            }
        };

    static final PropertyChanger<FindFood> fussc = (b, changer) -> b.familyThreshold = changer.change(b.familyThreshold, 0.8);

    public static final Gene avoidFamilyMore = new Tweak<FindFood>("avoidFamilyMore"   , name, DNA.more, fussc);
    public static final Gene avoidFamilyLess = new Tweak<FindFood>("avoidFamilyLess"   , name, DNA.less, fussc);

    private final Eat eat;
    public double familyThreshold = 0.02;

    public FindFood(Organism org, Eat eat) {
        super(org);
        this.eat = eat;
    }

    public String toString() {
        return name + "(ft=" + nf1.format(familyThreshold) + ")";
    }

    public String inspect() {
        return name + "(ft=" + nf1.format(familyThreshold) + " eat=" + eat + ")";
    }

    public void tick() {
        Organism prey = eat.food;

        if (prey != null && prey.alive()) return;

        for (int i = 0; i < 3; i++) {
            prey = findNeighbour(org);
            if (prey != null && !family(prey)) {
                eat.food = prey;
                return;
            }
        }
    }

    boolean family(Organism prey) {
        return prey.colourdsq(org.my_colour, prey.my_colour) < familyThreshold;
    }

    Organism findNeighbour(Organism org) {
        Cell cell = org.cell.pickANeighbour(org.universe.reach_length, org.random.nextDouble());

        if (cell.getOrganism() == null) {
            cell = org.cell.pickANeighbour(org.universe.reach_length, org.random.nextDouble());
        }

        if (cell.getOrganism() == null) {
            cell = org.cell.pickANeighbour(org.universe.reach_length, org.random.nextDouble());
        }

        return cell.getOrganism();
    }
}
