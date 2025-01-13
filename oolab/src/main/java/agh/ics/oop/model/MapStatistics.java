package agh.ics.oop.model;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.maps.BasicRectangularMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.newUtils.Genome;

public class MapStatistics {
    private final WorldMap worldMap;

    private int totalAnimalAmount = 0;
    private int totalGrasAmount = 0;
    private int totalEnergy =0 ;
    private int totalLifeTime = 0;
    private int totalChildrenAmount = 0;
    private int totalDeathAmount = 0;
    private int totalFreeSpace;


    public MapStatistics(BasicRectangularMap worldMap){
        this.worldMap = worldMap;
        int minX = worldMap.getCurrentBounds().leftDownCornerMap().getX();
        int minY = worldMap.getCurrentBounds().leftDownCornerMap().getY();
        int maxX = worldMap.getCurrentBounds().rightUpperCornerMap().getX();
        int maxY = worldMap.getCurrentBounds().rightUpperCornerMap().getY();
        totalFreeSpace = ((maxX - minX) * (maxY - minY));
    }

    public Genome getDominantGenomeType() {
        return worldMap.getDominantGenome();
    }

    public double getAverageChildrenAmount() {
        return (double) totalChildrenAmount / totalAnimalAmount;
    }

    public int getTotalFreeSpace() {
        return totalFreeSpace - totalGrasAmount - totalAnimalAmount;
    }

    public double getAverageEnergy() {
        return (double) totalEnergy / totalAnimalAmount;
    }

    public double getAverageLifeTime() {
        return (double) totalLifeTime / totalDeathAmount;
    }

    public int getTotalAnimalAmount() {
        return totalAnimalAmount;
    }

    public int getTotalGrasAmount() {
        return totalGrasAmount;
    }

    public void updateStatistic(MapStatisticAction action, int val){
        switch (action){
            case CHILDREN_AMOUNT -> totalChildrenAmount += val;
            case ENERGY -> totalEnergy += val;
            case LIFETIME -> totalLifeTime += val;
            case ANIMAL_AMOUNT -> totalAnimalAmount += val;
            case GRASS_AMOUNT -> totalGrasAmount += val;
            case DEATHS_AMOUNT -> totalDeathAmount += val;
        }
    }

    public void newAnimalUpdate(Animal animal){
        updateStatistic(MapStatisticAction.ANIMAL_AMOUNT,1);
        updateStatistic(MapStatisticAction.ENERGY,animal.getEnergy());
    }

    public void newBornUpdate(Animal animal){
        newAnimalUpdate(animal);
        updateStatistic(MapStatisticAction.CHILDREN_AMOUNT, 1);
    }

    public void deathAnimalUpdate(Animal animal){
        updateStatistic(MapStatisticAction.ANIMAL_AMOUNT,-1);
        updateStatistic(MapStatisticAction.LIFETIME, animal.getAge());
        updateStatistic(MapStatisticAction.ENERGY, -animal.getEnergy());
        updateStatistic(MapStatisticAction.CHILDREN_AMOUNT, -animal.getChildrenAmount());
        updateStatistic(MapStatisticAction.DEATHS_AMOUNT, 1);
    }

    public void feedAnimalUpdate(int feedVal){
        updateStatistic(MapStatisticAction.ENERGY,feedVal);
        updateStatistic(MapStatisticAction.GRASS_AMOUNT,-1);
    }

    // only for debug
    public void printStatistic(){
        System.out.println();
        System.out.println(getTotalAnimalAmount());
        System.out.println(getTotalGrasAmount());
        System.out.println(getDominantGenomeType());
        System.out.println(getAverageEnergy());
        System.out.println(getAverageChildrenAmount());
        System.out.println(getAverageLifeTime());
    }
}