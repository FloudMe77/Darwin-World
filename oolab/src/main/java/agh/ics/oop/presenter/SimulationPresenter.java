package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.AbstractAnimal;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.maps.OwlBearMap;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapType;
import agh.ics.oop.view.AbstractAnimalElementBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Label genomeLabel;
    @FXML
    public Label directionLabel;
    @FXML
    public Label energyLabel;
    @FXML
    public Label childrenLabel;
    @FXML
    public Label descendantsLabel;
    @FXML
    public Label dayLiveLabel;
    @FXML
    public Label dayDeathLabel;
    @FXML
    public Label grassEatenLabel;
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label grassCountLabel;
    @FXML
    private Label freeSpaceLabel;
    @FXML
    private Label dominantGenomeLabel;
    @FXML
    private Label avgEnergyLabel;
    @FXML
    private Label avgLifespanLabel;
    @FXML
    private Label avgChildrenAmountLabel;
    @FXML
    private Button resumeButton;
    @FXML
    private Button stopButton;

    private int cellWidth;
    private int cellHeight;

    private WorldMap worldMap;
    private MapStatistics mapStatistics;
//    private Thread simulationThread;
    private ExecutorService executor;
    private int mapWidth; // Number of columns
    private int mapHeight; // Number of rows
    private Config config;

    private Optional<Animal> trackedAnimal = Optional.empty();
    private boolean isStopped = false;

    @FXML
    private BorderPane rootPane;

    @FXML
    private GridPane mapGrid;

    @FXML
    private Label moveNotificationLabel;

    private Simulation simulation;

    @FXML
    private void initialize() {
        resumeButton.disableProperty().bind(stopButton.disableProperty().not());
    }

    private void updateMapSize() {
        var bounds = worldMap.getCurrentBounds();
        int minX = bounds.leftDownCornerMap().getX();
        int minY = bounds.leftDownCornerMap().getY();
        int maxX = bounds.rightUpperCornerMap().getX();
        int maxY = bounds.rightUpperCornerMap().getY();
        int numberOfCellsInAColumn = maxX - minX + 1;
        int numberOfCellsInARow = maxY - minY + 1;
        mapWidth = numberOfCellsInAColumn;
        mapHeight = numberOfCellsInARow;
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message, int id) {
        Platform.runLater(this::drawGrid);
    }

    @FXML
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void drawWorldElementsOnGrid() {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Vector2d thisPosition = new Vector2d(x, y);
                Pane cell = new Pane();
                cell.setPrefSize(cellWidth, cellHeight);
                cell.setStyle("-fx-background-color: #99d064;");

                if (worldMap.isGrassAt(thisPosition)) {
                    cell.setStyle("-fx-background-color: #118012; -fx-border-style: none;");
                }

                if (config.mapType() == MapType.OWLBEAR_MAP) {
                    WorldMap owlBearMap = ((OwlBearMap) worldMap).getOwlBearMap();
                    Vector2d owlBearMapLowerLeft = owlBearMap.getCurrentBounds().leftDownCornerMap();
                    Vector2d owlBearMapUpperRight = owlBearMap.getCurrentBounds().rightUpperCornerMap();

                    if (thisPosition.follows(owlBearMapLowerLeft) && thisPosition.precedes(owlBearMapUpperRight)) {
                        Pane owlBearLayer = new Pane();
                        owlBearLayer.setStyle("-fx-background-color: rgba(255,0,0,0.5);");
                        owlBearLayer.setPrefSize(cellWidth, cellHeight);
                        cell.getChildren().add(owlBearLayer);
                    }
                }

                Optional<AbstractAnimal> animalOpt = worldMap.animalAt(thisPosition);

                animalOpt.ifPresent(abstractAnimal -> {
                    cell.setOnMouseClicked((MouseEvent event) -> {
                        // nie wiem jak to inaczej zrobić

                        if(isStopped && abstractAnimal instanceof Animal currentAnimal) {
                            trackedAnimal = Optional.of(currentAnimal);
                            updateStatistics();
                        }
                    });

                    AbstractAnimalElementBox animalElement = new AbstractAnimalElementBox(abstractAnimal, cellWidth, cellHeight);
                    cell.getChildren().add(animalElement);
                });

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
        updateStatistics();
//        System.out.println(worldMap);
    }

    public void simulationStart(Config config) {
        this.config = config;
        simulation = new Simulation(config);
        worldMap = simulation.getWorldMap();
        worldMap.addObserver(this);
        mapStatistics = worldMap.getMapStatistics();
        executor = Executors.newSingleThreadExecutor();
        executor.submit(simulation);
        stopButton.disableProperty().bind(simulation.stoppedProperty());
    }

    public void simulationStop() {
        try {
            simulation.terminate();
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void updateStatistics() {
        animalCountLabel.textProperty().set(String.format("Liczba zwierząt: %d", mapStatistics.getTotalAnimalAmount()));
        grassCountLabel.textProperty().set(String.format("Liczba traw: %d", mapStatistics.getTotalGrasAmount()));
        freeSpaceLabel.textProperty().set(String.format("Liczba wolny miejsc: %d", mapStatistics.getTotalFreeSpace()));
        dominantGenomeLabel.textProperty().set(String.format("Najpopularniejszy genom: \n %s", mapStatistics.getDominantGenomeType()));
        avgEnergyLabel.textProperty().set(String.format("Średni poziom energii: %.2f", mapStatistics.getAverageEnergy()));
        avgLifespanLabel.textProperty().set(String.format("Średnia długość życia: %.2f",mapStatistics.getAverageLifeTime()));
        avgChildrenAmountLabel.textProperty().set(String.format("Średnia liczba dzieci: %.2f", mapStatistics.getAverageChildrenAmount()));

        if(trackedAnimal.isPresent()){
            var animal = trackedAnimal.get();
            genomeLabel.textProperty().set(String.format("Genom zwierzaka:\n %s", animal.getGenome()));
            directionLabel.textProperty().set(String.format("Aktywny genom: %s", animal.getCurrentGenome()));
            energyLabel.textProperty().set(String.format("Energii: %d", animal.getEnergy()));
            grassEatenLabel.textProperty().set(String.format("Zjedzona trawa: %d", animal.getEaten()));
            childrenLabel.textProperty().set(String.format("Liczba dzieci: %d", animal.getChildrenAmount()));
            // tmp
            descendantsLabel.textProperty().set(String.format("Liczba potomstwa: %d", animal.getDescendantsAmount()));

            dayLiveLabel.textProperty().set(String.format("Długość życia: %d",animal.getAge()));
            dayDeathLabel.textProperty().set(String.format("Dzień śmierci: %d", animal.getDayOfDeath()));
        }
    }

    public void handlePauseSimulation(ActionEvent actionEvent) {
        isStopped = true;
        System.out.println("stoped");
        this.simulation.setStopped(true);
    }

    public void handleResumeSimulation(ActionEvent actionEvent) {
        isStopped = false;
        simulation.resume();
    }

    private void calculateCellSizes() {
        double availableWidth = rootPane.getCenter().getBoundsInParent().getWidth();
        double availableHeight = rootPane.getCenter().getBoundsInParent().getHeight();
        cellWidth = (int) availableWidth / mapWidth;
        cellHeight = (int) availableHeight / mapHeight;
    }
}
