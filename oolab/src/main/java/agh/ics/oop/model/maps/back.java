//package agh.ics.oop.model.maps;
//
//import agh.ics.oop.model.*;
//import agh.ics.oop.model.MapObjects.AbstractAnimal;
//import agh.ics.oop.model.MapObjects.Animal;
//import agh.ics.oop.model.MapObjects.Grass;
//import agh.ics.oop.model.util.Boundary;
//import agh.ics.oop.model.util.MapChangeListener;
//import agh.ics.oop.model.util.MapVisualizer;
//import agh.ics.oop.model.util.Genome;
//import agh.ics.oop.model.util.GenomeChange;
//
//import javax.swing.*;
//import javax.swing.text.Position;
//import java.util.*;
//
//abstract public class BasicRectangularMap implements WorldMap {
//    protected final Map<Vector2d, List<AbstractAnimal>> animals = new HashMap<>();
//    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
//    private final MapVisualizer visualizer = new MapVisualizer(this);
//    private final List<MapChangeListener> observers = new ArrayList<>();
//    private final Vector2d upperRight;
//    private final Vector2d lowerLeft;
//    private final Boundary boundary;
//    private final int mapId;
//    private final MapStatistic mapStatistic;
//    private final GrassField grassField;
//
//    private static int counterOfId = 1;
//
//    public BasicRectangularMap(int height, int width) {
//
//        mapStatistic = new MapStatistic(this);
//        mapId = counterOfId++;
//        this.lowerLeft = new Vector2d(0, 0);
//        this.upperRight = new Vector2d(width , height );
//        boundary = new Boundary(lowerLeft, upperRight);
//        grassField = new GrassField(height, width);
//    }
//
//    public MapStatistic getMapStatistic() {
//        return mapStatistic;
//    }
//
//    public void addObserver(MapChangeListener observer) {
//        observers.add(observer);
//    }
//
//
//    private void notifyObservers(String message) {
//        for (var observer : observers) {
//            observer.mapChanged(this, message, mapId);
//        }
//    }
//
//    private void notifyMapStatistic(MapStatisticAction mapStatisticAction, int val){
//        mapStatistic.updateStatistic(mapStatisticAction,val);
//    }
//
//    @Override
//    public void place(Animal animal) {
//
//        addToAnimals(animal.getPosition(), animal);
//        notifyObservers("Ustawiono animal na " + animal.getPosition());
//
//        mapStatistic.newAnimalUpdate(animal);
//
//    }
//
//    @Override
//    public void addGrass() {
//        Optional<Vector2d> newPositionOpt = grassField.getNewGrassPosition();
//
//        if(newPositionOpt.isPresent()){
//            var newPosition = newPositionOpt.get();
//            var grass = new Grass(newPosition);
//            grasses.put(newPosition,grass);
//            notifyMapStatistic(MapStatisticAction.GRASS_AMOUNT,1);
//        }
//    }
//
//    @Override
//    public boolean isOccupied(Vector2d position) {
//        return animals.containsKey(position) || grasses.containsKey(position);
//    }
//
//    @Override
//    public List<WorldElement> objectsAt(Vector2d position) {
//        List<WorldElement> elements = new ArrayList<>();
//
//        if (animals.containsKey(position)) {
//            elements.addAll(animals.get(position));
//        }
//
//        if (grasses.containsKey(position)) {
//            elements.add(grasses.get(position));
//        }
//
//        return elements;
//    }
//
//    @Override
//    public void move(AbstractAnimal animal, int dailyDeclineValue) {
////        var oldPosition = animal.getPosition();
//        removeFromAnimals(animal.getPosition(), animal);
//        animal.move(getValidator(animal));
//        addToAnimals(animal.getPosition(), animal);
//
//        if(animal instanceof Animal){
//            ((Animal) animal).reduceEnergy(dailyDeclineValue);
//            animal.getOlder();
//            notifyMapStatistic(MapStatisticAction.ENERGY, -dailyDeclineValue);
//        }
//
////        notifyObservers("Przeniesiono Animal z " + oldPosition + " do " + animal.getPosition());
//    }
//
//    @Override
//    public boolean canMoveTo(Vector2d position) {
//        return position.follows(lowerLeft) && position.precedes(upperRight);
//    }
//
//    @Override
//    public Vector2d getNewPosition(Vector2d oldPosition, Vector2d newPosition) {
//        return newPosition;
//    }
//
//    @Override
//    public MapDirection getNewMapDirection(Vector2d newPosition, MapDirection mapDirection) {
//        return mapDirection;
//    }
//
//    public List<WorldElement> getElements() {
//        List<WorldElement> elements = new ArrayList<>();
//
//        for (List<AbstractAnimal> animalList : animals.values()) {
//            elements.addAll(animalList);
//        }
//
//        elements.addAll(grasses.values());
//        return Collections.unmodifiableList(elements);
//    }
//
//    public WorldMap getValidator(AbstractAnimal animal) {
//        return this;
//    };
//
//    @Override
//    public String toString() {
//        var boundary = getCurrentBounds();
//        return visualizer.draw(boundary.leftDownCornerMap(), boundary.rightUpperCornerMap());
//    }
//
//    @Override
//    public int getId() {
//        return mapId;
//    }
//
//    @Override
//    public Boundary getCurrentBounds() {
//        return boundary;
//    }
//
//    // możliwe ze w przyszłosci to będzie public?
//    protected void addToAnimals(Vector2d position, AbstractAnimal animal) {
//        if (!animals.containsKey(position)) {
//
//            animals.put(position, new ArrayList<>());
//        }
//
//        animals.get(position).add(animal);
//    }
//
//    protected void removeFromAnimals(Vector2d position, AbstractAnimal animal) {
//        List<AbstractAnimal> animalList = animals.get(position);
//        if (animalList != null) {
//            animalList.remove(animal);
//            if (animalList.isEmpty()) {
//                animals.remove(position);
//
//            }
//        }
//    }
//
//    public Genome getDominantGenome(){
//        if(animals.isEmpty()) return new Genome(0);
//        HashMap<Genome,Integer> genomesCounter = new HashMap<>();
//        for(var animalList: animals.values()){
//            for(var animal:animalList){
//                if(!(animal instanceof Animal)){
//                    continue;
//                }
//                Genome genome = animal.getGenome();
//                if (genomesCounter.containsKey(genome)) {
//                    genomesCounter.put(genome, genomesCounter.get(genome) + 1);
//                } else {
//                    genomesCounter.put(genome, 1);
//                }
//            }
//        }
//        Optional<Map.Entry<Genome, Integer>> maxEntry = genomesCounter.entrySet().stream()
//                .max(Map.Entry.comparingByValue()); // Porównujemy po wartościach
//
//        if (maxEntry.isPresent()) {
//            return maxEntry.get().getKey();
//        } else {
//            throw new RuntimeException("brak elementów w liście"); //tymczasowe, żeby tylko zabezpieczyć
//        }
//    }
//
//    @Override
//    public void feedAnimals(int feedVal) {
//        for (var animalList : animals.values()) {
//            List<Animal> animals = new ArrayList<>(animalList.stream()
//                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
//                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
//                    .toList());                        // Tworzy niemodyfikowalną listę
//
//            if (grasses.containsKey(animalList.getFirst().getPosition())) {
//                animals.sort(Comparator
//                        .comparingInt(Animal::getEnergy).reversed() // Najpierw sortuj po energii malejąco
//                        .thenComparingInt(Animal::getAge).reversed() // Następnie po wieku malejąco
//                        .thenComparingInt(Animal::getChildrenAmount).reversed() // Na końcu po liczbie dzieci malejąco
//                );
//                Animal bestAnimal = animals.getFirst();
//                var position = bestAnimal.getPosition();
//                bestAnimal.eat(feedVal);
//
//                bestAnimal.increaseEaten();
//                grasses.remove(position);
//                mapStatistic.feedAnimalUpdate(feedVal);
//
//                grassField.addGrassPosition(position);
//            }
//        }
//    }
//
//    public List<Animal> reproduceAnimals(Config config){
//        // daje config bo byloby 5 parametrów. chyba tak jest bardziej elegancko
//
//        List<Animal> newAnimalList = new ArrayList<>();
//        for(var animalList:animals.values()){
//            List<Animal> animals = new ArrayList<>(animalList.stream()
//                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
//                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
//                    .toList());                        // Tworzy niemodyfikowalną listę (JDK 16+)
//
//            // sortuje po największej ilości energii
//            animals.sort(Comparator
//                    .comparingInt(Animal::getEnergy).reversed() // Najpierw sortuj po energii malejąco
//            );
//            for(int i = 0; i < (int) (animals.size()/2); i++){
//
//                var animal1 = animals.get(2 * i);
//                var animal2 = animals.get(2 * i + 1);
//                // jezeli drugi może się rozmnożyć
//                if(animal2.getEnergy() >= config.energyRequireToReproduce()){
////                    System.out.println("jejsafjsagjsdgosdjigjsdjgsd" + animal1.getPosition());
//                    // można się zastanowić nad rzuceniem tu configu
//                    var newAnimal = animal1.reproduce(animal2,
//                            config.genomeChange(),
//                            config.minimalMutationAmount(),
//                            config.maximalMutationAmount(),
//                            config.offspringEnergyCost());
//                    newAnimalList.add(newAnimal);
//                    addToAnimals(newAnimal.getPosition(),newAnimal);
//
//                    mapStatistic.newBornUpdate(newAnimal);
//
//                }
//            }
//        }
//        notifyObservers("utworzono nowe zwierzeta");
//        return newAnimalList;
//    }
//
//    public List<Animal> removeDepthAnimals(){
//
//        List<Animal> removedAnimalList = new ArrayList<>();
//        for(var animalList:animals.values()){
//            List<Animal> animals = new ArrayList<>(animalList.stream()
//                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
//                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
//                    .toList());
//
//            for(var animal : animals){
//                if(animal.getEnergy()<0){
//                    removedAnimalList.add(animal);
//                }
//            }
//        }
//        // usuwam zwierzęta
//        for(var animal: removedAnimalList){
//            // nie wiem czy potrzebne
//            animal.die();
//
//            removeFromAnimals(animal.getPosition(),animal);
//            // do funkcji
//            mapStatistic.deathAnimalUpdate(animal);
//        }
//        notifyObservers("usunieto zwierzeta");
//        return removedAnimalList;
//    }
//
//    public void moveAllAnimals(int dailyDeclineValue) {
//        animals.values().stream()
//                .flatMap(List::stream)
//                .forEach(animal -> move(animal, dailyDeclineValue));
//    }
//
//}
