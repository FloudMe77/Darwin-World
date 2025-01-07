package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.newUtils.Genome;
import agh.ics.oop.model.util.newUtils.GenomeChange;

import javax.swing.*;
import javax.swing.text.Position;
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
    private final List<Vector2d> equator;
    private final List<Vector2d> beyondEquator;
    private final Vector2d lowerLeftEquator;
    private final Vector2d upperRightEquator;

    private static int counterOfId = 1;

    public BasicRectangularMap(int height, int width) {
        mapId = counterOfId++;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width , height );
        boundary = new Boundary(lowerLeft, upperRight);

        Vector2d lowerLeftBelowEquator = new Vector2d(0, 0);
        Vector2d upperRightBelowEquator = new Vector2d(width, (int) ((1.0/3) * height));

        lowerLeftEquator = new Vector2d(0, (int) ((1.0/3) * height)+1);
        upperRightEquator = new Vector2d(width, (int) ((2.0/3) * height));

        Vector2d lowerLeftAboveEquator = new Vector2d(0, (int) ((2.0/3) * height)+1);
        Vector2d upperRightAboveEquator = new Vector2d(width, height);


        equator = getAllPositionBetween(lowerLeftEquator,upperRightEquator);

        beyondEquator = getAllPositionBetween(lowerLeftBelowEquator,upperRightBelowEquator);
        beyondEquator.addAll(getAllPositionBetween(lowerLeftAboveEquator,upperRightAboveEquator));
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
    public void addGrass() {
        // parytet 80:20
        Random random = new Random();
        // jeżeli trafiło do tych 20%
        if((!beyondEquator.isEmpty() && random.nextInt(5)==0) || (!beyondEquator.isEmpty() && equator.isEmpty())){
            int index = random.nextInt(0,beyondEquator.size());
            var grass = new Grass(beyondEquator.get(index));
            beyondEquator.remove(index);
            grasses.put(grass.getPosition(),grass);
            System.out.println("dodano " + grass.getPosition().toString());
        }
        else if(!equator.isEmpty()){
            int index = random.nextInt(0,equator.size());
            var grass = new Grass(equator.get(index));
            equator.remove(index);
            grasses.put(grass.getPosition(),grass);
            System.out.println("dodano " + grass.getPosition().toString());
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position) || grasses.containsKey(position);
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
//        var oldPosition = animal.getPosition();
        removeFromAnimals(animal.getPosition(), animal);
        animal.move(getValidator(animal));
        addToAnimals(animal.getPosition(), animal);
//        notifyObservers("Przeniesiono Animal z " + oldPosition + " do " + animal.getPosition());
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
        if(animals.isEmpty()) return new Genome(0);
        HashMap<Genome,Integer> genomesCounter = new HashMap<>();
        for(var animalList: animals.values()){
            for(var animal:animalList){
                if(!(animal instanceof Animal)){
                    continue;
                }
                Genome genome = animal.getGenome();
                if (genomesCounter.containsKey(genome)) {
                    genomesCounter.put(genome, genomesCounter.get(genome) + 1);
                } else {
                    genomesCounter.put(genome, 1);
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
    @Override
    public void feedAnimals(int feedVal) {
        for (var animalList : animals.values()) {
            List<Animal> animals = new ArrayList<>(animalList.stream()
                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
                    .toList());                        // Tworzy niemodyfikowalną listę

            if (grasses.containsKey(animalList.getFirst().getPosition())) {
                animals.sort(Comparator
                        .comparingInt(Animal::getEnergy).reversed() // Najpierw sortuj po energii malejąco
                        .thenComparingInt(Animal::getAge).reversed() // Następnie po wieku malejąco
                        .thenComparingInt(Animal::getChildrenAmount).reversed() // Na końcu po liczbie dzieci malejąco
                );
                Animal bestAnimal = animals.getFirst();
                var position = bestAnimal.getPosition();
                bestAnimal.eat(feedVal);
                bestAnimal.increaseEaten();
                grasses.remove(position);
                if(position.follows(lowerLeftEquator) && position.precedes(upperRightEquator)){
                    equator.add(position);
                }
                else{
                    beyondEquator.add(position);
                }
            }
        }
    }

    public List<Animal> reproduceAnimals(Config config){
        // daje config bo byloby 5 parametrów. chyba tak jest bardziej elegancko

        List<Animal> newAnimalList = new ArrayList<>();
        for(var animalList:animals.values()){
            List<Animal> animals = new ArrayList<>(animalList.stream()
                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
                    .toList());                        // Tworzy niemodyfikowalną listę (JDK 16+)

            // sortuje po największej ilości energii
            animals.sort(Comparator
                    .comparingInt(Animal::getEnergy).reversed() // Najpierw sortuj po energii malejąco
            );
            for(int i = 0; i < (int) (animals.size()/2); i++){

                var animal1 = animals.get(2 * i);
                var animal2 = animals.get(2 * i + 1);
                // jezeli drugi może się rozmnożyć
                if(animal2.getEnergy() >= config.energyRequireToReproduce()){
//                    System.out.println("jejsafjsagjsdgosdjigjsdjgsd" + animal1.getPosition());
                    // można się zastanowić nad rzuceniem tu configu
                    var newAnimal = animal1.reproduce(animal2,
                            config.genomeChange(),
                            config.minimalMutationAmount(),
                            config.maximalMutationAmount(),
                            config.energyToReproduce());
                    newAnimalList.add(newAnimal);
                    addToAnimals(newAnimal.getPosition(),newAnimal);
                }
            }
        }
        notifyObservers("utworzono nowe zwierzeta");
        return newAnimalList;
    }

    public List<Animal> removeDepthAnimals(){
        // daje config bo byloby 5 parametrów. chyba tak jest bardziej elegancko

        List<Animal> removedAnimalList = new ArrayList<>();
        for(var animalList:animals.values()){
            List<Animal> animals = new ArrayList<>(animalList.stream()
                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
                    .toList());                        // Tworzy niemodyfikowalną listę (JDK 16+)

            for(var animal : animals){
                if(animal.getEnergy()<0){
                    removedAnimalList.add(animal);
                }
            }
        }
        // usuwam zwierzęta
        for(var animal: removedAnimalList){
            animal.die();
            removeFromAnimals(animal.getPosition(),animal);
            deathAnimals.add(animal);
        }
        notifyObservers("usunieto zwierzeta");
        return removedAnimalList;
    }

    private List<Vector2d> getAllPositionBetween(Vector2d lowerLeft, Vector2d upperRight){
        List<Vector2d> allPossiblePositions = new ArrayList<>();

        for (int x = lowerLeft.getX(); x < upperRight.getX()+1; x++) {
            for (int y = lowerLeft.getY(); y < upperRight.getY()+1; y++) {
                allPossiblePositions.add(new Vector2d(x, y));
            }
        }
        return allPossiblePositions;
    }

    // nie działa z uwagi na modyfikacje listy w trakcie przechodzenia przez nią
//    public void moveAllAnimals(int dailyDeclineValue){
//        // metoda przygotowana także dla OwlBear
//        for(var animalList : animals.values()){
//            for(var animal : animalList){
//                move(animal);
//                if (animal instanceof Animal) {
//                    ((Animal) animal).reduceEnergy(dailyDeclineValue);
//                }
//
//                animal.getOlder();
//            }
//        }
//    }

}
