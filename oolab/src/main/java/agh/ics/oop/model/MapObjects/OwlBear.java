package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.newUtils.Genom;

public class OwlBear extends AbstractAnimal {

    public OwlBear(Vector2d position, Genom genom) {
        super(position, genom);
    }

    @Override
    public String toString() {
        return switch (currentDirection) {
            case NORTH -> "*^*";
            case SOUTH -> "*v*";
            case EAST -> "*>*";
            case WEST -> "*<*";
            case NORTHEAST -> "*^>*";
            case NORTHWEST -> "*<^*";
            case SOUTHEAST -> "*v>*";
            case SOUTHWEST -> "*<v*";
        };
    }
}
