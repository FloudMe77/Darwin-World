package agh.ics.oop.view;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.maps.OwlBearMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapType;
import agh.ics.oop.model.util.newUtils.Genome;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.Optional;
import java.util.function.Supplier;

public class GridDrawer {
    private final GridPane mapGrid;
    private final WorldMap map;
    private final BorderPane rootPane;
    private final Config config;
    private final Supplier<Boolean> simulationRunningStatus;
    private int mapWidth, mapHeight, cellWidth, cellHeight;
    private Optional<Animal> trackedAnimal = Optional.empty();
    private final StatisticsUpdater statisticsUpdater;
    private boolean highlightPreferredGrassArea = false, highlightDominantGenomeAnimals = false;

    public GridDrawer(BorderPane rootPane, GridPane mapGrid, WorldMap map,
                      Config config, Supplier<Boolean> simulationRunningStatus,
                      StatisticsUpdater statisticsUpdater) {
        this.mapGrid = mapGrid;
        this.map = map;
        this.rootPane = rootPane;
        this.config = config;
        this.simulationRunningStatus = simulationRunningStatus;
        this.statisticsUpdater = statisticsUpdater;
        initializeMapSize();
    }

    public void draw() {
        clearGrid();
        calculateCellSizes();
        setGridCells();
        drawWorldElementsOnGrid();
        statisticsUpdater.updateMapStatistics();
        if (trackedAnimal.isPresent()) {
            statisticsUpdater.updateAnimalStatistics(trackedAnimal.get());
        }
        handleHighlights();

    }

    private void handleHighlights() {
        if (highlightPreferredGrassArea) {
            highlightPreferredGrassCells(map.getEquatorBoundary());
        }
        if (highlightDominantGenomeAnimals) {
            highlightDominantGenomeAnimals();
        }
    }

    public void highlightPreferredGrassCells(Boundary preferredArea) {
        Vector2d lowerLeft = preferredArea.leftDownCornerMap();
        Vector2d upperRight = preferredArea.rightUpperCornerMap();

        for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
            for (int y = lowerLeft.getY(); y <= upperRight.getY(); y++) {
                Pane preferredCell = new Pane();
                preferredCell.setStyle("-fx-background-color: rgba(255,183,0,0.25);");
                preferredCell.setMouseTransparent(true);
                preferredCell.setPrefSize(cellWidth, cellHeight);
                mapGrid.add(preferredCell, x, y);
            }
        }
    }

    public void highlightDominantGenomeAnimals() {
        Genome dominantGenome = map.getMapStatistics().getDominantGenome();
        for (Animal animal : map.getAnimals()) {
            if (animal.getGenome().equals(dominantGenome)) {
                int x = animal.getPosition().getX();
                int y = animal.getPosition().getY();

                Pane preferredCell = new Pane();
                preferredCell.setStyle("-fx-background-color: rgba(255,0,0,0.25);");
                preferredCell.setMouseTransparent(true);
                preferredCell.setPrefSize(cellWidth, cellHeight);
                mapGrid.add(preferredCell, x, y);
            }
        }
    }

    private void initializeMapSize() {
        var bounds = map.getCurrentBounds();
        int minX = bounds.leftDownCornerMap().getX();
        int minY = bounds.leftDownCornerMap().getY();
        int maxX = bounds.rightUpperCornerMap().getX();
        int maxY = bounds.rightUpperCornerMap().getY();
        mapWidth = maxX - minX + 1;
        mapHeight = maxY - minY + 1;
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void calculateCellSizes() {
        double availableWidth = rootPane.getCenter().getBoundsInParent().getWidth();
        double availableHeight = rootPane.getCenter().getBoundsInParent().getHeight();
        cellWidth = (int) availableWidth / mapWidth;
        cellHeight = (int) availableHeight / mapHeight;
    }

    private void setGridCells() {
        for (int x = 0; x < mapWidth; x++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }

        for (int y = 0; y < mapHeight; y++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }
    }

    private void drawWorldElementsOnGrid() {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Vector2d thisPosition = new Vector2d(x, y);
                Pane cell = new Pane();
                cell.setPrefSize(cellWidth, cellHeight);
                cell.setStyle("-fx-background-color: #99d064;");

                if (map.isGrassAt(thisPosition)) {
                    cell.setStyle("-fx-background-color: #118012; -fx-border-style: none;");
                }

                Optional<Animal> animalOpt = map.animalAt(thisPosition);

                animalOpt.ifPresent(animal -> {
                    cell.setOnMouseClicked((MouseEvent event) -> {

                        if (simulationRunningStatus.get()) {
                            trackedAnimal = Optional.of(animal);
                            statisticsUpdater.updateAnimalStatistics(animal);
                        }
                    });

                    AnimalElementBox AnimalBox = new AnimalElementBox(animal, cellWidth, cellHeight, config.startEnergy());
                    cell.getChildren().add(AnimalBox);
                });

                if (config.mapType() == MapType.OWLBEAR_MAP) {
                    drawOwlBearMap(thisPosition, cell);
                }

                mapGrid.add(cell, x, y);
            }
        }
    }

    private void drawOwlBearMap(Vector2d thisPosition, Pane cell) {
        OwlBearMap owlBearMap = ((OwlBearMap) map);
        WorldMap owlBearRectangularMap = owlBearMap.getOwlBearMap();
        Vector2d owlBearMapLowerLeft = owlBearRectangularMap.getCurrentBounds().leftDownCornerMap();
        Vector2d owlBearMapUpperRight = owlBearRectangularMap.getCurrentBounds().rightUpperCornerMap();
        Vector2d owlBearPosition = owlBearMap.getOwlBearPosition();

        if (thisPosition.equals(owlBearPosition)) {
            OwlBearElementBox owlBearBox = new OwlBearElementBox(cellWidth, cellHeight);
            cell.getChildren().setAll(owlBearBox);
        }

        if (thisPosition.follows(owlBearMapLowerLeft) && thisPosition.precedes(owlBearMapUpperRight)) {
            Pane owlBearLayer = new Pane();
            owlBearLayer.setStyle("-fx-background-color: rgba(255,0,0,0.25);");
            owlBearLayer.setPrefSize(cellWidth, cellHeight);
            cell.getChildren().add(owlBearLayer);
        }
    }

    public void setHighlightPreferredGrassArea(boolean highlightPreferredGrassArea) {
        this.highlightPreferredGrassArea = highlightPreferredGrassArea;
        draw();
    }

    public void setHighlightDominantGenomeAnimals(boolean highlightDominantGenomeAnimals) {
        this.highlightDominantGenomeAnimals = highlightDominantGenomeAnimals;
        draw();
    }

    public boolean isHighlightDominantGenomeAnimals() {
        return highlightDominantGenomeAnimals;
    }

    public boolean isHighlightPreferredGrassArea() {
        return highlightPreferredGrassArea;
    }
}
