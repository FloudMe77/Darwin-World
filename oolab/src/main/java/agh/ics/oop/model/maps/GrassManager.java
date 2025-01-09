package agh.ics.oop.model.maps;

import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.MapStatisticAction;
import agh.ics.oop.model.Vector2d;

import java.security.cert.TrustAnchor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GrassManager {
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final GrassField grassField;

    public GrassManager(int width, int height) {
        this.grassField = new GrassField(height, width);
    }

    public boolean addGrass() {
        Optional<Vector2d> newPositionOpt = grassField.getNewGrassPosition();

        if(newPositionOpt.isPresent()){
            var newPosition = newPositionOpt.get();
            var grass = new Grass(newPosition);
            grasses.put(newPosition,grass);
            return true;
        }
        return false;
    }

    public Grass getGrass(Vector2d position){
        return grasses.get(position);
    }

    public boolean isGrassAt(Vector2d position) {
        return grasses.containsKey(position);
    }

    public void removeGrass(Vector2d position) {
        grasses.remove(position);
        grassField.addGrassPosition(position);
    }

}
