package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.newUtils.Genome;

import java.util.Random;

public abstract class AbstractAnimal implements WorldElement {
    protected MapDirection currentDirection;
    protected Vector2d currentPosition;
    protected int age = 0;
    protected int eaten = 0;
    protected final Genome genome;
    protected int currentIndexOfGen = 0;


    public AbstractAnimal(Vector2d position, Genome genome) {
        this.currentPosition = position;
        this.genome = genome;
        Random random = new Random();
        // losowy kierunek początkowy
        currentDirection = MapDirection.values()[random.nextInt(8)];
    }


    public Vector2d getCurrentPosition() {
        return currentPosition;
    }

    public Genome getGenome() {
        return genome;
    }

    public int getAge() {
        return age;
    }

    public Vector2d getPosition() {
        return currentPosition;
    }

    public int getEaten() {
        return eaten;
    }

    public MapDirection getCurrentDirection() {
        return currentDirection;
    }

    // Do zastanowienia w przyszłości, można albo użyć unicodowych strzałek fajnych albo jakos inaczej przedstawiac zwierzak idk jak w sumie jeszcze
    public String toString() {
        return switch (currentDirection) {
            case NORTH -> "^";
            case SOUTH -> "v";
            case EAST -> ">";
            case WEST -> "<";
            case NORTHEAST -> "^>";
            case NORTHWEST -> "<^";
            case SOUTHEAST -> "v>";
            case SOUTHWEST -> "<v";
        };
    }

    @Override
    public boolean isAt(Vector2d position) {
        return currentPosition.equals(position);
    }

    public boolean atDirection(MapDirection direction) {
        return currentDirection.equals(direction);
    }

    public void move(MoveValidator validator) {
        GenomeDirection genomeDirection = genome.getGenVal(currentIndexOfGen);
        currentDirection = currentDirection.getDirection(genomeDirection);
        var positionAfterMove = currentPosition.add(currentDirection.toUnitVector());
        if (validator.canMoveTo(positionAfterMove)) {
            currentPosition = validator.getNewPosition(currentPosition, positionAfterMove);
        } else {
            currentDirection = validator.getNewMapDirection(positionAfterMove, currentDirection);
        }

        currentIndexOfGen = (currentIndexOfGen + 1) % (genome.getGenLength());
    }

}
