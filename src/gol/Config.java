package gol;

import java.io.*;
import java.util.*;

class Config extends Properties implements Global {
    public Config() throws IOException {
        super();
        setProperty("threads"             , "4"         );
        setProperty("universe_size"       , "256"       );
        setProperty("pixel_size"          , "8"         );
        setProperty("tick_energy"         , "100.0"     );
        setProperty("default_gene_length" , "32"        );
        setProperty("reach_length"        , "2"         );
        setProperty("initial_energy"      , "100.0"     );
        setProperty("cost_of_moving"      , "12.0"      );
        setProperty("watching"            , "0.0"       );
        setProperty("genesis_prob"        , "0.25"      );
        setProperty("capture_view"        , "colourage" );
        setProperty("capture_pixel_size"  , "1"         );
        setProperty("capture_interval_min", "50"        );
        setProperty("capture_interval_max", "500"       );
        setProperty("capture_trigger"     , "0.1"       );

        String home = System.getProperty("user.home");
        o.println("home is at " + home);
        File d = new File(home + "/.config/gol");
        if (!d.exists()) d.mkdirs();
        File f = new File(home + "/.config/gol/config.properties");
        if (f.exists() && !f.isDirectory()) {
            Reader source = new FileReader(f);
            o.println("reading config from " + f);
            load(source);
        }

        // re-store in same file so that new properties can be configured
        store(new FileWriter(f), null);
    }

    public int              intProperty(String name) { return Integer.parseInt(getProperty(name));   }
    public double        doubleProperty(String name) { return Double.parseDouble(getProperty(name)); }
    public String        stringProperty(String name) { return getProperty(name);                     }
    public String[] stringArrayProperty(String name) { return getProperty(name).split(",");          }

    public int    threads()                 { return intProperty("threads");                 }
    public int    universe_size()           { return intProperty("universe_size");           }
    public int    pixel_size()              { return intProperty("pixel_size");              }
    public int    default_gene_length()     { return intProperty("default_gene_length");     }
    public int    reach_length()            { return intProperty("reach_length");            }
    public int    capture_pixel_size()      { return intProperty("capture_pixel_size");      }
    public int    capture_interval_min()    { return intProperty("capture_interval_min");    }
    public int    capture_interval_max()    { return intProperty("capture_interval_max");    }

    public String capture_view()            { return stringProperty("capture_view");         }

    public double capture_trigger()         { return doubleProperty("capture_trigger");      }
    public double tick_energy()             { return doubleProperty("tick_energy");          }
    public double initial_energy()          { return doubleProperty("initial_energy");       }
    public double cost_of_moving()          { return doubleProperty("cost_of_moving");       }
    public double watching()                { return doubleProperty("watching");             }
    public double genesis_prob()            { return doubleProperty("genesis_prob");         }
}
