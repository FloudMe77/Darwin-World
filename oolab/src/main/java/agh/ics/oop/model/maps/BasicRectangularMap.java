package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.newUtils.Genome;

import java.util.*;

abstract public class BasicRectangularMap implements WorldMap {
    protected final Map<Vector2d, List<AbstractAnimal>> animals = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    protected final List<AbstractAnimal> deathAnimals = new ArrayList<>(); //nie potrzebuje pozycji zmarłych zwierząt
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
    public void move(AbstractAnimal animal) {
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

        for (List<AbstractAnimal> animalList : animals.values()) {
            elements.addAll(animalList);
        }

        elements.addAll(grasses.values());
        return Collections.unmodifiableList(elements);
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

    // możliwe ze w przyszłosci to będzie public?
    protected void addToAnimals(Vector2d position, AbstractAnimal animal) {
        if (!animals.containsKey(position)) {

            animals.put(position, new ArrayList<>());
        }

        animals.get(position).add(animal);
    }

    protected void removeFromAnimals(Vector2d position, AbstractAnimal animal) {
        List<AbstractAnimal> animalList = animals.get(position);
        if (animalList != null) {
            animalList.remove(animal);
            if (animalList.isEmpty()) {
                animals.remove(position);

            }
        }
        deathAnimals.add(animal);
    }
    public int getAnimalAmount(){
        return animals.values().stream()
                .mapToInt(List::size) // Pobieramy rozmiar każdej listy
                .sum(); // Sumujemy rozmiary
    }

    public int getGrassAmount(){
        return grasses.size();
    }

    public float getAverageEnergy(){
        int energySum = animals.values().stream()
                .flatMap(List::stream) // Spłaszczenie wszystkich list z mapy do jednego strumienia
                .filter(abstractAnimal -> abstractAnimal instanceof Animal animal) // Filtrujemy tylko instancje Animal
                .mapToInt(animal -> ((Animal) animal).getEnergy()) // Rzutowanie i pobranie energii
                .sum(); // Sumujemy wartości energii
        return (float) energySum /getAnimalAmount();
    }

    public float getAverageLifeTime(){
        int ageSum = deathAnimals.stream()
                .mapToInt(AbstractAnimal::getAge) // Pobieramy wiek każdego zwierzęcia
                .sum(); // Sumujemy wartości
        return (float) ageSum /deathAnimals.size();
    }

    public float getAverageChildrenAmount(){
        int childrenSum = animals.values().stream()
                .flatMap(List::stream) // Spłaszczenie list w mapie do pojedynczego strumienia obiektów
                .filter(abstractAnimal -> abstractAnimal instanceof Animal) // Filtrujemy tylko instancje Animal
                .mapToInt(abstractAnimal -> ((Animal) abstractAnimal).getChildrenAmount()) // Rzutujemy i pobieramy ilość dzieci
                .sum(); // Sumujemy wartości
        return (float) childrenSum /getAnimalAmount();
    }

    public Genome getDominantGenome(){
        HashMap<Genome,Integer> genomesCounter = new HashMap<>();
        for(var animalList: animals.values()){
            for(var animal:animalList){
                if(!(animal instanceof Animal)){
                    continue;
                }
                Genome genome = animal.getGenome();
                if(genomesCounter.containsKey(genome)){
                    genomesCounter.put(genome,1);
                }
                else{
                    genomesCounter.put(genome,genomesCounter.get(genome)+1);
                }
            }
        }
        Optional<Map.Entry<Genome, Integer>> maxEntry = genomesCounter.entrySet().stream()
                .max(Map.Entry.comparingByValue()); // Porównujemy po wartościach

        if (maxEntry.isPresent()) {
            return maxEntry.get().getKey();
        } else {
            throw new RuntimeException("brak elementów w liście"); //tymczasowe, żeby tylko zabezpieczyć
        }
    }
}
