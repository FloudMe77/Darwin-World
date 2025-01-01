package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.newUtils.Genom;
import agh.ics.oop.model.util.newUtils.GenomChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractAnimal{
    private final int START_ENERGY;
    private int energy;
    private int childrenAmount = 0;
    private int dayOfDeath;
    private boolean isAlive = true;
    private final int actuallIndexOfGen = 0;
    private List<Animal> kids = new ArrayList<>();


    public Animal(Vector2d position, int startEnergy, Genom genom ) {
        super(position,genom);
        this.START_ENERGY = startEnergy;
        this.energy = startEnergy;
    }

    public Animal reproduce(Animal other, GenomChange genomChange, int minMutationAmount, int maxMutationAmount){
        var newGenList = shuffleGenoms(other);
        genomChange.changeGenom(newGenList, minMutationAmount, maxMutationAmount);
        increaseChildrenAmount();
        other.increaseChildrenAmount();
        var child = new Animal(currentPosition, START_ENERGY,new Genom(newGenList));
        addChild(child);
        other.addChild(child);
        return child;
    }

    public ArrayList<Integer> shuffleGenoms(Animal other) {
        Random random = new Random();
        int otherEnergy = other.getEnergy();

        // Deklaracja zmiennych przed if/else
        Animal betterAnimal;
        Animal worseAnimal;

        // Określenie lepszego i gorszego zwierzęcia
        if (otherEnergy > energy) {
            betterAnimal = other;
            worseAnimal = this;
        } else {
            betterAnimal = this;
            worseAnimal = other;
        }

        // Wyznaczenie dominującej strony
        int dominantSide = random.nextInt(2);
        var newGenList = new ArrayList<Integer>();

        // Obliczenie indeksu podziału
        int divideIndex = (int) Math.ceil(
                betterAnimal.getEnergy() / (double) (betterAnimal.getEnergy() + worseAnimal.getEnergy())
        );

        // Dodawanie genów w zależności od dominującej strony
        if (dominantSide == 0) {
            // Dominująca strona po lewej
            newGenList.addAll(betterAnimal.getGenom().getGenList().subList(0, divideIndex));
            newGenList.addAll(worseAnimal.getGenom().getGenList().subList(divideIndex, worseAnimal.getGenom().getGenLength()));
        } else {
            // Dominująca strona po prawej
            newGenList.addAll(worseAnimal.getGenom().getGenList().subList(0, divideIndex));
            newGenList.addAll(betterAnimal.getGenom().getGenList().subList(divideIndex, betterAnimal.getGenom().getGenLength()));
        }

        return newGenList;
    }

    public int getChildrenAmount() {
        return childrenAmount;
    }

    public void addChild(Animal child){
        kids.add(child);
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public void increaseChildrenAmount(){
        childrenAmount++;
    }

    public int getEnergy() {
        return energy;
    }

    public void getOlder(int dailyDeclineValue){
        age += 1;
        energy -= dailyDeclineValue;
        if(energy < 0){
            die();
        }
    }

    public boolean isAlive() {
        return isAlive;
    }
    public void die(){
        isAlive = false;
        dayOfDeath = age;
    }
}
