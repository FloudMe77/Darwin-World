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
    private final int totalFreeSpace;


    public MapStatistics(BasicRectangularMap worldMap){
        this.worldMap = worldMap;
        int minX = worldMap.getCurrentBounds().leftDownCornerMap().getX();
        int minY = worldMap.getCurrentBounds().leftDownCornerMap().getY();
        int maxX = worldMap.getCurrentBounds().rightUpperCornerMap().getX();
        int maxY = worldMap.getCurrentBounds().rightUpperCornerMap().getY();
        totalFreeSpace = ((maxX - minX + 1) * (maxY - minY + 1));
    }

    public String getDominantGenomeType() {
        return worldMap.getDominantGenome().toString();
    }

    public double getAverageChildrenAmount() {
        return (double) totalChildrenAmount / totalAnimalAmount;
    }

    public int getTotalFreeSpace() {
        return totalFreeSpace - totalGrasAmount;
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

    public void grassUpdate(int val){
        totalGrasAmount += val;
    }

    public void energyUpdate(int val){
        totalEnergy += val;
    }


    public void newAnimalUpdate(Animal animal){
        totalAnimalAmount += 1;
        totalEnergy += animal.getEnergy();
    }

    public void newBornUpdate(){
        totalAnimalAmount += 1;
        totalChildrenAmount += 2;
    }

    public void deathAnimalUpdate(Animal animal){
        totalAnimalAmount -= 1;
        totalLifeTime += animal.getAge();
        totalEnergy-=animal.getEnergy();
        totalChildrenAmount-=animal.getChildrenAmount();
        totalDeathAmount+=1;
    }

    public void feedAnimalUpdate(int feedVal){
        totalEnergy+=feedVal;
        totalGrasAmount-=1;
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