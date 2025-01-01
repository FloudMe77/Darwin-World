package agh.ics.oop.model.util.newUtils;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ReplacmentGenomChange implements GenomChange {
    @Override
    public void changeGenom(ArrayList<Integer> genomList, int minMutationAmount, int maxMutationAmount) {
        Random random = new Random();
        int mutationAmount = random.nextInt(minMutationAmount, maxMutationAmount + 1);
        int genomLen = genomList.size();
        var allPossiblePairs = findAllPairs(genomLen,mutationAmount);
        Collections.shuffle(allPossiblePairs);

        for(var pair : allPossiblePairs) {
            int index1 = pair.getFirst();
            int index2 = pair.getLast();
            int buffer = genomList.get(index1);
            genomList.set(index1, genomList.get(index2));
            genomList.set(index2, buffer);
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
