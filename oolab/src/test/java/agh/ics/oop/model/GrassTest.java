package agh.ics.oop.model;

import agh.ics.oop.model.MapObjects.Grass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassTest {
    Grass grass = new Grass(new Vector2d(2,2));

    @Test
    void creatorCheck(){
        assertEquals(new Vector2d(2,2),grass.getPosition());
    }
    @Test
    void isAtForTheSameAndDifferentVectors(){
        assertTrue(grass.isAt(new Vector2d(2,2)));
        assertFalse(grass.isAt(new Vector2d(0,0)));
    }
    @Test
    void toStringCorrect(){
        assertEquals("*",grass.toString());
    }


}