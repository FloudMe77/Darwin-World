package agh.ics.oop.model.util.newUtils;

import java.util.*;

public class FullRandomGenomChange implements GenomChange {

    @Override
    public void changeGenom(ArrayList<Integer> genomList, int minMutationAmount, int maxMutationAmount) {
        Random random = new Random();
        int mutationAmount = random.nextInt(minMutationAmount, maxMutationAmount + 1);

        int genomLen = genomList.size();
        for (int index : getIndexes(genomLen, mutationAmount)) {
            int newVal = random.nextInt(8);
            genomList.set(index, newVal);
        }
    }
    private List<Integer> getIndexes(int length, int mutationAmount){
        ArrayList<Integer> indexTab = new ArrayList<>();
        for(int i=0;i<length;i++){
            indexTab.add(i);
        }
        Collections.shuffle(indexTab);
        return indexTab.subList(0,mutationAmount);
    }
}
