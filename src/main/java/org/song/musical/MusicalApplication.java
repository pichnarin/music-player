package org.song.musical;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MusicalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MusicalApplication.class.getResource("musical-startup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 408, 583);
        stage.setTitle("The Musical Player");
        stage.centerOnScreen();
        stage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/1834/1834342.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}