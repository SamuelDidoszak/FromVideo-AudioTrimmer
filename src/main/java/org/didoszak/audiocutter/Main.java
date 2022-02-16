package org.didoszak.audiocutter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.didoszak.audiocutter.CustomClasses.TextFieldCustom;

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
            scene.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.SPACE) {
                    audioFile.playAudio(guiHandler.getStartValue(), guiHandler.getEndValue());
                }
            });
        });

        controller.getSaveFile().addListener((observableValue, aBoolean, t1) -> {
            System.out.println("SAVE FILE");
            double start = ((TextFieldCustom) scene.lookup("#startTextField")).getTimeValue();
            double end = ((TextFieldCustom) scene.lookup("#endTextField")).getTimeValue();
            audioFile.trimAudioFile(start, end);
            (scene.lookup("#audioContainer")).requestFocus();
        });

    }

    /**
     * Sets up the program to handle file dropped by the user
     * @param audioPath path of the audioFile
     */
    public void fileReceivedHandler(String audioPath) {
        Path filePath = Paths.get(audioPath);
        // convert to .wav or copy file
        Path convertedPath = Paths.get(convertToWav(audioPath));
        if(filePath.equals(convertedPath)) {
            audioFile = new AudioFile(filePath);
            audioFile.copyToDirectory();
        } else
            audioFile = new AudioFile(filePath, convertedPath);

        AudioWaveform audioWaveform = new AudioWaveform(audioFile);
        audioWaveform.startProcess();
        guiHandler.initiateLengths(audioFile.getLength());
        guiHandler.setChart();
        guiHandler.setListeners();
    }

    private String convertToWav(String audioPath) {
        String extension = audioPath.substring(audioPath.lastIndexOf("."));
        if(extension.equals(".mp4"))
            return new Ffmpeg().convertMp4ToWav(audioPath);

        return audioPath;
    }


    public static void main(String[] args) {
        launch(args);
    }
}



























