package agh.ics.oop.model.util;

import agh.ics.oop.model.GenomeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Genome {
    private final List<GenomeDirection> genList;
    private final int genLength;

    public Genome(int genLength) {
        this.genLength = genLength;
        List<GenomeDirection> genListTmp = new ArrayList<>();

        for (int i = 0; i < genLength; i++) {
            genListTmp.add(GenomeDirection.getRandomGenome());
        }
        // niemodyfikowalna lista
        genList = List.copyOf(genListTmp);
    }

    public Genome(ArrayList<GenomeDirection> genList) {
        this.genList = List.copyOf(genList);
        this.genLength = genList.size();
    }

    public int getGenLength() {
        return genLength;
    }

    public GenomeDirection getGenVal(int index) {
        return genList.get(index);
    }

    public List<GenomeDirection> getGenList() {
        return List.copyOf(genList);
    }

    @Override
    public String toString() {
        return genList.toString();
    }

    @Override
    public int hashCode() {
        return genList.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome = (Genome) o;
        return genLength == genome.genLength && Objects.equals(genList, genome.genList);
    }
}