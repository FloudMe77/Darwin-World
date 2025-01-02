package agh.ics.oop.model.util.newUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    private final List<Integer> genList;
    private final int genLength;

    public Genome(int genLength) {
        Random random = new Random();
        this.genLength = genLength;
        genList = new ArrayList<>();

        for (int i = 0; i < genLength; i++) {
            genList.add(random.nextInt(8));
        }
    }

    public Genome(ArrayList<Integer> genList) {
        this.genList = genList;
        this.genLength = genList.size();
    }

    public int getGenLength() {
        return genLength;
    }

    public int getGenVal(int index) {
        return genList.get(index);
    }

    public List<Integer> getGenList() {
        return List.copyOf(genList);
    }
}