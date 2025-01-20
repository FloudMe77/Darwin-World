package agh.ics.oop.view.util;

import agh.ics.oop.model.Config;

// tu coś nie do końca dziala, trzeba potestować dalej
public class ConfigValidatorHelper {
    public static ValidationResult validate(Config config) {
        if (config.height() < 10 || config.height() > 80) {
            return new ValidationResult(false, "Wysokość mapy musi być w zakresie 25-100");
        }

        if (config.width() < 10 || config.width() > 80) {
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

        if (config.energyRequiredToReproduce() < 0) {
            return new ValidationResult(false, "Energia wymagana do rozmnażania nie może być ujemna");
        }

        if (config.offspringEnergyCost() < 0) {
            return new ValidationResult(false, "Energia potrzebna do rozmnażania nie może być ujemna");
        }

        if (config.offspringEnergyCost() > config.energyRequiredToReproduce()) {
            return new ValidationResult(false, "Energia potrzebna do rozmnażania nie może być większa niż energia wymagana do rozmnażania");
        }

        if (config.dailyDeclineValue() < 0) {
            return new ValidationResult(false, "Dzienne zużycie energii nie może być ujemne");
        }

        if (config.minMutationCount() < 0 || config.maxMutationCount() < config.minMutationCount()) {
            return new ValidationResult(false, "Liczba mutacji musi być w prawidłowym zakresie");
        }


        if (config.genomeLength() < 0) {
            return new ValidationResult(false, "Długość genomu nie może być ujemna");
        }

        return new ValidationResult(true, "");
    }
}
