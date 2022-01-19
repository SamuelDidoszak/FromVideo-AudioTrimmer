import javafx.application.Application;
import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {
    private FXMLLoader loader;
    private Gui gui;
    boolean imageCreated;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader(getClass().getResource("/window.fxml"));
        BorderPane root = loader.load();
        primaryStage.setTitle("Audio Cutter");

        Scene scene = new Scene(root, 1280, 720);

        primaryStage.setScene(scene);
        primaryStage.show();
        gui = new Gui(primaryStage, scene);

        imageCreated = false;

        application();
    }

    public void application() {
        Controller controller = loader.getController();
        controller.getFilePath().addListener((observableValue, s, t1) -> {
            handleFile(observableValue.getValue());
        });


    }

    public void handleFile(String string) {
        Path filePath = Paths.get(string);
        AudioFile audioFile = new AudioFile(filePath);
        audioFile.copyToDirectory();

        AudioWaveform audioWaveform = new AudioWaveform(audioFile);
        audioWaveform.startProcess();
        gui.setAudioLength(audioFile.getLength());
        if(!imageCreated) {
            gui.createImageView();
            imageCreated = true;
        }
        gui.setImage();

    }


    public static void main(String[] args) {
        launch(args);
    }
}



























