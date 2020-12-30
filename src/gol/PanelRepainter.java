package gol;

import java.awt.event.*;

class PanelRepainter implements ActionListener {
    public final ToroidalGridPanel panel;

    public PanelRepainter(ToroidalGridPanel panel) { this.panel = panel; }

    public void actionPerformed(ActionEvent e) {
        panel.redraw();
        panel.repaint();
    }
}
