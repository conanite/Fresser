package gol;

import java.util.*;
import java.time.*;
import java.util.stream.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Universe implements Global {
    public final double                         daily_energy_cycle        = 0.5;
    public final double                         annual_energy_cycle       = 0.003;
    public final int                            edge;
    public final Coordinate[][]                 coordinates;
    public final List<Coordinate>               allCoordinates;
    public final int[]                          neighbourCountsByDistance = new int[400];
    public final List<UniverseListener>         listeners                 = new ArrayList<UniverseListener>();
    public final Map<Integer, List<Coordinate>> coordinatesByDistance     = new HashMap<Integer, List<Coordinate>>();
    public final Cell[][]          cells;
    public final Cell[]            allCells;
    public final Config            config;
    public final double            gene_cost;
    public final double            tick_energy;
    public       Instant           now;
    public final double            reach_length;
    public final SplittableRandom  random             = new SplittableRandom(new Random().nextLong());
    public final List<Organism>    organisms          = new ArrayList<Organism>();
    public final List<Organism>    babies             = new ArrayList<Organism>();
    public       boolean           stopped            = false;
    public       boolean           stepped            = false;
    public       boolean           paused             = false;
    public       int               age                = 0;
    private      boolean           restartRequested   = false;
    private      boolean           statsRequested     = false;
    public       boolean           predStatsRequested = false;

    // for stats and drawing
    public int    deadCount;
    public int    newBabies;
    public int    minAge;
    public int    maxAge;

    public double minE       = Double.MAX_VALUE;
    public double maxE       = -Double.MAX_VALUE;
    public double totalE     = 0.0d;

    public Universe(Config config) throws InterruptedException {
        this.config         = config;
        this.edge           = config.universe_size();
        this.cells          = new Cell[edge][edge];
        this.allCells       = new Cell[edge * edge];
        this.coordinates    = new Coordinate[edge][edge];
        this.allCoordinates = new ArrayList<Coordinate>();
        this.tick_energy    = config.tick_energy();
        this.reach_length   = config.reach_length();
        this.gene_cost      = (tick_energy / config.default_gene_length()) * 0.2;

        for (int i = 0; i < edge; i++) {
            for (int j = 0; j < edge; j++) {
                Coordinate    co = new Coordinate(this, i, j);
                Cell        cell = new Cell(this, co);
                this.cells[i][j] = cell;

                int idx                = (i * edge) + j;
                this.allCells[idx]     = this.cells[i][j];
                this.coordinates[i][j] = co;
                this.allCoordinates.add(co);
            }
            o.print(".");
        }
        o.println("created " + allCells.length +  " cells");

        Collections.sort(allCoordinates);
        o.println("coordinates sorted");

        for(double i = 0.1; i < 11.0; i+=0.1) { // allowing max distance 10
            final double j = i;
            Predicate<Coordinate> byDistance = cell -> cell.len <= j;
            List<Coordinate> result = allCoordinates.stream().filter(byDistance).collect(Collectors.toList());
            this.neighbourCountsByDistance[(int)(10 * i)] = result.size() - 1;
            // o.println("at radius " + j + " there are " + (result.size() - 1) + " neighbours");
        }


        List<Coordinate> nearby = allCoordinates.subList(1, 315);

        for (int i = 0; i < edge; i++) {
            for (int j = 0; j < edge; j++) {
                this.cells[i][j].init(nearby);
            }
            o.print(".");
        }
        o.println("Universe setup complete, about to start");
    }

    public void addListener(UniverseListener listener) { listeners.add(listener); }

    private List<Coordinate> loadCoordinatesUpto(int distance) {
        List<Coordinate> result = coordinatesByDistance.get(distance);
        if (result != null) return result;

        int dsq = distance * distance;
        result = new ArrayList<Coordinate>();
        for (Coordinate co : allCoordinates) {
            if (co.lenSq <= dsq) {
                result.add(co);
            } else {
                break;
            }
        }

        coordinatesByDistance.put(distance, result);
        return result;
    }

    public List<Coordinate> coordinatesUpto(int distance) {
        List<Coordinate> result = coordinatesByDistance.get(distance);
        if (result == null) return loadCoordinatesUpto(distance);
        return result;
    }

    public void addOrganism(Organism o) {
        this.organisms.add(o);
    }

    public void addBaby(Organism o) {
        this.babies.add(o);
    }

    public void requestRestart() {
        this.restartRequested = true;
    }

    public void requestStats() {
        this.statsRequested = true;
    }

    public void restart() throws InterruptedException {
        try {
            this.paused = true;

            this.age = 0;
            this.now = Instant.now();

            for (Cell cell : allCells) {
                if (cell.getOrganism() != null) {
                    cell.getOrganism().die();
                    organisms.remove(cell.getOrganism());
                    cell.setOrganism(null);
                }

                // cell.energy = 0;
                cell.energy = tick_energy;

                if (random.nextDouble() < config.genesis_prob()) {
                    Organism o        = new Organism(this, cell, DNA.randomGenes(random, config.default_gene_length()), random);
                    o.food_colour     = DNA.randomColor(random);
                    o.my_colour       = DNA.randomColor(random);
                    o.predator_colour = DNA.randomColor(random);
                    o.addEnergy("genesis", config.initial_energy());
                    addOrganism(o);
                    cell.setOrganism(o);
                }
            }

            for (UniverseListener listener : listeners) { listener.universeRestarted(); }
        } finally {
            this.paused = false;
        }
    }

    public boolean alive() { return true; }

    public void nextGeneration() throws InterruptedException {
        tick();
        stepped = false;
        for (UniverseListener listener : listeners) { listener.universeTicked(); }
        while(stopped && !stepped) { Thread.yield(); Thread.sleep(100); }
    }

    public void tick() throws InterruptedException {
        try {
            this.paused = true;
            this.age++;
            boolean moreBabies = false;

            this.minAge          = Integer.MAX_VALUE;
            this.maxAge          = 0;
            this.minE            = Double.MAX_VALUE;
            this.maxE            = -Double.MAX_VALUE;
            this.totalE          = 0.0d;
            this.deadCount       = 0;
            this.newBabies       = babies.size();

            organisms.addAll(babies);
            babies.clear();

            // Collections.shuffle(organisms);
            Iterator<Organism> i = organisms.iterator();
            while(i.hasNext()) {
                Organism o = i.next();
                if (o == null) { throw new Error("got a null organism in organism list!"); }
                // if (!o.fertile()) o.die();
                if (o.dead) {
                    deadCount++;
                    i.remove();
                } else {
                    if (o.age < minAge) minAge = o.age;
                    if (o.age > maxAge) maxAge = o.age;
                    if (o.energy < minE) minE = o.energy;
                    if (o.energy > maxE) maxE = o.energy;
                    totalE += o.energy;

                    // if (!moreBabies && o.fertile()) moreBabies = true;
                }
            }

            // double hour              = Math.sin(daily_energy_cycle * age) + 1.0;  // varies from 0.0 to 2.0
            // double date              = Math.sin(annual_energy_cycle * age) + 1.0; // varies from 0.0 to 2.0
            // double energy_curve      = (hour + date) / 4.0;                       // varies from 0.0 to 1.0
            // double energy_adjustment = 0.4 + (0.6 * energy_curve);                // varies from 0.3 to 1.0 so energy is never zero
            // double this_tick         = tick_energy * energy_adjustment;

            for (Cell here : allCells) {
                here.energy /= 2.0;
                // here.energy += this_tick;
                here.energy += tick_energy;
            }

            if (statsRequested) {
                o.println(massStats());
                statsRequested     = false;
                predStatsRequested = false;
            }

            if (restartRequested) {
                o.println("everything is dead at age " + age + " ; restarting");
                try {
                    restartRequested = false;
                    Thread.sleep(100);
                    restart();
                } catch (InterruptedException e) { }
            }
        } finally {
            this.paused = false;
        }
    }

    public String massStats() {
        Organism smallest = null;
        Organism biggest  = null;
        double minM       = Double.MAX_VALUE;
        double maxM       = -Double.MAX_VALUE;
        double totalM     = 0.0d;
        Organism newest   = null;
        Organism oldest   = null;
        int minA          = Integer.MAX_VALUE;
        int maxA          = Integer.MIN_VALUE;
        Map<Integer, Integer>   ages_by_log       = new HashMap<Integer, Integer>();
        // Map<Double, Integer>    leafinesses       = new HashMap<Double, Integer>();
        Map<String, Integer>    behaviour_counts  = new HashMap<String, Integer>();
        Map<String, Integer>    gene_counts       = new HashMap<String, Integer>();
        String                  predators         = "";

        for (Organism o : organisms) {
            // if ((predStatsRequested && o.leafiness < 0.5) || !predStatsRequested) {
            if (true) {
                if (o.age > maxA) { maxA = o.age; oldest  = o;  }
                if (o.age < minA) { minA = o.age; newest = o; }
                int lga = (int)Math.log(1 + o.age);

                Integer algc = ages_by_log.get(lga);
                if (algc == null) { algc = 0; }
                ages_by_log.put(lga, algc + 1);

                DNA.stats(behaviour_counts, o.behaviours);
                DNA.stats(gene_counts, o.genes);

                // double leafy20 = ((int)(o.leafiness * 10)) / 10.0;
                // Integer leafyc = leafinesses.get(leafy20);
                // if (leafyc == null) { leafyc = 0; }
                // leafinesses.put(leafy20, leafyc + 1);
            }
        }

        String stats = "\n\n\n===================================================================\n\n";
        stats += "Age of universe : "     + age                  + "\n";
        stats += "Runtime : "             + (Duration.between(now, Instant.now()).toMillis()) + "\n";
        stats += "Tick energy : "         + config.tick_energy() + "\n";
        stats += MapToString.toString(behaviour_counts);
        stats += "\n";
        stats += MapToString.toString(gene_counts);
        stats += "\n\n";
        // stats += MapToString.toString(leafinesses);
        // stats += "\n\n";
        stats += "Min energy : "           + minE                + "\n";
        stats += "Max energy : "           + maxE                + "\n";
        stats += "Total energy : "         + totalE              + "\n\n";
        stats += "Min age : "              + minA                + " on " + newest + "\n";
        stats += "Max age : "              + maxA                + " on " + oldest  + "\n";
        stats += "Age counts per log : "   + ages_by_log         + "\n\n";
        stats += "Organism count : "       + organisms.size()    + "\n";
        stats += "Dead this generation : " + deadCount           + "\n";
        stats += "Born this generation : " + newBabies           + "";
        // stats += predators;

        return stats;
    }

    public String population() {
        StringBuffer b = new StringBuffer();
        for (Organism o : organisms) {
            b.append(o.toString()).append(" ").append(o.age).append("\n");
            for (Behaviour bh : o.behaviours) {
                b.append("  ").append(bh).append("\n");
            }
        }

        return b.toString();
    }

    public Iterator<Coordinate> coordinates() {
        return this.allCoordinates.iterator();
    }

    public Iterator<Cell> cells() {
        return Arrays.stream(this.allCells).iterator();
    }

    public int norm(int c) {
        c = c % this.edge;
        if (c < 0) c = c + this.edge;
        return c;
    }

    public Coordinate getCoordinate(int y, int x) {
        return coordinates[norm(y)][norm(x)];
    }

    public Cell getCell(Coordinate c) {
        return cells[c.y][c.x];
    }

    public Cell getCell(int y, int x) {
        return cells[norm(y)][norm(x)];
    }
}
