package agh.ics.oop.model;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.newUtils.Genome;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

// możliwe ze ta klasa moze byc wgl rekordem z tymi simpleintegerami bo ma tylko setter/getters
public class MapStatistics {
    private final WorldMap worldMap;
    private int totalEnergy = 0;
    private int totalLifeTime = 0;
    private int totalChildrenAmount = 0;
    private int totalDeathAmount = 0;

    private final IntegerProperty totalAnimalAmount = new SimpleIntegerProperty(0);
    private final IntegerProperty totalGrassAmount = new SimpleIntegerProperty(0);
    private final IntegerProperty totalFreeSpace = new SimpleIntegerProperty(0);
    private final DoubleProperty averageAnimalEnergy = new SimpleDoubleProperty(0);
    private final DoubleProperty averageLifespan = new SimpleDoubleProperty(0);
    private final DoubleProperty averageChildrenAmount = new SimpleDoubleProperty(0);

    public MapStatistics(WorldMap worldMap) {
        this.worldMap = worldMap;
        int minX = worldMap.getCurrentBounds().leftDownCornerMap().getX();
        int minY = worldMap.getCurrentBounds().leftDownCornerMap().getY();
        int maxX = worldMap.getCurrentBounds().rightUpperCornerMap().getX();
        int maxY = worldMap.getCurrentBounds().rightUpperCornerMap().getY();
        setTotalFreeSpace((maxX - minX) * (maxY - minY));
    }

    public Genome getDominantGenomeType() {
        return worldMap.getDominantGenome();
    }

    public void updateStatistic(MapStatisticAction action, int val) {
        switch (action) {
            case CHILDREN_AMOUNT -> updateTotalChildrenAmount(val);
            case ENERGY -> updateTotalEnergy(val);
            case LIFETIME -> totalLifeTime += val;
            case ANIMAL_AMOUNT -> updateTotalAnimalAmount(val);
            case GRASS_AMOUNT -> updateTotalGrassAmount(val);
            case DEATHS_AMOUNT -> updateTotalDeathAmount(val);
        }
    }

    private void updateTotalGrassAmount(int val) {
        Platform.runLater(() -> {
            setTotalGrassAmount(totalGrassAmount.get() + val);
            setTotalFreeSpace(totalFreeSpace.get() - val);
        });
    }

    private void updateTotalDeathAmount(int val) {
        totalDeathAmount += val;
        Platform.runLater(() -> {
            setAverageLifespan((double) totalLifeTime / totalDeathAmount);
        });
    }

    private void updateTotalAnimalAmount(int val) {
        Platform.runLater(() -> {
            setTotalAnimalAmount(totalAnimalAmount.get() + val);
            setTotalFreeSpace(totalFreeSpace.get() - val);
        });
    }

    private void updateTotalEnergy(int newEnergy) {
        totalEnergy += newEnergy;
        Platform.runLater(() -> {
            setAverageAnimalEnergy((double) totalEnergy / totalAnimalAmount.get());
        });
    }

    private void updateTotalChildrenAmount(int val) {
        totalChildrenAmount += val;
        Platform.runLater(() -> {
            setAverageChildrenAmount((double) totalChildrenAmount / totalAnimalAmount.get());
            setTotalFreeSpace(totalFreeSpace.get() - val);
        });
    }


    public void newAnimalUpdate(Animal animal) {
        updateStatistic(MapStatisticAction.ANIMAL_AMOUNT, 1);
        updateStatistic(MapStatisticAction.ENERGY, animal.getEnergy());
    }

    public void newBornUpdate(Animal animal) {
        newAnimalUpdate(animal);
        updateStatistic(MapStatisticAction.CHILDREN_AMOUNT, 1);
    }

    public void deathAnimalUpdate(Animal animal) {
        updateStatistic(MapStatisticAction.ANIMAL_AMOUNT, -1);
        updateStatistic(MapStatisticAction.LIFETIME, animal.getAge());
        updateStatistic(MapStatisticAction.ENERGY, -animal.getEnergy());
        updateStatistic(MapStatisticAction.CHILDREN_AMOUNT, -animal.getChildrenAmount());
        updateStatistic(MapStatisticAction.DEATHS_AMOUNT, 1);
    }

    public void feedAnimalUpdate(int feedVal) {
        updateStatistic(MapStatisticAction.ENERGY, feedVal);
        updateStatistic(MapStatisticAction.GRASS_AMOUNT, -1);
    }

    // only for debug
    public void printStatistic() {
        System.out.println();
        System.out.println(totalAnimalAmount.get());
        System.out.println(totalGrassAmount.get());
        System.out.println(getDominantGenomeType());
        System.out.println(averageAnimalEnergy.get());
        System.out.println(averageChildrenAmount.get());
        System.out.println(averageLifespan.get());
    }

    public IntegerProperty totalAnimalAmountProperty() {
        return totalAnimalAmount;
    }

    public void setTotalAnimalAmount(int totalAnimalAmount) {
        this.totalAnimalAmount.set(totalAnimalAmount);
    }

    public DoubleProperty averageAnimalEnergyProperty() {
        return averageAnimalEnergy;
    }

    public void setAverageAnimalEnergy(double averageAnimalEnergy) {
        this.averageAnimalEnergy.set(averageAnimalEnergy);
    }

    public void setAverageChildrenAmount(double averageChildrenAmount) {
        this.averageChildrenAmount.set(averageChildrenAmount);
    }

    public DoubleProperty averageChildrenAmountProperty() {
        return averageChildrenAmount;
    }

    public IntegerProperty TotalGrassAmount() {
        return totalGrassAmount;
    }

    public void setTotalGrassAmount(int totalGrassAmount) {
        this.totalGrassAmount.set(totalGrassAmount);
    }

    public IntegerProperty totalFreeSpaceProperty() {
        return totalFreeSpace;
    }

    public IntegerProperty totalGrassAmountProperty() {
        return totalGrassAmount;
    }

    public void setTotalFreeSpace(int totalFreeSpace) {
        this.totalFreeSpace.set(totalFreeSpace);
    }

    public DoubleProperty averageLifespanProperty() {
        return averageLifespan;
    }

    public void setAverageLifespan(double averageLifespan) {
        this.averageLifespan.set(averageLifespan);
    }
}
