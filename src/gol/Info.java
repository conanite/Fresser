package gol;

import java.time.*;
import java.awt.*;
import javax.swing.*;

class Info implements UniverseListener, Global {
    public final Box          panel      = Box.createVerticalBox();
    public final JLabel       age        = buildLabel(panel);
    public final JLabel       organisms  = buildLabel(panel);
    public final JLabel       births     = buildLabel(panel);
    public final JLabel       deaths     = buildLabel(panel);
    public final JLabel       runtime    = buildLabel(panel);
    public final JLabel       mouse      = buildLabel(panel);
    public final JLabel       watching   = buildLabel(panel);
    public final JLabel       view       = buildLabel(panel);
    public final Universe     universe;
    private Instant           lastUpdate = Instant.now();
    private Coordinate        lookingAt;
    private ToroidalGridPanel tgp;

    public Info(Universe universe, ToroidalGridPanel tgp) {
        this.universe  = universe;
        this.tgp       = tgp;

        panel.setAlignmentY(JPanel.TOP_ALIGNMENT);
        panel.setOpaque(true);
        // panel.add(age);
        // panel.add(organisms);
        // panel.add(births);
        // panel.add(deaths);
        // panel.add(runtime);
        // panel.add(mouse);
        // panel.add(watching);
        // panel.add(view);

        universe.addListener(this);
    }

    private static JLabel buildLabel(Box panel) {
        JLabel lbl = new JLabel();
        lbl.setMaximumSize(new Dimension(300, 20));
        lbl.setPreferredSize(new Dimension(300, 20));
        lbl.setAlignmentY(JPanel.TOP_ALIGNMENT);
        panel.add(lbl);
        return lbl;
    }

    public void universeRestarted() {
        // System.out.println("restarted: organisms length is " + universe.organisms.size());
        // update();
    }

    public void universeTicked()    { update(); }

    public void update() {
        Instant now = Instant.now();
        long interval = Duration.between(lastUpdate, now).toMillis();
        if (interval < 200) return;
        lastUpdate = now;

        age.setText("Age " + universe.age + (universe.stopped ? " Stopped" : ""));
        organisms.setText("Organisms " + universe.total);
        births.setText("Births +" + universe.births);
        deaths.setText("Deaths -" + universe.deaths);
        runtime.setText("Runtime " + (Duration.between(universe.now, Instant.now()).toMillis() / 1000));
        view.setText("Showing " + tgp.colourify);

        Coordinate lookingAt = tgp.lookingAt();
        if (lookingAt != null) {
            Cell c = universe.getCell(lookingAt);

            mouse.setText("At " + lookingAt + " E=" + nf2.format(c.energy));

            Organism o = c.getOrganism();
            if (o != null) {
                watching.setText("#" + o.id + " age " + o.age + " E=" + nf2.format(o.energy));
            } else {
                watching.setText("");
            }
        } else {
            mouse.setText("");
            watching.setText("");
        }
    }
}
