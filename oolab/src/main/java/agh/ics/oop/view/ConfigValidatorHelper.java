package agh.ics.oop.view;

import agh.ics.oop.model.util.newUtils.CsvConfigValues;

// tu coś nie do końca dziala, trzeba potestować dalej
public class ConfigValidatorHelper {
    public static ValidationResult validate(CsvConfigValues config) {
        if (config.height() < 25 || config.height() > 100) {
            return new ValidationResult(false, "Wysokość mapy musi być w zakresie 25-100");
        }

        if (config.width() < 25 || config.width() > 100) {
            return new ValidationResult(false, "Szerokość mapy musi być w zakresie 25-100");
        }

        if (config.startGrassAmount() < 0) {
            return new ValidationResult(false, "Początkowa liczba trawy nie może być ujemna");
        }

        if (config.energyFromGrass() < 0) {
            return new ValidationResult(false, "Energia z trawy nie może być ujemna");
        }

        if (config.everyDayGrassAmount() < 0) {
            return new ValidationResult(false, "Codzienna liczba trawy nie może być ujemna");
        }

        if (config.startAnimalAmount() < 0) {
            return new ValidationResult(false, "Początkowa liczba zwierząt nie może być ujemna");
        }

        if (config.startEnergy() < 0) {
            return new ValidationResult(false, "Początkowa energia zwierząt nie może być ujemna");
        }

        if (config.energyRequireToReproduce() < 0) {
            return new ValidationResult(false, "Energia wymagana do rozmnażania nie może być ujemna");
        }

        if (config.energyToReproduce() < 0) {
            return new ValidationResult(false, "Energia potrzebna do rozmnażania nie może być ujemna");
        }

        if (config.dailyDeclineValue() < 0) {
            return new ValidationResult(false, "Dzienne zużycie energii nie może być ujemne");
        }

        if (config.minimalMutationAmount() < 0 || config.maximalMutationAmount() < config.minimalMutationAmount()) {
            return new ValidationResult(false, "Liczba mutacji musi być w prawidłowym zakresie");
        }

        if (!config.genomeChange().equals("Pełna losowość") && !config.genomeChange().equals("Podmianka")) {
            return new ValidationResult(false, "Zmiana genomu musi być 'Pełna losowość' lub 'Podmianka'");
        }

        if (config.genomeLength() < 0) {
            return new ValidationResult(false, "Długość genomu nie może być ujemna");
        }

        if (!config.worldMap().equals("Kula ziemska") && !config.worldMap().equals("Dziki sowoniedźwiedź")) {
            return new ValidationResult(false, "Typ mapy musi być 'Kula ziemska' lub 'Dziki sowoniedźwiedź'");
        }

        return new ValidationResult(true, "");
    }
}
