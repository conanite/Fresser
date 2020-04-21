package gol;

public interface PropertyChanger<B extends Behaviour> {
    void modify(B b, Changes c);
}
