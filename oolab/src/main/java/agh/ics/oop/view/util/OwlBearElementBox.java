package agh.ics.oop.view.util;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class OwlBearElementBox extends VBox {
    public OwlBearElementBox(int cellWidth, int cellHeight) {
        this.setPrefSize(cellWidth, cellHeight);
        this.setAlignment(javafx.geometry.Pos.CENTER);

        Circle circle = new Circle();
        circle.setRadius(Math.min(cellWidth/2, cellHeight/2));
        circle.setFill(Color.RED);
        this.getChildren().addAll(circle);
    }
}
