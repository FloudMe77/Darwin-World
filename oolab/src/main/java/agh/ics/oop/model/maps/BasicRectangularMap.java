package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.newUtils.Genome;

import java.util.*;
import java.util.stream.Stream;

abstract public class BasicRectangularMap implements WorldMap {
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final Boundary boundary;
    private final int mapId;
    protected final MapStatistic mapStatistic;
    protected final GrassField grassField;
    protected final AnimalManager animalManager;
    protected final GrassManager grassManager;

    private static int counterOfId = 1;

    public BasicRectangularMap(int height, int width) {

        mapStatistic = new MapStatistic(this);
        mapId = counterOfId++;
        var lowerLeft = new Vector2d(0, 0);
        var upperRight = new Vector2d(width , height );
        boundary = new Boundary(lowerLeft, upperRight);
        grassField = new GrassField(height, width);

        this.grassManager = new GrassManager(width, height);
        this.animalManager = new AnimalManager(mapStatistic, grassManager);
    }

    public MapStatistic getMapStatistic() {
        return mapStatistic;
    }

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {
        for (var observer : observers) {
            observer.mapChanged(this, message, mapId);
        }
    }

    private void notifyMapStatistic(MapStatisticAction mapStatisticAction, int val){
        mapStatistic.updateStatistic(mapStatisticAction,val);
    }

    @Override
    public void place(Animal animal) {
        animalManager.addToAnimals(animal.getPosition(), animal);
        notifyObservers("Ustawiono animal na " + animal.getPosition());

        mapStatistic.newAnimalUpdate(animal);
    }

    @Override
    public void addGrass() {
        if(grassManager.addGrass()){
            notifyMapStatistic(MapStatisticAction.GRASS_AMOUNT,1);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalManager.isAnimalAt(position) || grassManager.isGrassAt(position);
    }

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

    @Override
    public void move(AbstractAnimal animal, int dailyDeclineValue) {
//        var oldPosition = animal.getPosition();
        animalManager.move(animal,dailyDeclineValue,this);

//        notifyObservers("Przeniesiono Animal z " + oldPosition + " do " + animal.getPosition());
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

    public WorldMap getValidator(AbstractAnimal animal) {
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

    public Genome getDominantGenome(){
        return animalManager.getDominantGenome();
    }

    @Override
    public void feedAnimals(int feedVal) {
        animalManager.feedAnimals(feedVal,grassField);
    }

    public List<Animal> reproduceAnimals(Config config){
        notifyObservers("utworzono nowe zwierzeta");
        return animalManager.reproduceAnimals(config);
    }

    public List<Animal> removeDepthAnimals(){
        notifyObservers("usunieto zwierzeta");
        return animalManager.removeDeadAnimals();
    }

    public void moveAllAnimals(int dailyDeclineValue) {
        animalManager.moveAllAnimals(dailyDeclineValue, this);
    }

}
