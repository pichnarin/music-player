package org.song.musical;

import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.collections.FXCollections;

import java.io.File;
import java.util.List;


public class MusicalController {
    private MediaPlayer mediaPlayer;
    private final ObservableList<String> playlist = FXCollections.observableArrayList();
    private RotateTransition rotateTransition;
    public int nextIndex;
    @FXML
    private ImageView rotationDisk;
    @FXML
    private Button ForwardBtn;
    @FXML
    private Button BackwardBtn;
    @FXML
    private Button playlistButton;
    @FXML
    private Slider slider;
    @FXML
    private Label minLabel;
    @FXML
    private Label timeLabel;
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
                fileChooser.setTitle("Chooser a music");
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

                                rotateTransition = new RotateTransition(Duration.seconds(20), rotationDisk);

                                rotateTransition.setByAngle(360); // Rotate by 360 degrees

                                rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely

                                rotateTransition.setAutoReverse(false); // Do not reverse the rotation

                                rotateTransition.play();

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
                            rotateTransition.stop();
                            mediaPlayer.seek(Duration.seconds(0));
                        });
                    });
                }
            }else if(event.getSource() == playlistButton){
                FileChooser playlistFile = new FileChooser();
                playlistFile.setTitle("Choose a bunch of songs");
                playlistFile.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Mp3 files", "*.mp3")
                );

                //choose a list of files
                List<File> selectedFile  = playlistFile.showOpenMultipleDialog(null);
                if(selectedFile != null){
                    for(File file : selectedFile){
                        playlist.add(file.getAbsolutePath());
                        System.out.println(file.getName());
                        System.out.println(playlist.size());
                    }
                }

                // play the first song in the playlist
                assert selectedFile != null;
                if(!selectedFile.isEmpty()){
                    playTheFuckingSong(0);
                }
            }
        }catch(Exception e){
            System.out.println(e.getCause().getMessage());
        }
    }

    //play the next song in playlist
    private void playTheFuckingSong(int indexOfSongInPlaylist){
        try{

            String songPath = playlist.get(indexOfSongInPlaylist);
//            System.out.println(songPath);

            Media media = new Media(new File(songPath).toURI().toString());

            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {

                fileNameDisplay.setText(new File(songPath).getName());

                mediaPlayer.play();

                rotateTransition = new RotateTransition(Duration.seconds(20), rotationDisk);

                rotateTransition.setByAngle(360); // Rotate by 360 degrees

                rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely

                rotateTransition.setAutoReverse(false); // Do not reverse the rotation

                rotateTransition.play();

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
                    //play the next song in the playlist
                    //keep track of the current song index when the one before is ended
                    nextIndex = (indexOfSongInPlaylist + 1) % playlist.size();
                    playTheFuckingSong(nextIndex);
                });

                ForwardBtn.setOnAction(_ -> {
                    mediaPlayer.stop();
                    rotateTransition.stop();
                    if(indexOfSongInPlaylist <= playlist.size() - 1){
                        playTheFuckingSong((indexOfSongInPlaylist + 1) % playlist.size());
//                        System.out.println(songPath);
//                        System.out.println(indexOfSongInPlaylist);
//                        System.out.println(new File(songPath));
                    }
                });

                BackwardBtn.setOnAction(_ -> {
                    mediaPlayer.stop();
                    rotateTransition.stop();
                    if(indexOfSongInPlaylist > 0) {
                        playTheFuckingSong((indexOfSongInPlaylist - 1) % playlist.size());
                    }
                });
            });

        }catch(Exception e){
            System.out.println("Error something i don't know");
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
            rotateTransition.stop();
        }
    }

    @FXML
    void OnPlayButtonClick(ActionEvent event) {
        if(event.getSource() == playButton){
            mediaPlayer.play();
            rotateTransition.play();
        }
    }

    @FXML
    void OnStopButtonClick(ActionEvent event) {
        if(event.getSource() == stopButton){
            mediaPlayer.stop();
            rotateTransition.stop();
            mediaPlayer.seek(Duration.seconds(0));
        }
    }

}
