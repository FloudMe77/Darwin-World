package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.newUtils.Genome;
import agh.ics.oop.model.util.newUtils.GenomeChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractAnimal {
    private final int START_ENERGY;
    private int energy;
    private int childrenAmount = 0;
    private int dayOfDeath;
    private boolean isAlive = true;
    private final List<Animal> kids = new ArrayList<>();

    public Animal(Vector2d position, Genome genome, int startEnergy) {
        super(position, genome);
        this.START_ENERGY = startEnergy;
        this.energy = startEnergy;
    }

    public Animal reproduce(Animal other, GenomeChange genomeChange, int minMutationAmount, int maxMutationAmount, int energyToReproduce) {
        // tworze liste genów
        var newGenList = shuffleGenomes(other);
        // dokonuje mutacji
        genomeChange.changeGenome(newGenList, minMutationAmount, maxMutationAmount);
        // zwiększam liczniki dzieci zwierząt
        increaseChildrenAmount();
        other.increaseChildrenAmount();
        // tworze nowego zwierzaka
        var child = new Animal(currentPosition, new Genome(newGenList), 2 * energyToReproduce);
        // dodaje zwierzaka do list
        addChild(child);
        other.addChild(child);
        // obniżam energie rodziców
        reduceEnergy(energyToReproduce);
        other.reduceEnergy(energyToReproduce);
        return child;
    }

    public ArrayList<GenomeDirection> shuffleGenomes(Animal other) {
        Random random = new Random();
        int otherEnergy = other.getEnergy();
        int genomeLen = getGenome().getGenLength();

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
        var newGenList = new ArrayList<GenomeDirection>();

        // Obliczenie indeksu podziału
        int divideIndex = (int) Math.ceil(
                genomeLen * (betterAnimal.getEnergy() / (double) (betterAnimal.getEnergy() + worseAnimal.getEnergy()))
        );
//        System.out.println(divideIndex);
        // Dodawanie genów w zależności od dominującej strony
        if (dominantSide == 0) {
            // Dominująca strona po lewej
            newGenList.addAll(betterAnimal.getGenome().getGenList().subList(0, divideIndex));
            newGenList.addAll(worseAnimal.getGenome().getGenList().subList(divideIndex, worseAnimal.getGenome().getGenLength()));
        } else {
            // Dominująca strona po prawej
            newGenList.addAll(worseAnimal.getGenome().getGenList().subList(0, genomeLen-divideIndex));
            newGenList.addAll(betterAnimal.getGenome().getGenList().subList(genomeLen-divideIndex, betterAnimal.getGenome().getGenLength()));
        }

        return newGenList;
    }

    public int getChildrenAmount() {
        return childrenAmount;
    }

    public void addChild(Animal child) {
        kids.add(child);
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public void increaseChildrenAmount() {
        childrenAmount++;
    }

    public int getEnergy() {
        return energy;
    }

    public void getOlder() {
        age += 1;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void die() {
        isAlive = false;
        dayOfDeath = age;
    }
    public List<Animal> getKids(){
        return kids;
    }

    public void eat(int feedVal) {
        energy+=feedVal;
    }

    public void reduceEnergy(int val){
        energy-=val;
    }
}
