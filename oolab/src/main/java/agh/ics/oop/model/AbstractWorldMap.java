package agh.ics.oop.model;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap {
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final int mapId;

    private static int counterOfId = 1;

    public AbstractWorldMap(){
        mapId = counterOfId++;
    }


    public void addObserver(MapChangeListener observer){
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer){
        observers.remove(observer);
    }

    private void notifyObservers(String message){
        for(var observer:observers){
            observer.mapChanged(this,message, mapId);
        }
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (!animals.containsKey(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            notifyObservers("Ustawiono animal na "+animal.getPosition());
        }
        else {
            throw new IncorrectPositionException(animal.getPosition());
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        var oldPosition = animal.getPosition();
        animals.remove(animal.getPosition());
        animal.move(direction, this);
        animals.put(animal.getPosition(), animal);
        notifyObservers("Przeniesiono Animal z " + oldPosition + " do " + animal.getPosition());
    }

    public List<WorldElement> getElements(){
        return new ArrayList<>(animals.values());
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !animals.containsKey(position);
    }

    @Override
    public String toString(){
        var boundary = getCurrentBounds();
        return visualizer.draw(boundary.leftDownCornerMap(),boundary.rightUpperCornerMap());
    }
    public int getId(){
        return mapId;
    }
}
