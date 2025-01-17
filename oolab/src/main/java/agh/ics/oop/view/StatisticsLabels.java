package agh.ics.oop.view;

import javafx.scene.control.Label;

public record StatisticsLabels(Label animalCount, Label grassCount, Label freeSpace,
                               Label dominantGenome, Label avgEnergy, Label avgLifespan,
                               Label avgChildrenCount, Label animalGenome, Label direction,
                               Label energy, Label grassEaten, Label children,
                               Label descendants, Label daysLived, Label dayOfDeath) {
}
