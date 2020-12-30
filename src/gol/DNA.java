package gol;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Color;
import gol.behaviours.*;

public class DNA {
    public static final Changes more = (p, by) -> 1.0 - ((1.0 - p) * by);
    public static final Changes less = (p, by) -> p * by;

    public static final List<Gene> available = new ArrayList<Gene>();
    public static final Set<String> names = new HashSet<String>();

    public static int mutations    = 0;
    public static int insertions   = 0;
    public static int deletions    = 0;
    public static int duplications = 0;
    public static int replacements = 0;

    // phlogiston
    // pneuma / prana
    // prana / apana / udana / samana / vyana
    // monad
    // dark matter
    // miasma
    // alkahest
    // alicorn
    // coronium
    // omnium
    // panacea

    static void addGene(Gene g) {
        available.add(g);
        names.add(g.name());
    }

    public static void init() {
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);

        addGene(Fission.gene);
        addGene(Fission.gene);
        addGene(Fission.fissionMore   );
        addGene(Fission.fissionMore   );
        addGene(Fission.fissionMore   );
        addGene(Fission.fissionMore   );
        addGene(Fission.fissionLess   );
        addGene(Fission.fissionLess   );
        addGene(Fission.fissionBigger );
        addGene(Fission.fissionBigger );
        addGene(Fission.fissionBigger );
        addGene(Fission.fissionBigger );
        addGene(Fission.fissionSmaller);
        addGene(Fission.fissionSmaller);

        addGene(Eat.gene);
        addGene(Eat.gene);
        addGene(Eat.gene);
        addGene(Eat.gene);
        addGene(Eat.gene);
        addGene(Eat.gene);
        addGene(Eat.gene);
        addGene(Eat.gene);
        addGene(Eat.eatMore);
        addGene(Eat.eatMore);
        addGene(Eat.eatLess);
        addGene(Eat.eatLess);
        addGene(Eat.greedy);
        addGene(Eat.greedy);
        addGene(Eat.abstemious);
        addGene(Eat.abstemious);

        addGene(AbsorbSunlight.growMore);
        addGene(AbsorbSunlight.growMore);
        addGene(AbsorbSunlight.growMore);
        addGene(AbsorbSunlight.growMore);
        addGene(AbsorbSunlight.growLess);
        addGene(AbsorbSunlight.growLess);
        addGene(AbsorbSunlight.growFaster);
        addGene(AbsorbSunlight.growFaster);
        addGene(AbsorbSunlight.growFaster);
        addGene(AbsorbSunlight.growFaster);
        addGene(AbsorbSunlight.growSlower);
        addGene(AbsorbSunlight.growSlower);

        addGene(FindFood.gene);
        addGene(FindFood.gene);
        // addGene(FindFood.gene);
        // addGene(FindFood.gene);
        addGene(FindFood.avoidFamilyMore);
        addGene(FindFood.avoidFamilyMore);
        addGene(FindFood.avoidFamilyLess);
        addGene(FindFood.avoidFamilyLess);

        addGene(Attack.gene);
        addGene(Attack.gene);
        addGene(Attack.gene);
        addGene(Attack.attackMore);
        addGene(Attack.attackLess);

        addGene(Defence.gene);
        addGene(Defence.gene);
        addGene(Defence.gene);
        addGene(Defence.defendMore);
        addGene(Defence.defendLess);

    }

    public static Gene nextLetter(SplittableRandom random) {
        return available.get(random.nextInt(available.size()));
    }

    public static double mutateColorChannel(SplittableRandom random, double cc) {
        double change = 1.0 + (0.05 * random.nextDouble() * random.nextDouble() * random.nextDouble());
        if (random.nextBoolean()) { // going up
            return 1.0 - ((1.0 - cc) / change);
        } else { // going down
            return cc / change;
        }
    }

    public static Color mutate(SplittableRandom random, Color color) {
        float r = (float)mutateColorChannel(random, color.getRed()  / 255.0);
        float g = (float)mutateColorChannel(random, color.getGreen()/ 255.0);
        float b = (float)mutateColorChannel(random, color.getBlue() / 255.0);

        return new Color(r,g,b, 1.0f);
    }

    public static Gene[] mutate(SplittableRandom random, Gene[] gene) {
        mutations++;
        double     prob = 0.0008;
        double        r = random.nextDouble();
        List<Gene> newg = new ArrayList<Gene>();

        for(int i = 0; i < gene.length; i++) {
            if (r < prob) {        // random replacement
                replacements++;
                newg.add(nextLetter(random));
            } else if (r < (2.0 * prob)) { // insertion
                insertions++;
                newg.add(gene[i]);
                newg.add(nextLetter(random));
            } else if (r < (3.0 * prob)) { // duplication
                duplications++;
                newg.add(gene[i]);
                newg.add(gene[i]);
            } else if (r < (5.0 * prob)) { // deletion (double prob to compensate for insertion/duplication)
                deletions++;
            } else {
                newg.add(gene[i]); // faithful copy
            }
        }

        return newg.toArray(new Gene[newg.size()]);
    }

    public static Map<String, Integer> stats() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("mutations"   , mutations);
        result.put("replacements", replacements);
        result.put("insertions"  , insertions);
        result.put("duplications", duplications);
        result.put("deletions"   , deletions);
        return result;
    }


    public static Gene[] predefined(SplittableRandom random, int n) {
        return new Gene[] {
            AbsorbSunlight.gene,
            AbsorbSunlight.growMore,
            AbsorbSunlight.growMore,
            AbsorbSunlight.growFaster,
            AbsorbSunlight.growFaster,
            Fission.gene,
            Fission.fissionMore,
            Fission.fissionMore,
            Fission.fissionMore,
            Fission.fissionMore,
            Fission.fissionBigger,
            Fission.fissionBigger,
            FindFood.gene,
            FindFood.gene,
            FindFood.gene,
            FindFood.avoidFamilyMore,
            FindFood.avoidFamilyMore,
            FindFood.avoidFamilyMore,
            Eat.gene,
            Eat.gene,
            Eat.gene,
            Eat.eatMore,
            Eat.eatMore,
            Eat.eatMore,
            Eat.eatMore,
            Eat.greedy,
            Eat.greedy,
            Eat.greedy,
            Eat.greedy,
            Attack.gene,
            Attack.gene,
            Attack.attackMore,
            Attack.attackMore,
            Attack.attackLess,
            Defence.gene,
            Defence.gene,
            Defence.defendMore,
            Defence.defendMore,
            Defence.defendLess,
        };

    }

    public static Gene[] randomGenes(SplittableRandom random, int n) {
        if (random.nextDouble() < 0.0001) {
            return predefined(random, n);
        }

        Gene[] gene = new Gene[n];
        // gene[0] = Defence.gene;

        for (int i = 0 ; i < n ; i++) {
            gene[i] = nextLetter(random);
        }

        return gene;
    }

    public static Map<String, Integer> stats(Map<String, Integer> accum, List<Behaviour> behaviours) {
        for (Behaviour b : behaviours) {
            Integer c = accum.get(b.toString());
            if (c == null) { c = 0; }
            accum.put(b.toString(), c + 1);
        }
        return accum;
    }

    public static Map<String, Integer> stats(Map<String, Integer> accum, Gene[] genes) {
        for (Gene g : genes) {
            String n = g.name();
            if (n == null) { throw new RuntimeException("no name for gene " + g); }
            Integer c = accum.get(n);
            if (c == null) { c = 0; }
            accum.put(n, c + 1);
        }
        return accum;
    }
}
