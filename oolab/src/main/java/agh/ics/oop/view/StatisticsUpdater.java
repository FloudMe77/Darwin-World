package agh.ics.oop.view;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.maps.MapStatistics;
import agh.ics.oop.view.util.StatisticsLabels;

public class StatisticsUpdater {
    private final MapStatistics mapStatistics;
    private final StatisticsLabels statisticsLabels;

    public StatisticsUpdater(MapStatistics mapStatistics, StatisticsLabels statisticsLabels) {
        this.mapStatistics = mapStatistics;
        this.statisticsLabels = statisticsLabels;
    }

    public void updateMapStatistics() {
        statisticsLabels.animalCount().textProperty().set(String.format("Liczba zwierząt: %d", mapStatistics.getTotalAnimalAmount()));
        statisticsLabels.grassCount().textProperty().set(String.format("Liczba traw: %d", mapStatistics.getTotalGrasAmount()));
        statisticsLabels.freeSpace().textProperty().set(String.format("Liczba wolny miejsc: %d", mapStatistics.getTotalFreeSpace()));
        statisticsLabels.dominantGenome().textProperty().set(String.format("Najpopularniejszy genom: \n %s", mapStatistics.getDominantGenome()));
        statisticsLabels.avgEnergy().textProperty().set(String.format("Średni poziom energii: %.2f", mapStatistics.getAverageEnergy()));
        statisticsLabels.avgLifespan().textProperty().set(String.format("Średnia długość życia: %.2f", mapStatistics.getAverageLifeTime()));
        statisticsLabels.avgChildrenCount().textProperty().set(String.format("Średnia liczba dzieci: %.2f", mapStatistics.getAverageChildrenAmount()));

    }

    public void updateAnimalStatistics(Animal animal) {
        statisticsLabels.animalGenome().textProperty().set(String.format("Genom zwierzaka:\n %s", animal.getGenome()));
        statisticsLabels.direction().textProperty().set(String.format("Aktywny genom: %s", animal.getCurrentGenome()));
        statisticsLabels.energy().textProperty().set(String.format("Energii: %d", animal.getEnergy()));
        statisticsLabels.grassEaten().textProperty().set(String.format("Zjedzona trawa: %d", animal.getEaten()));
        statisticsLabels.children().textProperty().set(String.format("Liczba dzieci: %d", animal.getChildrenAmount()));
        // tmp
        statisticsLabels.descendants().textProperty().set(String.format("Liczba potomstwa: %d", animal.getDescendantsAmount()));

        statisticsLabels.daysLived().textProperty().set(String.format("Długość życia: %d", animal.getAge()));
        statisticsLabels.dayOfDeath().textProperty().set(String.format("Dzień śmierci: %d", animal.getDayOfDeath()));

    }
}
