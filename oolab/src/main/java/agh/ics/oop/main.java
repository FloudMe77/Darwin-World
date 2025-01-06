package agh.ics.oop;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.MapStatistic;
import agh.ics.oop.model.maps.EarthMap;
import agh.ics.oop.model.util.ConsoleMapDisplay;
import agh.ics.oop.model.util.newUtils.FullRandomGenomeChange;

// do testowania
public class main {
    public static void main(String[] args) {
        var map = new EarthMap(10,10);
        map.addObserver(new ConsoleMapDisplay());

        Config config = new Config(10,
                10,
                30,
                10,
                2,
                20,
                10,
                5,
                2,
                4,
                0,
                2,
                new FullRandomGenomeChange(),
                5,
                map);
        Simulation simulation = new Simulation(config);
        simulation.run();
        var statistics = new MapStatistic(map);
        statistics.updateStatistic();
        statistics.printStatistic();
    }
}
