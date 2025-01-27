package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.util.Config;
import agh.ics.oop.model.genomes.GenomeDirection;
import agh.ics.oop.model.genomes.FullRandomGenomeChange;
import agh.ics.oop.model.genomes.Genome;
import agh.ics.oop.model.genomes.GenomeType;
import agh.ics.oop.model.maps.EarthMap;
import agh.ics.oop.model.maps.MapType;
import agh.ics.oop.model.maps.WorldMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalTest {
    @Test
    void constructorAndGetterTest() {
        // when
        var animal = new Animal(new Vector2d(2, 2), new Genome(5), 10);
        // then
        assertEquals(new Vector2d(2, 2), animal.getPosition());
        assertEquals(5, animal.getGenome().getGenLength());
        assertEquals(10, animal.getEnergy());
        assertEquals(0, animal.getChildrenAmount());
        assertEquals(new ArrayList<>(), animal.getKids());
        assertEquals(0, animal.getEaten());
    }


    @Test
    void reproduceAnimalTest() {
        // given

        var animal1 = new Animal(new Vector2d(2, 2), new Genome(new ArrayList<>(List.of(GenomeDirection.ONE, GenomeDirection.TWO, GenomeDirection.THREE))), 10);
        var animal2 = new Animal(new Vector2d(2, 2), new Genome(new ArrayList<>(List.of(GenomeDirection.FOUR, GenomeDirection.FIVE, GenomeDirection.SIX))), 0);
        // when
        var animal3 = animal1.reproduce(animal2, new FullRandomGenomeChange(), 0, 0, 3);
        // then
        assertEquals(new ArrayList<>(List.of(GenomeDirection.ONE, GenomeDirection.TWO, GenomeDirection.THREE)), animal3.getGenome().getGenList());
        assertEquals(1, animal1.getChildrenAmount());
        assertEquals(1, animal2.getChildrenAmount());
        assertEquals(List.of(animal3), animal1.getKids());
        assertEquals(List.of(animal3), animal2.getKids());
        assertEquals(6, animal3.getEnergy());
    }

    @Test
    void moveTest() {
        // given
        var map = new EarthMap(10, 10);
        var animal1 = new Animal(new Vector2d(2, 2), new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO, GenomeDirection.ZERO, GenomeDirection.ZERO))), 10);
        var animal2 = new Animal(new Vector2d(2, 2), new Genome(new ArrayList<>(List.of(GenomeDirection.ONE, GenomeDirection.THREE, GenomeDirection.FIVE, GenomeDirection.SEVEN))), 10);
        // when
        map.place(animal1);

        //then
        for (int i = 0; i < 3; i++) {
            map.move(animal1, 0);
            assertTrue(animal1.isAt(new Vector2d(2, 2 + i + 1)));
            map.move(animal2, 0);
        }
        map.move(animal2, 0);
        assertTrue(animal2.isAt(new Vector2d(2, 2)));
    }

    @Test
    public void integrationTest() {
        //given
        ArrayList<GenomeDirection> genotype = new ArrayList<>(List.of(GenomeDirection.FIVE, GenomeDirection.FIVE, GenomeDirection.FIVE, GenomeDirection.FIVE));
        Animal animal1 = new Animal(new Vector2d(5, 5), new Genome(genotype), 101);
        WorldMap map = new EarthMap(10, 10);
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

        //then

        //when
        try {
            map.place(animal1);
        } catch (IncorrectPositionException e) {
            System.out.println("Uwaga: " + e.getMessage());
        }

        map.move(animal1, 0);


        //then
        assertEquals(new Vector2d(4, 4), animal1.getPosition());


        //given
        genotype = new ArrayList<>(List.of(GenomeDirection.SIX, GenomeDirection.SIX, GenomeDirection.SIX, GenomeDirection.SIX));
        Animal animal2 = new Animal(new Vector2d(4, 4), new Genome(genotype), 100);

        //when
        try {
            map.place(animal2);
        } catch (IncorrectPositionException e) {
            System.out.println("Uwaga: " + e.getMessage());
        }
        map.reproduceAnimals(config);
        Animal child = animal1.getKids().getFirst();
        System.out.println(animal1.getKids());


        //then
        assertEquals(81, animal1.getEnergy());
        assertEquals(80, animal2.getEnergy());
        assertEquals(40, child.getEnergy());
        System.out.println(child.getGenome());
        assertTrue(Objects.equals(child.getGenome().getGenList(), List.of(GenomeDirection.FIVE, GenomeDirection.FIVE, GenomeDirection.SIX, GenomeDirection.SIX))
                || Objects.equals(child.getGenome().getGenList(), List.of(GenomeDirection.SIX, GenomeDirection.SIX, GenomeDirection.FIVE, GenomeDirection.FIVE)));
        assertEquals(1, animal1.getChildrenAmount());
        assertEquals(1, animal2.getChildrenAmount());

        assertEquals(1, animal1.getDescendantsAmount());
        assertEquals(1, animal2.getDescendantsAmount());


        //when
        System.out.println(animal1.getEnergy() + " " + animal2.getEnergy() + " " + child.getEnergy());
        for (int i = 0; i < 5; i++) {
            map.moveAllAnimals(10);
        }


        map.removeDepthAnimals();

        //then
        assertEquals(6, animal1.getAge());
        assertEquals(5, animal2.getAge());
        assertEquals(5, child.getDayOfDeath());
        assertEquals(30, animal2.getEnergy());


        //when
        for (int i = 0; i < 121; i++) {
            map.addGrass();
        }

        map.feedAnimals(20);
        //then
        assertEquals(51, animal1.getEnergy());
        assertEquals(50, animal2.getEnergy());
    }
}