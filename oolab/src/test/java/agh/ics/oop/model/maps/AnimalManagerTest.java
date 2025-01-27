package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Vector2d;
import agh.ics.oop.model.genomes.Genome;
import agh.ics.oop.model.genomes.GenomeDirection;
import agh.ics.oop.model.genomes.GenomeType;
import agh.ics.oop.model.util.Config;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalManagerTest {
    @Test
    void addTest() {
        // given
        var mapStatistics = new MapStatistics(new EarthMap(10, 10));
        var grassManager = new GrassManager(10, 10);
        var animalManager = new AnimalManager(mapStatistics, grassManager);
        // when
        assertEquals(0, animalManager.getElements().size());
        var genotype = new ArrayList<>(List.of(GenomeDirection.ZERO));
        Animal animal1 = new Animal(new Vector2d(0, 0), new Genome(genotype), 100);
        Animal animal2 = new Animal(new Vector2d(0, 0), new Genome(genotype), 50);
        // then
        animalManager.addToAnimals(animal1.getPosition(), animal1);
        animalManager.addToAnimals(animal2.getPosition(), animal2);
        assertEquals(animal1, animalManager.getStrongestAnimal(new Vector2d(0, 0)).get());
        assertEquals(animal1.getGenome(), animalManager.getDominantGenome());
    }

    @Test
    void reproductionTest() {
        // given
        var config = new Config(10,
                10,
                0,
                0,
                0,
                0,
                0,
                50,
                20,
                10,
                0,
                0,
                GenomeType.FULL_RANDOM_GENOME_CHANGE,
                4,
                MapType.EARTH_MAP,
                false);

        var mapStatistics = new MapStatistics(new EarthMap(10, 10));
        var grassManager = new GrassManager(10, 10);
        var animalManager = new AnimalManager(mapStatistics, grassManager);
        // when
        var genotype = new ArrayList<>(List.of(GenomeDirection.ZERO));
        Animal animal1 = new Animal(new Vector2d(0, 0), new Genome(genotype), 100);
        Animal animal2 = new Animal(new Vector2d(0, 0), new Genome(genotype), 50);
        animalManager.addToAnimals(animal1.getPosition(), animal1);
        animalManager.addToAnimals(animal2.getPosition(), animal2);

        // then
        animalManager.reproduceAnimals(config);
        var child = animal1.getKids().getFirst();
        assertEquals(child.getGenome(), animal1.getGenome());
    }

    @Test
    void eatTest() {
        // given
        var mapStatistics = new MapStatistics(new EarthMap(10, 10));
        var grassManager = new GrassManager(10, 10);
        var animalManager = new AnimalManager(mapStatistics, grassManager);
        // when
        var genotype = new ArrayList<>(List.of(GenomeDirection.ZERO));
        Animal animal1 = new Animal(new Vector2d(0, 0), new Genome(genotype), 100);
        Animal animal2 = new Animal(new Vector2d(0, 0), new Genome(genotype), 50);
        Animal animal3 = new Animal(new Vector2d(1, 1), new Genome(genotype), 50);
        // then
        animalManager.addToAnimals(animal1.getPosition(), animal1);
        animalManager.addToAnimals(animal2.getPosition(), animal2);
        animalManager.addToAnimals(animal3.getPosition(), animal3);

        for (int i = 0; i < 121; i++) {
            grassManager.addGrass();
        }

        animalManager.feedAnimals(10);
        assertEquals(110, animal1.getEnergy());
        assertEquals(50, animal2.getEnergy());
        assertEquals(60, animal3.getEnergy());

        assertFalse(grassManager.isGrassAt(new Vector2d(0, 0)));
        assertFalse(grassManager.isGrassAt(new Vector2d(1, 1)));
    }

    @Test
    void moveAnimalsAndRemoveTest() {
        // given
        var mapStatistics = new MapStatistics(new EarthMap(10, 10));
        var grassManager = new GrassManager(10, 10);
        var animalManager = new AnimalManager(mapStatistics, grassManager);
        // when
        var genotype = new ArrayList<>(List.of(GenomeDirection.ZERO));
        Animal animal1 = new Animal(new Vector2d(0, 0), new Genome(genotype), 100);
        Animal animal2 = new Animal(new Vector2d(0, 0), new Genome(genotype), 50);
        Animal animal3 = new Animal(new Vector2d(1, 1), new Genome(genotype), 50);
        // then
        animalManager.addToAnimals(animal1.getPosition(), animal1);
        animalManager.addToAnimals(animal2.getPosition(), animal2);
        animalManager.addToAnimals(animal3.getPosition(), animal3);

        for (int i = 0; i < 8; i++) {
            animalManager.moveAllAnimals(10, new EarthMap(10, 10));
            assertEquals(100 - 10 * (i + 1), animal1.getEnergy());
            assertEquals(50 - 10 * (i + 1), animal2.getEnergy());
            assertEquals(50 - 10 * (i + 1), animal3.getEnergy());
            assertTrue(animalManager.isAnimalAt(new Vector2d(0, i + 1)));
            assertTrue(animalManager.isAnimalAt(new Vector2d(1, i + 2)));
        }
        animalManager.removeDeadAnimals();
        assertEquals(1, animalManager.getElements().size());
        assertEquals(animal1, animalManager.getElements().getFirst());
    }
}