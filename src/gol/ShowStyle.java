public enum ShowStyle {
    colour, energy, ground_energy, age;
    private static ShowStyle[] options = values();
    public ShowStyle next() {
        return options[(ordinal() + 1) % options.length];
    }
    public ShowStyle previous() {
        return options[(options.length + ordinal() - 1) % options.length];
    }
}
