package gol;

public enum ShowStyle {
    age, colourage, genecount, eat, sunlight, fission, energy, colour, ground_energy;
    private static ShowStyle[] options = values();
    public ShowStyle next() {
        return options[(ordinal() + 1) % options.length];
    }
    public ShowStyle previous() {
        return options[(options.length + ordinal() - 1) % options.length];
    }
}
