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
//        scene.setOnScroll(scrollEvent -> rescaleImage(scrollEvent.getX(), scrollEvent.getY(), scrollEvent.getDeltaY()));

        mainChart.getChart().setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getX() < 27 || mouseEvent.getX() >= mainChart.getChart().getXAxis().getLayoutBounds().getWidth())
                return;

            double audioLength = ((TextFieldCustom) scene.lookup("#lengthTextField")).getTimeValue();
            double length = audioLength / mainChart.getChart().getXAxis().getLayoutBounds().getWidth() * (mouseEvent.getX() - 26);
            double start = ((TextFieldCustom) scene.lookup("#startTextField")).getTimeValue();
            double end = ((TextFieldCustom) scene.lookup("#endTextField")).getTimeValue();

            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (length > end) {
                    setEndValue(length);
                    mainChart.setRightLine(length);
                    setStartValue(end);
                    mainChart.setLeftLine(end);
                    start = end;
                    end = length;
                    mainChart.setRightLine(length);
                } else {
                    setStartValue(length);
                    mainChart.setLeftLine(length);
                    start = length;
                }
            }
            else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                if(length < start) {
                    setStartValue(length);
                    mainChart.setLeftLine(length);
                    setEndValue(start);
                    mainChart.setRightLine(start);
                    end = start;
                    start = length;
                } else {
                    setEndValue(length);
                    mainChart.setRightLine(length);
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

//    public void rescaleImage(double x, double y, double delta) {
//        if(!chartCreated) {
//            return;
//        }
//        Double currentScale = mainChart.getScaleX();
//
//        if(delta >= 0) {
//            if (currentScale >= 50)
//                return;
//            mainChart.setScaleX(currentScale + (currentScale * 0.1));
//        }
//        else {
//            if (currentScale <= 1) {
//                mainChart.setScaleX(1);
//                return;
//            }
//            mainChart.setScaleX(currentScale - (currentScale * 0.1));
//        }
//    }

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
