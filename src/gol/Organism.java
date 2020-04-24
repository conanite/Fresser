package gol;

import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.awt.Color;
import gol.behaviours.*;

public class Organism implements Global {
    public static int              index         = 0;
    public final  Universe         universe;
    public final  Gene[]           genes;
    public final  int              id;
    public final  SplittableRandom random;
    public        Cell             cell;
    public        boolean          dead          = false;
    public        boolean          watching      = false;
    public        int              age           = 0;
    public        double           energy        = 0.0;
    public        double           gained        = 0.0;  // updated each tick to record net gain

    public        Color            food_colour;
    public        Color            my_colour;
    public        Color            predator_colour;

    public final List<Behaviour>     behaviours    = new ArrayList<Behaviour>();
    public final List<String>        history       = new ArrayList<String>();
    public final Map<String, Object> blackboard    = new HashMap<String, Object>();

    public Organism(Universe u, Cell cell, Gene[] genes, SplittableRandom random) {
        this.id         = Organism.index++;
        this.universe   = u;
        this.cell       = cell;
        this.genes      = genes;
        this.random     = random.split();

        for (Gene g : genes) { g.install(this); }

        this.watching = (id % 1000) == 0;
        // this.watching   = random.nextDouble() < u.config.watching();
    }

    public List<Cell> neighbours() {
        return cell.getNeighbours((int)universe.reach_length);
    }

    public Map<String, Object> status() {
        String bs = "";
        for (Behaviour b : behaviours) {
            bs += "\n  " + b.toString();
        }

        String gs = "";
        for (Gene g : genes) {
            gs += "\n  " + g.name();
        }

        Map<String, Object> s = new HashMap<String, Object>();
        s.put("id", id);
        s.put("A age", age);
        s.put("A dead", dead);
        s.put("A genes", genes.length);
        s.put("A behaviour count", behaviours.size());
        s.put("B behaviours", bs);
        s.put("C genes", gs);
        s.put("E energy", nf2.format(energy));
        s.put("E new energy", nf2.format(gained));
        s.put("F my_colour", "" + my_colour.getRed() + "," + my_colour.getGreen() + "," + my_colour.getBlue());
        s.put("F food_colour", "" + food_colour.getRed() + "," + food_colour.getGreen() + "," + food_colour.getBlue());
        s.put("F predator_colour", "" + predator_colour.getRed() + "," + predator_colour.getGreen() + "," + predator_colour.getBlue());
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

    public void addHistory(String what) {
        history.add(what);
    }

    public double addEnergy(String from, double moreEnergy) {
        this.energy += moreEnergy;
        String co = (cell == null) ? "unborn" : cell.coordinate.toString();
        if (watching) addHistory(toString() + " at " + co + " added energy " + moreEnergy + " from " + from + ", new energy is " + this.energy);
        return this.energy;
    }

    public boolean dying() {
        return energy <= 1;
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
        double oldenergy = this.energy;
        this.age++;
        this.energy -= 1.0 + (genes.length * universe.gene_cost);
        // this.energy -= ((1 + behaviours.size()) * 0.33);
        healthCheck();

        if (dead) return;
        if (cell == null) { throw new Error(toString() + " can't tick, no cell"); }

        for(Behaviour b : behaviours) { b.tick(); }

        healthCheck();
        this.gained = this.energy - oldenergy;
    }
}
