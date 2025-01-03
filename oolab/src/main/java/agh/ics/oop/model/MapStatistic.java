package agh.ics.oop.model;

import agh.ics.oop.model.maps.BasicRectangularMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.newUtils.Genome;

public class MapStatistic {
    private final BasicRectangularMap worldMap;

    private int totalAnimalAmount = 0;
    private int totalGrasAmount = 0;
    private Genome dominantGenomeType;
    private float averageEnergy =0 ;
    private float averageLifeTime = 0;
    private float averageChildrenAmount = 0;

    MapStatistic(BasicRectangularMap worldMap){
        this.worldMap = worldMap;
    }

    public Genome getDominantGenomeType() {
        return dominantGenomeType;
    }

    public float getAverageChildrenAmount() {
        return averageChildrenAmount;
    }

    public float getAverageEnergy() {
        return averageEnergy;
    }

    public float getAverageLifeTime() {
        return averageLifeTime;
    }

    public int getTotalAnimalAmount() {
        return totalAnimalAmount;
    }

    public int getTotalGrasAmount() {
        return totalGrasAmount;
    }

    public void updateStatistic(){
        totalAnimalAmount=worldMap.getAnimalAmount();
        totalGrasAmount = worldMap.getGrassAmount();
        dominantGenomeType = worldMap.getDominantGenome();
        averageEnergy = worldMap.getAverageEnergy();
        averageChildrenAmount = worldMap.getAverageChildrenAmount();
        averageLifeTime = worldMap.getAverageLifeTime();
    }
}
