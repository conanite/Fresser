package gol;

import java.time.*;
import java.awt.*;
import javax.swing.*;

import gol.behaviours.*;

class Info implements UniverseListener, Global {
    public final Box          panel      = Box.createVerticalBox();
    public final JLabel       age        = buildLabel(panel);
    public final JLabel       organisms  = buildLabel(panel);
    public final JLabel       births     = buildLabel(panel);
    public final JLabel       deaths     = buildLabel(panel);
    public final JLabel       runtime    = buildLabel(panel);
    public final JLabel       mouse      = buildLabel(panel);
    public final JLabel       watching   = buildLabel(panel);
    public final JLabel       newenergy  = buildLabel(panel);
    public final JLabel       fission    = buildLabel(panel);
    public final JLabel       eat        = buildLabel(panel);
    public final JLabel       sunlight   = buildLabel(panel);
    public final JLabel       findfood   = buildLabel(panel);
    public final JLabel       attack     = buildLabel(panel);
    public final JLabel       defend     = buildLabel(panel);
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

        universe.addListener(this);
    }

    private static JLabel buildLabel(Box panel) {
        JLabel lbl = new JLabel();
        lbl.setMaximumSize(new Dimension(360, 20));
        lbl.setPreferredSize(new Dimension(360, 20));
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
                newenergy.setText(nf2.format(o.newenergy));
                fission  .setText(geneString(Fission.get(o)));
                eat      .setText(geneString(Eat.get(o)));
                sunlight .setText(geneString(AbsorbSunlight.get(o)));
                findfood .setText(geneString(FindFood.get(o)));
                attack   .setText(geneString(Attack.get(o)));
                defend   .setText(geneString(Defence.get(o)));
            } else {
                watching .setText("");
                newenergy.setText("");
                fission  .setText("");
                eat      .setText("");
                sunlight .setText("");
                findfood .setText("");
                attack   .setText("");
                defend   .setText("");
            }
        } else {
            mouse.setText("");
            watching.setText("");
        }
    }

    String geneString(Behaviour b) {
        if (b == null) return "";
        return b.toString();
    }
}
