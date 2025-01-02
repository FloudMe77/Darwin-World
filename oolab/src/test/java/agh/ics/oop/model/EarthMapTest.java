//package agh.ics.oop.model;
//
//import agh.ics.oop.model.maps.EarthMap;
//import agh.ics.oop.model.maps.WorldMap;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class EarthMapTest {
//    WorldMap map = new EarthMap(5,5);
//
//    @Test
//    void isOccupiedTwoPlacesAndPlaceCorrect(){
//        // given
//        var vector11 = new Vector2d(1, 1);
//        var vector12 = new Vector2d(1, 2);
//        var vector13 = new Vector2d(1, 3);
//        var a1 = new Animal(vector11);
//        var a2 = new Animal(vector12);
//        // when
//        assertDoesNotThrow(()->map.place(a1));
//        assertDoesNotThrow(()->map.place(a2));
//
//        // then
//        assertTrue(map.isOccupied(vector11));
//        assertTrue(map.isOccupied(vector12));
//        assertFalse(map.isOccupied(vector13));
//    }
//    @Test
//    void objectAtForTwoAnimal(){
//        // given
//        var vector11 = new Vector2d(1, 1);
//        var vector12 = new Vector2d(1, 2);
//        var a1 = new Animal(vector11);
//        var a2 = new Animal(vector12);
//        // when
//        assertDoesNotThrow(()->map.place(a1));
//        assertDoesNotThrow(()->map.place(a2));
//        // then
//        assertEquals(a1,map.objectsAt(vector11).getFirst());
//        assertEquals(a2,map.objectsAt(vector12).getFirst());
//        assertTrue(map.objectsAt(new Vector2d(2,2)).isEmpty());
//    }
//
//
//    @Test
//    void moveToAllDirection() {
//        // given
//        var a1 = new Animal(new Vector2d(1, 1));
//        //when
//        assertDoesNotThrow(() -> map.place(a1));
//        // then
//        map.move(a1, MoveDirection.FORWARD);
//        System.out.println(a1.getPosition());
//        System.out.println(map);
//        assertTrue(map.isOccupied(new Vector2d(1, 2)));
//
//        map.move(a1, MoveDirection.BACKWARD);
//        assertTrue(map.isOccupied(new Vector2d(1, 1)));
//    }
//
//    @Test
//    void goingOutOfBoundToPolesWorksCorrectly() {
//        WorldMap map = new EarthMap(5,5);
//        var animal1 = new Animal(new Vector2d(4, 4));
//        var animal2 = new Animal(new Vector2d(3, 4));
//        var animal3 = new Animal(new Vector2d(4, 2));
//        var animal4 = new Animal(new Vector2d(0, 2));
//        var animal3ExpectedPosition = new Vector2d(0, 2);
//        var animal4ExpectedPosition = new Vector2d(4, 2);
//
//
//        assertDoesNotThrow(() -> {
//            map.place(animal1);
//            map.place(animal2);
//            map.place(animal3);
//            map.place(animal4);
//        });
//
//        map.move(animal1, MoveDirection.FORWARD);
//        map.move(animal2, MoveDirection.RIGHT);
//        map.move(animal2, MoveDirection.FORWARD);
//        map.move(animal3, MoveDirection.RIGHT);
//        map.move(animal3, MoveDirection.RIGHT);
//        map.move(animal3, MoveDirection.FORWARD);
//        map.move(animal4, MoveDirection.LEFT);
//        map.move(animal4, MoveDirection.LEFT);
//        map.move(animal4, MoveDirection.FORWARD);
//
//        assertEquals(MapDirection.SOUTH, animal1.getCurrentDirection());
//        assertEquals(MapDirection.SOUTHWEST, animal2.getCurrentDirection());
//        assertEquals(animal3ExpectedPosition, animal3.getPosition());
//        assertEquals(animal4ExpectedPosition, animal4.getPosition());
//    }
//
//    @Test
//    void canPlaceTwoAnimalsOnTheSamePosition() {
//        WorldMap map = new EarthMap(2,2);
//        var position = new Vector2d(2, 2);
//        var animal1 = new Animal(position);
//        var animal2 = new Animal(position);
//
//        assertDoesNotThrow(() -> {
//            map.place(animal1);
//            map.place(animal2);
//        });
//    }
//}