package gol;

public class Coordinate implements Comparable<Coordinate> {
    public final Universe u;
    public final int      y;
    public final int      x;
    public final int      lenSq;
    public final double   len;

    public Coordinate(Universe u, int y, int x) {
        this.u = u;
        this.y = y;
        this.x = x;

        int  e = u.edge;
        int he = e / 2;
        int hy = (y > he) ? e - y : y;
        int hx = (x > he) ? e - x : x;
        this.lenSq = (hx * hx) + (hy * hy);
        this.len   = Math.sqrt(lenSq);
    }

    public int distanceSq(Coordinate other) {
        return u.getCoordinate(other.y - this.y, other.x - this.x).lenSq;
    }

    public int compareTo(Coordinate other) {
        return Integer.compare(this.lenSq, other.lenSq);
    }

    public String toString() {
        return "(" + y + "," + x + ")";
    }
}
