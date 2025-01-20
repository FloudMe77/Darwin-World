package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassManagerTest {
    @Test
    void fullMapOfGrass(){
        // given
        var grassManager = new GrassManager(10,10);
        // when
        for(int i=0;i<121;i++){
            assertTrue(grassManager.addGrass());
            assertEquals(i+1,grassManager.getElements().size());
        }
        // then
        assertFalse(grassManager.addGrass());
        // usuwanie
        grassManager.removeGrass(new Vector2d(0,0));
        assertFalse(grassManager.isGrassAt(new Vector2d(0,0)));


    }

}