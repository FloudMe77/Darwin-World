package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
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

public class ConfigPresenter {

    @FXML
    private Spinner spinnerTest;

    @FXML
    private TextField heightField, widthField, startGrassField, energyFromGrassField, dailyGrassField, startAnimalsField,
            startEnergyField, energyToReproduceField, energyForOffspringField, dailyDeclineField, minMutationsField,
            maxMutationsField, genomeLengthField;

    @FXML
    private ComboBox<String> genomeChangeBox;
    @FXML
    private ComboBox<String> worldMapBox;

    private Config config;

    @FXML
    public void initialize() {
        // tu może warto zrobić enuma?
        genomeChangeBox.getItems().addAll("Pełna losowość", "Podmianka");
        genomeChangeBox.setValue("Pełna losowość");
        worldMapBox.getItems().addAll("Kula ziemska", "Dziki sowoniedźwiedź");
        worldMapBox.setValue("Kula ziemska");

        // to gowno przerobic na spinnery idk czemu na pocztku tak nie zrobiłem ...
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 100, 25);

        spinnerTest.setValueFactory(valueFactory);
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

    @FXML
    private void handleCancel() {
        ((Stage) heightField.getScene().getWindow()).close();
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
        heightField.setText(String.valueOf(config.height()));
        widthField.setText(String.valueOf(config.width()));
        startGrassField.setText(String.valueOf(config.startGrassAmount()));
        energyFromGrassField.setText(String.valueOf(config.energyFromGrass()));
        dailyGrassField.setText(String.valueOf(config.everyDayGrassAmount()));
        startAnimalsField.setText(String.valueOf(config.startAnimalAmount()));
        startEnergyField.setText(String.valueOf(config.startEnergy()));
        energyToReproduceField.setText(String.valueOf(config.energyRequireToReproduce()));
        energyForOffspringField.setText(String.valueOf(config.energyToReproduce()));
        dailyDeclineField.setText(String.valueOf(config.dailyDeclineValue()));
        minMutationsField.setText(String.valueOf(config.minimalMutationAmount()));
        maxMutationsField.setText(String.valueOf(config.maximalMutationAmount()));
        genomeLengthField.setText(String.valueOf(config.genomeLength()));
        genomeChangeBox.setValue(config.genomeChange());
        worldMapBox.setValue(config.worldMap());
    }

    private Optional<CsvConfigValues> createCsvConfigValues() {
        try {
            int height = Integer.parseInt(heightField.getText());
            int width = Integer.parseInt(widthField.getText());
            int startGrass = Integer.parseInt(startGrassField.getText());
            int energyFromGrass = Integer.parseInt(energyFromGrassField.getText());
            int dailyGrass = Integer.parseInt(dailyGrassField.getText());
            int startAnimals = Integer.parseInt(startAnimalsField.getText());
            int startEnergy = Integer.parseInt(startEnergyField.getText());
            int energyToReproduce = Integer.parseInt(energyToReproduceField.getText());
            int energyForOffspring = Integer.parseInt(energyForOffspringField.getText());
            int dailyDecline = Integer.parseInt(dailyDeclineField.getText());
            int minMutations = Integer.parseInt(minMutationsField.getText());
            int maxMutations = Integer.parseInt(maxMutationsField.getText());
            int genomeLength = Integer.parseInt(genomeLengthField.getText());
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
