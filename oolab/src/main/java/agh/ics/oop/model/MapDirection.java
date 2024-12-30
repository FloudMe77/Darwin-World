package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    static final Vector2d unitNorth = new Vector2d(0, 1);
    static final Vector2d unitSouth = new Vector2d(0, -1);
    static final Vector2d unitWest = new Vector2d(-1, 0);
    static final Vector2d unitEast = new Vector2d(1, 0);

    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case EAST -> SOUTH;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case EAST -> NORTH;
            case WEST -> SOUTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> unitNorth;
            case SOUTH -> unitSouth;
            case EAST -> unitEast;
            case WEST -> unitWest;
        };
    }
}