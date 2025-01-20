package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.Vector2d;
import agh.ics.oop.model.genomes.GenomeDirection;

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

    public MapDirection getDirection(GenomeDirection genomeDirection) {
        return switch (genomeDirection) {
            case ZERO -> NORTH;
            case ONE -> NORTHEAST;
            case TWO -> EAST;
            case THREE -> SOUTHEAST;
            case FOUR -> SOUTH;
            case FIVE -> SOUTHWEST;
            case SIX -> WEST;
            case SEVEN -> NORTHWEST;
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