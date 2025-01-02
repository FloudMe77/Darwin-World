package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.OwlBear;
import agh.ics.oop.model.util.newUtils.Genome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WildOwlBearMap extends BasicRectangularMap {
    private final OwlBear owlBear; // add final
    private final WorldMap owlBearMap;

    public WildOwlBearMap(int height, int width) {
        super(height, width);
        int bearSideLength = (int) Math.floor(Math.sqrt(height * width * 0.2));
        owlBearMap = new RectangularMap(bearSideLength, bearSideLength);
        owlBear = new OwlBear(new Vector2d(0, 0), new Genome(5)); // póki co hard-coded 5 ale pewnie potem w symulacji bedzie to przekazywane
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
    public WorldMap getValidator(AbstractAnimal animal) {
        return animal.getClass() == OwlBear.class ? owlBearMap : this;
    }
}

