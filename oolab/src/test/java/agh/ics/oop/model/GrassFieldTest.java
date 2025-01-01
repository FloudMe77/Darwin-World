package agh.ics.oop.model;

import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.util.RandomPositionGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GrassFieldTest {
    // będę używać mocka
    private GrassField prepareMap(List<Vector2d> mockPositionsGrass){
        RandomPositionGenerator mockGenerator = mock(RandomPositionGenerator.class);

        Iterator<Vector2d> mockIterator = mockPositionsGrass.iterator();
        when(mockGenerator.iterator()).thenReturn(mockIterator);
        // when
        return new GrassField(mockGenerator);

    }

    @Test
    void isOccupiedOnlyByGrass() {
        // given
        GrassField grassField = prepareMap(Arrays.asList(
                new Vector2d(0, 0),
                new Vector2d(1, 1),
                new Vector2d(2, 2),
                new Vector2d(3, 5),
                new Vector2d(1, 3)
        ));
        //then
        var tmp = grassField.getElements();
        for(var cos:tmp){
            System.out.println(cos.getPosition());
        }
        assertTrue(grassField.isOccupied(new Vector2d(0,0)));
        assertFalse(grassField.isOccupied(new Vector2d(5,5)));
    }
    @Test
    void objectAtOnlyByGrass() {
        // given
        GrassField grassField = prepareMap(Arrays.asList(
                new Vector2d(0, 0),
                new Vector2d(1, 1),
                new Vector2d(2, 2),
                new Vector2d(3, 5),
                new Vector2d(1, 3)
        ));
        //then
        assertTrue( grassField.objectAt(new Vector2d(0,0)).isAt(new Vector2d(0,0)));
        assertNull(grassField.objectAt(new Vector2d(5,5)));
    }
    @Test
    void getElementsOnlyGrass(){
        // given
        List<Vector2d> vectorList = Arrays.asList(
                new Vector2d(0, 0),
                new Vector2d(1, 1),
                new Vector2d(2, 2),
                new Vector2d(3, 5),
                new Vector2d(1, 3)
        );
        GrassField grassField = prepareMap(vectorList);
        //when
        List<WorldElement> listFromGrassField = grassField.getElements();

        //then
        for(int i=0;i < vectorList.size();i++){
            var elementFromGrassField = listFromGrassField.get(i);
            assertTrue(elementFromGrassField instanceof Grass);
            assertTrue( elementFromGrassField.isAt(vectorList.get(i)));
        }
    }
}