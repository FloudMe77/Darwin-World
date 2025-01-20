package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.GenomeDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.EarthMap;
import agh.ics.oop.model.util.Genome;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OwlBearTest {
    @Test
    void constructorAndGetterTest(){
        // when
        var owlbear = new OwlBear(new Vector2d(2,2), new Genome(5));
        // then
        assertEquals(new Vector2d(2,2),owlbear.getPosition());
        assertEquals(5,owlbear.getGenome().getGenLength());
    }

}