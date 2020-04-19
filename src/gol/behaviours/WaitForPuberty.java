package gol.behaviours;

import java.util.*;
import gol.*;

public class WaitForPuberty implements Behaviour {
    public String  toString()         { return "WaitForPuberty";                                   }
    public void    init(Organism org) {}
    public void    tick(Organism org) {
        if (org.age < 100) { org.fertility = 0.0; return; }
        if (org.age < 200) org.fertility *= (1.0d * (org.age - 100.0) / 100.0);
    }
}
