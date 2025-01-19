package agh.ics.oop.model.util;

import agh.ics.oop.model.Config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class CsvConfigHandler {
    public static final String DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "configs").toString();;
    private static final String HEADER = "height,width,startGrassAmount,energyFromGrass,everyDayGrassAmount,startAnimalAmount,"
            + "startEnergy,energyRequireToReproduce,offspringEnergyCost,dailyDeclineValue,minimalMutationAmount,"
            + "maximalMutationAmount,genomeChange,genomeLength,worldMap,saveStatsToCsv";
    static {
        try {
            Files.createDirectories(Paths.get(DIRECTORY_PATH));
        } catch (IOException e) {
            throw new IllegalStateException("Nie można utworzyć katalogu na konfiguracje: " + DIRECTORY_PATH, e);
        }
    }

    public static void saveConfig(Config config, String configName) throws IOException {
        File configFile = new File(DIRECTORY_PATH, configName + ".csv");

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(HEADER + "\n");
            writer.append(config.toCsvString());
        }
    }

    public static Optional<Config> loadConfig(String configName) throws IOException {
        File configFile = new File(DIRECTORY_PATH, configName + ".csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            reader.readLine();
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                int height = Integer.parseInt(parts[0]);
                int width = Integer.parseInt(parts[1]);
                int startGrassAmount = Integer.parseInt(parts[2]);
                int energyFromGrass = Integer.parseInt(parts[3]);
                int everyDayGrassAmount = Integer.parseInt(parts[4]);
                int startAnimalAmount = Integer.parseInt(parts[5]);
                int startEnergy = Integer.parseInt(parts[6]);
                int energyRequireToReproduce = Integer.parseInt(parts[7]);
                int energyToReproduce = Integer.parseInt(parts[8]);
                int dailyDeclineValue = Integer.parseInt(parts[9]);
                int minimalMutationAmount = Integer.parseInt(parts[10]);
                int maximalMutationAmount = Integer.parseInt(parts[11]);
                GenomeType genomeType = GenomeType.fromDisplayName(parts[12]).orElseThrow(
                        () -> new IllegalArgumentException("Nie znaleziono takiego genomu")
                );
                int genomeLength = Integer.parseInt(parts[13]);
                MapType mapType = MapType.fromDisplayName(parts[14]).orElseThrow(
                        () -> new IllegalArgumentException("Nie znaleziono takiej mapy")

                );
                boolean saveStatsToCsv = Boolean.parseBoolean(parts[15]);


                return Optional.of(new Config(height, width, startGrassAmount, energyFromGrass, everyDayGrassAmount,
                        startAnimalAmount, startEnergy, energyRequireToReproduce, energyToReproduce,
                        dailyDeclineValue, minimalMutationAmount, maximalMutationAmount, genomeType,
                        genomeLength, mapType, saveStatsToCsv));
            }
        }

        return Optional.empty();
    }
}
