module org.song.musical {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires javafx.media;

    opens org.song.musical to javafx.fxml;
    exports org.song.musical;
}