package agh.ics.oop.model;

public enum MapDirection {
    NORTH(new Vector2d(0, 1)),
    NORTHEAST(new Vector2d(1, 1)),
    NORTHWEST(new Vector2d(-1, 1)),
    SOUTH(new Vector2d(0, -1)),
    SOUTHEAST(new Vector2d(1, -1)),
    SOUTHWEST(new Vector2d(-1, -1)),
    WEST(new Vector2d(-1, 0)),
    EAST(new Vector2d(1, 0));

    private final Vector2d unitVector;

    MapDirection(Vector2d unitVector) {
        this.unitVector = unitVector;
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case NORTHEAST -> "Północny Wschód";
            case NORTHWEST -> "Północny Zachód";
            case SOUTH -> "Południe";
            case SOUTHEAST -> "Południowy Wschód";
            case SOUTHWEST -> "Południowy Zachód";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHWEST -> WEST;
            case WEST -> SOUTHWEST;
            case SOUTHWEST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHEAST -> EAST;
            case EAST -> NORTHEAST;
            case NORTHEAST -> NORTH;
        };
    }

    public Vector2d toUnitVector() {
        return unitVector;
    }

    public static MapDirection unitVectorToDirection(Vector2d vector) {
        for (var direction : MapDirection.values()) {
            if (direction.toUnitVector().equals(vector)) {
                return direction;
            }
        }
        
        throw new IllegalArgumentException("No direction for vector: " + vector);
    }
}