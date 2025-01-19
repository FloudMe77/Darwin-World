package agh.ics.oop.model.util;

import agh.ics.oop.model.GenomeDirection;

import java.util.ArrayList;

public interface GenomeChange {
    void changeGenome(ArrayList<GenomeDirection> GenomeList, int minMutationAmount, int maxMutationAmount);
}
