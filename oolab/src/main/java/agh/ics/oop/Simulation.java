package agh.ics.oop;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final List<MoveDirection> directionList;
    private final WorldMap worldMap;

    public Simulation(List<Vector2d> positionList, List<MoveDirection> directionList, WorldMap worldMap) {
        this.animals = new ArrayList<>();
        this.directionList = directionList;
        this.worldMap = worldMap;

        for (Vector2d position : positionList) {
            Animal animal = new Animal(position);
            try{
                worldMap.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println("Uwaga: " + e.getMessage());
            }
        }

    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }

    public void run() {
        int numberOfAnimal = 0;
        for (MoveDirection direction : directionList) {
            var currentAnimal = animals.get(numberOfAnimal);
            worldMap.move(currentAnimal, direction);
            numberOfAnimal = (numberOfAnimal + 1) % animals.size();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Wątek został przerwany"+e.getMessage());
            }
        }
    }
}
