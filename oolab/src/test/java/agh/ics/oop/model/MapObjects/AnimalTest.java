package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.GenomeDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.newUtils.FullRandomGenomeChange;
import agh.ics.oop.model.util.newUtils.Genome;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void dieAnimalTest(){
        // given
        var animal = new Animal(new Vector2d(2,2), new Genome(5),10);
        // when
        animal.getOlder();
        assertFalse(animal.isAlive());
        assertEquals(1,animal.getDayOfDeath());
    }
    @Test
    void reproduceAnimalTest(){
        // given
        var animal1 = new Animal(new Vector2d(2,2), new Genome(new ArrayList<>(List.of(GenomeDirection.ONE, GenomeDirection.TWO, GenomeDirection.THREE))),10);
        var animal2 = new Animal(new Vector2d(2,2), new Genome(new ArrayList<>(List.of(GenomeDirection.FOUR, GenomeDirection.FIVE, GenomeDirection.SIX))),0);
        // when
        var animal3 = animal1.reproduce(animal2,new FullRandomGenomeChange(),0,0);
        // then
        assertEquals(new ArrayList<>(List.of(GenomeDirection.ONE, GenomeDirection.TWO, GenomeDirection.THREE)),animal3.getGenome().getGenList());
        assertEquals(1,animal1.getChildrenAmount());
        assertEquals(1,animal2.getChildrenAmount());
        assertEquals(List.of(animal3),animal1.getKids());
        assertEquals(List.of(animal3),animal2.getKids());
    }
    // todo move test
}