package agh.ics.oop.view.util;

import agh.ics.oop.model.mapObjects.Animal;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AnimalElementBox extends VBox {
    private final Animal animal;
    private final int initialEnergy;

    public AnimalElementBox(Animal animal, int cellWidth, int cellHeight, int initialEnergy) {
        this.animal = animal;
        this.initialEnergy = initialEnergy;
        this.setPrefSize(cellWidth, cellHeight);
        this.setAlignment(javafx.geometry.Pos.CENTER);

        Circle circle = new Circle();
        circle.setRadius(Math.min(cellWidth/2, cellHeight/2));
        circle.setFill(getColor());
        this.getChildren().addAll(circle);
    }

    public Color getColor() {
        double fraction = Math.min(animal.getEnergy() / (double) initialEnergy, 1);

        // Kolor biały (dla fraction = 0)
        double whiteR = 0.7, whiteG = 0.7, whiteB = 0.7;

        // Kolor ciemnobrązowy (dla fraction = 1)
        double brownR = 101 / 255.0, brownG = 67 / 255.0, brownB = 33 / 255.0;

        // Upewnij się, że fraction mieści się w zakresie [0, 1]
        fraction = Math.max(0, Math.min(fraction, 1));

        // Wylicz proporcjonalne składowe RGB (interpolacja od białego do ciemnobrązowego)
        double red = whiteR + fraction * (brownR - whiteR);
        double green = whiteG + fraction * (brownG - whiteG);
        double blue = whiteB + fraction * (brownB - whiteB);

        return new Color(red, green, blue, 1.0);
    }
}
