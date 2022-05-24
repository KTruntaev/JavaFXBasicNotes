package com.example.introsamples;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SampleApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Loads the FXML file (another way to describe the graphical elements)
        FXMLLoader fxmlLoader = new FXMLLoader(SampleApplication.class.getResource("sample-view.fxml"));
        // Sets the dimensions of the window (900x600 in this case)
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        // Sets the title of the application
        stage.setTitle("Sample Application");
        stage.setScene(scene);

        // These 2 lines allow us to execute code when the app is launched by calling the setup method of the
        // Controller class
        SampleController controller = fxmlLoader.getController();
        controller.setup();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}