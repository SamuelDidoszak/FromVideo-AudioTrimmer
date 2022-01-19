import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Gui {
    Stage stage;
    Scene scene;

    public Gui(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    /**
     * Sets the length of lengthTextField and endTextField
     * @param length time in milliseconds
     */
    public void setAudioLength(float length) {
        TextField textField = (TextField) scene.lookup("#lengthTextField");
        TextField endTextField = (TextField) scene.lookup("#endTextField");

        String time = ((int)length / 60) + ":" + (int)(length % 60);

        textField.setText(time);
        endTextField.setText(time);
    }

    /**
     * Sets the length of startTextField
     * @param length time in milliseconds
     */
    public void setStartValue(float length) {
        TextField startTextField = (TextField) scene.lookup("#startTextField");
        String time = ((int)length / 60) + ":" + (int)(length % 60);
        startTextField.setText(time);
    }

    /**
     * Sets the length of endTextField
     * @param length time in milliseconds
     */
    public void setEndValue(float length) {
        TextField endTextField = (TextField) scene.lookup("#endTextField");
        String time = ((int)length / 60) + ":" + (int)(length % 60);
        endTextField.setText(time);
    }
}
