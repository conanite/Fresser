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
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);
        addGene(AbsorbSunlight.gene);

        addGene(Fission.gene);
        // addGene(ReluctantFission.gene);
        // addGene(EnthusiasticFission.gene);
        addGene(Fission.fissionMore   );
        addGene(Fission.fissionLess   );
        addGene(Fission.fissionBigger );
        addGene(Fission.fissionSmaller);

        addGene(Eat.gene);
        addGene(Eat.eatMore);
        addGene(Eat.eatLess);
        addGene(Eat.greedy);
        addGene(Eat.abstemious);

        addGene(AbsorbSunlight.growMore);
        addGene(AbsorbSunlight.growLess);
        addGene(AbsorbSunlight.growFaster);
        addGene(AbsorbSunlight.growSlower);


        // WaitBetweenBabies
        // DetectAvailableFood
        // SleepIfNoFood
        // Sleep
        // FamilyStricter
        // FamilyLooser
        // IdentifyFamily
        // ShareWithFamily

        // behaviours.put("Bg0", new Bigger());
        // behaviours.put("Ea", new EatNeighbour());
        // behaviours.put("Es0", new EatSelectively());
        // behaviours.put("Ee", new AvoidEatingWhenNotHungry());
        // behaviours.put("Ef", new AvoidEatingFamily());
        // behaviours.put("Ei0", new GatherCandidatePrey());
        // behaviours.put("Ej", new HungerCheck());
        // behaviours.put("Ep", new PreferEatingPrey());
        // behaviours.put("Ee00", new EatEverything());
        // behaviours.put("Eha" , new HuntAnything());
        // behaviours.put("Ehf0", new HuntFood());
        // behaviours.put("Ehs0", new HuntStranger());
        // behaviours.put("Erp", new RunFromPredator());
        // behaviours.put("Ma", new RandomMovement());
        // behaviours.put("Mb", new Stockiness());
        // behaviours.put("Ff0", new Fission());
        // behaviours.put("Finh0", new InhibitFertilityWhenCrowded());
        // behaviours.put("Finh1", new InhibitFertilityWhenAlreadyPregnant());
        // behaviours.put("Finh2", new InhibitFertilityWhenNotEnoughEnergyForChildren());
        // behaviours.put("Fr", new ReproduceReluctantly());
        // behaviours.put("Fs", new MakeSmallerBabies());
        // behaviours.put("Ft", new ReproduceEnthusiastically());
        // behaviours.put("Fpub", new WaitForPuberty());
        // behaviours.put("Pa", new AbsorbSunlight());
        // behaviours.put("Pc", new Leafiness());
        // behaviours.put("Pz", new AbsorbNeighbourCellEnergy());
        // behaviours.put("Na", new Nothing());
        // behaviours.put("Ra", new Reach());
        // behaviours.put("Va", new Vision());
    }

    public static Gene nextLetter(SplittableRandom random) {
        return available.get(random.nextInt(available.size()));
    }

    public static double mutateColorChannel(SplittableRandom random, double cc) {
        double change = 1.0 + (0.2 * random.nextDouble() * random.nextDouble() * random.nextDouble());
        if (random.nextBoolean()) { // going up
            return 1.0 - ((1.0 - cc) / change);
        } else { // going down
            return cc / change;
        }
    }

    public static Color randomColor(SplittableRandom random) {
        float r = (float)((random.nextDouble() * 0.6) + 0.4);
        float g = (float)((random.nextDouble() * 0.6) + 0.4);
        float b = (float)((random.nextDouble() * 0.6) + 0.4);
        return new Color(r, g, b, 1.0f);
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

    public static Gene[] randomGenes(SplittableRandom random, int n) {
        // if (random.nextDouble() < 0.1) {
        //     return predefined(random);
        // }

        Gene[] gene = new Gene[n];

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
