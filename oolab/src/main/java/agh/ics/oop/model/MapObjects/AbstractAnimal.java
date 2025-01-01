package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.newUtils.Genom;

public abstract class AbstractAnimal implements WorldElement {
    protected MapDirection currentDirection = MapDirection.NORTH;
    protected Vector2d currentPosition;
    protected int age = 0;
    protected int Eaten = 0;
    protected final Genom genom;
    protected int currentIndexOfGen = 0;


    public AbstractAnimal(Vector2d position, Genom genom ) {
        this.currentPosition = position;
        this.genom = genom;
    }


    public Vector2d getCurrentPosition() {
        return currentPosition;
    }

    public Genom getGenom() {
        return genom;
    }

    public int getAge() {
        return age;
    }

    public Vector2d getPosition() {
        return currentPosition;
    }

    public int getEaten() {
        return Eaten;
    }

    public MapDirection getCurrentDirection() {
        return currentDirection;
    }

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
        int currentGen = genom.getGenVal(currentIndexOfGen);
        currentDirection = currentDirection.next(currentGen);
        var positionAfterMove = currentPosition.add(currentDirection.toUnitVector());
        if (validator.canMoveTo(positionAfterMove)) {
            currentPosition = positionAfterMove;
        }
        currentIndexOfGen = (currentIndexOfGen +1)%(genom.getGenLength());
    }

}
