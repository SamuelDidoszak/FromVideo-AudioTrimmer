import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {
    private FXMLLoader loader;
    private GuiHandler guiHandler;
    private AudioFile audioFile;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader(getClass().getResource("/window.fxml"));
        BorderPane root = loader.load();
        primaryStage.setTitle("Audio Cutter");

        Scene scene = new Scene(root, 1280, 720);

        primaryStage.setScene(scene);
        primaryStage.show();
        guiHandler = new GuiHandler(primaryStage, scene);


        Controller controller = loader.getController();
        controller.getFilePath().addListener((observableValue, s, t1) -> {
            fileRecievedHandler(observableValue.getValue());
        });

        controller.getSaveFile().addListener((observableValue, aBoolean, t1) -> {
            System.out.println("SAVE FILE");
            double start = ((TextFieldCustom) scene.lookup("#startTextField")).getTimeValue();
            double end = ((TextFieldCustom) scene.lookup("#endTextField")).getTimeValue();
            audioFile.trimAudioFile(start, end);
        });

    }

    public void fileRecievedHandler(String string) {
        Path filePath = Paths.get(string);
        audioFile = new AudioFile(filePath);
        audioFile.copyToDirectory();

        AudioWaveform audioWaveform = new AudioWaveform(audioFile);
        audioWaveform.startProcess();
        guiHandler.initiateLengths(audioFile.getLength());
        guiHandler.createImageView();
        guiHandler.setImage();
        guiHandler.setListeners();
    }


    public static void main(String[] args) {
        launch(args);
    }
}



























