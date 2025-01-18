package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.view.GridDrawer;
import agh.ics.oop.view.StatisticsLabels;
import agh.ics.oop.view.StatisticsUpdater;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Label animalGenome;
    @FXML
    public Label directionLabel;
    @FXML
    public Label energyLabel;
    @FXML
    public Label childrenLabel;
    @FXML
    public Label descendantsLabel;
    @FXML
    public Label daysLived;
    @FXML
    public Label dayOfDeath;
    @FXML
    public Label grassEatenLabel;
    @FXML
    public Button highlightPreferredGrassButton;
    @FXML
    public Button highlightGenomeButton;
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

    private WorldMap worldMap;
    private MapStatistics mapStatistics;
    private ExecutorService executor;
    GridDrawer gridDrawer;
    private Config config;

    private Optional<Animal> trackedAnimal = Optional.empty();
    private boolean isStopped = false;
    private StatisticsUpdater statisticsUpdater;

    @FXML
    private BorderPane rootPane;

    @FXML
    private GridPane mapGrid;

    @FXML
    private Label moveNotificationLabel;

    private Simulation simulation;
    private StatisticsLabels statisticsLabels;

    @FXML
    private void initialize() {
        resumeButton.disableProperty().bind(stopButton.disableProperty().not());
        highlightPreferredGrassButton.disableProperty().bind(stopButton.disableProperty().not());
        highlightGenomeButton.disableProperty().bind(stopButton.disableProperty().not());
        statisticsLabels = new StatisticsLabels(animalCountLabel, grassCountLabel, freeSpaceLabel, dominantGenomeLabel,
                avgEnergyLabel, avgLifespanLabel, avgChildrenAmountLabel, animalGenome, directionLabel, energyLabel,
                grassEatenLabel, childrenLabel, descendantsLabel, daysLived, dayOfDeath);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message, int id) {
        Platform.runLater(() -> gridDrawer.draw());
    }

    public void simulationStart(Config config) {
        this.config = config;
        simulation = new Simulation(config);
        worldMap = simulation.getWorldMap();
        worldMap.addObserver(this);
        mapStatistics = worldMap.getMapStatistics();
        executor = Executors.newSingleThreadExecutor();
        executor.submit(simulation);
        statisticsUpdater = new StatisticsUpdater(mapStatistics, statisticsLabels);

        gridDrawer = new GridDrawer(rootPane, mapGrid, worldMap, config, this::simulationRunningStatus, statisticsUpdater);
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

    public boolean simulationRunningStatus() {
        return isStopped;
    }

    public void handlePauseSimulation(ActionEvent actionEvent) {
        isStopped = true;
        this.simulation.setStopped(true);
    }

    public void handleResumeSimulation(ActionEvent actionEvent) {
        isStopped = false;
        simulation.resume();
    }

    public void handleHighlightGenome(ActionEvent actionEvent) {
        if (gridDrawer.isHighlightDominantGenomeAnimals()) {
            gridDrawer.setHighlightDominantGenomeAnimals(false);
            Platform.runLater(() -> highlightGenomeButton.setText("Wyświetl zwierzęta z najpopularniejszym genotypem"));
        } else {
            gridDrawer.setHighlightDominantGenomeAnimals(true);
            Platform.runLater(() -> highlightGenomeButton.setText("Ukryj zwierzęta z najpopularniejszym genotypem"));
        }
    }

    public void highlightPreferredGrassCells(ActionEvent actionEvent) {
        if (gridDrawer.isHighlightPreferredGrassArea()) {
            gridDrawer.setHighlightPreferredGrassArea(false);
            Platform.runLater(() -> highlightPreferredGrassButton.setText("Wyświetl preferowane przez rośliny pola"));
        } else {
            gridDrawer.setHighlightPreferredGrassArea(true);
            Platform.runLater(() -> highlightPreferredGrassButton.setText("Ukryj preferowane przez rośliny pola"));
        }
    }
}
