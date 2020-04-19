package gol.behaviours;

import java.util.*;
import gol.*;

public class MakeSmallerBabies implements Behaviour {
    public String toString() {
        return "MakeSmallerBabies";
    }

    public void init(Organism org) {
        // org.babySize *= 0.8;
    }

    public void tick(Organism org) { }
}
