package gol;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import gol.behaviours.*;
import gol.painter.*;

public class ToroidalGridPanel extends JPanel implements UniverseListener, Global {
    private static final int WIDTH    = 640;
    private static final int HEIGHT   = 640;
    private static final Color BLANK  = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    private static final Color FALSE  = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    private static final Color TRUE   = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static final Color DARK   = new Color(0.02f, 0.02f, 0.02f, 1.0f);
    private static final Color BRIGHT = new Color(0.95f, 0.95f, 0.95f, 1.0f);
    private static final Color GREY   = new Color(0.5f , 0.5f , 0.5f , 1.0f);

    public final Universe universe;
    public final Map<ShowStyle, CellPainter> painters = new HashMap<ShowStyle, CellPainter>();
    public  double        scale;
    public  int           cellSize;
    public  int           pixels;
    private BufferedImage image;
    private Coordinate    offset;
    private Point         pointingAt;
    public  ShowStyle     colourify     = ShowStyle.colourage;
    public  String        bgColour      = "dark";
    public  String        filter        = "all";
    public  boolean       redrawable    = true;
    public  int           lastRedrawAge = 2;

    public ToroidalGridPanel(Universe u, int scale) {
        super();
        this.offset   = u.getCoordinate(0, 0);
        this.universe = u;
        this.scale    = scale;
        this.painters.put(ShowStyle.age          , new Age());
        this.painters.put(ShowStyle.colourage    , new ColourAgePainter());
        this.painters.put(ShowStyle.genecount    , new GeneCountPainter());
        this.painters.put(ShowStyle.sunlight     , new AbsorbSunlightPainter());
        this.painters.put(ShowStyle.eat          , new EatPainter());
        this.painters.put(ShowStyle.fission      , new FissionPainter());
        this.painters.put(ShowStyle.attack       , new AttackPainter());
        this.painters.put(ShowStyle.defend       , new DefendPainter());
        this.painters.put(ShowStyle.energy       , new EnergyPainter());
        this.painters.put(ShowStyle.ground_energy, new GroundEnergyPainter());
        this.painters.put(ShowStyle.colour       , new ColourPainter());
        this.painters.put(ShowStyle.inverse_age  , new InverseAgePainter());

        setCellSize();

        setOpaque(false);
        setFocusable(true);
        blank();

        new javax.swing.Timer(50, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ToroidalGridPanel.this.redraw();
                    ToroidalGridPanel.this.repaint();
                }
            }).start();
    }

    private void setCellSize() {
        this.cellSize = (int)this.scale;
        this.pixels   = cellSize * universe.edge;
        this.image    = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_ARGB);
    }

    public void zoomIn() {
        this.scale *= 1.2;
        setCellSize();
        o.println("zoom in: scale is " + scale + ", cellsize is " + cellSize);
    }

    public void zoomOut() {
        this.scale /= 1.2;
        setCellSize();
        o.println("zoom out: scale is " + scale + ", cellsize is " + cellSize);
    }

    public void universeTicked()    { requireRedraw(); redraw(); }
    public void universeRestarted() { lastRedrawAge = 2;    }
    public void requireRedraw()     { redrawable    = true; }
    public void pointingAt(Point p) { pointingAt    = p;    }

    public Coordinate lookingAt() {
        if (pointingAt != null) return getUniverseCoordinate(pointingAt);
        return null;
    }

    public Coordinate getUniverseCoordinate(Point p) {
        int y = ((int)(p.y / cellSize)) - offset.y - 1;
        int x = ((int)(p.x / cellSize)) - offset.x - 1;
        return universe.getCoordinate(y,x);
    }

    public void scroll(int y, int x) {
        this.offset = universe.getCoordinate(y + offset.y, x + offset.x);
    }

    public Dimension getPreferredSize() {
        return new Dimension(cellSize * universe.edge, cellSize * universe.edge);
    }

    private void blank() {
        Graphics g = image.getGraphics();
        g.setColor(BLANK);
        Dimension s = getPreferredSize();
        g.fillRect(0, 0, s.width, s.height);
    }

    private Color getLeafinessColour(double leafiness) {
        double r = 1.0 - Math.abs((2.0 * leafiness) - 1.0);
        return new Color((float)(1.0 - leafiness), (float)leafiness, (float)r);
    }

    private Color getBackgroundColour(Cell c) {
        if (bgColour == "dark") {
            if (c.coordinate.y == 0 || c.coordinate.x == 0) {
                return GREY;
            } else {
                return DARK;
            }
        } else if (bgColour == "bright") {
            return BRIGHT;
        } else {
            return GREY;
        }
    }

    private Color getNotBackgroundColour() {
        if (bgColour == "dark") {
            return BRIGHT;
        } else {
            return DARK;
        }
    }

    public BufferedImage getImage() { return image; }

    public void redraw() {
        if (!redrawable) return;

        redrawable    = false;
        lastRedrawAge = universe.age;
        Graphics    b = image.getGraphics();

        Coordinate looking = lookingAt();

        CellPainter painter = painters.get(colourify);
        painter.prepare(universe);

        for (Cell c : universe.allCells) {
            Coordinate nc = universe.getCoordinate(offset.y + c.coordinate.y, offset.x + c.coordinate.x);
            int yc = nc.y * cellSize;
            int xc = nc.x * cellSize;
            Color colour = painter.colourFor(c);
            if (colour == null) colour = getBackgroundColour(c);
            b.setColor(colour);
            b.fillRect(xc, yc, cellSize, cellSize);

            if (looking == c.coordinate) {
                b.setColor(getNotBackgroundColour());
                b.drawRect(xc, yc, cellSize, cellSize);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, pixels, pixels, this);
    }
}
