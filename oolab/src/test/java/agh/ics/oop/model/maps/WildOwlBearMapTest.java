package agh.ics.oop.model.maps;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.Config;
import agh.ics.oop.model.GenomeDirection;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapStatistic;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.ConsoleMapDisplay;
import agh.ics.oop.model.util.newUtils.FullRandomGenomeChange;
import agh.ics.oop.model.util.newUtils.Genome;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WildOwlBearMapTest {

    @Test
    void moveTest(){
        var map = new WildOwlBearMap(10,10,new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO))));
        MapStatistic statistics = map.getMapStatistic();

        map.addObserver(new ConsoleMapDisplay());

        Config config = new Config(10,
                10,
                0,
                10,
                0,
                0,
                10,
                5,
                2,
                4,
                0,
                2,
                new FullRandomGenomeChange(),
                5,
                map);

        for(int i=0 ;i<10;i++){
            map.moveAllAnimals(0);
            System.out.println(map);
        }
        assertTrue(map.isOccupied(new Vector2d(0,4)));
    }
    @Test
    void eatTest(){
        var map = new WildOwlBearMap(10,10,new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO))));
        MapStatistic statistics = map.getMapStatistic();
        var animal1 = new Animal(new Vector2d(0,2), new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO, GenomeDirection.FOUR))),10);
        var animal2 = new Animal(new Vector2d(0,2), new Genome(new ArrayList<>(List.of(GenomeDirection.ZERO, GenomeDirection.FOUR))),10);

        map.place(animal1);
        map.place(animal2);

        map.addObserver(new ConsoleMapDisplay());

        Config config = new Config(10,
                10,
                0,
                10,
                0,
                0,
                10,
                5,
                2,
                4,
                0,
                2,
                new FullRandomGenomeChange(),
                5,
                map);

        for(int i=0 ;i<10;i++){
            map.moveAllAnimals(0);
            map.feedAnimals(0);
            System.out.println(map);
        }
        var stats= map.getMapStatistic();
        stats.printStatistic();
        assertTrue(map.isOccupied(new Vector2d(0,4)));
    }
}