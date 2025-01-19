package agh.ics.oop.model.util.newUtils;

import agh.ics.oop.model.MapStatistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StatisticsSaveHandler {
    public static final String DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "statistics").toString();
    private static final String HEADER = "Liczba zwierząt,Liczba traw,Liczba wolnych miejsc,Najpopularniejszy genom,Średni poziom energii,Średnia długość życia,Średnia liczba dzieci\n";

    static {
        try {
            Files.createDirectories(Paths.get(DIRECTORY_PATH));
        } catch (IOException e) {
            throw new IllegalStateException("Nie można utworzyć katalogu na statystyki: " + DIRECTORY_PATH, e);
        }
    }

    public static void addStatistics(MapStatistics mapStatistics, String filename) {
        File file = new File(DIRECTORY_PATH, filename + ".csv");

        boolean isNewFile = !file.exists();

        try (FileWriter writer = new FileWriter(file, true)) {
            if (isNewFile) {
                writer.write(HEADER);
            }

            writer.write(String.format("%d,%d,%d,%s,%.2f,%.2f,%.2f\n",
                    mapStatistics.getTotalAnimalAmount(),
                    mapStatistics.getTotalGrasAmount(),
                    mapStatistics.getTotalFreeSpace(),
                    mapStatistics.getDominantGenome(),
                    mapStatistics.getAverageEnergy(),
                    mapStatistics.getAverageLifeTime(),
                    mapStatistics.getAverageChildrenAmount()));
        } catch (IOException e) {
            System.err.println("Błąd zapisu statystyk: " + e.getMessage());
        }
    }
}
