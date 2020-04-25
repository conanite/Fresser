package gol.behaviours;

import java.util.*;
import gol.*;

public class FindFood extends SimpleBehaviour {
    public static final String        name = "FindFood";

    public static final Gene gene = new Gene() {
            public String name() { return FindFood.name; }
            public void install(Organism org) {
                FindFood b = (FindFood)org.blackboard.get(name());
                if (b != null) { return; }
                b = new FindFood(org);
                org.behaviours.add(b);
                org.blackboard.put(name(), b);
            }
        };

    static final PropertyChanger<FindFood> fussc = (b, changer) -> b.familyThreshold = changer.change(b.familyThreshold, 0.8);

    public static final Gene avoidFamilyMore = new Tweak<FindFood>("avoidFamilyMore"   , name, DNA.more, fussc);
    public static final Gene avoidFamilyLess = new Tweak<FindFood>("avoidFamilyLess"   , name, DNA.less, fussc);

    public double familyThreshold = 0.1;

    public FindFood(Organism org) {
        super(org);
    }

    public String toString() {
        return name + "(ft=" + nf1.format(familyThreshold) + ")";
    }

    public String inspect() {
        return name + "(ft=" + nf1.format(familyThreshold) + " eat=" + eat + ")";
    }

    public void tick() {
        Organism prey = (Organism)org.blackboard.get("food");

        if (prey != null && prey.alive()) return;

        for (int i = 0; i < 3; i++) {
            prey = findNeighbour(org);
            if (prey != null && !family(prey)) {
                org.blackboard.put("food", prey);
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
