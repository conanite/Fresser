package gol;

import java.time.*;
import javax.swing.*;

class Info implements UniverseListener, Global {
    public final JLabel label;
    public final Universe universe;
    private Coordinate    lookingAt;
    private ToroidalGridPanel panel;

    public Info(JLabel label, Universe universe, ToroidalGridPanel panel) {
        this.label = label;
        this.universe = universe;
        this.panel = panel;
        universe.addListener(this);
    }

    public void universeRestarted() {
        // System.out.println("restarted: organisms length is " + universe.organisms.size());
        // update();
    }

    public void universeTicked()    { update(); }

    public void update() {
        StringBuffer b = new StringBuffer();
        b.append("Age ").append(universe.age);
        b.append(" Organisms ").append(universe.organisms.size());
        b.append("  +").append(universe.newBabies);
        b.append("  -").append(universe.deadCount);
        b.append(" Runtime ").append(Duration.between(universe.now, Instant.now()).toMillis() / 1000);
        Coordinate lookingAt = panel.lookingAt();
        if (lookingAt != null) {
            b.append(" at " + lookingAt);
            Cell c = universe.getCell(lookingAt);
            Organism o = c.organism;
            if (o != null) {
                // b.append(" " + (((int)(100 * o.leafiness)) / 100.0));
                b.append(" id#" + o.id);
                b.append(" age " + o.age);
            }
            b.append(" ground ").append(nf2.format(c.energy));
        }
        b.append(" showing ").append(panel.colourify);
        if (universe.stopped) b.append(" Stopped");
        label.setText(b.toString());
    }
}
