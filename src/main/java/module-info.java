module org.didoszak.audiocutter {
    requires javafx.media;
    requires java.desktop;
    requires javafx.fxml;
    requires javafx.controls;
    requires jfxutils;
    requires com.jfoenix;

    exports org.didoszak.audiocutter to javafx.graphics, javafx.fxml;
    opens org.didoszak.audiocutter to javafx.fxml;
}