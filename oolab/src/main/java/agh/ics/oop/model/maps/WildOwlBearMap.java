package agh.ics.oop.model.maps;

import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.OwlBear;
import agh.ics.oop.model.util.newUtils.Genome;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WildOwlBearMap extends BasicRectangularMap {
    private final OwlBear owlBear; // add final
    private final WorldMap owlBearMap;

    public WildOwlBearMap(int height, int width, Genome genome) {
        super(height, width);
        int bearSideLength = (int) Math.floor(Math.sqrt(height * width * 0.2));
        System.out.println(bearSideLength);
        owlBearMap = new RectangularMap(bearSideLength, bearSideLength);
        owlBear = new OwlBear(new Vector2d(0, 0), genome);
    }
    public WildOwlBearMap(int height, int width) {
        // póki co hard-coded 5 ale pewnie potem w symulacji bedzie to przekazywane
        this(height,width,new Genome(5));
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return owlBear.isAt(position) || super.isOccupied(position);
    }

    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        var elements = new ArrayList<>(super.objectsAt(position));
        if (owlBear.isAt(position)) {
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
    public void feedAnimals(int feedVal){
        List<AbstractAnimal> animalsAtPosition = animalManager.getAnimals(owlBear.getPosition());
        if (animalsAtPosition != null) {
            for (var animal : new ArrayList<>(animalsAtPosition)) {
                // można by to pominąc, jakby animals to po prostu tablica animali
                if (animal instanceof Animal concreatAnimal) {
                    concreatAnimal.die();
                    animalManager.removeFromAnimals(animal.getPosition(), animal);
                    mapStatistic.deathAnimalUpdate(concreatAnimal);
                }
            }
        }
        super.feedAnimals(feedVal);
    }

    @Override
    public void moveAllAnimals(int dailyDeclineValue){
        owlBear.move(owlBearMap);
        super.moveAllAnimals(dailyDeclineValue);
    }

    // trzeba się zastanowić nad sensem tego
//    @Override
//    public WorldMap getValidator(AbstractAnimal animal) {
//        return animal.getClass() == OwlBear.class ? owlBearMap : this;
//    }
}

