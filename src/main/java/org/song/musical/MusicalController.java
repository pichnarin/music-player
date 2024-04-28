package org.song.musical;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileNotFoundException;

public class MusicalController {

    private MediaPlayer mediaPlayer;

    @FXML
    private Label fileNameDisplay;

    @FXML
    private Button pauseButton;

    @FXML
    private Button playButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button OnChooseMusicButton;

    @FXML
    public void OnChooseMusicButton(ActionEvent event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chooser your music");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Mp3 files", "*.mp3")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null){
            String fullPathFileName = file.toURI().toString();
            Media media = new Media(fullPathFileName);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> fileNameDisplay.setText(file.getName()));
        }
    }

    @FXML
    void OnPauseButtonClick(ActionEvent event) {
        mediaPlayer.pause();
    }

    @FXML
    void OnPlayButtonClick(ActionEvent event) {
        mediaPlayer.play();
    }

    @FXML
    void OnStopButtonClick(ActionEvent event) {
        mediaPlayer.stop();
        fileNameDisplay.setText("none.mp3");
    }

}
