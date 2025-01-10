package agh.ics.oop;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.Vector2d;

import agh.ics.oop.model.IncorrectPositionException;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.util.newUtils.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final WorldMap worldMap;
    private final Config config;

    // jak narazie dostosowuje do EarthMap
    public Simulation(Config config) {
        worldMap = config.worldMap();
        animals = new ArrayList<>();
        this.config = config;
        // dodawanie zwierząt
        for (Vector2d position : new RandomPositionGenerator(new Vector2d(0,0), new Vector2d(config.width(),config.high()), config.startAnimalAmount())) {
            Animal animal = new Animal(position,new Genome(config.genomeLength()),config.startEnergy());
            try{
                worldMap.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println("Uwaga: " + e.getMessage());
            }
        }

        // dodawanie trawy
        // mamy tylko jeden wariant generowania trawy więc nie będę się rozdrabniał na pojedyncze klasy
        // uznaje, że równik to pas o szerokości 1/5 mapy na środku
        // pewnie lepiej by było wsadzić metodę do wordlmap

        for(int i = 0; i < config.startGrassAmount();i++){
            worldMap.addGrass();
        }
    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }

    public void run() {
        // duży for tylko na potrzeby testów
        // najpierw zwierzęta się poruszają

        for(int i=0;i<5;i++) {

            // usuwanie zdechłych zwierząt
            animals.removeAll(worldMap.removeDepthAnimals());

            // można przemyśleć, żeby tą metodę umieścić w samej mapie
            worldMap.moveAllAnimals(config.dailyDeclineValue());

            // następnie jedzą
            worldMap.feedAnimals(config.energyFromGrass());

            // reprodukcja zwierząt
            animals.addAll(worldMap.reproduceAnimals(config));


            // porost traw
            for(int j=0; j < config.everyDayGrassAmount();j++){
                worldMap.addGrass();
            }
        }
    }
}
