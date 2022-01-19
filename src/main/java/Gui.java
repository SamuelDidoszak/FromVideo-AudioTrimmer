import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui {
    private Stage stage;
    private Scene scene;

    public Gui(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    public void createImageView() {
        VBox container = (VBox) scene.lookup("#audioContainer");

        ImageView imageView = new ImageView();
        imageView.setId("mainImage");
        imageView.setFitWidth(container.getWidth());
        imageView.setFitHeight(container.getHeight() * 0.9);

        container.getChildren().add(imageView);
    }

    public void setImage() {
        ImageView imageView = (ImageView) scene.lookup("#mainImage");
        String res = getClass().getResource("full.png").toString().substring(6);
        imageView.setImage(new Image(res));
    }

    /**
     * Sets the length of lengthTextField and endTextField
     * @param length time in milliseconds
     */
    public void setAudioLength(float length) {
        TextField textField = (TextField) scene.lookup("#lengthTextField");
        TextField endTextField = (TextField) scene.lookup("#endTextField");

        int seconds = (int)(length % 60);
        String time = ((int)length / 60) + ":" + (seconds > 9 ? seconds : "0" + seconds);

        textField.setText(time);
        endTextField.setText(time);
    }

    /**
     * Sets the length of startTextField
     * @param length time in milliseconds
     */
    public void setStartValue(float length) {
        TextField startTextField = (TextField) scene.lookup("#startTextField");
        int seconds = (int)(length % 60);
        String time = ((int)length / 60) + ":" + (seconds > 9 ? seconds : "0" + seconds);
        startTextField.setText(time);
    }

    /**
     * Sets the length of endTextField
     * @param length time in milliseconds
     */
    public void setEndValue(float length) {
        TextField endTextField = (TextField) scene.lookup("#endTextField");
        int seconds = (int)(length % 60);
        String time = ((int)length / 60) + ":" + (seconds > 9 ? seconds : "0" + seconds);
        endTextField.setText(time);
    }
}
