package org.song.musical;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.List;

public class MP3PlaylistPlayer extends Application {
    private ObservableList<String> playlist = FXCollections.observableArrayList();
    private ListView<String> playlistView = new ListView<>(playlist);
    private Button addBtn = new Button("Add MP3");
    private Button playBtn = new Button("Play");
    private MediaPlayer mediaPlayer;
    private int currentTrackIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        addBtn.setOnAction(event -> addMP3Files());
        playBtn.setOnAction(event -> playPlaylist());

        VBox root = new VBox(addBtn, playlistView, playBtn);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MP3 Playlist Player");
        primaryStage.show();
    }

    private void addMP3Files() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose MP3 Files");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")
        );
        // Allow user to select multiple files
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                playlist.add(file.getPath());
            }
        }
    }

    private void playPlaylist() {
        if (playlist.isEmpty()) {
            return;
        }
        playNextTrack();
    }

    private void playNextTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (currentTrackIndex < playlist.size()) {
            String trackPath = playlist.get(currentTrackIndex);
            Media media = new Media(new File(trackPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(this::playNextTrack);
            mediaPlayer.play();
            currentTrackIndex++;
        } else {
            currentTrackIndex = 0; // Reset index to play from the beginning
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
