package org.didoszak.audiocutter;

import org.didoszak.audiocutter.CustomClasses.TextFieldCustom;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
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
        guiHandler = new GuiHandler(scene);


        Controller controller = loader.getController();
        controller.getFilePath().addListener((observableValue, s, t1) -> {
            fileReceivedHandler(observableValue.getValue());
        });

        controller.getSaveFile().addListener((observableValue, aBoolean, t1) -> {
            System.out.println("SAVE FILE");
            double start = ((TextFieldCustom) scene.lookup("#startTextField")).getTimeValue();
            double end = ((TextFieldCustom) scene.lookup("#endTextField")).getTimeValue();
            audioFile.trimAudioFile(start, end);
        });

    }

    /**
     * Sets up the program to handle file dropped by the user
     * @param audioPath path of the audioFile
     */
    public void fileReceivedHandler(String audioPath) {
        System.out.println("oh god");
        Path filePath = Paths.get(audioPath);
        audioFile = new AudioFile(filePath);
        audioFile.copyToDirectory();

        AudioWaveform audioWaveform = new AudioWaveform(audioFile);
        audioWaveform.startProcess();
        guiHandler.initiateLengths(audioFile.getLength());
        guiHandler.setChart();
        guiHandler.setListeners();

        MediaPlayer player = new MediaPlayer(new Media(new File(audioFile.getTempPath()).toURI().toString()));
        System.out.println("playing audio");
        player.play();
    }


    public static void main(String[] args) {
        launch(args);
    }
}



























