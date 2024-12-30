package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;
    private final List<Vector2d> allPossiblePositions;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.grassCount = grassCount;

        allPossiblePositions = findAllPositions();
        Collections.shuffle(allPossiblePositions);
    }

    private List<Vector2d> findAllPositions() {
        List<Vector2d> allPossiblePositions = new ArrayList<Vector2d>();
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                allPossiblePositions.add(new Vector2d(x, y));
            }
        }
        return allPossiblePositions;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return allPossiblePositions.subList(0, grassCount).iterator();
    }
}