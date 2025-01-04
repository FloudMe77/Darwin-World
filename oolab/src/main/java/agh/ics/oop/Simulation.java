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

        // chyba można operować tylko na high i zakładać, że mapa zaczyna się od 0.0
        // jak coś to do zmiany
        var bounds = worldMap.getCurrentBounds();

        // to jest do całkowitego refactora, ale jak na razie sprawdzam, czy działa

        Vector2d lowerLeftBelowEquator = new Vector2d(bounds.leftDownCornerMap().getX(),
                (int) (bounds.leftDownCornerMap().getY() ));
        Vector2d upperRightBelowEquator = new Vector2d(bounds.rightUpperCornerMap().getX(),
                (int) (bounds.leftDownCornerMap().getY() + (2.0/5) * config.high()));

        Vector2d lowerLeftEquator = new Vector2d(bounds.leftDownCornerMap().getX(),
                (int) (bounds.leftDownCornerMap().getY() + (2.0/5) * config.high()));
        Vector2d upperRightEquator = new Vector2d(bounds.rightUpperCornerMap().getX(),
                (int) (bounds.leftDownCornerMap().getY() + (3.0/5) * config.high()));

        Vector2d lowerLeftAboveEquator = new Vector2d(bounds.leftDownCornerMap().getX(),
                (int) (bounds.leftDownCornerMap().getY() + (3.0/5) * config.high()));
        Vector2d upperRightAboveEquator = new Vector2d(bounds.rightUpperCornerMap().getX(),
                (int) (bounds.leftDownCornerMap().getY() + config.high()));

        var belowGenerator = new RandomPositionGenerator(lowerLeftBelowEquator, upperRightBelowEquator, config.startGrassAmount());
        var aboveGenerator = new RandomPositionGenerator(lowerLeftAboveEquator,upperRightAboveEquator, config.startGrassAmount());

        // chyba do zmiany ten RandomPositionGenerator, żeby przy każdym wywołaniu next na iteratorze było losowane, które
        // pozycje. byłoby chyba prościej
        for (Vector2d position : new RandomPositionGenerator(lowerLeftEquator, upperRightEquator, config.startGrassAmount())) {

            // parytet 80:20
            Random random = new Random();
            // jeżeli trafiło do tych 20%
            if(random.nextInt(5)==0) {
                // losuje, czy bierzemy pole nad, czy pod równikiem
                if(random.nextBoolean()){
                    position = belowGenerator.iterator().next();
                }
                else{
                    position = aboveGenerator.iterator().next();
                }
            }
            Grass grass = new Grass(position);
            try {
                worldMap.place(grass);
            } catch (IncorrectPositionException e) {
                System.out.println("Uwaga: " + e.getMessage());
            }
        }
    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }

    public void run() {
        // duży for tylko na potrzeby testów
        // najpierw zwierzęta się poruszają
        // można przemyśleć, żeby tą metodę umieścić w samej mapie
        for(int i=0;i<5;i++) {
            for (var animal : animals) {
                animal.move(worldMap);
            }

            // następnie jedzą
            worldMap.feedAnimals(config.energyFromGrass());

            // reprodukcja zwierząt
            animals.addAll(worldMap.reproduceAnimals(config));
        }
    }
}
