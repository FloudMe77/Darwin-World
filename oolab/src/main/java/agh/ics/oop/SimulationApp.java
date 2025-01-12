package agh.ics.oop;

import agh.ics.oop.model.Config;
import agh.ics.oop.presenter.ConfigPresenter;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("ConfigView.fxml"));
        BorderPane viewRoot = loader.load();
        ConfigPresenter presenter = loader.getController();

        configureFirstStage(primaryStage, viewRoot);

        primaryStage.show();
    }

    private void configureFirstStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public void createNewSimulation(Stage primaryStage, Config config) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("newSimulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();

        primaryStage.setOnCloseRequest(event -> {
            presenter.onStop();
        });

        configureNewStage(primaryStage, viewRoot);

        primaryStage.show();
        presenter.simulationStart(config);
    }

    private void configureNewStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("newSimulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
