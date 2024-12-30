package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap extends AbstractWorldMap {
    private final Vector2d leftDownBorderToMove;
    private final Vector2d rightUpperBorderToMove;

    public RectangularMap(int width, int height) {
        super();
        this.leftDownBorderToMove = new Vector2d(0,0);
        this.rightUpperBorderToMove = new Vector2d(width-1,height-1);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position) && position.follows(leftDownBorderToMove) && position.precedes(rightUpperBorderToMove);
    }
    @Override
    public Boundary getCurrentBounds(){
        return new Boundary(leftDownBorderToMove,rightUpperBorderToMove);
    }
}
