package agh.ics.oop.model.util.newUtils;

import agh.ics.oop.model.GenomeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    private final List<GenomeDirection> genList;
    private final int genLength;

    public Genome(int genLength) {
        this.genLength = genLength;
        genList = new ArrayList<>();

        for (int i = 0; i < genLength; i++) {
            genList.add(GenomeDirection.getRandomGenome());
        }
    }

    public Genome(ArrayList<GenomeDirection> genList) {
        this.genList = genList;
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
    public int hashCode() {
        return genList.hashCode();
    }
}