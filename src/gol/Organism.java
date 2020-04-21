package gol;

import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.awt.Color;
import gol.behaviours.*;

public class Organism {
    public static int              index         = 0;
    public final  Universe         universe;
    public final  Gene[]           genes;
    public final  int              id;
    public final  SplittableRandom random;
    public        Cell             cell;
    public        boolean          dead          = false;
    public        boolean          watching      = false;
    public        int              age           = 0;
    public        double           reachLength   = 2.0;
    public        int              visionLength  = 0;
    public        double           energy        = 0.0;
    public        double           stockiness    = 0.5;  // constraint: must equal 1.0 - leafiness (set in #init)
    public        double           eating        = 0.0;  // range 0.0 - 1.0 ; derived from leafiness
    public        double           photosynth    = 0.0;  // range 0.0 - 1.0 ; derived from leafiness

    public Color             food_colour;
    public Color             my_colour;
    public Color             predator_colour;

    // reset after each tick
    public List<Cell>          myReach; // places I can reach
    public List<Cell>           vision   = new ArrayList<Cell>(); // places I can see

    // trade-offs
    // 1) limit the number of behaviours
    // 2) cost of behaviours
    public final List<Behaviour>     allBehaviours = new ArrayList<Behaviour>();
    public final List<Behaviour>     behaviours    = new ArrayList<Behaviour>();
    public final List<Organism>      babies        = new ArrayList<Organism>();
    public final List<String>        history       = new ArrayList<String>();
    public final Map<String, Object> blackboard    = new HashMap<String, Object>();

    public Organism(Universe u, Cell cell, Gene[] genes, SplittableRandom random) {
        this.id         = Organism.index++;
        this.universe   = u;
        this.cell       = cell;
        this.genes      = genes;
        this.random     = random.split();

        for (Gene g : genes) { g.install(this); }

        init();
        this.watching = (id % 1000) == 0;
        // this.watching   = random.nextDouble() < u.config.watching();
    }

    public List<Cell> neighbours() {
        if (myReach == null) { myReach = cell.getNeighbours((int)reachLength); }
        return myReach;
    }

    public Map<String, Object> status() {
        Map<String, Object> s = new HashMap<String, Object>();
        s.put("id", id);
        s.put("A energy", energy);
        s.put("A age", age);
        s.put("A dead", dead);
        s.put("A reach", reachLength);
        s.put("A genes", genes.length);
        s.put("A behaviour count", behaviours.size());
        s.put("A behaviours", behaviours.toString());
        s.put("C my_colour", "" + my_colour.getRed() + "," + my_colour.getGreen() + "," + my_colour.getBlue());
        s.put("C food_colour", "" + food_colour.getRed() + "," + food_colour.getGreen() + "," + food_colour.getBlue());
        s.put("C predator_colour", "" + predator_colour.getRed() + "," + predator_colour.getGreen() + "," + predator_colour.getBlue());
        s.put("T eating", eating);
        s.put("T photosynth", photosynth);
        s.put("F carrying babies", babies.size());
        if (cell != null) { s.put("coordinates", cell.coordinate); }
        return s;
    }

    public String toString() {
        return "Organism#" + id;
    }

    public double colourdsq(Color a, Color b) {
        double dr = (a.getRed()   / 255.0) - (b.getRed()   / 255.0);
        double dg = (a.getGreen() / 255.0) - (b.getGreen() / 255.0);
        double db = (a.getBlue()  / 255.0) - (b.getBlue()  / 255.0);
        return (dr * dr) + (dg * dg) + (db * db);
    }

    public double visualDistanceSq(Organism other) {
        if (other == null) return Double.MAX_VALUE;
        return colourdsq(my_colour, other.my_colour);
    }

    public double tastiness(Organism other) { // lower is tastier
        return colourdsq(food_colour, other.my_colour);
    }

    public String geneNames() {
        return allBehaviours.stream().map( g -> g.toString() ).collect( Collectors.joining("\n") );
    }

    public void addHistory(String what) {
        history.add(what);
    }

    public double addEnergy(String from, double moreEnergy) {
        this.energy += moreEnergy;
        String co = (cell == null) ? "unborn" : cell.coordinate.toString();
        if (watching) addHistory(toString() + " at " + co + " added energy " + moreEnergy + " from " + from + ", new energy is " + this.energy);
        return this.energy;
    }

    public List<Organism> visibleOrganisms() {
        List<Organism> oo = new ArrayList<Organism>();
        for(Cell c : vision) if (c.organism != null && c != cell) oo.add(c.organism);
        return oo;
    }

    public void init() {
    }

    public boolean dying() {
        return energy <= 0;
    }

    public void healthCheck() {
        if (dying()) die();
    }

    public void die() {
        if (dead) return;
        if (this.cell != null) {
            if (watching) addHistory(toString() + " died at age " + this.age + " with energy " + energy + " in " + this.cell.coordinate);
            this.cell.organism = null;
            this.cell.absorb(this);
        } else {
            if (watching) addHistory(toString() + " died at age " + this.age + " with energy " + energy + " nowhere");
        }
        this.cell = null;
        this.dead = true;
    }

    public boolean alive() { return !dead; }

    public void tick() {
        this.age++;
        this.energy -= ((1 + genes.length     ) * 0.33);
        this.energy -= ((1 + behaviours.size()) * 0.33);
        healthCheck();

        if (dead) return;
        if (cell == null) { throw new Error(toString() + " can't tick, no cell"); }

        for(Behaviour b : behaviours) { b.tick(); }

        healthCheck();
    }
}
