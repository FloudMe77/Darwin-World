package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MapStatistic;
import agh.ics.oop.model.Vector2d;

public class EarthMap extends BasicRectangularMap {
    private final int leftX = getCurrentBounds().leftDownCornerMap().getX();
    private final int rightX = getCurrentBounds().rightUpperCornerMap().getX();
    private final int lowerY = getCurrentBounds().leftDownCornerMap().getY();
    private final int upperY = getCurrentBounds().rightUpperCornerMap().getY();

    public EarthMap(int height, int width) {
        super(height, width);
    }

    // Move out of bounds is always okay because we have special behaviour implemented in methods below
    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getY() <= upperY && position.getY() >= lowerY;
    }

    // Wrapping around the left and right edges of the map
    @Override
    public Vector2d getNewPosition(Vector2d oldPosition, Vector2d newPosition) {
        if (newPosition.getX() > rightX) {
            return new Vector2d(leftX, newPosition.getY());
        } else if (newPosition.getX() < leftX) {
            return new Vector2d(rightX, newPosition.getY());
        }

        return newPosition;
    }

    // Handling the poles
    @Override
    public MapDirection getNewMapDirection(Vector2d potentialPosition, MapDirection mapDirection) {
        if (potentialPosition.getY() < lowerY || potentialPosition.getY() > upperY) {
            return MapDirection.unitVectorToDirection(mapDirection.toUnitVector().opposite());
        }

        return mapDirection;
    }
}