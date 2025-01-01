package agh.ics.oop.model;

import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap{
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public GrassField(RandomPositionGenerator randomPositionGenerator ){
        super();
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    // rozdzieliłem konstruktory, żeby mock działał
    public GrassField(int numberOfGrassOnMap){
        // dodaje 1, żeby brane pod uwagę były też punkty na krańcu przedziału
        this(new RandomPositionGenerator((int) sqrt(10 * numberOfGrassOnMap)+1, (int) sqrt(10 * numberOfGrassOnMap)+1, numberOfGrassOnMap));
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grasses.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement animal = super.objectAt(position);
        if (animal != null) {
            return animal;
        }
        return grasses.get(position);
    }

    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> listWithAllWorldElements = super.getElements();

        // kopiuje wartości, żeby nie mieć problemu z błędnym stanem obiektu
        listWithAllWorldElements.addAll(grasses.values());
        return listWithAllWorldElements;
    }

    @Override
    public Boundary getCurrentBounds() {
        var listWithAllWorldElements = getElements();
        var dynamicRightUpperCornerMap = listWithAllWorldElements.getFirst().getPosition();
        var dynamicLeftDownCornerMap = listWithAllWorldElements.getFirst().getPosition();

        for(WorldElement element:listWithAllWorldElements){
            var positionOfElement = element.getPosition();
            dynamicRightUpperCornerMap = dynamicRightUpperCornerMap.upperRight(positionOfElement);
            dynamicLeftDownCornerMap = dynamicLeftDownCornerMap.lowerLeft(positionOfElement);
        }
        return new Boundary(dynamicLeftDownCornerMap, dynamicRightUpperCornerMap);
    }
}
