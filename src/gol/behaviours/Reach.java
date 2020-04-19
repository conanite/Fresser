package gol.behaviours;

import java.util.*;
import gol.*;

public class Reach implements Behaviour {
    public static final double MAX_REACH = 10.0;
    public  String toString()         { return "Reach";         }
    public    void init(Organism org) {
        // double inv_reach = MAX_REACH - org.reachLength;
        // org.reachLength = MAX_REACH - (inv_reach / 1.05);
        // if (org.reachLength > MAX_REACH) { org.reachLength = MAX_REACH; }
    }
    public    void tick(Organism org) {                         }
}
