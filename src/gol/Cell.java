package gol;

import java.util.*;

public class Cell implements Tickable {
    private final    Universe   universe;
    public  final    Coordinate coordinate;
    public  final    List<Cell> neighbours                 = new ArrayList<Cell>(315); // enough for d=10 (10^2 * 3.14)
    public  final    List<Cell>[] neighbourListsByDistance = (List<Cell>[])(new List[40]);
    public  double   energy                                = 0.0d;
    private Organism organism;
    public  Ribbon   ribbon;

    public Cell(Universe u, Coordinate co) {
        this.universe   = u;
        this.coordinate = co;
    }

    public void init(List<Coordinate> nearby) {
        for (Coordinate coo : nearby) {
            neighbours.add(neighbour(coo));
        }
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism org) {
        this.organism      = org;
    }

    public void notifyDeath() {
        this.ribbon.deaths++;
    }

    public void notifyBirth() {
        this.ribbon.births++;
    }

    public boolean alive() {
        return organism != null && organism.alive();
    }

    public void tick() {
        if (organism != null) organism.tick();
    }

    public Cell pickANeighbour(double distance, double at) {
        int d = (int) distance * 10;
        int nbCount = universe.neighbourCountsByDistance[d];
        return neighbours.get((int)(nbCount * at));
    }

    public List<Cell> getNeighbours(double distance) {
        int d = (int) distance * 10;
        if (neighbourListsByDistance[d] == null) {
            if (distance < 1) {
                neighbourListsByDistance[d] = (List<Cell>)Collections.<Cell>emptyList();
            } else {
                int nbCount = universe.neighbourCountsByDistance[d];
                neighbourListsByDistance[d] = neighbours.subList(0, nbCount);
            }
        }
        return neighbourListsByDistance[d];
    }

    public int distanceSq(Cell other) {
        return this.coordinate.distanceSq(other.coordinate);
    }

    public Coordinate relative(Cell other) {
        return universe.getCoordinate(other.coordinate.y - coordinate.y, other.coordinate.x - coordinate.x);
    }

    // need to rethink, fuels uncontrolled growth
    public void absorb(Organism o) {
        if (o.energy > 0) { this.energy += o.energy; }
    }

    public Cell neighbour(int y, int x) {
        return universe.getCell(coordinate.y + y, coordinate.x + x);
    }

    public Cell neighbour(Coordinate n) {
        return universe.getCell(coordinate.y + n.y, coordinate.x + n.x);
    }

    public String toString() {
        return "Cell at " + coordinate.toString() + ", organism is " + organism + ", energy is " + energy;
    }
}
