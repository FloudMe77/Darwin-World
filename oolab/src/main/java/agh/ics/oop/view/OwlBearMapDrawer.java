package agh.ics.oop.view;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.OwlBearMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.view.util.OwlBearElementBox;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class OwlBearMapDrawer {
    private final OwlBearMap owlBearMap;
    private final WorldMap owlBearBoundsMap;
    private final HashMap<Vector2d, Pane> owlBearMapCache = new HashMap<>();

    public OwlBearMapDrawer(OwlBearMap owlBearMap) {
        this.owlBearMap = owlBearMap;
        owlBearBoundsMap = owlBearMap.getOwlBearMap();
    }

    public void drawCell(Vector2d thisPosition, Pane cell, double cellWidth, double cellHeight) {
        Vector2d lowerLeft = owlBearBoundsMap.getCurrentBounds().leftDownCornerMap();
        Vector2d upperRight = owlBearBoundsMap.getCurrentBounds().rightUpperCornerMap();
        Vector2d owlBearPosition = owlBearMap.getOwlBearPosition();

        if (thisPosition.equals(owlBearPosition)) {
            OwlBearElementBox owlBearBox = new OwlBearElementBox(cellWidth, cellHeight);
            cell.getChildren().setAll(owlBearBox); // Wyświetlamy owlbeara ponad zwierzakiem
        }

        if (thisPosition.follows(lowerLeft) && thisPosition.precedes(upperRight)) {
            if (!owlBearMapCache.containsKey(thisPosition)) {
                Pane owlBearLayer = new Pane();
                owlBearLayer.setStyle("-fx-background-color: rgba(255,0,0,0.25);");
                owlBearLayer.setPrefSize(cellWidth, cellHeight);
                owlBearMapCache.put(thisPosition, owlBearLayer);
                cell.getChildren().add(owlBearLayer);
            } else {
                cell.getChildren().add(owlBearMapCache.get(thisPosition));
            }
        }
    }
}
