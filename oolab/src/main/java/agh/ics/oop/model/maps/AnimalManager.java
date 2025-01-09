package agh.ics.oop.model.maps;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapStatistic;
import agh.ics.oop.model.MapStatisticAction;
import agh.ics.oop.model.Vector2d;

import java.util.*;

public class AnimalManager {
    private final Map<Vector2d, List<AbstractAnimal>> animals = new HashMap<>();
    private final MapStatistic mapStatistic;
    private final GrassManager grassManager;

    public AnimalManager(MapStatistic mapStatistic, GrassManager grassManager) {
        this.mapStatistic = mapStatistic;
        this.grassManager = grassManager;
    }

    public boolean isAnimalAt(Vector2d position){
        return animals.containsKey(position);
    }
    public List<AbstractAnimal> getAnimals(Vector2d position){
        return animals.get(position);
    }

    public void moveAllAnimals(int dailyDeclineValue, WorldMap map) {
        animals.values().stream()
                .flatMap(List::stream)
                .forEach(animal -> move(animal, dailyDeclineValue, map));
    }

    private void move(AbstractAnimal animal, int dailyDeclineValue, WorldMap map) {
        Vector2d oldPosition = animal.getPosition();
        removeFromAnimals(oldPosition, animal);

        animal.move(map.getValidator(animal));
        addToAnimals(animal.getPosition(), animal);

        if (animal instanceof Animal concreteAnimal) {
            concreteAnimal.reduceEnergy(dailyDeclineValue);
            concreteAnimal.getOlder();
            mapStatistic.updateStatistic(MapStatisticAction.ENERGY,-dailyDeclineValue);
        }
    }

    public void feedAnimals(int feedVal, GrassField grassField) {
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
                mapStatistic.feedAnimalUpdate(feedVal);

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
                    mapStatistic.newBornUpdate(newAnimal);

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
            mapStatistic.deathAnimalUpdate(animal);
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

    private void removeFromAnimals(Vector2d position, AbstractAnimal animal) {
        var list = animals.get(position);
        if (list != null) {
            list.remove(animal);
            if (list.isEmpty()) animals.remove(position);
        }
    }
}