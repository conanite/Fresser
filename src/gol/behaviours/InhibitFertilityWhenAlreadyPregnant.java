package gol.behaviours;

import java.util.*;
import gol.*;

public class InhibitFertilityWhenAlreadyPregnant implements Behaviour {
    public String toString() {
        return "InhibitFertilityWhenAlreadyPregnant";
    }

    public void tick(Organism org) {
        org.fertility /= Math.pow(2.0, org.babies.size());
    }
}
