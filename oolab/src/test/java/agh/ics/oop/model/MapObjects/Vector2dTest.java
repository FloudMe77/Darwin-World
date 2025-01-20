package agh.ics.oop.model.MapObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    @Test
    void equalsForTwoDifferentVectors() {
        // when
        var v1 = new Vector2d(1,2);
        var v2 = new Vector2d(3,5);
        //then
        assertFalse(v1.equals(v2));
    }
    @Test
    void equalsForTwoSameVectors() {
        // when
        var v1 = new Vector2d(1,2);
        var v2 = new Vector2d(1,2);
        //then
        assertTrue(v1.equals(v2));
    }
    @Test
    void toStringForVector() {
        // when
        var v1 = new Vector2d(1,2);
        //then
        assertEquals("(1,2)", v1.toString());
    }
    @Test
    void precedesAndFollowForHigherFirstVector() {
        // when
        var v1 = new Vector2d(3,5);
        var v2 = new Vector2d(3,4);
        //then
        assertFalse(v1.precedes(v2));
        assertTrue(v1.follows(v2));
    }
    @Test
    void precedesAndFollowForLowerFirstVector() {
        // when
        var v1 = new Vector2d(0,0);
        var v2 = new Vector2d(3,4);
        //then
        assertTrue(v1.precedes(v2));
        assertFalse(v1.follows(v2));
    }
    @Test
    void precedesAndFollowForNonOrderedVectors() {
        // when
        var v1 = new Vector2d(10,0);
        var v2 = new Vector2d(0,10);
        //then
        assertFalse(v1.precedes(v2));
        assertFalse(v1.follows(v2));
    }

    @Test
    void upperRightForNonOrderedVectors() {
        // when
        var v1 = new Vector2d(3,5);
        var v2 = new Vector2d(9,1);
        // then
        assertEquals(new Vector2d(9,5), v1.upperRight(v2));
        assertEquals(new Vector2d(9,5), v2.upperRight(v1));
    }
    @Test
    void lowerLeftForNonOrderedVectors() {
        // when
        var v1 = new Vector2d(3,5);
        var v2 = new Vector2d(9,1);
        // then
        assertEquals(new Vector2d(3,1), v1.lowerLeft(v2));
        assertEquals(new Vector2d(3,1), v2.lowerLeft(v1));
    }
    @Test
    void addAndSubtractTwoDifferentVectors() {
        // when
        var v1 = new Vector2d(3,5);
        var v2 = new Vector2d(9,1);
        // then
        assertEquals(new Vector2d(12,6), v1.add(v2));
        assertEquals(new Vector2d(-6,4), v1.subtract(v2));
    }
    @Test
    void oppositeForNormalVector() {
        // when
        var v1 = new Vector2d(3,0);
        // then
        assertEquals(new Vector2d(-3,0), v1.opposite());
    }


}