package agh.ics.oop;

import agh.ics.oop.model.util.Config;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Vector2d;

import agh.ics.oop.model.MapObjects.IncorrectPositionException;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.genomes.Genome;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;

public class Simulation implements Runnable {
    private final WorldMap worldMap;
    private final Config config;
    private BooleanProperty stopped = new SimpleBooleanProperty(true);
    private final Object pauseLock = new Object();
    private boolean running = true;

    public Simulation(Config config) {
        // Dodawanie mapy
        this.config = config;
        worldMap = config.mapType().createMap(config.width(), config.height());
        // dodawanie zwierząt
        for (Vector2d position : new RandomPositionGenerator(new Vector2d(0, 0), new Vector2d(config.width(), config.height()), config.startAnimalAmount())) {
            Animal animal = new Animal(position, new Genome(config.genomeLength()), config.startEnergy());
            try {
                worldMap.place(animal);
            } catch (IncorrectPositionException e) {
                System.out.println("Uwaga: " + e.getMessage());
            }
        }

        for (int i = 0; i < config.startGrassAmount(); i++) {
            worldMap.addGrass();
        }
    }

    public List<Animal> getAnimals() {
        return worldMap.getAnimals();
    }
    public WorldMap getWorldMap() { return worldMap; }


    public void run() {
        setStopped(false);
        try {
            while (running) {
                synchronized (pauseLock) {
                    while (stopped.get()) {
                        pauseLock.wait();
                    }

                    // usuwanie zdechłych zwierząt
                    worldMap.removeDepthAnimals();

                    // można przemyśleć, żeby tą metodę umieścić w samej mapie
                    worldMap.moveAllAnimals(config.dailyDeclineValue());

                    // następnie jedzą
                    worldMap.feedAnimals(config.energyFromGrass());

                    // reprodukcja zwierząt
                    worldMap.reproduceAnimals(config);


                    // porost traw
                    for (int j = 0; j < config.everyDayGrassAmount(); j++) {
                        worldMap.addGrass();
                    }

                    Thread.sleep(100);
                }
            }


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        synchronized (pauseLock) {
            try {
                pauseLock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void terminate() {
        running = false;
        resume();
    }

    public void resume() {
        synchronized (pauseLock) {
            this.setStopped(false);
            pauseLock.notify();
        }
    }
}
