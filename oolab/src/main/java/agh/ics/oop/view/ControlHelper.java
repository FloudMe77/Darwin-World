package agh.ics.oop.view;

import javafx.scene.control.*;

import java.io.File;
import java.util.Optional;

public class ControlHelper {
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccessAlert(String fileName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Zapisano konfigurację");
        alert.setHeaderText("Sukces!");
        alert.setContentText("Konfiguracja została zapisana jako \"" + fileName + ".csv\".");
        alert.showAndWait();
    }

    public static Optional<String> showConfigSelectionDialog(File[] files) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Wybór konfiguracji");
        dialog.setHeaderText("Wybierz konfigurację do wczytania:");

        ComboBox<String> comboBox = new ComboBox<>();
        for (File file : files) {
            comboBox.getItems().add(file.getName().replace(".csv", ""));
        }

        comboBox.getSelectionModel().selectFirst();

        ButtonType loadButtonType = new ButtonType("Wczytaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loadButtonType);

        dialog.getDialogPane().setContent(comboBox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loadButtonType) {
                return comboBox.getValue();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<String> getConfigName() {
        TextInputDialog dialog = new TextInputDialog("Nazwa Konfiguracji");
        dialog.setTitle("Zapis konfiguracji");
        dialog.setHeaderText("Podaj nazwę dla zapisywanej konfiguracji");
        dialog.setContentText("Nazwa pliku:");

        return dialog.showAndWait().filter(name -> !name.trim().isEmpty());
    }
}
