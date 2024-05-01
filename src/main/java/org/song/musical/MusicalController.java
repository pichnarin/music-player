package org.song.musical;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;


public class MusicalController {
    @FXML
    private Slider slider;

    @FXML
    private Label minLabel;

    @FXML
    private Label timeLabel;

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
    public void OnChooseMusicButton(ActionEvent event){
        try{
            if(event.getSource() == OnChooseMusicButton){

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

                    mediaPlayer.setOnReady(() -> {

                                fileNameDisplay.setText(file.getName());

                                mediaPlayer.play();

                                slider.setMin(0);

                                slider.setMax(mediaPlayer.getTotalDuration().toSeconds());

                                minLabel.textProperty().bind(Bindings.createStringBinding(() ->

                                        timeFormat(mediaPlayer.getCurrentTime().toSeconds()),

                                        mediaPlayer.currentTimeProperty()
                                ));

                                timeLabel.textProperty().bind(Bindings.createStringBinding(() ->

                                                    timeFormat(mediaPlayer.getTotalDuration().toSeconds()),

                                                    mediaPlayer.totalDurationProperty()

                                ));

                        slider.valueProperty().addListener((_, _, newValue) -> {
                            if (slider.isValueChanging()) {
                                mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                            }
                        });

                        mediaPlayer.currentTimeProperty().addListener((_, _, _) -> {
                            if(!slider.isValueChanging()){
                                slider.setValue(mediaPlayer.getCurrentTime().toSeconds());
                            }
                        });

                        mediaPlayer.setOnEndOfMedia(() ->{
                            mediaPlayer.stop();
                            mediaPlayer.seek(Duration.seconds(0));
                        });
                    });
                }
            }
        }catch(Exception e){

            System.out.println(e.getCause().getMessage());

        }
    }

    private String timeFormat(double seconds){
        int minus = (int)seconds / 60;
        int remainingSecond = (int)seconds % 60;
        return String.format("%02d:%02d", minus, remainingSecond);
    }

    @FXML
    void OnPauseButtonClick(ActionEvent event) {
        if(event.getSource() == pauseButton){
            mediaPlayer.pause();
        }
    }

    @FXML
    void OnPlayButtonClick(ActionEvent event) {
        if(event.getSource() == playButton){
            mediaPlayer.play();
        }
    }

    @FXML
    void OnStopButtonClick(ActionEvent event) {
        if(event.getSource() == stopButton){
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.seconds(0));
        }
    }

}
