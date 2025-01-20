package agh.ics.oop.model.MapObjects;

import agh.ics.oop.model.genomes.Genome;
import agh.ics.oop.model.genomes.GenomeChange;
import agh.ics.oop.model.genomes.GenomeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractAnimal {
    private int energy;
    private int dayOfDeath;
    private int age = 0;
    private int eaten = 0;
    private final List<Animal> kids = new ArrayList<>();

    public Animal(Vector2d position, Genome genome, int startEnergy) {
        super(position, genome);
        this.energy = startEnergy;
    }

    public Animal reproduce(Animal other, GenomeChange genomeChange, int minMutationAmount, int maxMutationAmount, int energyToReproduce) {
        // tworze liste genów
        var newGenList = shuffleGenomes(other);
        // dokonuje mutacji
        genomeChange.changeGenome(newGenList, minMutationAmount, maxMutationAmount);
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

    private ArrayList<GenomeDirection> shuffleGenomes(Animal other) {
        Random random = new Random();
        int otherEnergy = other.getEnergy();
        int genomeLen = getGenome().getGenLength();

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
        int divideIndex = (int) (genomeLen * ((double) betterAnimal.getEnergy() / (betterAnimal.getEnergy() + worseAnimal.getEnergy())));

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
        return kids.size();
    }

    private void addChild(Animal child) {
        kids.add(child);
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public int getEnergy() {
        return energy;
    }

    public void die() {
        dayOfDeath = age;
    }

    public List<Animal> getKids() {
        return new ArrayList<>(kids);
    }

    public void eat(int feedVal) {
        eaten+=1;
        energy += feedVal;
    }

    public void reduceEnergy(int val){
        energy -= val;
    }

    public int getDescendantsAmount(){
        return getDescendants().size();
    }

    private List<Animal> getDescendants(){
        List<Animal> descendants = new ArrayList<>();
        for(var child : kids){
            descendants.add(child);
            descendants.addAll(child.getDescendants());
        }
        return descendants.stream()
                .distinct()
                .toList();
    }

    public int getAge() {
        return age;
    }

    public int getEaten() {
        return eaten;
    }

    @Override
    public void move(MoveValidator validator){
        super.move(validator);
        age+=1;
    }
}
