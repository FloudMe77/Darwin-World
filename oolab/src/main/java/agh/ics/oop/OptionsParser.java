//package agh.ics.oop;
//
//import agh.ics.oop.model.MoveDirection;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
//
//public class OptionsParser {
//
//    public static List<MoveDirection> parse(String[] allCharacters) {
//        List<MoveDirection> commands = new LinkedList<>();
//
//            for (String com : allCharacters) {
//                try {
//                    switch (com) {
//                        case "f" -> commands.add(MoveDirection.FORWARD);
//                        case "b" -> commands.add(MoveDirection.BACKWARD);
//                        case "r" -> commands.add(MoveDirection.RIGHT);
//                        case "l" -> commands.add(MoveDirection.LEFT);
//                        default -> throw new IllegalArgumentException(com + " is not legal move specification");
//                    }
//                }
//                catch (IllegalArgumentException e){
//                    System.out.println(e);
//                    e.printStackTrace();
//                    System.exit(1);
//                }
//            }
//
//        return commands;
//    }
//    public static List<MoveDirection> parse(List<String> allCharacters) {
//        List<MoveDirection> commands = new LinkedList<>();
//
//        for (String com : allCharacters) {
//            try {
//                switch (com) {
//                    case "f" -> commands.add(MoveDirection.FORWARD);
//                    case "b" -> commands.add(MoveDirection.BACKWARD);
//                    case "r" -> commands.add(MoveDirection.RIGHT);
//                    case "l" -> commands.add(MoveDirection.LEFT);
//                    default -> throw new IllegalArgumentException(com + " is not legal move specification");
//                }
//            }
//            catch (IllegalArgumentException e){
//                System.out.println(e);
//                e.printStackTrace();
//                System.exit(1);
//            }
//        }
//
//        return commands;
//    }
//}
