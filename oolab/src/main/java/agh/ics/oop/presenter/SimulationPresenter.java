package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.MapObjects.Grass;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.MapChangeListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimulationPresenter implements MapChangeListener {
    private static final String EMPTY_CELL = " ";
    private int cellWidth;
    private int cellHeight;

    private WorldMap worldMap;
    private int initialAnimalEnergy;
    private int mapWidth; // Number of columns
    private int mapHeight; // Number of rows
    private int minX;
    private int minY;
    private int maxY;
    private int maxX;
    private int singleCellSize;
    private int sideLength;

    @FXML
    private BorderPane rootPane;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Button StartButton;
    @FXML
    private Label moveNotificationLabel;
    @FXML
    private TextField moveListTextField;

    @FXML
    private void initialize() {

    }

    private void updateMapSize() {
        var bounds = worldMap.getCurrentBounds();
        minX = bounds.leftDownCornerMap().getX();
        minY = bounds.leftDownCornerMap().getY();
        maxX = bounds.rightUpperCornerMap().getX();
        maxY = bounds.rightUpperCornerMap().getY();
        int numberOfCellsInARow = maxX - minX + 1;
        int numberOfCellsInAColumn = maxY - minY + 1;
        mapWidth = numberOfCellsInAColumn;
        mapHeight = numberOfCellsInARow;
        sideLength = Math.max(numberOfCellsInARow, numberOfCellsInAColumn);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message, int id) {
        this.worldMap = worldMap;

        Platform.runLater(() -> {
            moveNotificationLabel.setText(message + " na mapie nr. " + id);
            drawGrid();
        });
    }

    @FXML
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void drawFirstCeil() {
        Label label = new Label("y/x");
        mapGrid.getColumnConstraints().add(new ColumnConstraints(singleCellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(singleCellSize));
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    private void drawWorldElementsOnGrid() {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Vector2d thisPosition = new Vector2d(x, y);
                // sprawdzamy czy na danej pozycji jest zwierzak:
                Pane cell = new Pane();
                cell.setPrefSize(cellWidth, cellHeight);
                cell.setStyle("-fx-background-color: #8B4513;");
                // na tej pozycji nie ma zwierzaka ani trawy więc malujemy na sraczkowo
                if (worldMap.getGrassManager().isGrassAt(thisPosition)) {
                    cell.setStyle("-fx-background-color: #118012;");
                }


                if (worldMap.getAnimalManager().isAnimalAt(thisPosition)) {
                    Optional<Animal> animalOptional = worldMap.getAnimalManager()
                            .getAnimals(thisPosition)
                            .stream()
                            .filter(abstractAnimal -> abstractAnimal instanceof Animal)
                            .map(AbstractAnimal -> (Animal) AbstractAnimal)
                            .findFirst();
                    if (animalOptional.isEmpty()) {
                        return;
                    }
                    Animal animal = animalOptional.get();

                    VBox vbox = new VBox();
                    vbox.setPrefSize(cellWidth, cellHeight);
                    vbox.setAlignment(javafx.geometry.Pos.CENTER);

                    Circle circle = new Circle();
                    circle.setRadius(Math.min(cellWidth, cellHeight) / 4);
                    circle.setStyle("-fx-fill: #ac00ff; -fx-stroke: black; -fx-stroke-width: 1;");

                    double energyFraction = (double) animal.getEnergy() / initialAnimalEnergy;

                    Label energyLabel = new Label(String.format("%.2f", energyFraction));
                    energyLabel.setStyle("-fx-font-size: 10px;");
                    energyLabel.setAlignment(javafx.geometry.Pos.CENTER);

                    vbox.getChildren().addAll(circle, energyLabel);

                    cell.getChildren().add(vbox);
                }

                mapGrid.add(cell, x, y);
            }
        }
    }

    private void setGridWidthAndHeight() {
        for (int x = 0; x < mapWidth; x++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }

        for (int y = 0; y < mapHeight; y++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }
    }

    private void drawGrid() {
        clearGrid();
        updateMapSize();
        calculateCellSizes();
        setGridWidthAndHeight();
        drawWorldElementsOnGrid();
        System.out.println(worldMap);
    }

    public void simulationStart(Config config) {
        List<Simulation> simulations = new ArrayList<>();
        initialAnimalEnergy = config.startEnergy();
        WorldMap map = config.worldMap();
        map.addObserver(this);
        Simulation simulation = new Simulation(config);
        simulations.add(simulation);
        SimulationEngine simulationEngine = new SimulationEngine(simulations);
        new Thread(simulationEngine::runAsync).start();
    }

    public void handlePauseSimulation(ActionEvent actionEvent) {
    }

    public void handleResumeSimulation(ActionEvent actionEvent) {
    }

    private void calculateCellSizes() {
        double availableWidth = rootPane.getCenter().getBoundsInParent().getWidth();
        double availableHeight = rootPane.getCenter().getBoundsInParent().getHeight();
        cellWidth = (int) availableWidth / mapWidth;
        cellHeight = (int) availableHeight / mapHeight;
    }
}
