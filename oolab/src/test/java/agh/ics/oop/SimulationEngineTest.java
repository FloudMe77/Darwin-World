//package agh.ics.oop;
//
//import agh.ics.oop.model.*;
//import agh.ics.oop.model.util.ConsoleMapDisplay;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SimulationEngineTest {
//    @Test
//    void correctnessOfRunSync(){
//        // test sprawdza popraność ułożenia zwierząt dla wszsytkich symulacji
//        // przy wywoływaniu RunSync
//
//        // given
//        String[] characters = {"f", "b",
//                "r", "l",
//                "f", "f",
//                "r", "r",
//                "f", "f",
//                "f", "f",
//                "f", "f",
//                "f", "f"};
//        List<MoveDirection> directions = OptionsParser.parse(characters);
//        List<Vector2d> positions = List.of(new Vector2d(1, 1), new Vector2d(1, 2));
//
//        List<Simulation> simulations = new ArrayList<>();
//        for(int i=0;i<100;i++) {
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
//
//        // when
//        simulationEngine.runSync();
//
//        // then
//        int cntOfSimulation=0;
//        for(var simulation:simulations){
//            var animalsList = simulation.getAnimals();
//
//            if(cntOfSimulation==0){ // sprawdzanie GrassField
//                assertTrue(animalsList.getFirst().isAt(new Vector2d(2,-3)));
//                assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));
//
//                assertTrue(animalsList.getLast().isAt(new Vector2d(0,6)));
//                assertTrue(animalsList.getLast().atDirection(MapDirection.NORTH));
//            }
//            else{ // sprawdzanie RectangleMap
//                assertTrue(animalsList.getFirst().isAt(new Vector2d(2,0)));
//                assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));
//
//                assertTrue(animalsList.getLast().isAt(new Vector2d(0,4)));
//                assertTrue(animalsList.getLast().atDirection(MapDirection.NORTH));
//            }
//            cntOfSimulation=(cntOfSimulation+1)%2;
//        }
//    }
//
//    @Test
//    void correctnessOfRunAsync(){
//        // test sprawdza popraność ułożenia zwierząt dla wszsytkich symulacji
//        // przy wywoływaniu RunAsync
//
//        // given
//        String[] characters = {"f", "b",
//                "r", "l",
//                "f", "f",
//                "r", "r",
//                "f", "f",
//                "f", "f",
//                "f", "f",
//                "f", "f"};
//        List<MoveDirection> directions = OptionsParser.parse(characters);
//        List<Vector2d> positions = List.of(new Vector2d(1, 1), new Vector2d(1, 2));
//
//        List<Simulation> simulations = new ArrayList<>();
//        for(int i=0;i<100;i++) {
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
//
//        // when
//        simulationEngine.runAsync();
//
//        // then
//        int cntOfSimulation=0;
//        for(var simulation:simulations){
//            var animalsList = simulation.getAnimals();
//            if(cntOfSimulation==0){  // sprawdzanie GrassField
//                assertTrue(animalsList.getFirst().isAt(new Vector2d(2,-3)));
//                assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));
//
//                assertTrue(animalsList.getLast().isAt(new Vector2d(0,6)));
//                assertTrue(animalsList.getLast().atDirection(MapDirection.NORTH));
//            }
//            else{ // sprawdzanie RectangleMap
//                assertTrue(animalsList.getFirst().isAt(new Vector2d(2,0)));
//                assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));
//
//                assertTrue(animalsList.getLast().isAt(new Vector2d(0,4)));
//                assertTrue(animalsList.getLast().atDirection(MapDirection.NORTH));
//            }
//            cntOfSimulation=(cntOfSimulation+1)%2;
//        }
//    }
//    @Test
//    void correctnessOfRunAsyncInThreadPool(){
//        // test sprawdza popraność ułożenia zwierząt dla wszsytkich symulacji
//        // przy wywoływaniu runAsyncInThreadPool
//
//        // given
//        String[] characters = {"f", "b",
//                "r", "l",
//                "f", "f",
//                "r", "r",
//                "f", "f",
//                "f", "f",
//                "f", "f",
//                "f", "f"};
//        List<MoveDirection> directions = OptionsParser.parse(characters);
//        List<Vector2d> positions = List.of(new Vector2d(1, 1), new Vector2d(1, 2));
//
//        List<Simulation> simulations = new ArrayList<>();
//        for(int i=0;i<100;i++) {
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
//
//        // when
//        simulationEngine.runAsyncInThreadPool();
//
//        // then
//        int cntOfSimulation=0;
//        for(var simulation:simulations){
//            var animalsList = simulation.getAnimals();
//
//
//            if(cntOfSimulation==0){ // sprawdzanie GrassField
//                assertTrue(animalsList.getFirst().isAt(new Vector2d(2,-3)));
//                assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));
//
//                assertTrue(animalsList.getLast().isAt(new Vector2d(0,6)));
//                assertTrue(animalsList.getLast().atDirection(MapDirection.NORTH));
//            }
//            else{ // sprawdzanie RectangleMap
//                assertTrue(animalsList.getFirst().isAt(new Vector2d(2,0)));
//                assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));
//
//                assertTrue(animalsList.getLast().isAt(new Vector2d(0,4)));
//                assertTrue(animalsList.getLast().atDirection(MapDirection.NORTH));
//            }
//            cntOfSimulation=(cntOfSimulation+1)%2;
//        }
//    }
//}