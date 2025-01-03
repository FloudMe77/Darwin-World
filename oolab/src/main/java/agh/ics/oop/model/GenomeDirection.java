package agh.ics.oop.model;

import java.util.Random;

public enum GenomeDirection {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN;

    private static final Random RANDOM = new Random();

    public static GenomeDirection getRandomGenome() {
        GenomeDirection[] directions = values();
        int randomIndex = RANDOM.nextInt(directions.length);
        return directions[randomIndex];
    }
}
