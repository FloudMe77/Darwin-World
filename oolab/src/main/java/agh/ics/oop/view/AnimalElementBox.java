//package agh.ics.oop.view;
//
//import agh.ics.oop.model.MapObjects.Animal;
//import javafx.scene.control.Label;
//import javafx.scene.layout.VBox;
//import javafx.scene.shape.Circle;
//
//public class AnimalElementBox extends VBox {
//    public AnimalElementBox(Animal animal, int cellWidth, int cellHeight, int initialAnimalEnergy) {
//        this.setPrefSize(cellWidth, cellHeight);
//        this.setAlignment(javafx.geometry.Pos.CENTER);
//        this.setStyle("-fx-background-color:  rgba(144, 238, 144, 0.2);");
//
//        Circle circle = new Circle();
//        circle.setRadius(Math.min(cellWidth, cellHeight) / 4);
//        circle.setStyle("-fx-fill: #ac00ff; -fx-stroke: black; -fx-stroke-width: 1;");
//
//        Label energyLabel = new Label(String.format("%d/%d", animal.getEnergy(), initialAnimalEnergy));
//        energyLabel.setStyle("-fx-font-size: 10px;");
//        energyLabel.setAlignment(javafx.geometry.Pos.CENTER);
//
//        this.getChildren().addAll(circle, energyLabel);
//    }
//}
