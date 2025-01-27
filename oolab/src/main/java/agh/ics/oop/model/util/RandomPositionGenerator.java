package agh.ics.oop.model.util;

import agh.ics.oop.model.MapObjects.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final int count;
    private final List<Vector2d> allPossiblePositions;

    public RandomPositionGenerator(Vector2d lowerLeft, Vector2d upperRight, int count) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.count = count;

        allPossiblePositions = findAllPositions();
        Collections.shuffle(allPossiblePositions);
    }

    private List<Vector2d> findAllPositions() {
        List<Vector2d> allPossiblePositions = new ArrayList<>();

        for (int x = lowerLeft.x(); x < upperRight.x() + 1; x++) {
            for (int y = lowerLeft.y(); y < upperRight.y() + 1; y++) {
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