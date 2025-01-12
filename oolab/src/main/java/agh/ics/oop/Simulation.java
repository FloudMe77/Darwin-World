package agh.ics.oop;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.Vector2d;

import agh.ics.oop.model.IncorrectPositionException;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.util.newUtils.Genome;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final WorldMap worldMap;
    private final Config config;
    private BooleanProperty stopped = new SimpleBooleanProperty(true);
    private final Object pauseLock = new Object();

    // jak narazie dostosowuje do EarthMap
    public Simulation(Config config) {
        worldMap = config.worldMap();
        animals = new ArrayList<>();
        this.config = config;
        // dodawanie zwierząt
        for (Vector2d position : new RandomPositionGenerator(new Vector2d(0, 0), new Vector2d(config.width(), config.high()), config.startAnimalAmount())) {
            Animal animal = new Animal(position, new Genome(config.genomeLength()), config.startEnergy());
            try {
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

        for (int i = 0; i < config.startGrassAmount(); i++) {
            worldMap.addGrass();
        }
    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }

    public void run() {
        // duży for tylko na potrzeby testów
        // najpierw zwierzęta się poruszają
        setStopped(false);

        // tutaj rzecz jasna refactor musi byc

        for (int i = 0; i < 1000; i++) {
            synchronized (pauseLock) {
                while (stopped.get()) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                // usuwanie zdechłych zwierząt
                animals.removeAll(worldMap.removeDepthAnimals());

                // można przemyśleć, żeby tą metodę umieścić w samej mapie
                worldMap.moveAllAnimals(config.dailyDeclineValue());

                // następnie jedzą
                worldMap.feedAnimals(config.energyFromGrass());

                // reprodukcja zwierząt
                animals.addAll(worldMap.reproduceAnimals(config));


                // porost traw
                for (int j = 0; j < config.everyDayGrassAmount(); j++) {
                    worldMap.addGrass();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public boolean isStopped() {
        return stopped.get();
    }

    public BooleanProperty stoppedProperty() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped.set(stopped);
    }

    public void pause() {
        this.setStopped(true);
    }

    public void resume() {
        synchronized (pauseLock) {
            this.setStopped(false);
            pauseLock.notify();
        }
    }
}
