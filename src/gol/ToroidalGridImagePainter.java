package gol;

import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import gol.painter.*;

public class ToroidalGridImagePainter {
    private static final Color BLANK  = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    private static final Color DARK   = new Color(0.02f, 0.02f, 0.02f, 1.0f);
    private static final Color BRIGHT = new Color(0.95f, 0.95f, 0.95f, 1.0f);
    private static final Color GREY   = new Color(0.5f , 0.5f , 0.5f , 1.0f);

    public final Universe universe;
    public final Map<ShowStyle, CellPainter> painters = new HashMap<ShowStyle, CellPainter>();
    public final int           cellSize;
    public final int           pixels;
    public final BufferedImage image;

    private Coordinate    offset;
    private Point         pointingAt;
    public  ShowStyle     colourify     = ShowStyle.colourage;
    public  String        bgColour      = "dark";
    public  boolean       redrawable    = true;
    public  int           lastRedrawAge = 2;

    public ToroidalGridImagePainter(Universe u, int cellSize) {
        this.offset   = u.getCoordinate(0, 0);
        this.universe = u;
        this.cellSize = cellSize;
        this.painters.put(ShowStyle.age          , new Age());
        this.painters.put(ShowStyle.colourage    , new ColourAgePainter());
        this.painters.put(ShowStyle.genecount    , new GeneCountPainter());
        this.painters.put(ShowStyle.sunlight     , new AbsorbSunlightPainter());
        this.painters.put(ShowStyle.eat          , new EatPainter());
        this.painters.put(ShowStyle.fission      , new FissionPainter());
        this.painters.put(ShowStyle.attack       , new AttackPainter());
        this.painters.put(ShowStyle.attackb      , new AttackBPainter());
        this.painters.put(ShowStyle.defend       , new DefendPainter());
        this.painters.put(ShowStyle.defendb      , new DefendBPainter());
        this.painters.put(ShowStyle.energy       , new EnergyPainter());
        this.painters.put(ShowStyle.ground_energy, new GroundEnergyPainter());
        this.painters.put(ShowStyle.colour       , new ColourPainter());
        this.painters.put(ShowStyle.inverse_age  , new InverseAgePainter());

        this.pixels   = cellSize * u.edge;
        this.image    = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_ARGB);
        blank();
    }

    public void scroll(int y, int x) {
        this.offset = universe.getCoordinate(y + offset.y, x + offset.x);
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

    private void blank() {
        Graphics g = image.getGraphics();
        g.setColor(BLANK);
        g.fillRect(0, 0, pixels, pixels);
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

}
