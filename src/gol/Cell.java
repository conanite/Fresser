package gol;

import java.util.*;

public class Cell {
    private final    Universe   universe;
    public  final    Coordinate coordinate;
    public  final    List<Cell> neighbours = new ArrayList<Cell>(3000); // sorted by distance from this
    public  final    List<Cell>[] neighbourListsByDistance = (List<Cell>[])(new List[400]);
    public  double   energy     = 0;
    public  Organism organism;

    public Cell(Universe u, Coordinate co) {
        this.universe   = u;
        this.coordinate = co;
    }

    public void init(List<Coordinate> nearby) {
        for (Coordinate coo : nearby) {
            neighbours.add(neighbour(coo));
        }
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
                neighbourListsByDistance[d] = (List<Cell>)Collections.EMPTY_LIST;
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
