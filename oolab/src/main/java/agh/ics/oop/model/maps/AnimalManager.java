package agh.ics.oop.model.maps;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapStatistics;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.newUtils.Genome;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimalManager {
    // Możliwe, że da sie lepiej niż copyonwritearraylist ale nie mam pomysłu a to skutecznie
    // eliminuje problem, że wątek UI iteruje po liście gdy wątek symulacji tą liste modyfikuje
    private final Map<Vector2d, CopyOnWriteArrayList<Animal>> animals = new ConcurrentHashMap<>();
    private final MapStatistics mapStatistics;
    private final GrassManager grassManager;

    public AnimalManager(MapStatistics mapStatistics, GrassManager grassManager) {
        this.mapStatistics = mapStatistics;
        this.grassManager = grassManager;
    }

    public boolean isAnimalAt(Vector2d position) {
        return animals.containsKey(position);
    }

    public List<Animal> getAnimals(Vector2d position) {
        return animals.get(position);
    }

    public void moveAllAnimals(int dailyDeclineValue, WorldMap map) {
        // Tworzymy kopię wszystkich zwierząt w mapie
        List<Animal> allAnimals = animals.values().stream()
                .flatMap(List::stream)
                .toList(); // Kopia wszystkich zwierząt

        // Iterujemy przez kopię, dzięki czemu możemy bezpiecznie modyfikować oryginalną strukturę
        allAnimals.forEach(animal -> move(animal, dailyDeclineValue, map));
    }

    public List<Animal> getElements() {
        return animals.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    public void move(Animal animal, int dailyDeclineValue, WorldMap map) {
        Vector2d oldPosition = animal.getPosition();
        removeFromAnimals(oldPosition, animal);

        animal.move(map);
        addToAnimals(animal.getPosition(), animal);

        animal.reduceEnergy(dailyDeclineValue);
        animal.getOlder();
        mapStatistics.energyUpdate(-dailyDeclineValue);

    }

    public void feedAnimals(int feedVal) {

        for (var position : animals.keySet()) {

            Optional<Animal> bestAnimalOpt = getStrongestAnimal(position);
            if (bestAnimalOpt.isPresent() && grassManager.isGrassAt(position)) {
                Animal bestAnimal = bestAnimalOpt.get();
                bestAnimal.eat(feedVal);

                bestAnimal.increaseEaten();
                grassManager.removeGrass(position);
                mapStatistics.feedAnimalUpdate(feedVal);
            }
        }
    }

    public List<Animal> reproduceAnimals(Config config) {
        List<Animal> newAnimalList = new ArrayList<>();
        for (var animals : animals.values()) {

            // sortuje po największej ilości energii
            animals.sort(Comparator
                    .comparingInt(Animal::getEnergy).reversed() // Najpierw sortuj po energii malejąco
            );
            for (int i = 0; i < (int) (animals.size() / 2); i++) {

                var animal1 = animals.get(2 * i);
                var animal2 = animals.get(2 * i + 1);
                // jezeli drugi może się rozmnożyć
                if (animal2.getEnergy() >= config.energyRequiredToReproduce()) {

                    var newAnimal = animal1.reproduce(animal2,
                            config.genomeType().getGenomeChange(),
                            config.minMutationCount(),
                            config.maxMutationCount(),
                            config.offspringEnergyCost());
                    newAnimalList.add(newAnimal);
                    addToAnimals(newAnimal.getPosition(), newAnimal);
                    mapStatistics.newBornUpdate();
                }
            }
        }
        return newAnimalList;
    }

    public void removeDeadAnimals() {
        List<Animal> removedAnimalList = new ArrayList<>();
        for (var animals : animals.values()) {

            for (var animal : animals) {
                if (animal.getEnergy() < 0) {
                    removedAnimalList.add(animal);
                }
            }
        }
        // usuwam zwierzęta
        for (var animal : removedAnimalList) {
            // nie wiem czy potrzebne
            animal.die();

            removeFromAnimals(animal.getPosition(), animal);
            // do funkcji
            mapStatistics.deathAnimalUpdate(animal);
        }
    }

    public void addToAnimals(Vector2d position, Animal animal) {
        if (!animals.containsKey(position)) {
            animals.put(position, new CopyOnWriteArrayList<>());
        }

        animals.get(position).add(animal);
    }

    void removeFromAnimals(Vector2d position, Animal animal) {
        var list = animals.get(position);
        if (list != null) {
            list.remove(animal);
            if (list.isEmpty()) animals.remove(position);
        }
    }

    public Genome getDominantGenome() {
        if (animals.isEmpty()) return new Genome(0);
        HashMap<Genome, Integer> genomesCounter = new HashMap<>();
        for (var animalList : animals.values()) {
            for (var animal : animalList) {
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

    public Optional<Animal> getStrongestAnimal(Vector2d position) {
        return animals.get(position).stream()
                .max(Comparator
                        .comparingInt(Animal::getEnergy)
                        .thenComparingInt(Animal::getAge)
                        .thenComparingInt(Animal::getChildrenAmount)
                );
    }
}