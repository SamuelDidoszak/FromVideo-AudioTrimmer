import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.gillius.jfxutils.chart.StableTicksAxis;

import javax.sound.sampled.Line;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;


public class GuiHandler {
    private Stage stage;
    private Scene scene;

    private VBox container;
    private LineChart mainChart;

    boolean chartCreated;

    public GuiHandler(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;

        System.out.println("bruh");
        container = (VBox) scene.lookup("#audioContainer");
        mainChart = (LineChart) scene.lookup("#mainChart");
        mainChart.getYAxis().setTickLabelsVisible(false);

        container.getChildren().remove(mainChart);
        container.requestFocus();

        setCustomTextField();
    }

    public void setListeners() {
//        scene.setOnScroll(scrollEvent -> rescaleImage(scrollEvent.getX(), scrollEvent.getY(), scrollEvent.getDeltaY()));

        mainChart.setOnMouseClicked(mouseEvent -> {
            double audioLength = ((TextFieldCustom) scene.lookup("#lengthTextField")).getTimeValue();
            double length = audioLength / container.getWidth() * mouseEvent.getX();
            double start = ((TextFieldCustom) scene.lookup("#startTextField")).getTimeValue();
            double end = ((TextFieldCustom) scene.lookup("#endTextField")).getTimeValue();

            if(mouseEvent.getButton() == MouseButton.PRIMARY)
                if(length > end) {
                    setEndValue(length);
                    setStartValue(end);
                    start = end;
                    end = length;
                } else {
                    setStartValue(length);
                    start = length;
                }
            else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                if(length < start) {
                    setStartValue(length);
                    setEndValue(start);
                    end = start;
                    start = length;
                } else {
                    setEndValue(length);
                    end = length;
                }
            }
            setlengthValue(end - start);
        });
    }

    private void setCustomTextField() {
        GridPane gridPane = (GridPane) scene.lookup("#valueContainer");

        TextFieldCustom customStart = new TextFieldCustom();
        customStart.setId("startTextField");
        customStart.getStyleClass().add("text-field");
        customStart.setPromptText("00:00");

        TextFieldCustom customEnd = new TextFieldCustom();
        customEnd.setId("endTextField");
        customEnd.getStyleClass().add("text-field");
        customEnd.setPromptText("00:00");

        TextFieldCustom customLength = new TextFieldCustom();
        customLength.setId("lengthTextField");
        customLength.getStyleClass().add("text-field");
        customLength.setPromptText("00:00");
        customLength.setDisable(true);

        gridPane.add(customStart, 0, 1);
        gridPane.add(customEnd, 1, 1);
        gridPane.add(customLength, 2, 1);
    }

    /**
     * Initiates the imageView only one time.
     * Recreates it from FXML, because if node is added programmatically, it doesn't resize with borderpane
     */
    public void createChart() {
        if(chartCreated) {
            return;
        }

        container.getChildren().add(mainChart);
        chartCreated = true;
    }

    /**
     * Sets the imageView image
     */
    public void setImage() {
        try {
            LineChart mainChart = (LineChart) scene.lookup("#mainChart");
            File dataSrc = new File(getClass().getResource("full.json").toString().substring(6));
            Scanner scanner = new Scanner(dataSrc);
            String dataString = scanner.next();
            dataString = dataString.substring(dataString.indexOf(":[") + 2, dataString.length() -2);
            ArrayList<Integer> data = new ArrayList<>();
            for(String val : dataString.split(",")) {
                data.add(Integer.valueOf(val));
            }

            XYChart.Series series = new XYChart.Series();

            System.out.println(data.size());

            for(int i = 0; i < data.size(); i++) {
                series.getData().add(new XYChart.Data(i, data.get(i)));
            }
//
            mainChart.getData().add(series);






        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



//        XYChart.Series series = new XYChart.Series();
//        series.getData().addAll()
    }

    public void rescaleImage(double x, double y, double delta) {
        if(!chartCreated) {
            return;
        }
        Double currentScale = mainChart.getScaleX();

        if(delta >= 0) {
            if (currentScale >= 50)
                return;
            mainChart.setScaleX(currentScale + (currentScale * 0.1));
        }
        else {
            if (currentScale <= 1) {
                mainChart.setScaleX(1);
                return;
            }
            mainChart.setScaleX(currentScale - (currentScale * 0.1));
        }
    }

    /**
     * Initiates the length of lengthTextField and endTextField
     * @param length time in seconds with millisecond decimal part
     */
    public void initiateLengths(float length) {
        TextField textField = (TextField) scene.lookup("#lengthTextField");
        TextField endTextField = (TextField) scene.lookup("#endTextField");

        int seconds = (int)(length % 60);
        String time = ((int)length / 60) + ":" + (seconds > 9 ? seconds : "0" + seconds);

        textField.setText(time);
        endTextField.setText(time);

        ((TextFieldCustom) scene.lookup("#lengthTextField")).setTimeValue(length);
        ((TextFieldCustom) scene.lookup("#endTextField")).setTimeValue(length);
    }

    /**
     * Sets the length of startTextField
     * @param length time in milliseconds
     */
    public void setStartValue(double length) {
        TextField startTextField = (TextField) scene.lookup("#startTextField");
        int seconds = (int)(length % 60);
        String time = ((int)length / 60) + ":" + (seconds > 9 ? seconds : "0" + seconds);
        startTextField.setText(time);

        ((TextFieldCustom) scene.lookup("#startTextField")).setTimeValue(length);
    }

    /**
     * Sets the length of endTextField
     * @param length time in milliseconds
     */
    public void setEndValue(double length) {
        TextField endTextField = (TextField) scene.lookup("#endTextField");
        int seconds = (int)(length % 60);
        String time = ((int)length / 60) + ":" + (seconds > 9 ? seconds : "0" + seconds);
        endTextField.setText(time);

        ((TextFieldCustom) scene.lookup("#endTextField")).setTimeValue(length);
    }


    /**
     * Sets the length of lengthTextField
     * @param length time in milliseconds
     */
    public void setlengthValue(double length) {
        TextField textField = (TextField) scene.lookup("#lengthTextField");

        int seconds = (int)(length % 60);
        String time = ((int)length / 60) + ":" + (seconds > 9 ? seconds : "0" + seconds);

        textField.setText(time);
    }
}
