package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.GenomeDirection;
import agh.ics.oop.model.IncorrectPositionException;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.BasicRectangularMap;
import agh.ics.oop.model.maps.EarthMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.newUtils.FullRandomGenomeChange;
import agh.ics.oop.model.util.newUtils.Genome;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
class AnimalTest {
    @Test
    void constructorAndGetterTest(){
        // when
        var animal = new Animal(new Vector2d(2,2), new Genome(5),10);
        // then
        assertEquals(new Vector2d(2,2),animal.getPosition());
        assertEquals(5,animal.getGenome().getGenLength());
        assertEquals(10,animal.getEnergy());
        assertEquals(0,animal.getChildrenAmount());
        assertEquals(new ArrayList<>(),animal.getKids());
        assertTrue(animal.isAlive());
    }

//    @Test
//    void dieAnimalTest(){
//        // given
//        var animal = new Animal(new Vector2d(2,2), new Genome(5),10);
//        // when
//        animal.getOlder();
//        assertFalse(animal.isAlive());
//        assertEquals(1,animal.getDayOfDeath());
//    }

    @Test
    void reproduceAnimalTest(){
        // given
        var animal1 = new Animal(new Vector2d(2,2), new Genome(new ArrayList<>(List.of(GenomeDirection.ONE, GenomeDirection.TWO, GenomeDirection.THREE))),10);
        var animal2 = new Animal(new Vector2d(2,2), new Genome(new ArrayList<>(List.of(GenomeDirection.FOUR, GenomeDirection.FIVE, GenomeDirection.SIX))),0);
        // when
        var animal3 = animal1.reproduce(animal2,new FullRandomGenomeChange(),0,0,3);
        // then
        assertEquals(new ArrayList<>(List.of(GenomeDirection.ONE, GenomeDirection.TWO, GenomeDirection.THREE)),animal3.getGenome().getGenList());
        assertEquals(1,animal1.getChildrenAmount());
        assertEquals(1,animal2.getChildrenAmount());
        assertEquals(List.of(animal3),animal1.getKids());
        assertEquals(List.of(animal3),animal2.getKids());
        assertEquals(6,animal3.getEnergy());
    }

    @Test
    void moveTest(){
        // given
        var map = new EarthMap(10,10);
        var animal1 = new Animal(new Vector2d(2,2), new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO, GenomeDirection.ZERO, GenomeDirection.ZERO))),10);
        var animal2 = new Animal(new Vector2d(2,2), new Genome(new ArrayList<>(List.of(GenomeDirection.ONE, GenomeDirection.THREE, GenomeDirection.FIVE, GenomeDirection.SEVEN))),10);
        // when
        map.place(animal1);

        //then
        for(int i=0;i<3;i++) {
            map.move(animal1);
            assertTrue(animal1.isAt(new Vector2d(2,2+i+1)));
            map.move(animal2);
        }
        map.move(animal2);
        assertTrue(animal2.isAt(new Vector2d(2,2)));
    }

    @Test
    public void integrationTest(){
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
                new FullRandomGenomeChange(),
                4,
                map);

        //then
        assertTrue(animal1.isAlive());

        //when
        try {
            map.place(animal1);
        } catch (IncorrectPositionException e) {
            System.out.println("Uwaga: " + e.getMessage());
        }

        map.move(animal1);
        animal1.getOlder();


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
        Animal child = map.reproduceAnimals(config).getFirst();

        //then
        assertEquals(81, animal1.getEnergy());
        assertEquals(80, animal2.getEnergy());
        assertEquals(40, child.getEnergy());
        System.out.println(child.getGenome());
        assertTrue(Objects.equals(child.getGenome().getGenList(), List.of(GenomeDirection.FIVE, GenomeDirection.FIVE, GenomeDirection.FIVE, GenomeDirection.SIX))
                || Objects.equals(child.getGenome().getGenList(), List.of(GenomeDirection.SIX, GenomeDirection.FIVE, GenomeDirection.FIVE, GenomeDirection.FIVE)));
        assertEquals(1, animal1.getChildrenAmount());
        assertEquals(1, animal2.getChildrenAmount());


        //when
        for (int i = 0; i < 5; i++) {
            animal1.reduceEnergy(10);
            animal1.getOlder();
            animal2.reduceEnergy(10);
            animal2.getOlder();
            child.reduceEnergy(10);
            child.getOlder();
        }
        map.removeDepthAnimals();

        //then
        System.out.println(child.getEnergy());
        assertFalse(child.isAlive());
        assertEquals(6, animal1.getAge());
        assertEquals(5, animal2.getAge());
        assertEquals(5, child.getDayOfDeath());
        assertTrue(animal1.isAlive());
        assertEquals(30, animal2.getEnergy());


        //when
        for(int i=0 ;i <121;i++){
            map.addGrass();
        }

        map.feedAnimals(20);
        System.out.println(map );
        //then
        assertEquals(51, animal1.getEnergy());
        assertEquals(30, animal2.getEnergy());

    }
}