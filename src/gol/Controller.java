package gol;

public class Controller implements Runnable, Global {
    public final Universe universe;
    public final Ribbon[] evens;
    public final Ribbon[] odds;

    public Controller(Universe u, int poolsize) {
        this.universe = u;

        if (u.edge % poolsize != 0) {
            throw new Error("universe size must be an integer multiple of poolsize: size is " + u.edge + ", poolsize is " + poolsize);
        }

        int ribbonCount = poolsize * 2;
        int columns     = u.edge / ribbonCount;
        evens           = new Ribbon[poolsize];
        odds            = new Ribbon[poolsize];

        for (int i = 0; i < poolsize; i++) {
            int j = i * 2;
            evens[i] = new Ribbon(j    , u, columns); // 0,2,4,6...
            odds[i]  = new Ribbon(j + 1, u, columns); // 1,3,5,7...
        }
    }

    public void run() {
        try {
            launch(evens);
            launch(odds);

            while(true) {
                tickStart();

                reset(evens);
                reset(odds);

                cycle(evens, "tick"    , "tick-done"    );
                cycle(odds , "tick"    , "tick-done"    );

                stats(evens);
                stats(odds);

                universe.nextGeneration();
            }
        } catch (InterruptedException ie) {
            throw new Error("interrupted!", ie);
        }
    }

    void reset(Ribbon[] ribbons)  {
        for(Ribbon r : ribbons) r.reset();
    }

    void tickStart()  {
        universe.births = 0;
        universe.deaths = 0;
        universe.total  = 0;
    }

    void stats(Ribbon[] ribbons)  {
        for(Ribbon r : ribbons) {
            universe.births += r.births;
            universe.deaths += r.deaths;
            universe.total  += r.total ;
        }
    }

    synchronized void cycle(Ribbon[] ribbons, String state, String waitfor) throws InterruptedException {
        for(Ribbon r : ribbons) {
            synchronized(r) {
                r.state = state;
                r.notify();
            }
        }

        for(Ribbon r : ribbons) {
            synchronized(r) {
                while(r.state != waitfor) { r.wait(); }
            }
        }
    }

    Thread[] launch(Ribbon[] ribbons) {
        Thread[] t = new Thread[ribbons.length];
        for(int i = 0; i < ribbons.length; i++) {
            t[i] = new Thread(ribbons[i]);
            t[i].start();
        }
        return t;
    }
}
