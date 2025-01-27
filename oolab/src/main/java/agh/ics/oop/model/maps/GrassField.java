package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.Vector2d;
import agh.ics.oop.model.util.Boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GrassField {
    private final List<Vector2d> equator;
    private final List<Vector2d> beyondEquator;

    private final Vector2d lowerLeftEquator;
    private final Vector2d upperRightEquator;

    public GrassField(int height, int width) {
        Vector2d lowerLeftBelowEquator = new Vector2d(0, 0);
        Vector2d upperRightBelowEquator = new Vector2d(width, (int) ((1.0 / 3) * height));

        lowerLeftEquator = new Vector2d(0, (int) ((1.0 / 3) * height) + 1);
        upperRightEquator = new Vector2d(width, (int) ((2.0 / 3) * height));

        Vector2d lowerLeftAboveEquator = new Vector2d(0, (int) ((2.0 / 3) * height) + 1);
        Vector2d upperRightAboveEquator = new Vector2d(width, height);


        equator = getAllPositionBetween(lowerLeftEquator, upperRightEquator);

        beyondEquator = getAllPositionBetween(lowerLeftBelowEquator, upperRightBelowEquator);
        beyondEquator.addAll(getAllPositionBetween(lowerLeftAboveEquator, upperRightAboveEquator));
    }

    private List<Vector2d> getAllPositionBetween(Vector2d lowerLeft, Vector2d upperRight) {
        List<Vector2d> allPossiblePositions = new ArrayList<>();

        for (int x = lowerLeft.x(); x < upperRight.x() + 1; x++) {
            for (int y = lowerLeft.y(); y < upperRight.y() + 1; y++) {
                allPossiblePositions.add(new Vector2d(x, y));
            }
        }
        return allPossiblePositions;
    }

    public Optional<Vector2d> getNewGrassPosition() {
        // parytet 80:20
        Random random = new Random();

        // Jeżeli trafiło do tych 20% lub equator jest pusty
        if ((!beyondEquator.isEmpty() && random.nextInt(5) == 0) || (!beyondEquator.isEmpty() && equator.isEmpty())) {
            int index = random.nextInt(beyondEquator.size());
            Vector2d chosen = beyondEquator.get(index);
            beyondEquator.remove(index);
            return Optional.of(chosen);
        }

        // Jeżeli equator nie jest pusty
        if (!equator.isEmpty()) {
            int index = random.nextInt(equator.size());
            Vector2d chosen = equator.get(index);
            equator.remove(index);
            return Optional.of(chosen);
        }

        // Jeżeli żadna z list nie ma elementów
        return Optional.empty();
    }

    public void addGrassPosition(Vector2d position) {
        if (position.follows(lowerLeftEquator) && position.precedes(upperRightEquator)) {
            if (equator.contains(position)) {
                throw new IllegalArgumentException();
            }
            equator.add(position);
        } else {
            if (beyondEquator.contains(position)) {
                throw new IllegalArgumentException();
            }
            beyondEquator.add(position);
        }
    }

    public Boundary getEquatorBoundary() {
        return new Boundary(lowerLeftEquator, upperRightEquator);
    }
}
