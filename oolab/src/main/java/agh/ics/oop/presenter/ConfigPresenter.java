package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.Config;
import agh.ics.oop.model.util.GenomeType;
import agh.ics.oop.model.util.MapType;
import agh.ics.oop.model.util.newUtils.*;
import agh.ics.oop.view.ConfigValidatorHelper;
import agh.ics.oop.view.ControlHelper;
import agh.ics.oop.view.ValidationResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ConfigPresenter {
    @FXML
    private Button loadConfigButton;
    @FXML
    private Button saveConfigButton;
    @FXML
    private CheckBox saveStatsCheckBox;
    @FXML
    private Button runSimulationButton;
    @FXML
    private Spinner<Integer> heightSpinner, widthSpinner, startGrassSpinner, energyFromGrassSpinner,
            dailyGrassSpinner, startAnimalsSpinner, startEnergySpinner, energyToReproduceSpinner,
            energyForOffspringSpinner, dailyDeclineSpinner, minMutationsSpinner,
            maxMutationsSpinner, genomeLengthSpinner;

    @FXML
    private ComboBox<String> genomeChangeBox, worldMapBox;

    private Config config;

    @FXML
    public void initialize() {
        genomeChangeBox.getItems().addAll(
                GenomeType.FULL_RANDOM_GENOME_CHANGE.getDisplayName(),
                GenomeType.REPLACEMENT_GENOME_CHANGE.getDisplayName());
        worldMapBox.getItems().addAll(
                MapType.EARTH_MAP.getDisplayName(),
                MapType.OWLBEAR_MAP.getDisplayName());

        genomeChangeBox.setValue(GenomeType.FULL_RANDOM_GENOME_CHANGE.getDisplayName());
        worldMapBox.setValue(MapType.EARTH_MAP.getDisplayName());

        setSpinnerValueFactories();
    }

    @FXML
    private void handleSaveConfig() {
        Optional<Config> configOptional = createConfig();
        if (configOptional.isEmpty()) {
            return;
        }

        Optional<String> configName = ControlHelper.getConfigName();
        if (configName.isPresent()) {
            String fileName = configName.get().trim();
            try {
                CsvConfigHandler.saveConfig(configOptional.get(), fileName);
                ControlHelper.showSuccessAlert(fileName);
            } catch (IOException e) {
                ControlHelper.showAlert("Nie udało się zapisać konfiguracji.");
            }
        }

    }

    public Config getConfig() {
        return config;
    }

    @FXML
    private void runSimulation(ActionEvent actionEvent) {
        Optional<Config> configOptional = createConfig();
        if (configOptional.isEmpty()) {
            return;
        }

        Config config = configOptional.get();

        SimulationApp simulationApp = new SimulationApp();
        try {
            simulationApp.createNewSimulation(new Stage(), config);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void handleLoadConfig(ActionEvent actionEvent) {
        try {
            File directory = new File(CsvConfigHandler.DIRECTORY_PATH);
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files == null || files.length == 0) {
                ControlHelper.showAlert("Nie znaleziono zapisanych konfiguracji.");
                return;
            }

            Optional<String> selectedConfig = ControlHelper.showConfigSelectionDialog(files);

            if (selectedConfig.isPresent()) {
                Optional<Config> configOptional = CsvConfigHandler.loadConfig(selectedConfig.get());

                if (configOptional.isPresent()) {
                    loadConfigToFields(configOptional.get());
                } else {
                    ControlHelper.showAlert("Nie można odczytać tej konfiguracji");
                }
            }
        } catch (IOException e) {
            ControlHelper.showAlert("Nie można otworzyć tego pliku");
        }
    }

    private Optional<Config> createConfig() {
        try {
            int height = heightSpinner.getValue();
            int width = widthSpinner.getValue();
            int startGrass = startGrassSpinner.getValue();
            int energyFromGrass = energyFromGrassSpinner.getValue();
            int dailyGrass = dailyGrassSpinner.getValue();
            int startAnimals = startAnimalsSpinner.getValue();
            int startEnergy = startEnergySpinner.getValue();
            int energyToReproduce = energyToReproduceSpinner.getValue();
            int energyForOffspring = energyForOffspringSpinner.getValue();
            int dailyDecline = dailyDeclineSpinner.getValue();
            int minMutations = minMutationsSpinner.getValue();
            int maxMutations = maxMutationsSpinner.getValue();
            int genomeLength = genomeLengthSpinner.getValue();
            String genomeChangeValue = genomeChangeBox.getValue();
            String worldMapValue = worldMapBox.getValue();
            boolean saveStatsToCsv = saveStatsCheckBox.isSelected();


            GenomeType genomeType = GenomeType.fromDisplayName(genomeChangeValue).orElseThrow(
                    () -> new IllegalArgumentException("Nie znaleziono takiego genomu")
            );

            MapType mapType = MapType.fromDisplayName(worldMapValue).orElseThrow(
                    () -> new IllegalArgumentException("Nie znaleziono takiej mapy")
            );

            Config config = new Config(height, width, startGrass, energyFromGrass, dailyGrass, startAnimals,
                    startEnergy, energyToReproduce, energyForOffspring, dailyDecline, minMutations, maxMutations,
                    genomeType, genomeLength, mapType, saveStatsToCsv);

            ValidationResult validationResult = ConfigValidatorHelper.validate(config);
            // Walidacja configu
            if (!validationResult.isValid()) {
                ControlHelper.showAlert(validationResult.message());
            } else {
                return Optional.of(config);
            }
        } catch (NumberFormatException e) {
            ControlHelper.showAlert("Niepoprawne dane wejściowe");
        }

        return Optional.empty();
    }


    private void setSpinnerValueFactories() {
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 100, 25));
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 100, 25));
        startGrassSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 20));
        energyFromGrassSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 25));
        dailyGrassSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 30));
        startAnimalsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10));
        startEnergySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 50));
        energyToReproduceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 20));
        energyForOffspringSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 10));
        dailyDeclineSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1));
        minMutationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1));
        maxMutationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 3));
        genomeLengthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 8));
    }

    private void loadConfigToFields(Config config) {
        heightSpinner.getValueFactory().setValue(config.height());
        widthSpinner.getValueFactory().setValue(config.width());
        startGrassSpinner.getValueFactory().setValue(config.startGrassAmount());
        energyFromGrassSpinner.getValueFactory().setValue(config.energyFromGrass());
        dailyGrassSpinner.getValueFactory().setValue(config.everyDayGrassAmount());
        startAnimalsSpinner.getValueFactory().setValue(config.startAnimalAmount());
        startEnergySpinner.getValueFactory().setValue(config.startEnergy());
        energyToReproduceSpinner.getValueFactory().setValue(config.energyRequiredToReproduce());
        energyForOffspringSpinner.getValueFactory().setValue(config.offspringEnergyCost());
        dailyDeclineSpinner.getValueFactory().setValue(config.dailyDeclineValue());
        minMutationsSpinner.getValueFactory().setValue(config.minMutationCount());
        maxMutationsSpinner.getValueFactory().setValue(config.maxMutationCount());
        genomeLengthSpinner.getValueFactory().setValue(config.genomeLength());
        genomeChangeBox.setValue(config.genomeType().getDisplayName());
        worldMapBox.setValue(config.mapType().getDisplayName());
        saveStatsCheckBox.setSelected(config.saveStatsToCsv());
    }
}
