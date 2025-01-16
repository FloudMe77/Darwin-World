//package agh.ics.oop;
//
//import agh.ics.oop.model.MoveDirection;
//import org.junit.jupiter.api.Test;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class OptionsParserTest {
//    @Test
//    void parseForOnlyCorrectCharInTable() {
//        // when
//        String[] testTab = {"f","b","l","r"};
//        //then
//        LinkedList<MoveDirection> expectedTab = new LinkedList<>(List.of(
//                MoveDirection.FORWARD,
//                MoveDirection.BACKWARD,
//                MoveDirection.LEFT,
//                MoveDirection.RIGHT
//        ));
//
//    }
//
//    @Test
//    void parseForOnlyUnCorrectCharInTable() {
//        // when
//        String[] testTab = {"x","fb","ff","w","sdx"};
//        LinkedList<MoveDirection> expectedTab = new LinkedList<>();
//        //then
//        assertThrows(RuntimeException.class, () -> {
//            // Kod, który powinien rzucić wyjątek
//        });
//    }
//    @Test
//    void parseForEmptySet() {
//        // when
//        String[] testTab = {};
//        LinkedList<MoveDirection> expectedTab = new LinkedList<>();
//        //then
//        assertEquals(expectedTab, OptionsParser.parse(testTab));
//    }
//    @Test
//    void parseForMixCharInTable() {
//        // when
//        String[] testTab = {"r","fr","l","xl","b","s","rR","f","as"};
//
//        //then
//        assertThrows(RuntimeException.class, () -> {
//            // Kod, który powinien rzucić wyjątek
//            OptionsParser.parse(testTab);
//        });
//    }
//
//}