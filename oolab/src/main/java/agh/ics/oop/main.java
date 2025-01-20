package agh.ics.oop;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.util.GenomeType;
import agh.ics.oop.model.util.MapType;

// do testowania
public class main {
    public static void main(String[] args) {

//        var map = new EarthMap(10,10);
//        MapStatistics statistics = map.getMapStatistics();
//
//        map.addObserver(new ConsoleMapDisplay());

        Config config = new Config(100,100,20,100,30,70,50,1,1,1,1,3,GenomeType.FULL_RANDOM_GENOME_CHANGE,8,MapType.EARTH_MAP,false);
        Simulation simulation = new Simulation(config);
        simulation.run();

//        statistics.printStatistic();
    }
}
