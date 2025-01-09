package agh.ics.oop.model;

import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.maps.BasicRectangularMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.newUtils.Genome;

public class MapStatistic {
    private final BasicRectangularMap worldMap;

//    private float averageEnergy =0 ;
//    private float averageLifeTime = 0;
//    private float averageChildrenAmount = 0;
    private int totalAnimalAmount = 0;
    private int totalGrasAmount = 0;
    private int totalEnergy =0 ;
    private int totalLifeTime = 0;
    private int totalChildrenAmount = 0;
    private int totalDeathAmount = 0;


    public MapStatistic(BasicRectangularMap worldMap){
        this.worldMap = worldMap;
    }

    public Genome getDominantGenomeType() {
        return worldMap.getDominantGenome();
    }

    public float getAverageChildrenAmount() {
        return (float) totalChildrenAmount / totalAnimalAmount;
    }

    public float getAverageEnergy() {
        return (float) totalEnergy / totalAnimalAmount;
    }

    public float getAverageLifeTime() {
        return (float) totalLifeTime / totalDeathAmount;
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
