package agh.ics.oop.model.util.newUtils;

import agh.ics.oop.model.maps.WorldMap;

// w odroznieniu do zwykłego Configu tutaj nie inicjalizujemy zadnych zaawansowanych obiektów
// aby potem móc te wartości wkleić bezposrednio jako values do front-endu
public record CsvConfigValues(
        int height,
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
        String genomeChange,
        int genomeLength,
        String worldMap
) {
    public String toCsvString() {
        return String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s,%d,%s",
                height, width, startGrassAmount, energyFromGrass, everyDayGrassAmount, startAnimalAmount, startEnergy,
                energyRequireToReproduce, energyToReproduce, dailyDeclineValue, minimalMutationAmount,
                maximalMutationAmount, genomeChange, genomeLength, worldMap);
    }
}
