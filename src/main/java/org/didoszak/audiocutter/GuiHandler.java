package org.didoszak.audiocutter;

import org.didoszak.audiocutter.CustomClasses.TextFieldCustom;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class GuiHandler {
    private Scene scene;

    private VBox container;
    private Chart mainChart;

    boolean chartCreated;

    public GuiHandler(Scene scene) {
        this.scene = scene;

        container = (VBox) scene.lookup("#audioContainer");
        mainChart = new Chart((LineChart) scene.lookup("#mainChart"));

        container.getChildren().remove(mainChart.getChart());
        container.requestFocus();

        setCustomTextField();
    }

    /**
     * Sets up the listeners for user input
     */
    public void setListeners() {
        mainChart.setUpZoomAndPan(container);

        mainChart.getChart().setOnMouseClicked(mouseEvent -> {
            if(mainChart.wasDragged())
                return;

            if(mouseEvent.getX() < 27 || mouseEvent.getX() >= mainChart.getChart().getXAxis().getLayoutBounds().getWidth() + 27)
                return;

            double audioLength = ((TextFieldCustom) scene.lookup("#lengthTextField")).getTimeValue();

            // This way of determining length is 0,002265% inaccurate, which adds an additional second every 7:22 minutes
            double lengthFromChart = mainChart.getxAxis().getLowerBound() +
                    ((mainChart.getxAxis().getUpperBound() - mainChart.getxAxis().getLowerBound()) /
                            mainChart.getChart().getXAxis().getLayoutBounds().getWidth() * (mouseEvent.getX() - 26));
            // Cast the slightly inaccurate length onto actual length
            double length = (lengthFromChart / mainChart.dataSize) * audioLength;
            double start = ((TextFieldCustom) scene.lookup("#startTextField")).getTimeValue();
            double end = ((TextFieldCustom) scene.lookup("#endTextField")).getTimeValue();

            System.out.println(audioLength + "\t" + length + "\t|\t" + audioLength * 200 + "\t" + mainChart.dataSize);

            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (length > end) {
                    setEndValue(length);
                    mainChart.setRightLine(lengthFromChart);
                    setStartValue(end);
                    // Cast the actual length onto slightly inaccurate chart data length
                    mainChart.setLeftLine((end * mainChart.dataSize) / audioLength);
                    start = end;
                    end = length;
                } else {
                    setStartValue(length);
                    mainChart.setLeftLine(lengthFromChart);
                    start = length;
                }
            }
            else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                if(length < start) {
                    setStartValue(length);
                    mainChart.setLeftLine(lengthFromChart);
                    setEndValue(start);
                    // Cast the actual length onto slightly inaccurate chart data length
                    mainChart.setRightLine((start * mainChart.dataSize) / audioLength);
                    end = start;
                    start = length;
                } else {
                    setEndValue(length);
                    mainChart.setRightLine(lengthFromChart);
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
    public void setChart() {
        if(!chartCreated) {
            container.getChildren().add(mainChart.getChart());
            chartCreated = true;
        }
        mainChart.setChartValues();
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

    public double getStartValue() {
        return ((TextFieldCustom) scene.lookup("#startTextField")).getTimeValue();
    }

    public double getEndValue() {
        return ((TextFieldCustom) scene.lookup("#endTextField")).getTimeValue();
    }
}


