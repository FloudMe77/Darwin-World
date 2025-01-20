package agh.ics.oop.model.maps;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.GenomeDirection;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapStatistics;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OwlBearMapTest {

    @Test
    void moveTest(){
        var map = new OwlBearMap(10,10,new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO))));
        for(int i=0 ;i<10;i++){
            map.moveAllAnimals(0);
            System.out.println(map);
        }
        assertTrue(map.isOccupied(new Vector2d(2,3)));
    }
    @Test
    void eatTest(){
        var map = new OwlBearMap(10,10,new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO))));
        var animal1 = new Animal(new Vector2d(2,3), new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO, GenomeDirection.FOUR))),10);
        var animal2 = new Animal(new Vector2d(2,3), new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO, GenomeDirection.FOUR))),10);

        map.place(animal1);
        map.place(animal2);


        for(int i=0 ;i<10;i++){
            map.moveAllAnimals(0);
            map.feedAnimals(0);
        }
        assertTrue(map.isOccupied(new Vector2d(2,3)));
        assertEquals(1,map.getElements().size());
    }
}