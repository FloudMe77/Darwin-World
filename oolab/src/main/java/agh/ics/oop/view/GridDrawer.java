package agh.ics.oop.view;

import agh.ics.oop.model.Config;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.maps.OwlBearMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Genome;
import agh.ics.oop.model.util.MapType;
import agh.ics.oop.view.util.AnimalElementBox;
import agh.ics.oop.view.util.OwlBearElementBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.*;
import java.util.function.Supplier;

public class GridDrawer {
    private final GridPane mapGrid;
    private final WorldMap map;
    private final BorderPane rootPane;
    private final Config config;
    private final Supplier<Boolean> simulationRunningStatus;
    private double mapWidth, mapHeight, cellWidth, cellHeight;
    private Optional<Animal> trackedAnimal = Optional.empty();
    private final StatisticsUpdater statisticsUpdater;
    private boolean highlightPreferredGrassArea = false, highlightDominantGenomeAnimals = false;

    private final Map<Vector2d, Pane> gridCells = new HashMap<>(); //  Cache cellów
    private final Set<Vector2d> highlightedGrassCells = new HashSet<>();

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
        calculateCellSizes();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int x = 0; x < mapWidth; x++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
        for (int y = 0; y < mapHeight; y++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Vector2d position = new Vector2d(x, y);
                Pane cell = new Pane();
                cell.setPrefSize(cellWidth, cellHeight);
                cell.setStyle("-fx-background-color: #99d064;");
                cell.setOnMouseClicked((MouseEvent event) -> {
                    if (simulationRunningStatus.get()) {
                        Optional<Animal> animalOpt = map.animalAt(position);
                        if (animalOpt.isPresent()) {
                            trackedAnimal = animalOpt;
                            statisticsUpdater.updateAnimalStatistics(animalOpt.get());
                        }
                    }
                });

                mapGrid.add(cell, x, y);
                gridCells.put(position, cell);
            }
        }
    }

    public void draw() {
        clearGrid();
        drawWorldElementsOnGrid();
        statisticsUpdater.updateMapStatistics();
        if (trackedAnimal.isPresent()) {
            statisticsUpdater.updateAnimalStatistics(trackedAnimal.get());
        }
        handleHighlights();
        calculateCellSizes();
        for (int x = 0; x < mapWidth; x++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
        for (int y = 0; y < mapHeight; y++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }
    }

    private void handleHighlights() {
        if (highlightPreferredGrassArea && highlightedGrassCells.isEmpty()) {
            highlightPreferredGrassCells(map.getEquatorBoundary());
        } else {
            highlightedGrassCells.clear();
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
                Vector2d thisPosition = new Vector2d(x, y);
                Pane cell = gridCells.get(thisPosition);

                if (!highlightedGrassCells.contains(thisPosition)) {
                    Pane highlightLayer = new Pane();
                    highlightLayer.setStyle("-fx-background-color: rgba(255,183,0,0.25);");
                    highlightLayer.setMouseTransparent(true);
                    highlightLayer.setPrefSize(cellWidth, cellHeight);
                    highlightedGrassCells.add(thisPosition);
                    cell.getChildren().add(highlightLayer);
                }
            }
        }
    }

    public void highlightDominantGenomeAnimals() {
        Genome dominantGenome = map.getMapStatistics().getDominantGenome();
        for (Animal animal : map.getAnimals()) {
            if (animal.getGenome().equals(dominantGenome)) {
                Vector2d thisPosition = animal.getPosition();
                Pane cell = gridCells.get(thisPosition);

                Pane preferredCell = new Pane();
                preferredCell.setStyle("-fx-background-color: rgba(255,0,0,0.25);");
                preferredCell.setMouseTransparent(true);
                preferredCell.setPrefSize(cellWidth, cellHeight);
                cell.getChildren().add(preferredCell);
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
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void calculateCellSizes() {
        double availableWidth = rootPane.getCenter().getBoundsInParent().getWidth();
        double availableHeight = rootPane.getCenter().getBoundsInParent().getHeight();
        cellWidth = Math.round((availableWidth / mapWidth) * 10) / 10.0;
        cellHeight = Math.round((availableHeight / mapHeight) * 10) / 10.0;
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
        for (Vector2d thisPosition : gridCells.keySet()) {
            Pane cell = gridCells.get(thisPosition);
            Optional<Animal> animalOpt = map.animalAt(thisPosition);

            String backgroundStyle = map.isGrassAt(thisPosition) ?
                    "-fx-background-color: #118012;" :
                    "-fx-background-color: #99d064;";
            cell.setStyle(backgroundStyle);
            cell.getChildren().clear();

            animalOpt.ifPresent(animal -> {
                AnimalElementBox AnimalBox = new AnimalElementBox(animal, cellWidth, cellHeight, config.startEnergy());
                cell.getChildren().add(AnimalBox);
            });

            if (config.mapType() == MapType.OWLBEAR_MAP) {
                drawOwlBearMap(thisPosition, cell);
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
