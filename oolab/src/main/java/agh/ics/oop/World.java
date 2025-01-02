//package agh.ics.oop;
//
//import agh.ics.oop.model.*;
//import agh.ics.oop.model.util.ConsoleMapDisplay;
//import javafx.application.Application;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class World {
//    public static void main(String[] args) {
////        Application.launch(SimulationApp.class, args);
//        List<MoveDirection> directions = OptionsParser.parse(args);
//        List<Vector2d> positions = List.of(new Vector2d(1, 1), new Vector2d(1, 2));
//
//        List<Simulation> simulations = new ArrayList<>();
//        for(int i=0;i<1;i++) {
//            GrassField map1 = new GrassField(10);
//            RectangularMap map2 = new RectangularMap(5, 5);
//            map1.addObserver(new ConsoleMapDisplay());
//            map2.addObserver(new ConsoleMapDisplay());
//            Simulation simulation1 = new Simulation(positions, directions, map1);
//            Simulation simulation2 = new Simulation(positions, directions, map2);
//            simulations.add(simulation1);
//            simulations.add(simulation2);
//        }
//        SimulationEngine simulationEngine = new SimulationEngine(simulations);
//        simulationEngine.runAsync();
//
//        System.out.println("System zakończył działanie");
//
//
//
//    }
//}
