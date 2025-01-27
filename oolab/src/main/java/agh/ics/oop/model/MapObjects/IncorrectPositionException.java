package agh.ics.oop.model.MapObjects;

public class IncorrectPositionException extends Exception {
    private final Vector2d position;

    public IncorrectPositionException(Vector2d position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("Position %s is not correct", position);
    }
}
