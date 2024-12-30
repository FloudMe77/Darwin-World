package agh.ics.oop.model;

import agh.ics.oop.model.RectangularMap;

public class Animal implements WorldElement{
    private MapDirection currentDirection = MapDirection.NORTH;
    private Vector2d currentPosition;


    public Animal(Vector2d position) {
        this.currentPosition = position;
    }

    public Animal() {
        this(new Vector2d(2, 2));
    }

    public Vector2d getPosition() {
        return currentPosition;
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
        };
    }

    @Override
    public boolean isAt(Vector2d position) {
        return currentPosition.equals(position);
    }
    public boolean atDirection(MapDirection direction) {
        return currentDirection.equals(direction);
    }

    public void move(MoveDirection direction, MoveValidator validator) {
        switch (direction) {
            case RIGHT -> currentDirection = currentDirection.next();

            case LEFT -> currentDirection = currentDirection.previous();

            case FORWARD -> {
                var positionAfterMove = currentPosition.add(currentDirection.toUnitVector());
                if (validator.canMoveTo(positionAfterMove)) {
                    currentPosition = positionAfterMove;
                }
            }
            case BACKWARD -> {
                var positionAfterMove = currentPosition.add(currentDirection.toUnitVector().opposite());
                if (validator.canMoveTo(positionAfterMove)) {
                    currentPosition = positionAfterMove;
                }
            }
        }
    }
}
