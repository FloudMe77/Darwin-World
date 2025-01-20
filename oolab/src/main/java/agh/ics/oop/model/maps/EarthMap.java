package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.Vector2d;

public class EarthMap extends BasicRectangularMap {
    private final int leftX = getCurrentBounds().leftDownCornerMap().x();
    private final int rightX = getCurrentBounds().rightUpperCornerMap().x();
    private final int lowerY = getCurrentBounds().leftDownCornerMap().y();
    private final int upperY = getCurrentBounds().rightUpperCornerMap().y();

    public EarthMap(int height, int width) {
        super(height, width);
    }

    // Move out of bounds is always okay because we have special behaviour implemented in methods below
    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.y() <= upperY && position.y() >= lowerY;
    }

    // Wrapping around the left and right edges of the map
    @Override
    public Vector2d getNewPosition(Vector2d oldPosition, Vector2d newPosition) {
        if (newPosition.x() > rightX) {
            return new Vector2d(leftX, newPosition.y());
        } else if (newPosition.x() < leftX) {
            return new Vector2d(rightX, newPosition.y());
        }

        return newPosition;
    }

    // Handling the poles
    @Override
    public MapDirection getNewMapDirection(Vector2d potentialPosition, MapDirection mapDirection) {
        if (potentialPosition.y() < lowerY || potentialPosition.y() > upperY) {
            return MapDirection.unitVectorToDirection(mapDirection.toUnitVector().opposite());
        }

        return mapDirection;
    }
}