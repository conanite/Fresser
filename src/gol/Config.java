package gol;

import java.io.*;
import java.util.*;

class Config extends Properties {
    public Config() throws IOException {
        super();
        setProperty("threads"            , "4"     );
        setProperty("universe_size"      , "256"   );
        setProperty("pixel_size"         , "8"     );
        setProperty("tick_energy"        , "100.0" );
        setProperty("default_gene_length", "32"    );
        setProperty("screenshot_interval", "200"   );
        setProperty("initial_energy"     , "100.0" );
        setProperty("cost_of_moving"     , "12.0"  );
        setProperty("watching"           , "0.0"   );
        setProperty("genesis_prob"       , "0.25"  );

        String home = System.getProperty("user.home");
        System.out.println("home is at " + home);
        File d = new File(home + "/.config/gol");
        if (!d.exists()) d.mkdirs();
        File f = new File(home + "/.config/gol/config.properties");
        if (f.exists() && !f.isDirectory()) {
            Reader source = new FileReader(f);
            System.out.println("reading config from " + f);
            load(source);
        }

        // re-store in same file so that new properties can be configured
        store(new FileWriter(f), null);
    }

    public int              intProperty(String name) { return Integer.parseInt(getProperty(name));   }
    public double        doubleProperty(String name) { return Double.parseDouble(getProperty(name)); }
    public String[] stringArrayProperty(String name) { return getProperty(name).split(",");          }

    public int      threads()             { return intProperty("threads");                 }
    public int      universe_size()       { return intProperty("universe_size");           }
    public int      pixel_size()          { return intProperty("pixel_size");              }
    public int      default_gene_length() { return intProperty("default_gene_length");     }
    public int      screenshot_interval() { return intProperty("screenshot_interval");     }
    public double   tick_energy()         { return doubleProperty("tick_energy");          }
    public double   initial_energy()      { return doubleProperty("initial_energy");       }
    public double   cost_of_moving()      { return doubleProperty("cost_of_moving");       }
    public double   watching()            { return doubleProperty("watching");             }
    public double   genesis_prob()        { return doubleProperty("genesis_prob");         }
}
