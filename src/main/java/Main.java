import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {
    double windowWidth = 1280;
    double windowHeight = 720;

    Gui gui;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = FXMLLoader.load(getClass().getResource("/window.fxml"));
        primaryStage.setTitle("Audio Cutter");

        Scene scene = new Scene(root, windowWidth, windowHeight);

        primaryStage.setScene(scene);
        primaryStage.show();
        gui = new Gui(primaryStage, scene);

        application();
    }

    public void application() {
        handleFile();
    }

    public void handleFile() {
        Path filePath = Paths.get("C:\\Users\\Jeff\\Desktop\\programmierenMachen\\java\\AudioCutter\\test\\KaguraSuzu.wav");
        AudioFile audioFile = new AudioFile(filePath);
//        audioFile.copyToDirectory();

        AudioWaveform audioWaveform = new AudioWaveform(audioFile);
//        audioWaveform.startProcess();
        gui.setAudioLength(audioFile.getLength());
    }


    public static void main(String[] args) {
        launch(args);
    }
}



























