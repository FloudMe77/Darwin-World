package agh.ics.oop.model.util;

import agh.ics.oop.model.genomes.GenomeType;
import agh.ics.oop.model.maps.MapType;

public record Config(
        int height,
        int width,
        int startGrassAmount,
        int energyFromGrass,
        int everyDayGrassAmount,
        int startAnimalAmount,
        int startEnergy,
        int energyRequiredToReproduce,
        int offspringEnergyCost,
        int dailyDeclineValue,
        int minMutationCount,
        int maxMutationCount,
        GenomeType genomeType,
        int genomeLength,
        MapType mapType,
        Boolean saveStatsToCsv
) {
    public String toCsvString() {
        return String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s,%d,%s,%b",
                height, width, startGrassAmount, energyFromGrass, everyDayGrassAmount, startAnimalAmount, startEnergy,
                energyRequiredToReproduce, offspringEnergyCost, dailyDeclineValue, minMutationCount,
                maxMutationCount, genomeType.getDisplayName(), genomeLength, mapType.getDisplayName(), saveStatsToCsv);
    }
}