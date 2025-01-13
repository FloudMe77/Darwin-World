package agh.ics.oop.model.maps;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapStatistics;
import agh.ics.oop.model.MapStatisticAction;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.newUtils.Genome;

import java.util.*;

public class AnimalManager {
    private final Map<Vector2d, List<AbstractAnimal>> animals = new HashMap<>();
    private final MapStatistics mapStatistics;
    private final GrassManager grassManager;

    public AnimalManager(MapStatistics mapStatistics, GrassManager grassManager) {
        this.mapStatistics = mapStatistics;
        this.grassManager = grassManager;
    }

    public boolean isAnimalAt(Vector2d position){
        return animals.containsKey(position);
    }
    public List<AbstractAnimal> getAnimals(Vector2d position){
        return animals.get(position);
    }

    public void moveAllAnimals(int dailyDeclineValue, WorldMap map) {
        // Tworzymy kopię wszystkich zwierząt w mapie
        List<AbstractAnimal> allAnimals = animals.values().stream()
                .flatMap(List::stream)
                .toList(); // Kopia wszystkich zwierząt

        // Iterujemy przez kopię, dzięki czemu możemy bezpiecznie modyfikować oryginalną strukturę
        allAnimals.forEach(animal -> move(animal, dailyDeclineValue, map));
    }

    public List<AbstractAnimal> getElements() {
        return animals.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    public void move(AbstractAnimal animal, int dailyDeclineValue, WorldMap map) {
        Vector2d oldPosition = animal.getPosition();
        removeFromAnimals(oldPosition, animal);

        // nie można tu wrzucić WorldMap co dostaliśmy?
        animal.move(map);
        addToAnimals(animal.getPosition(), animal);

        if (animal instanceof Animal concreteAnimal) {
            concreteAnimal.reduceEnergy(dailyDeclineValue);
            concreteAnimal.getOlder();
            mapStatistics.updateStatistic(MapStatisticAction.ENERGY,-dailyDeclineValue);
        }
    }

    public void feedAnimals(int feedVal, GrassField grassField) {
        // podzielić na osobne funkcje


        for (var animalList : animals.values()) {
            List<Animal> animals = new ArrayList<>(animalList.stream()
                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
                    .toList());                        // Tworzy niemodyfikowalną listę

            if (grassManager.isGrassAt(animalList.getFirst().getPosition())) {
                animals.sort(Comparator
                        .comparingInt(Animal::getEnergy).reversed() // Najpierw sortuj po energii malejąco
                        .thenComparingInt(Animal::getAge).reversed() // Następnie po wieku malejąco
                        .thenComparingInt(Animal::getChildrenAmount).reversed() // Na końcu po liczbie dzieci malejąco
                );
                Animal bestAnimal = animals.getFirst();
                var position = bestAnimal.getPosition();
                bestAnimal.eat(feedVal);

                bestAnimal.increaseEaten();
                grassManager.removeGrass(position);
                mapStatistics.feedAnimalUpdate(feedVal);

                grassField.addGrassPosition(position);
            }
        }
    }

    public List<Animal> reproduceAnimals(Config config) {
        List<Animal> newAnimalList = new ArrayList<>();
        for(var animalList:animals.values()){
            List<Animal> animals = new ArrayList<>(animalList.stream()
                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
                    .toList());

            // sortuje po największej ilości energii
            animals.sort(Comparator
                    .comparingInt(Animal::getEnergy).reversed() // Najpierw sortuj po energii malejąco
            );
            for(int i = 0; i < (int) (animals.size()/2); i++){

                var animal1 = animals.get(2 * i);
                var animal2 = animals.get(2 * i + 1);
                // jezeli drugi może się rozmnożyć
                if(animal2.getEnergy() >= config.energyRequireToReproduce()){
                    // można się zastanowić nad rzuceniem tu configu
                    var newAnimal = animal1.reproduce(animal2,
                            config.genomeChange(),
                            config.minimalMutationAmount(),
                            config.maximalMutationAmount(),
                            config.energyToReproduce());
                    newAnimalList.add(newAnimal);
                    addToAnimals(newAnimal.getPosition(),newAnimal);
                    mapStatistics.newBornUpdate(newAnimal);

                }
            }
        }
//        notifyObservers("utworzono nowe zwierzeta");
        return newAnimalList;
    }

    public List<Animal> removeDeadAnimals() {
        List<Animal> removedAnimalList = new ArrayList<>();
        for(var animalList:animals.values()){
            List<Animal> animals = new ArrayList<>(animalList.stream()
                    .filter(Animal.class::isInstance) // Zachowaj tylko instancje klasy Animal
                    .map(Animal.class::cast)          // Zamień AbstractAnimal na Animal
                    .toList());

            for(var animal : animals){
                if(animal.getEnergy()<0){
                    removedAnimalList.add(animal);
                }
            }
        }
        // usuwam zwierzęta
        for(var animal: removedAnimalList){
            // nie wiem czy potrzebne
            animal.die();

            removeFromAnimals(animal.getPosition(),animal);
            // do funkcji
            mapStatistics.deathAnimalUpdate(animal);
        }
//        notifyObservers("usunieto zwierzeta");
        return removedAnimalList;
    }

    public void addToAnimals(Vector2d position, AbstractAnimal animal) {
        if (!animals.containsKey(position)) {

            animals.put(position, new ArrayList<>());
        }

        animals.get(position).add(animal);
    }

    void removeFromAnimals(Vector2d position, AbstractAnimal animal) {
        var list = animals.get(position);
        if (list != null) {
            list.remove(animal);
            if (list.isEmpty()) animals.remove(position);
        }
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

}