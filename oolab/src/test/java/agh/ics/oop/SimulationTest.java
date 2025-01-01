package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.maps.EarthMap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SimulationTest {
    WorldMap map = new EarthMap(5, 5);

    @Test
    void onlyOrientationCheck() {
        // given
        String[] characters = {"l", "l", "l", "r"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        // then
        List<Animal> animalsList = simulation.getAnimals();
        assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));
    }

    @Test
    void onlyMovingCheck() {
        // given
        String[] characters = {"f", "f", "b", "b", "b"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        // then
        List<Animal> animalsList = simulation.getAnimals();
        assertTrue(animalsList.getFirst().isAt(new Vector2d(2, 1)));
    }

    @Test
    void movingOnMap() {
        // given
        String[] characters = {"f", "l", "f", "l", "b", "r", "b", "b"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        // then
        List<Animal> animalsList = simulation.getAnimals();
        assertTrue(animalsList.getFirst().isAt(new Vector2d(3, 4)));
        assertTrue(animalsList.getFirst().atDirection(MapDirection.WEST));
    }

    @Test
    void crossingBordersLeftAndDown() {

        // given
        String[] characters = {"l", "f", "l", "f"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(0, 0));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        // then
        List<Animal> animalsList = simulation.getAnimals();
        assertTrue(animalsList.getFirst().isAt(new Vector2d(0, 0)));
        assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));

    }

    @Test
    void crossingBordersUpperAndRight() {
        // given
        String[] characters = {"f", "r", "f"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(4, 4));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        // then
        List<Animal> animalsList = simulation.getAnimals();
        assertTrue(animalsList.getFirst().isAt(new Vector2d(4, 4)));
        assertTrue(animalsList.getFirst().atDirection(MapDirection.EAST));

    }

    @Test
    void onFourAnimals() {
        // given
        String[] characters = {"f", "b", "l", "r",
                "f", "f", "f", "f"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(0, 0), new Vector2d(1, 1), new Vector2d(2, 2), new Vector2d(3, 3));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        List<Animal> animalsList = simulation.getAnimals();
        // then
        assertTrue(animalsList.getFirst().isAt(new Vector2d(0, 2)));
        assertTrue(animalsList.getFirst().atDirection(MapDirection.NORTH));

        assertTrue(animalsList.get(1).isAt(new Vector2d(1, 1)));
        assertTrue(animalsList.get(1).atDirection(MapDirection.NORTH));

        assertTrue(animalsList.get(2).isAt(new Vector2d(1, 2)));
        assertTrue(animalsList.get(2).atDirection(MapDirection.WEST));

        assertTrue(animalsList.get(3).isAt(new Vector2d(4, 3)));
        assertTrue(animalsList.get(3).atDirection(MapDirection.EAST));
    }

    @Test
    void unCorrectCommand() {
        // given
        String[] characters = {"das", "rw", "sa"};
        assertThrows(IllegalArgumentException.class, () -> {
            OptionsParser.parse(characters);
        });

    }

    @Test
    void testWithCollisionBetweenAnimals() {
        // given
        String[] characters = {"f", "b",
                "r", "l",
                "f", "f",
                "r", "r",
                "f", "f",
                "f", "f",
                "f", "f",
                "f", "f"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        // then
        List<Animal> animalsList = simulation.getAnimals();
        assertTrue(animalsList.getFirst().isAt(new Vector2d(2, 0)));
        assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));

        assertTrue(animalsList.getLast().isAt(new Vector2d(3, 4)));
        assertTrue(animalsList.getLast().atDirection(MapDirection.NORTH));

    }

    @Test
    void tooFawCommandsForAllAnimals() {
        // given
        String[] characters = {"f", "b", "l", "r",
                "f"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(0, 0), new Vector2d(1, 1), new Vector2d(2, 2), new Vector2d(3, 3));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        List<Animal> animalsList = simulation.getAnimals();
        // then
        assertTrue(animalsList.getFirst().isAt(new Vector2d(0, 2)));
        assertTrue(animalsList.getFirst().atDirection(MapDirection.NORTH));

        assertTrue(animalsList.get(1).isAt(new Vector2d(1, 0)));
        assertTrue(animalsList.get(1).atDirection(MapDirection.NORTH));

        assertTrue(animalsList.get(2).isAt(new Vector2d(2, 2)));
        assertTrue(animalsList.get(2).atDirection(MapDirection.WEST));

        assertTrue(animalsList.get(3).isAt(new Vector2d(3, 3)));
        assertTrue(animalsList.get(3).atDirection(MapDirection.EAST));
    }

    @Test
    void differentSizeOfMap() {
        WorldMap map = new EarthMap(3, 4);
        // given
        String[] characters = {"f", "b",
                "r", "l",
                "f", "f",
                "r", "r",
                "f", "f",
                "f", "f",
                "f", "f",
                "f", "f"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(2, 0), new Vector2d(1, 1));
        Simulation simulation = new Simulation(positions, directions, map);
        // when
        simulation.run();
        // then
        List<Animal> animalsList = simulation.getAnimals();
        assertTrue(animalsList.getFirst().isAt(new Vector2d(2, 0)));
        assertTrue(animalsList.getFirst().atDirection(MapDirection.SOUTH));

        assertTrue(animalsList.getLast().isAt(new Vector2d(0, 3)));
        assertTrue(animalsList.getLast().atDirection(MapDirection.NORTH));

    }

    @Test
    void createSomeAnimalsInTheSamePlace() {
        // given
        String[] characters = {"f", "b"};
        List<MoveDirection> directions = OptionsParser.parse(characters);
        List<Vector2d> positions = List.of(new Vector2d(2, 0), new Vector2d(1, 1), new Vector2d(1, 1), new Vector2d(2, 0));
        // when
        Simulation simulation = new Simulation(positions, directions, map);
        // then
        assertEquals(2, simulation.getAnimals().size());
    }
}