package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.OptionsParser.parse;

public class SimulationPresenter implements MapChangeListener {
    private static final String EMPTY_CELL = " ";
    private static final int GRIDSIZE = 400;

    private WorldMap worldMap;
    private int singleCellSize;
    private int minX;
    private int minY;
    private int sideLength;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Button StartButton;
    @FXML
    private Label moveNotificationLabel;
    @FXML
    private TextField moveListTextField;

    private void updateMapSize(){
        var bounds = worldMap.getCurrentBounds();
        minX = bounds.leftDownCornerMap().getX();
        minY = bounds.leftDownCornerMap().getY();
        int maxX = bounds.rightUpperCornerMap().getX();
        int maxY = bounds.rightUpperCornerMap().getY();

        int numberOfCellsInRow = maxX - minX + 1;
        int numberOfCellsInColumn = maxY - minY + 1;
        sideLength = Math.max(numberOfCellsInRow, numberOfCellsInColumn);
        singleCellSize = (int) Math.ceil( (double) GRIDSIZE / (sideLength+1));
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

    private void drawFirstCeil(){
        Label label = new Label("y/x");
        mapGrid.getColumnConstraints().add(new ColumnConstraints(singleCellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(singleCellSize));
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }
    
    private void drawHeaderOfGrid(){
        for(int x=0; x<sideLength; x++){
            Label label = new Label(Integer.toString(x+minX));

            mapGrid.add(label,x+1,0);
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(singleCellSize));
        }
    }

    private void drawFirstColumnOfGrid(){
        for(int y=0; y<sideLength;y++){
            Label label = new Label(Integer.toString(y+minY));

            mapGrid.add(label,0,sideLength-y);
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(singleCellSize));
        }
    }

    private String getStringFromObjectFromMap(Vector2d currentPosition) {
        if (worldMap.isOccupied(currentPosition)) {
            Object object = worldMap.objectAt(currentPosition);
            if (object != null) {
                return object.toString();
            }
        }
        return EMPTY_CELL;
    }

    private void drawWorldElementsOnGrid(){
        for(int x = 0; x < sideLength; x++){
            for(int y = 0; y < sideLength; y++){
                var position = new Vector2d(x+minX,y+minY);
                Label label = new Label(getStringFromObjectFromMap(position));
                mapGrid.add(label, x + 1,sideLength - y);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }
    }
    
    private void drawGrid(){
        updateMapSize();
        drawFirstCeil();
        drawHeaderOfGrid();
        drawFirstColumnOfGrid();
        drawWorldElementsOnGrid();
    }

    @FXML
    public void startClicked(){
        String moves = moveListTextField.getText();
        SimulationApp simulationApp = new SimulationApp();
        try {
            simulationApp.creatNewSimulation(new Stage(), moves);
        } catch (Exception e) {
            System.out.println("nie udało się uruchomić kolejnej symulacji");
            e.printStackTrace();
        }
    }

    public void simulationStart(String moves){
        List<Simulation> simulations = new ArrayList<>();
        List<MoveDirection> directions = parse(moves.split(" "));
        List<Vector2d> positions = List.of(new Vector2d(1, 1), new Vector2d(1, 2));
        GrassField map = new GrassField(10);
        map.addObserver(this);
        Simulation simulation = new Simulation(positions, directions, map);
        simulations.add(simulation);
        SimulationEngine simulationEngine = new SimulationEngine(simulations);
        // symulacje działają równolegle
        new Thread(simulationEngine::runAsync).start();
    }
}
