package agh.ics.oop.model;

import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.newUtils.GenomeChange;

public record Config(
        int high,
        int width,
        int startGrassAmount,
        int energyFromGrass,
        int everyDayGrassAmount,
        int startAnimalAmount,
        int startEnergy,
        int energyRequireToReproduce,
        int energyToReproduce,
        int dailyDeclineValue,
        int minimalMutationAmount,
        int maximalMutationAmount,
        GenomeChange genomeChange,
        int genomeLength,
        WorldMap worldMap
) {}