package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTHWEST,
    SOUTHWEST,
    NORTHEAST,
    SOUTHEAST;

    static final Vector2d unitNorth = new Vector2d(0, 1);
    static final Vector2d unitSouth = new Vector2d(0, -1);
    static final Vector2d unitWest = new Vector2d(-1, 0);
    static final Vector2d unitEast = new Vector2d(1, 0);
    static final Vector2d unitNorthEast = new Vector2d(1, 1);
    static final Vector2d unitNorthWest = new Vector2d(-1, 1);
    static final Vector2d unitSouthEast = new Vector2d(1, -1);
    static final Vector2d unitSouthWest = new Vector2d(-1, -1);

    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
            case NORTHEAST -> "Północny wschód";
            case SOUTHEAST -> "Południowy wschód";
            case NORTHWEST -> "Północny zachód";
            case SOUTHWEST -> "Południowy zachód";
        };
    }
    // Przesunięcie do następnej wartości
    public MapDirection next(int jumpTo) {
        return values()[(ordinal() + jumpTo) % values().length];
    }

    // Przesunięcie do poprzedniej wartości
    public MapDirection previous(int jumpTo) {
        return values()[(ordinal() - jumpTo + values().length) % values().length];
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> unitNorth;
            case SOUTH -> unitSouth;
            case EAST -> unitEast;
            case WEST -> unitWest;
            case NORTHEAST -> unitNorthEast;
            case NORTHWEST -> unitNorthWest;
            case SOUTHEAST -> unitSouthEast;
            case SOUTHWEST -> unitSouthWest;
        };
    }
}