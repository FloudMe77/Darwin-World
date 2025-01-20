package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {

    @Test
    void addTwoGrassOnSideTest(){
        var grassField = new GrassField(20,3);
        assertThrows(IllegalArgumentException.class, () ->
                grassField.addGrassPosition(new Vector2d(0, 0)));

    }
    @Test
    void fullMapTest(){
        // given
        var grassField = new GrassField(10,10);
        // when
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                assertNotEquals(Optional.empty(),grassField.getNewGrassPosition());
            }
        }
        // then
        assertEquals(Optional.empty(),grassField.getNewGrassPosition());
        // Dwie trawy w jednym miejscu
        grassField.addGrassPosition(new Vector2d(0, 0));
        assertThrows(IllegalArgumentException.class, () ->
                grassField.addGrassPosition(new Vector2d(0, 0)));

        // równik
        grassField.addGrassPosition(new Vector2d(0, 5));
        assertThrows(IllegalArgumentException.class, () ->
                grassField.addGrassPosition(new Vector2d(0, 5)));
    }
}