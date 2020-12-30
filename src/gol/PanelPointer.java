package gol;

import java.awt.event.*;

public class PanelPointer extends MouseAdapter implements Global {
    public final ToroidalGridPanel panel;
    public final Info              info;
    public       Organism          watching;

    public PanelPointer(ToroidalGridPanel panel, Info info) {
        this.panel = panel;
        this.info = info;
    }

    public void mouseMoved(MouseEvent e) {
        panel.tgip.pointingAt(e.getPoint());
        info.update();
        panel.tgip.requireRedraw();
    }

    public void mouseClicked(MouseEvent e) {
        if (watching != null) {
            watching.watching = false;
            watching = null;
        }
        Coordinate co = panel.tgip.getUniverseCoordinate(e.getPoint());
        Cell     cell = panel.universe.getCell(co);
        Organism  org = cell.getOrganism();

        o.println(cell);

        if (org != null) {
            org.watching = true;
            watching     = org;
            o.println(MapToString.toString(org.status()));
        }
    }
}
