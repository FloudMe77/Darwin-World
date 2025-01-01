package agh.ics.oop.model;

import agh.ics.oop.model.maps.EarthMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    WorldMap map = new EarthMap(5,5);

    @Test
    void defaultConstructor(){
        //given
        WorldMap map = new EarthMap(3,3);
        // when
        var a1 = new Animal();
        // then
        assertEquals(new Vector2d(2,2),a1.getPosition());
        assertTrue(a1.atDirection(MapDirection.NORTH));
    }
    @Test
    void constructorWithVector(){
        // when
        var a1 = new Animal(new Vector2d(0,0));
        // then
        assertEquals(new Vector2d(0,0),a1.getPosition());
        assertTrue(a1.atDirection(MapDirection.NORTH));
    }
    @Test
    void IsAtOnNormalVector(){
        // when
        var a1 = new Animal(new Vector2d(2,3));
        // then
        assertTrue(a1.isAt(new Vector2d(2,3)));
    }
    @Test
    void atDirectionNormalVector(){
        // when
        var a1 = new Animal(new Vector2d(2,3));
        // then
        assertTrue(a1.atDirection(MapDirection.NORTH));
    }
    @Test
    void leftTurnAndToString(){
        // given
        var a1 = new Animal();
        // when
        a1.move(MoveDirection.LEFT,map);
        // then
        assertTrue(a1.atDirection(MapDirection.WEST));
        assertTrue(a1.isAt(new Vector2d(2,2)));
    }
    @Test
    void rightTurnAndToString(){
        // given
        var a1 = new Animal();
        // when
        a1.move(MoveDirection.RIGHT,map);
        // then
        assertTrue(a1.atDirection(MapDirection.EAST));
        assertTrue(a1.isAt(new Vector2d(2,2)));
    }
    @Test
    void GoForwardAndToString(){
        // given
        var a1 = new Animal();
        System.out.println(a1.getPosition());
        // when
        a1.move(MoveDirection.FORWARD,map);
        // then
        assertTrue(a1.atDirection(MapDirection.NORTH));
        System.out.println(a1.getPosition());
        assertTrue(a1.isAt(new Vector2d(2,3)));
    }
    @Test
    void GoBackwardAndToString(){
        // given
        var a1 = new Animal();
        // when
        a1.move(MoveDirection.BACKWARD,map);
        // then
        assertTrue(a1.atDirection(MapDirection.NORTH));
        assertTrue(a1.isAt(new Vector2d(2,1)));
    }
    @Test
    void CrossLeftBorder(){
        // given
        var a1 = new Animal(new Vector2d(0,1));
        // when
        a1.move(MoveDirection.LEFT,map);
        a1.move(MoveDirection.FORWARD,map);
        // then
        assertTrue(a1.atDirection(MapDirection.WEST));
        assertTrue(a1.isAt(new Vector2d(0,1)));
    }
    @Test
    void CrossUpperBorder(){
        // given
        var a1 = new Animal(new Vector2d(1,4));
        // when
        a1.move(MoveDirection.FORWARD,map);
        // then
        assertTrue(a1.isAt(new Vector2d(1,4)));
    }
    @Test
    void CrossRightBorder(){
        // given
        var a1 = new Animal(new Vector2d(4,3));
        // when
        a1.move(MoveDirection.RIGHT,map);
        a1.move(MoveDirection.FORWARD,map);
        // then
        assertTrue(a1.atDirection(MapDirection.EAST));
        assertTrue(a1.isAt(new Vector2d(4,3)));
    }
    @Test
    void CrossDownBorder(){
        // given
        var a1 = new Animal(new Vector2d(1,0));
        // when
        a1.move(MoveDirection.BACKWARD,map);
        // then
        assertEquals(new Vector2d(1,0),a1.getPosition());
    }


}