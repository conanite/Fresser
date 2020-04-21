package gol.behaviours;

import java.util.*;
import gol.*;

public class Leafiness implements Behaviour {
    public String  toString()         { return "Leafiness";                                    }
    public void    init(Organism org) {
        // org.leafiness = 1.0 - ((1.0 - org.leafiness) * 0.666);
    }
    public void    tick(Organism org) {                                                        }
}
