package agh.ics.oop.view;

import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AbstractAnimalElementBox extends VBox {
    public AbstractAnimalElementBox(AbstractAnimal animal, int cellWidth, int cellHeight) {
        this.setPrefSize(cellWidth, cellHeight);
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setStyle("-fx-background-color:  rgba(144, 238, 144, 0.2);");


        Circle circle = new Circle();
        circle.setRadius(Math.min(cellWidth/2, cellHeight/2));
        circle.setFill(animal.getColor());
        this.getChildren().addAll(circle);
        // tymczasowo
//        if(animal instanceof Animal thisAnimal) {
//            Label energyLabel = new Label(String.format("%d/%d", thisAnimal.getEnergy(), initialAnimalEnergy));
//            energyLabel.setStyle("-fx-font-size: 10px;");
//            energyLabel.setAlignment(javafx.geometry.Pos.CENTER);
//            this.getChildren().addAll(energyLabel);
//        }

    }
}
