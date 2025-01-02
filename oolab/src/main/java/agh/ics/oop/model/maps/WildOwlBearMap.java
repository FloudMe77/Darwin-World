package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.Animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WildOwlBearMap extends BasicRectangularMap {
    private Animal owlBear; // add final
    private final WorldMap owlBearMap;

    public WildOwlBearMap(int height, int width) {
        super(height, width);
        int bearSideLength = (int) Math.floor(Math.sqrt(height * width * 0.2));
        owlBearMap = new RectangularMap(bearSideLength, bearSideLength);
        Vector2d owlBearPosition = new Vector2d(0, 0);
    }

    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        var elements = new ArrayList<>(super.objectsAt(position));
        if (owlBear.getPosition() == position) {
            elements.addFirst(owlBear); // wstawiam na początku, żeby display najpiewr wychwytywał owlbeara, to do zmiany w przyszłości
        }

        return Collections.unmodifiableList(elements);
    }

    public List<WorldElement> getElements() {
        var elements = new ArrayList<>(super.getElements());
        elements.add(owlBear);

        return Collections.unmodifiableList(elements);
    }

    @Override
    public WorldMap getValidator(Animal animal) {
//        return animal.getClass() == OwlBear.class ? wildOwlBearValidator : animalValidator
        return owlBearMap;
    }
}

