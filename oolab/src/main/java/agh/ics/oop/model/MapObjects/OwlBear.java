package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.genomes.Genome;

public class OwlBear extends AbstractAnimal {

    public OwlBear(Vector2d position, Genome genome) {
        super(position, genome);
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
