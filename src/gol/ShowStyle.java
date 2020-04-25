package gol;

public enum ShowStyle {
    age, colourage, genecount, eat, sunlight, fission, energy, ground_energy, colour, inverse_age;
    private static ShowStyle[] options = values();
    public ShowStyle next() {
        return options[(ordinal() + 1) % options.length];
    }
    public ShowStyle previous() {
        return options[(options.length + ordinal() - 1) % options.length];
    }
}
