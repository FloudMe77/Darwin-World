package agh.ics.oop.model.MapObjects;

public interface WorldElement {

    boolean isAt(Vector2d position);

    Vector2d getPosition();
}
