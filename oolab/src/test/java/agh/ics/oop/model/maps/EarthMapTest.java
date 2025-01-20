package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Vector2d;
import agh.ics.oop.model.genomes.Genome;
import agh.ics.oop.model.genomes.GenomeDirection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EarthMapTest {
    WorldMap map = new EarthMap(5,5);
    Genome dummyGenome = new Genome(1);
    int dummyStartEnergy = 5;

    @Test
    void isOccupiedTwoPlacesAndPlaceCorrect(){
        // given
        var vector11 = new Vector2d(1, 1);
        var vector12 = new Vector2d(1, 2);
        var vector13 = new Vector2d(1, 3);
        var a1 = new Animal(vector11, dummyGenome, dummyStartEnergy);
        var a2 = new Animal(vector12, dummyGenome, dummyStartEnergy);
        // when
        assertDoesNotThrow(()->map.place(a1));
        assertDoesNotThrow(()->map.place(a2));

        // then
        assertTrue(map.isOccupied(vector11));
        assertTrue(map.isOccupied(vector12));
        assertFalse(map.isOccupied(vector13));
    }
    @Test
    void objectAtForTwoAnimals(){
        // given
        var vector11 = new Vector2d(1, 1);
        var vector12 = new Vector2d(1, 2);
        var a1 = new Animal(vector11, dummyGenome, dummyStartEnergy);
        var a2 = new Animal(vector12, dummyGenome, dummyStartEnergy);
        // when
        assertDoesNotThrow(()->map.place(a1));
        assertDoesNotThrow(()->map.place(a2));
        // then
        assertEquals(a1,map.objectsAt(vector11).getFirst());
        assertEquals(a2,map.objectsAt(vector12).getFirst());
        assertTrue(map.objectsAt(new Vector2d(2,2)).isEmpty());
    }

    @Test
    void goingOutOfBoundToPolesWorksCorrectly() {
        WorldMap map = new EarthMap(5,5);
        var animal1Genome = new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO)));
        var animal2Genome = new Genome(new ArrayList<>(List.of(GenomeDirection.ONE)));
        var animal3Genome = new Genome(new ArrayList<>(List.of(GenomeDirection.TWO)));
        var animal4Genome = new Genome(new ArrayList<>(List.of(GenomeDirection.SIX)));
        var animal1 = new Animal(new Vector2d(4, 4), animal1Genome, dummyStartEnergy);
        var animal2 = new Animal(new Vector2d(3, 4), animal2Genome, dummyStartEnergy);
        var animal3 = new Animal(new Vector2d(4, 2), animal3Genome, dummyStartEnergy);
        var animal4 = new Animal(new Vector2d(0, 2), animal4Genome, dummyStartEnergy);
        var animal3ExpectedPosition = new Vector2d(0, 2);
        var animal4ExpectedPosition = new Vector2d(4, 2);


        assertDoesNotThrow(() -> {
            map.place(animal1);
            map.place(animal2);
            map.place(animal3);
            map.place(animal4);
        });

        map.move(animal1,0);
        map.move(animal2,0);
        map.move(animal3,0);
        map.move(animal4,0);

        assertEquals(MapDirection.NORTH, animal1.getCurrentDirection());
        assertEquals(MapDirection.NORTHEAST, animal2.getCurrentDirection());
        assertEquals(animal3ExpectedPosition, animal3.getPosition());
        assertEquals(animal4ExpectedPosition, animal4.getPosition());
    }

    @Test
    void canPlaceTwoAnimalsOnTheSamePosition() {
        WorldMap map = new EarthMap(2,2);
        var position = new Vector2d(2, 2);
        var animal1 = new Animal(position, dummyGenome, dummyStartEnergy);
        var animal2 = new Animal(position, dummyGenome, dummyStartEnergy);

        assertDoesNotThrow(() -> {
            map.place(animal1);
            map.place(animal2);
        });
    }
}