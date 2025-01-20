package agh.ics.oop.model.genomes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ReplacmentGenomeChange implements GenomeChange {
    @Override
    public void changeGenome(ArrayList<GenomeDirection> GenomeList, int minMutationAmount, int maxMutationAmount) {
        Random random = new Random();
        int mutationAmount = random.nextInt(minMutationAmount, maxMutationAmount + 1);
        int genomeLen = GenomeList.size();
        var allPossiblePairs = findAllPairs(genomeLen,mutationAmount);
        Collections.shuffle(allPossiblePairs);

        for(var pair : allPossiblePairs) {
            int index1 = pair.getFirst();
            int index2 = pair.getLast();
            GenomeDirection buffer = GenomeList.get(index1);
            GenomeList.set(index1, GenomeList.get(index2));
            GenomeList.set(index2, buffer);
        }
    }

    private List<List<Integer>> findAllPairs(int length, int mutationAmount) {
        List<List<Integer>> allPossiblePairs = new ArrayList<>();
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                allPossiblePairs.add(List.of(x, y));
            }
        }
        return allPossiblePairs.subList(0,mutationAmount);
    }
}
