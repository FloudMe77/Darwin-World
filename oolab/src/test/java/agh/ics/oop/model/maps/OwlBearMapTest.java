package agh.ics.oop.model.maps;

import agh.ics.oop.model.genomes.GenomeDirection;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Vector2d;
import agh.ics.oop.model.genomes.Genome;
import agh.ics.oop.model.util.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OwlBearMapTest {

    @Test
    void moveTest(){
        var map = new OwlBearMap(10,10,new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO))));
        int maxY = map.getOwlBearMap().getCurrentBounds().rightUpperCornerMap().y();
        int x = map.getOwlBear().getPosition().x();
        map.addObserver(new ConsoleMapDisplay());

        for(int i=0; i<10; i++){
            map.moveAllAnimals(0);
//            System.out.println(map);
        }
        assertTrue(map.isOccupied(new Vector2d(x, maxY)));
    }
    @Test
    void eatTest(){
        var map = new OwlBearMap(10,10,new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO))));
        int maxY = map.getOwlBearMap().getCurrentBounds().rightUpperCornerMap().y();
        Vector2d owlBearPos = map.getOwlBear().getPosition();

        var animal1 = new Animal(new Vector2d(owlBearPos.x(), Math.min(owlBearPos.y() + 2, 9)), new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO, GenomeDirection.FOUR))),10);
        var animal2 = new Animal(new Vector2d(owlBearPos.x(), Math.min(owlBearPos.y() + 2, 9)), new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO, GenomeDirection.FOUR))),10);

        map.place(animal1);
        map.place(animal2);

        map.addObserver(new ConsoleMapDisplay());
        for(int i=0; i<10; i++){
            map.moveAllAnimals(0);
            map.feedAnimals(0);
//            System.out.println(map);
        }

        assertTrue(map.isOccupied(new Vector2d(owlBearPos.x(),maxY)));
        assertEquals(1,map.getElements().size());
    }
}