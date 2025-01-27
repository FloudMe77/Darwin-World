package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.genomes.Genome;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OwlBearTest {
    @Test
    void constructorAndGetterTest() {
        // when
        var owlbear = new OwlBear(new Vector2d(2, 2), new Genome(5));
        // then
        assertEquals(new Vector2d(2, 2), owlbear.getPosition());
        assertEquals(5, owlbear.getGenome().getGenLength());
    }

}