package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.Config;
import agh.ics.oop.model.maps.EarthMap;
import agh.ics.oop.model.maps.WildOwlBearMap;
import agh.ics.oop.model.maps.WorldMap;
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


// Używanie configvalidatorhelpera mozliwe ze jest zbędne po zmianie na spinnery ale idk potem zobacze
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
        // tu może warto zrobić enuma?
        genomeChangeBox.getItems().addAll("Pełna losowość", "Podmianka");
        genomeChangeBox.setValue("Pełna losowość");
        worldMapBox.getItems().addAll("Kula ziemska", "Dziki sowoniedźwiedź");
        worldMapBox.setValue("Kula ziemska");

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

    @FXML
    private void handleSaveConfig() {
        Optional<CsvConfigValues> csvConfigValues = createCsvConfigValues();
        if (csvConfigValues.isEmpty()) {
            return;
        }

        Optional<String> configName = ControlHelper.getConfigName();
        if (configName.isPresent()) {
            String fileName = configName.get().trim();
            try {
                CsvConfigHandler.saveConfig(csvConfigValues.get(), fileName);
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
        Optional<CsvConfigValues> csvConfigValuesOptional = createCsvConfigValues();
        if (csvConfigValuesOptional.isEmpty()) {
            return;
        }

        CsvConfigValues csvConfigValues = csvConfigValuesOptional.get();

        GenomeChange genomeChange = csvConfigValues.genomeChange().equals("Pełna losowość") ? new FullRandomGenomeChange() : new ReplacmentGenomeChange();
        WorldMap worldMap = csvConfigValues.worldMap().equals("Kula ziemska") ? new EarthMap(csvConfigValues.height(), csvConfigValues.width()) : new WildOwlBearMap(csvConfigValues.height(), csvConfigValues.width());

        Config config = new Config(
            csvConfigValues.height(),
            csvConfigValues.width(),
            csvConfigValues.startGrassAmount(),
            csvConfigValues.energyFromGrass(),
            csvConfigValues.everyDayGrassAmount(),
            csvConfigValues.startAnimalAmount(),
            csvConfigValues.startEnergy(),
            csvConfigValues.energyRequireToReproduce(),
            csvConfigValues.energyToReproduce(),
            csvConfigValues.dailyDeclineValue(),
            csvConfigValues.minimalMutationAmount(),
            csvConfigValues.maximalMutationAmount(),
            genomeChange,
            csvConfigValues.genomeLength(),
            worldMap
        );

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
            File directory = new File(System.getProperty("user.home") + "/savedConfigurations");
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files == null || files.length == 0) {
                ControlHelper.showAlert("Nie znaleziono zapisanych konfiguracji.");
                return;
            }

            Optional<String> selectedConfig = ControlHelper.showConfigSelectionDialog(files);

            if (selectedConfig.isPresent()) {
                Optional<CsvConfigValues> csvConfigValues = CsvConfigHandler.loadConfig(selectedConfig.get());

                if (csvConfigValues.isPresent()) {
                    loadConfigToFields(csvConfigValues.get());
                } else {
                    ControlHelper.showAlert("Nie można odczytać tej konfiguracji");
                }
            }
        } catch (IOException e) { // ale w sumei w jakich przypadkach tak bedzie?
            ControlHelper.showAlert("Nie można otworzyć tego pliku");
        }
    }

    private void loadConfigToFields(CsvConfigValues config) {
        heightSpinner.getValueFactory().setValue(config.height());
        widthSpinner.getValueFactory().setValue(config.width());
        startGrassSpinner.getValueFactory().setValue(config.startGrassAmount());
        energyFromGrassSpinner.getValueFactory().setValue(config.energyFromGrass());
        dailyGrassSpinner.getValueFactory().setValue(config.everyDayGrassAmount());
        startAnimalsSpinner.getValueFactory().setValue(config.startAnimalAmount());
        startEnergySpinner.getValueFactory().setValue(config.startEnergy());
        energyToReproduceSpinner.getValueFactory().setValue(config.energyToReproduce());
        energyForOffspringSpinner.getValueFactory().setValue(config.energyRequireToReproduce());
        dailyDeclineSpinner.getValueFactory().setValue(config.dailyDeclineValue());
        minMutationsSpinner.getValueFactory().setValue(config.minimalMutationAmount());
        maxMutationsSpinner.getValueFactory().setValue(config.maximalMutationAmount());
        genomeLengthSpinner.getValueFactory().setValue(config.genomeLength());
        genomeChangeBox.setValue(config.genomeChange());
        worldMapBox.setValue(config.worldMap());
    }

    private Optional<CsvConfigValues> createCsvConfigValues() {
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

            CsvConfigValues csvConfigValues = new CsvConfigValues(height, width, startGrass, energyFromGrass, dailyGrass, startAnimals, startEnergy,
                    energyToReproduce, energyForOffspring, dailyDecline, minMutations, maxMutations, genomeChangeValue, genomeLength, worldMapValue);
            ValidationResult validationResult = ConfigValidatorHelper.validate(csvConfigValues);

            if (!validationResult.isValid()) {
                ControlHelper.showAlert(validationResult.message());
            } else {
                return Optional.of(csvConfigValues);
            }
        } catch (NumberFormatException e) {
            ControlHelper.showAlert("Niepoprawne dane wejściowe");
        }

        return Optional.empty();
    }
}
