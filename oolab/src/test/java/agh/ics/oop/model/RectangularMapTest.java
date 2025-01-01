package agh.ics.oop.model;

import agh.ics.oop.model.MapObjects.Animal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {
    WorldMap map = new RectangularMap(5,5);

    @Test
    void isOccupiedTwoPlacesAndPlaceCorrect(){
        // given
        var vector11 = new Vector2d(1, 1);
        var vector12 = new Vector2d(1, 2);
        var vector13 = new Vector2d(1, 3);
        var a1 = new Animal(vector11);
        var a2 = new Animal(vector12);
        // when
        assertDoesNotThrow(()->map.place(a1));
        assertDoesNotThrow(()->map.place(a2));

        // then
        assertTrue(map.isOccupied(vector11));
        assertTrue(map.isOccupied(vector12));
        assertFalse(map.isOccupied(vector13));
    }
    @Test
    void objectAtForTwoAnimal(){
        // given
        var vector11 = new Vector2d(1, 1);
        var vector12 = new Vector2d(1, 2);
        var a1 = new Animal(vector11);
        var a2 = new Animal(vector12);
        // when
        assertDoesNotThrow(()->map.place(a1));
        assertDoesNotThrow(()->map.place(a2));
        // then
        assertEquals(a1,map.objectAt(vector11));
        assertEquals(a2,map.objectAt(vector12));
        assertNull(map.objectAt(new Vector2d(2,2)));
    }


    @Test
    void moveToAllDirection(){
        // given
        var a1 = new Animal(new Vector2d(1,1));
        //when
        assertDoesNotThrow(()->map.place(a1));
        // then
        map.move(a1,MoveDirection.FORWARD);
        assertTrue(map.isOccupied(new Vector2d(1,2)));

        map.move(a1,MoveDirection.BACKWARD);
        assertTrue(map.isOccupied(new Vector2d(1,1)));

        map.move(a1,MoveDirection.LEFT);
        assertEquals("W", map.objectAt(new Vector2d(1,1)).toString());
        
        map.move(a1,MoveDirection.RIGHT);
        assertEquals("N", map.objectAt(new Vector2d(1,1)).toString());
    }



}