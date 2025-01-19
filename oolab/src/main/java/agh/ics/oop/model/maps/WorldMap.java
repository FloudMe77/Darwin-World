package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.Genome;

import java.util.List;
import java.util.Optional;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) forward.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal, int dailyDeclineValue);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    List<WorldElement> objectsAt(Vector2d position);

    List<WorldElement> getElements();

    Boundary getCurrentBounds();

    int getId();

    void addGrass();

    void feedAnimals(int feedVal);

    List<Animal> reproduceAnimals(Config config);

    void removeDepthAnimals();

    void moveAllAnimals(int dailyDeclineValue);

    void addObserver(MapChangeListener observer);

    void removeObserver(MapChangeListener observer);

    Genome getDominantGenome();
    MapStatistics getMapStatistics();

    public Optional<Animal> animalAt(Vector2d position);

    public boolean isGrassAt(Vector2d position);

    public List<Animal> getAnimals();

    Boundary getEquatorBoundary();
}