package agh.ics.oop.model.genomes;

import java.util.ArrayList;

public interface GenomeChange {
    void changeGenome(ArrayList<GenomeDirection> GenomeList, int minMutationAmount, int maxMutationAmount);
}
