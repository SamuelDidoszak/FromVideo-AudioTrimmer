package org.didoszak.audiocutter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
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

        // handle help popup
        Popup popup = new Popup();
        Label label = new Label("chuj");
        label.setMinWidth(80);
        label.setMinHeight(50);
        popup.getContent().add(label);

        VBox popupBox = new VBox();
        popupBox.getStyleClass().add("popup-box");

        Text item1 = new Text("How to");
        Text item2 = new Text("Use left and right mouse button to set start and end position");
        Text item3 = new Text("Click spacebar to play audio");
        item1.setStyle("font-weigh: bold;");

        popupBox.getChildren().add(item1);
        popupBox.getChildren().add(item2);
        popupBox.getChildren().add(item3);
        popup.getContent().add(popupBox);

////        fxPopup.set.setStyle("-fx-background-color: white;");
//        fxPopup.show(audioContainer, 0, 0);

        controller.getHelpClicked().addListener((observableValue, aBoolean, t1) -> {
            // calculate help button position on the screen in px
            double x = primaryStage.getX() + controller.getHelpButton().layoutXProperty().get();
            double y = primaryStage.getY() + controller.getHelpButton().getLayoutY();

            if(!popup.isShowing())
                popup.show(primaryStage, x, y - 50);
            else
                popup.hide();
        });

    }

    /**
     * Sets up the program to handle file dropped by the user
     * @param audioPath path of the audioFile
     */
    public void fileReceivedHandler(String audioPath) {
        System.out.println(audioFile);
        if(audioFile != null)
            audioFile.removeFile();

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
        if(!extension.equals(".wav"))
            return new Ffmpeg().convertToWav(audioPath);
        return audioPath;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if(audioFile != null)
            audioFile.removeFile();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



























