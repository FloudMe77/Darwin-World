package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Genome;

import java.util.*;
import java.util.stream.Stream;

abstract public class BasicRectangularMap implements WorldMap {
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final Boundary boundary;
    private final int mapId;
    protected final MapStatistics mapStatistics;

    protected final AnimalManager animalManager;
    protected final GrassManager grassManager;

    private static int counterOfId = 1;

    public BasicRectangularMap(int height, int width) {
        mapId = counterOfId++;
        var lowerLeft = new Vector2d(0, 0);
        var upperRight = new Vector2d(width - 1, height - 1);
        boundary = new Boundary(lowerLeft, upperRight);

        this.grassManager = new GrassManager( height-1,width -1);
        mapStatistics = new MapStatistics(this);
        this.animalManager = new AnimalManager(mapStatistics, grassManager);
    }

    public MapStatistics getMapStatistics() {
        return mapStatistics;
    }

    @Override
    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {
        for (var observer : observers) {
            observer.mapChanged(this, message, mapId);
        }
    }

    @Override
    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    @Override
    public void place(Animal animal) {
        animalManager.addToAnimals(animal.getPosition(), animal);
        notifyObservers("Ustawiono animal na " + animal.getPosition());

        mapStatistics.newAnimalUpdate(animal);
    }

    @Override
    public void addGrass() {
        if(grassManager.addGrass()){
            mapStatistics.grassUpdate(1);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalManager.isAnimalAt(position) || grassManager.isGrassAt(position);
    }

    // jest nie mała szansa ze ta metoda jest zbędna bo mozna uzywac managerów zamiennie
    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        List<WorldElement> elements = new ArrayList<>();

        if (animalManager.isAnimalAt(position)) {
            elements.addAll(animalManager.getAnimals(position));
        }
        if (grassManager.isGrassAt(position)) {
            elements.add(grassManager.getGrass(position));
        }
        return elements;
    }

    public Optional<Animal> animalAt(Vector2d position) {
        return animalManager.isAnimalAt(position)
                ? animalManager.getStrongestAnimal(position)
                : Optional.empty();
    }

    public boolean isGrassAt(Vector2d position){
        return grassManager.isGrassAt(position);
    }

    @Override
    public void move(Animal animal, int dailyDeclineValue) {
        animalManager.move(animal,dailyDeclineValue,this);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(boundary.leftDownCornerMap()) && position.precedes(boundary.rightUpperCornerMap());
    }

    // dziwne
    @Override
    public Vector2d getNewPosition(Vector2d oldPosition, Vector2d newPosition) {
        return newPosition;
    }

    // dziwne
    @Override
    public MapDirection getNewMapDirection(Vector2d newPosition, MapDirection mapDirection) {
        return mapDirection;
    }

    @Override
    public List<WorldElement> getElements() {
        return Stream.concat(
                animalManager.getElements().stream(),
                grassManager.getElements().stream()
        ).toList();
    }


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

    @Override
    public Genome getDominantGenome(){
        return animalManager.getDominantGenome();
    }

    @Override
    public void feedAnimals(int feedVal) {
        notifyObservers("nakarmiono animale");
        animalManager.feedAnimals(feedVal);
    }

    public void reproduceAnimals(Config config){
        notifyObservers("utworzono nowe zwierzeta");
        animalManager.reproduceAnimals(config);
    }

    public void removeDepthAnimals(){
        notifyObservers("usunieto zwierzeta");
        animalManager.removeDeadAnimals();
    }

    public void moveAllAnimals(int dailyDeclineValue) {
        animalManager.moveAllAnimals(dailyDeclineValue, this);
        notifyObservers("poruszono zwierzeta");
    }

    public List<Animal> getAnimals(){
        return animalManager.getElements();
    }

    @Override
    public Boundary getEquatorBoundary() {
        return grassManager.getEquatorBoundary();
    }
}
