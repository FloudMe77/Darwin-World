package agh.ics.oop.model.util;

import agh.ics.oop.model.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    private static int counterOfReceivedNotification = 1;

    @Override
    public void mapChanged(WorldMap worldMap, String message, int id) {
        synchronized (System.out){
        System.out.println((counterOfReceivedNotification++)+" powiadomienie:");
        System.out.println(message + " na mapie nr. " + id);
        System.out.println(worldMap);
        }
    }
}
