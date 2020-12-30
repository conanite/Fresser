package gol;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ToroidalGridPanel extends JPanel implements UniverseListener, Global {
    private static final int[] sizes = new int[] { 1, 2, 3, 4, 6, 8, 12, 16, 24, 32 };
    private              int   scale = 2;

    public  final Universe                 universe;
    public        ToroidalGridImagePainter tgip;

    public ToroidalGridPanel(Universe u, int scale) {
        super();

        this.universe = u;

        setCellSize();

        setOpaque(false);
        setFocusable(true);
    }

    public void universeTicked()    { tgip.universeTicked();    }
    public void universeRestarted() { tgip.universeRestarted(); }

    private void setCellSize() {
        if (scale < 0) { scale = 0; }
        if (scale >= sizes.length - 1) { scale = sizes.length - 1; }

        tgip = new ToroidalGridImagePainter(universe, sizes[scale]);
        tgip.universeTicked();
    }

    public void zoomIn() {
        scale++;
        setCellSize();
        o.println("zoom in: scale is " + scale + ", cellsize is " + tgip.cellSize);
    }

    public void zoomOut() {
        scale--;
        setCellSize();
        o.println("zoom out: scale is " + scale + ", cellsize is " + tgip.cellSize);
    }

    public Dimension getPreferredSize() {
        return new Dimension(tgip.pixels, tgip.pixels);
    }

    public Dimension getMaximumSize() {
        return new Dimension(tgip.pixels, tgip.pixels);
    }

    public void redraw() { tgip.redraw(); }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tgip.image, 0, 0, tgip.pixels, tgip.pixels, this);
    }
}
