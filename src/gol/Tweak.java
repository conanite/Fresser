package gol;

public class Tweak<B extends Behaviour> extends Named implements Gene {
    public final String targetName;
    public final Changes changer;
    public final PropertyChanger<B> prop;

    public Tweak(String name, String targetName, Changes changer, PropertyChanger<B> prop) {
        this.name       = name;
        this.targetName = targetName;
        this.changer    = changer;
        this.prop       = prop;
    }

    public String toString() {
        return "" + name() + " on " + targetName;
    }

    public void install(Organism org) {
        B b = (B)org.blackboard.get(targetName);
        if (b == null) { return; }
        prop.modify(b, changer);
    }
}
