package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
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

import java.util.ArrayList;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private static final String EMPTY_CELL = " ";
    private double cellWidth;
    private double cellHeight;

    private WorldMap worldMap;
    private int mapWidth; // Number of columns
    private int mapHeight; // Number of rows
    private int minX;
    private int minY;
    private int maxY;
    private int maxX;
    private int singleCellSize;
    private int sideLength;

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
        // tutaj jakies redraw grid?s

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
            clearGrid();
            drawGrid();
        });
    }

    @FXML
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void drawHeaderOfGrid() {
        for (int x = 0; x < sideLength; x++) {
//            mapGrid.add(label,x+1,0);
//            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }
    }

    private void drawFirstCeil() {
        Label label = new Label("y/x");
        mapGrid.getColumnConstraints().add(new ColumnConstraints(singleCellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(singleCellSize));
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }


    private String getStringFromObjectFromMap(Vector2d currentPosition) {
        if (worldMap.isOccupied(currentPosition)) {
            Object object = worldMap.objectsAt(currentPosition);
            if (object != null) {
                return object.toString();
            }
        }
        return EMPTY_CELL;
    }

    private void drawWorldElementsOnGrid() {
        for (WorldElement worldElement : worldMap.getElements()) {
            Pane cell = new Pane();
            cell.setPrefSize(cellWidth, cellHeight);
            int x = worldElement.getPosition().getX();
            int y = worldElement.getPosition().getY();
            Label label = new Label(String.format("%s", worldElement));
            mapGrid.add(label, x - minX, maxY - y);
            GridPane.setHalignment(label, HPos.CENTER);
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
        updateMapSize();
        calculateCellSizes();
        setGridWidthAndHeight();
        drawWorldElementsOnGrid();
        System.out.println(worldMap);
    }

    public void simulationStart(Config config) {
        List<Simulation> simulations = new ArrayList<>();
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
        cellWidth = mapGrid.getWidth() / mapWidth;
        cellHeight = mapGrid.getHeight() / mapHeight;
    }
}
