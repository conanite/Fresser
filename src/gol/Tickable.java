package gol;

public interface Tickable {
    boolean alive();
    void    pretick();
    void    tick();
    void    posttick();
}
