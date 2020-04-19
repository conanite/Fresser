package gol.behaviours;

import java.util.*;
import gol.*;

public class Eat {
    public void eat(Organism predator, Organism prey) {
        if (prey == predator || prey == null || prey.dead) return;

        if (predator.random.nextDouble() < 0.8) return;

        prey.healthCheck();

        if (prey == predator || prey == null || prey.dead || prey.energy < 0) return;

        if (takingM > maxTaking) takingM = maxTaking;

        double preyE       = prey.energy;
        double predatorE   = predator.energy;
        double takingE     = preyE * predator.eating;

        double energyCost  = preyE * prey.eating;

        if (energyCost < 0 || takingE < 0) {
            throw new Error(this.toString() + " energyCost is " + energyCost + " takingE " + takingE);
        }

        double conversionCost = 0.5;

        prey.addEnergy("eaten by " + predator, -takingE);
        predator.addEnergy("eating " + prey, takingE * conversionCost);

        Coordinate  relco = predator.cell.relative(prey.cell);
        double movingCost = relco.len * takingM / 24.0;
        predator.addEnergy("reaching dinner " + prey, -movingCost);

        if (Double.isNaN(predator.energy)) {
            throw new Error("energy " + predator.energy);
        }

        prey.healthCheck();

    }
}
