package agh.ics.oop.model.maps;

import agh.ics.oop.model.util.Config;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Vector2d;
import agh.ics.oop.model.genomes.Genome;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimalManager {

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
        mapStatistics.energyUpdate(-dailyDeclineValue);

    }

    public void feedAnimals(int feedVal) {

        for (var position : animals.keySet()) {

            Optional<Animal> bestAnimalOpt = getStrongestAnimal(position);
            if (bestAnimalOpt.isPresent() && grassManager.isGrassAt(position)) {
                Animal bestAnimal = bestAnimalOpt.get();
                bestAnimal.eat(feedVal);

                grassManager.removeGrass(position);
                mapStatistics.feedAnimalUpdate(feedVal);
            }
        }
    }

    public void reproduceAnimals(Config config) {
        for (var animalsAtPosition : animals.values()) {
            List<Animal> animalsCopy = new ArrayList<>(animalsAtPosition);
            animalsCopy.sort(Comparator.comparingInt(Animal::getEnergy).reversed());

            for (int i = 0; i < (int) (animalsCopy.size() / 2); i++) {
                var animal1 = animalsCopy.get(2 * i);
                var animal2 = animalsCopy.get(2 * i + 1);

                if (animal2.getEnergy() >= config.energyRequiredToReproduce()) {
                    reproduceTwoAnimals(config, animal1, animal2);
                }
                else {
                    break;
                }
            }
        }
    }

    private void reproduceTwoAnimals(Config config, Animal animal1, Animal animal2) {
        var newAnimal = animal1.reproduce(
                animal2,
                config.genomeType().getGenomeChange(),
                config.minMutationCount(),
                config.maxMutationCount(),
                config.offspringEnergyCost()
        );
        addToAnimals(newAnimal.getPosition(), newAnimal);
        mapStatistics.newBornUpdate();
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

    public void removeFromAnimals(Vector2d position, Animal animal) {
        var list = animals.get(position);
        if (list != null) {
            list.remove(animal);
            if (list.isEmpty()) animals.remove(position);
        }
    }

    public Genome getDominantGenome() {
        if (animals.isEmpty()) return new Genome(0);

        Map<Genome, Integer> genomesCounter = new HashMap<>();
        animals.values().stream()
                .flatMap(Collection::stream) // Spłaszczamy wszystkie listy zwierząt do jednego strumienia
                .map(Animal::getGenome) // Pobieramy genomy
                .forEach(genome -> genomesCounter.merge(genome, 1, Integer::sum)); // Liczymy wystąpienia genomów

        return genomesCounter.entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Znajdujemy genom z największą liczbą wystąpień
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("brak elementów w liście")); // Obsługa błędu, jeśli lista pusta
    }

    public Optional<Animal> getStrongestAnimal(Vector2d position) {
        CopyOnWriteArrayList<Animal> animalsList = animals.get(position);
        if (animalsList == null) {
            return Optional.empty();
        }
        return animalsList.stream()
                .max(Comparator
                        .comparingInt(Animal::getEnergy)
                        .thenComparingInt(Animal::getAge)
                        .thenComparingInt(Animal::getChildrenAmount)
                );
    }
}