package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.Boundary;

public class OwlBearBounds implements MoveValidator {
    private final Boundary boundary;

    public OwlBearBounds(Vector2d lowerLeft, Vector2d upperRight) {
        boundary = new Boundary(lowerLeft, upperRight);
    }

    public Boundary getCurrentBounds() {
        return boundary;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(boundary.leftDownCornerMap()) && position.precedes(boundary.rightUpperCornerMap());
    }

    @Override
    public Vector2d getNewPosition(Vector2d oldPosition, Vector2d newPosition) {
        return newPosition;
    }

    @Override
    public MapDirection getNewMapDirection(Vector2d newPosition, MapDirection mapDirection) {
        return mapDirection;
    }

}
