package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator2 implements Iterable<Vector2d> {
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final int count;
    private final List<Vector2d> allPossiblePositions;

    public RandomPositionGenerator2(Vector2d lowerLeft, Vector2d upperRight,int count ) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.count = count;

        allPossiblePositions = findAllPositions();
    }

    private List<Vector2d> findAllPositions() {
        List<Vector2d> allPossiblePositions = new ArrayList<>();

        for (int x = lowerLeft.getX(); x < upperRight.getX()+1; x++) {
            for (int y = lowerLeft.getY(); y < upperRight.getY()+1; y++) {
                allPossiblePositions.add(new Vector2d(x, y));
            }
        }
        return allPossiblePositions;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return allPossiblePositions.subList(0, count).iterator();
    }
}