package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

abstract public class BasicRectangularMap implements WorldMap {
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final Vector2d upperRight;
    private final Vector2d lowerLeft;
    private final Boundary boundary;
    private final int mapId;

    private static int counterOfId = 1;

    public BasicRectangularMap(int height, int width) {
        mapId = counterOfId++;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
        boundary = new Boundary(lowerLeft, upperRight);
    }

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (var observer : observers) {
            observer.mapChanged(this, message, mapId);
        }
    }

    @Override
    public void place(Animal animal) {
        addToAnimals(animal.getPosition(), animal);
        notifyObservers("Ustawiono animal na " + animal.getPosition());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        List<WorldElement> elements = new ArrayList<>();

        if (animals.containsKey(position)) {
            elements.addAll(animals.get(position));
        }

        if (grasses.containsKey(position)) {
            elements.add(grasses.get(position));
        }

        return elements;
    }

    @Override
    public void move(Animal animal) {
        var oldPosition = animal.getPosition();
        removeFromAnimals(animal.getPosition(), animal);
        animal.move(getValidator(animal));
        addToAnimals(animal.getPosition(), animal);
        notifyObservers("Przeniesiono Animal z " + oldPosition + " do " + animal.getPosition());
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(lowerLeft) && position.precedes(upperRight);
    }

    @Override
    public Vector2d getNewPosition(Vector2d oldPosition, Vector2d newPosition) {
        return newPosition;
    }

    @Override
    public MapDirection getNewMapDirection(Vector2d newPosition, MapDirection mapDirection) {
        return mapDirection;
    }

    public List<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>();

        for (List<Animal> animalList : animals.values()) {
            elements.addAll(animalList);
        }

        elements.addAll(grasses.values());
        return Collections.unmodifiableList(elements);
    }

    public WorldMap getValidator(Animal animal) {
        return this;
    };

    @Override
    public String toString() {
        var boundary = getCurrentBounds();
        return visualizer.draw(boundary.leftDownCornerMap(), boundary.rightUpperCornerMap());
    }

    @Override
    public int getId() {
        return mapId;
    }

    @Override
    public Boundary getCurrentBounds() {
        return boundary;
    }

    // możliwe ze w przyszłosci to będzie public?
    protected void addToAnimals(Vector2d position, Animal animal) {
        if (!animals.containsKey(position)) {
            animals.put(position, new ArrayList<>());
        }

        animals.get(position).add(animal);
    }

    protected void removeFromAnimals(Vector2d position, Animal animal) {
        List<Animal> animalList = animals.get(position);
        if (animalList != null) {
            animalList.remove(animal);
            if (animalList.isEmpty()) {
                animals.remove(position);
            }
        }
    }
}
