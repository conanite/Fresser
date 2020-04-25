package gol;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class ImageCapture implements UniverseListener, Global {
    public final double            image_save_trigger;
    public final int               image_save_cell_count;
    public final int               image_save_min_interval;
    public final Universe          universe;
    public final ToroidalGridPanel panel;
    private      int               changeCount = 0;
    private      String            uni_id;
    private      int               lastSave;

    public ImageCapture(Universe u, ToroidalGridPanel panel) {
        this.universe = u;
        this.panel = panel;
        this.image_save_trigger      = u.config.image_save_trigger();
        this.image_save_min_interval = u.config.image_save_min_interval();
        this.image_save_cell_count   = (int)(u.edge * u.edge * image_save_trigger);
    }

    public void universeRestarted() {
        changeCount  = 0;
        lastSave     = -100;
        this.uni_id  = dtf.format(universe.now);
        File dir     = new File("pix/life-" + uni_id);
        dir.mkdirs();
    }

    public void universeTicked()    {
        changeCount += universe.deadCount;
        changeCount += universe.newBabies;
        if ((changeCount > image_save_cell_count) && (universe.age - lastSave >= image_save_min_interval)) {
            changeCount = 0;
            saveImage();
        }
    }

    private void saveImage() {
        lastSave = universe.age;
        String index = int7.format(universe.age);
        File outputfile = new File("pix/life-" + uni_id + "/" + index + ".png");
        try {
            ImageIO.write(panel.getImage(), "png", outputfile);
        } catch (IOException ioe) {
            o.println("couldn't write file " + outputfile);
            o.println(ioe);
        }
    }
}
