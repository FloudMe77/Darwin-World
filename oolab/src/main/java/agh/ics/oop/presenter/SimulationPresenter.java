package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.MapObjects.Animal;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.view.AnimalElementBox;
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
    private static final String EMPTY_CELL = " ";
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
    private Thread simulationThread;
    private ExecutorService executor;
    private int initialAnimalEnergy;
    private int mapWidth; // Number of columns
    private int mapHeight; // Number of rows
    private int minX;
    private int minY;
    private int maxY;
    private int maxX;

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
//        System.out.println(mapStatistics.totalAnimalAmountProperty());
//        System.out.println("WTF???");
//        animalCountLabel.textProperty().bind(mapStatistics.totalAnimalAmountProperty().asString("L zwirząt: %d"));
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

    private void drawWorldElementsOnGrid() {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Vector2d thisPosition = new Vector2d(x, y);
                Pane cell = new Pane();
                cell.setPrefSize(cellWidth, cellHeight);
                // początkowo przed sprawdzeniem czy mamy trawe to malujemy na sraczkowo
                cell.setStyle("-fx-background-color: #8B4513;");
                if (worldMap.getGrassManager().isGrassAt(thisPosition)) {
                    cell.setStyle("-fx-background-color: #118012; -fx-border-style: none;");
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
                    AnimalElementBox animalElement = new AnimalElementBox(animal, cellWidth, cellHeight, initialAnimalEnergy);
                    cell.getChildren().add(animalElement);
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
        updateStatistics();
//        System.out.println(worldMap);
    }

    // nwm czy tu koniecznie trzeba uzywac sim engine, ale moze trzeba do zastanowienia.
    public void simulationStart(Config config) {
        initialAnimalEnergy = config.startEnergy();
        WorldMap map = config.worldMap();
        map.addObserver(this);
        Simulation simulation = new Simulation(config);
        this.simulation = simulation;
        this.mapStatistics = map.getMapStatistics();
//        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        executor = Executors.newSingleThreadExecutor();
        executor.submit(simulation);
        stopButton.disableProperty().bind(simulation.stoppedProperty());
    }

    public void onStop() {
        try {
            simulation.terminate();
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateStatistics() {
        animalCountLabel.textProperty().set(String.format("Liczba zwierząt: %d", mapStatistics.getTotalAnimalAmount()));
        grassCountLabel.textProperty().set(String.format("Liczba traw: %d", mapStatistics.getTotalGrasAmount()));
        freeSpaceLabel.textProperty().set(String.format("Liczba wolny miejsc: %d", mapStatistics.getTotalFreeSpace()));
//        dominujący genom nwm TODO
        avgEnergyLabel.textProperty().set(String.format("Średni poziom energii: %.2f", mapStatistics.getAverageEnergy()));
        avgLifespanLabel.textProperty().set(String.format("Średnia długość życia: %.2f",mapStatistics.getAverageLifeTime()));
        avgChildrenAmountLabel.textProperty().set(String.format("Średnia liczba dzieci: %.2f", mapStatistics.getAverageChildrenAmount()));
    }

    public void handlePauseSimulation(ActionEvent actionEvent) {
        this.simulation.setStopped(true);
    }

    public void handleResumeSimulation(ActionEvent actionEvent) {
        simulation.resume();
    }

    private void calculateCellSizes() {
        double availableWidth = rootPane.getCenter().getBoundsInParent().getWidth();
        double availableHeight = rootPane.getCenter().getBoundsInParent().getHeight();
        cellWidth = (int) availableWidth / mapWidth;
        cellHeight = (int) availableHeight / mapHeight;
    }

    public void bindStatistics() {
        if (mapStatistics == null) {
            throw new IllegalStateException("mapStatistics is not initialized yet");
        }

//        animalCountLabel.textProperty().bind(mapStatistics.totalAnimalAmountProperty().asString("Liczba zwierząt: %d"));
//        grassCountLabel.textProperty().bind(mapStatistics.totalGrassAmountProperty().asString("Liczba traw: %d"));
//        freeSpaceLabel.textProperty().bind(mapStatistics.totalFreeSpaceProperty().asString("Liczba wolny miejsc: %d"));
//         dominujący genom nwm TODO
//        avgEnergyLabel.textProperty().bind(mapStatistics.averageAnimalEnergyProperty().asString("Średni poziom energii: %.2f"));
//        avgLifespanLabel.textProperty().bind(mapStatistics.averageLifespanProperty().asString("Średnia długość życia: %.2f"));
//        avgChildrenAmountLabel.textProperty().bind(mapStatistics.averageChildrenAmountProperty().asString("Średnia liczba dzieci: %.2f"));
    }
}
