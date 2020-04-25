package gol;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import gol.behaviours.*;

public class ToroidalGridPanel extends JPanel implements UniverseListener, Global {
    private static final int WIDTH    = 640;
    private static final int HEIGHT   = 640;
    private static final Color BLANK  = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    private static final Color FALSE  = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    private static final Color TRUE   = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static final Color DARK   = new Color(0.01f, 0.01f, 0.01f, 1.0f);
    private static final Color BRIGHT = new Color(0.95f, 0.95f, 0.95f, 1.0f);
    private static final Color GREY   = new Color(0.5f , 0.5f , 0.5f , 1.0f);

    public final Universe universe;
    public  double        scale;
    public  int           cellSize;
    public  int           pixels;
    private BufferedImage image;
    private Coordinate    offset;
    private Point         pointingAt;
    public  Organism      watching;
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

    private Color grey(float d) {
        if (d < 0) d = 0;
        if (d > 1) d = 1;
        return new Color(d, d, d);
    }

    private float getLogScaleValue(double min, double v, double log_gap) {
        return (float)(Math.log(1.0 + v - min) / log_gap);
    }

    private float getLinearScaleValue(double min, double v, double max) {
        return (float)((v - min) / (max - min));
    }

    public BufferedImage getImage() { return image; }

    public void redraw() {
        if (!redrawable) return;

        redrawable    = false;
        lastRedrawAge = universe.age;
        Graphics    b = image.getGraphics();

        double base_ground_energy    = 2.0 * universe.config.tick_energy(); // arrange for this to always be 50%-grey (128,128,128)
        double log_age_gap           = Math.log(1.0d + universe.maxAge - universe.minAge);
        double log_energy_gap        = Math.log(1.0d + universe.maxE   - universe.minE  );
        double log_ground_energy_gap = Math.log(1.0d + universe.maxGroundEnergy);

        Coordinate looking = lookingAt();

        double minx    = 0;
        double maxx    = 0;
        double loggapx = 0;
        if (colourify == ShowStyle.eat) {
            for(Cell cell : universe.allCells) {
                Organism org = cell.getOrganism();
                if (org != null) {
                  Eat e = Eat.get(org);
                  if (e != null) {
                      double me = e.amount();
                      if (me < minx) minx = me;
                      if (me > maxx) maxx = me;
                  }
                }
            }
            loggapx = Math.log(1.0d + maxx - minx  );
        } else if (colourify == ShowStyle.sunlight) {
            for(Cell cell : universe.allCells) {
                Organism org = cell.getOrganism();
                if (org != null) {
                  AbsorbSunlight e = AbsorbSunlight.get(org);
                  if (e != null) {
                      double me = e.amount();
                      if (me < minx) minx = me;
                      if (me > maxx) maxx = me;
                  }
                }
            }
            loggapx = Math.log(1.0d + maxx - minx  );
        } else if (colourify == ShowStyle.fission) {
            for(Cell cell : universe.allCells) {
                Organism org = cell.getOrganism();
                if (org != null) {
                  Fission e = Fission.get(org);
                  if (e != null) {
                      double me = e.amount();
                      if (me < minx) minx = me;
                      if (me > maxx) maxx = me;
                  }
                }
            }
            loggapx = Math.log(1.0d + maxx - minx  );
        }

        for (Cell c : universe.allCells) {
            Coordinate nc = universe.getCoordinate(offset.y + c.coordinate.y, offset.x + c.coordinate.x);
            int yc = nc.y * cellSize;
            int xc = nc.x * cellSize;
            Organism org = c.organism;
            if (colourify == ShowStyle.ground_energy) {
                double e    = c.energy;
                double scale = 0;
                scale = getLinearScaleValue(universe.minGroundEnergy, e, universe.maxGroundEnergy);
                b.setColor(grey((float)scale));
            } else if (org == null) {
                b.setColor(getBackgroundColour(c));
            // } else if (colourify == ShowStyle.leafiness) {
            //     b.setColor(getLeafinessColour(org.leafiness));
            } else if (colourify == ShowStyle.energy) {
                float scale =  getLogScaleValue(universe.minE, org.energy, log_energy_gap);
                b.setColor(grey(scale));
            } else if (colourify == ShowStyle.genecount) {
                float scale =  getLinearScaleValue(universe.minG, org.genes.length, universe.maxG);
                b.setColor(grey(scale));
            } else if (colourify == ShowStyle.eat) {
                Eat e = Eat.get(org);
                double me = 0;
                if (e != null) {
                    float scale = getLogScaleValue(minx, e.amount(), loggapx);
                    b.setColor(grey(scale));
                } else {
                    b.setColor(getBackgroundColour(c));
                }

            } else if (colourify == ShowStyle.sunlight) {
                AbsorbSunlight e = AbsorbSunlight.get(org);
                double me = 0;
                if (e != null) {
                    float scale = getLogScaleValue(minx, e.amount(), loggapx);
                    b.setColor(grey(scale));
                } else {
                    b.setColor(getBackgroundColour(c));
                }

            } else if (colourify == ShowStyle.fission) {
                Fission e = Fission.get(org);
                double me = 0;
                if (e != null) {
                    float scale = getLogScaleValue(minx, e.amount(), loggapx);
                    b.setColor(grey(scale));
                } else {
                    b.setColor(getBackgroundColour(c));
                }

            } else if (colourify == ShowStyle.age) {
                float scale =  getLogScaleValue(universe.minAge, org.age, log_age_gap);
                b.setColor(grey(scale));
            } else if (colourify == ShowStyle.colourage) {
                float scale =  getLogScaleValue(universe.minAge, org.age, log_age_gap);
                b.setColor(Appearance.darkify(org.my_colour, scale));
            } else {
                b.setColor(org.my_colour);
            }
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
