package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.OwlBear;
import agh.ics.oop.model.MapObjects.Vector2d;
import agh.ics.oop.model.MapObjects.WorldElement;
import agh.ics.oop.model.genomes.Genome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OwlBearMap extends EarthMap {
    private final OwlBear owlBear; // add final
    private final OwlBearBounds owlBearMap;

    public OwlBearMap(int height, int width, Genome genome) { // czemu mapa dostaje genom?
        super(height, width);
        int bearSideLength = (int) Math.floor(Math.sqrt(height * width * 0.2));

        Random random = new Random(); // nowy obiekt co wywołanie?
        int startX = random.nextInt(bearSideLength);
        int startY = random.nextInt(bearSideLength);
        Vector2d lowerLeftOwlBearMap = new Vector2d(startX, startY);
        Vector2d upperRightOwlBearMap = new Vector2d(startX + bearSideLength, startY + bearSideLength);

        owlBearMap = new OwlBearBounds(lowerLeftOwlBearMap, upperRightOwlBearMap);
        owlBear = new OwlBear(new Vector2d((2 * startX + bearSideLength) / 2, (startY + bearSideLength / 2)), genome);
    }

    public OwlBearMap(int height, int width) {
        this(height, width, new Genome(100));
    }

    public OwlBearBounds getOwlBearMap() {
        return owlBearMap;
    }

    public Vector2d getOwlBearPosition() {
        return owlBear.getPosition();
    }

    public OwlBear getOwlBear() {
        return owlBear;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return owlBear.isAt(position) || super.isOccupied(position);
    }

    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        var elements = new ArrayList<>(super.objectsAt(position));
        if (owlBear.isAt(position)) {
            elements.addFirst(owlBear);
        }
        return Collections.unmodifiableList(elements);
    }

    public List<WorldElement> getElements() {
        var elements = new ArrayList<>(super.getElements());
        elements.add(owlBear);

        return Collections.unmodifiableList(elements);
    }

    @Override
    public void feedAnimals(int feedVal) {
        List<Animal> animalsAtPosition = animalManager.getAnimals(owlBear.getPosition());
        if (animalsAtPosition != null) {
            for (var animal : new ArrayList<>(animalsAtPosition)) {
                animal.die();
                animalManager.removeFromAnimals(animal.getPosition(), animal);
                mapStatistics.deathAnimalUpdate(animal);

            }
        }
        super.feedAnimals(feedVal);
    }

    @Override
    public void moveAllAnimals(int dailyDeclineValue) {
        owlBear.move(owlBearMap);
        super.moveAllAnimals(dailyDeclineValue);
    }
}

