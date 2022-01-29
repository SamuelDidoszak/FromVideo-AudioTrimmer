import CustomClasses.ChartPanManagerCustom;
import CustomClasses.ChartZoomManagerCustom;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import org.gillius.jfxutils.chart.AxisConstraint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Chart {
    private LineChart chart;
    private NumberAxis xAxis;

    private ObservableList leftPointer;
    private ObservableList rightPointer;
    private ObservableList chartData;

    private ChartZoomManagerCustom zoomManager;
    private ChartPanManagerCustom panManager;

    // debugging purposes
    public int dataSize;

    public Chart(LineChart chart) {
        this.chart = chart;
        chart.getYAxis().setTickLabelsVisible(false);
        xAxis = (NumberAxis) chart.getXAxis();

        xAxis.setTickLabelFormatter(new StringConverter() {
            @Override
            public String toString(Object o) {
                Integer length = ((Double) o).intValue() / 200;
                int seconds = length % 60;
                return (length / 60) + ":" + (seconds > 9 ? seconds : "0" + seconds);
            }

            @Override
            public Integer fromString(String s) {
                return 0;
            }
        });
    }

    public void setUpZoomAndPan(Pane container) {
        if (zoomManager != null || panManager != null) {
            zoomManager.stop();
            panManager.stop();
        }
        zoomManager = new ChartZoomManagerCustom(container, new Rectangle(), chart);
        zoomManager.restrictPanning(0, dataSize);
        zoomManager.setZoomAnimated(false);
        zoomManager.setMouseWheelAxisConstraintStrategy(chartInputContext -> AxisConstraint.Horizontal);
        zoomManager.start();

        //Panning works via either secondary (right) mouse or primary with ctrl held down
        panManager = new ChartPanManagerCustom(chart);
        panManager.restrictPanning(0, dataSize);
        panManager.setMouseFilter(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.SECONDARY || mouseEvent.getButton() == MouseButton.PRIMARY) {
                //let it through
            } else {
                mouseEvent.consume();
            }
        });
        panManager.setAxisConstraintStrategy(chartInputContext -> AxisConstraint.Horizontal);
        panManager.start();

//        JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(chart);
    }

    public boolean wasDragged() {
        return panManager.wasDragged();
    }

    /**
     * Sets the chart data values
     */
    public void setChartValues() {
        try {
            File dataSrc = new File(getClass().getResource("full.json").toString().substring(6));
            Scanner scanner = new Scanner(dataSrc);
            String dataString = scanner.next();
            dataString = dataString.substring(dataString.indexOf(":[") + 2, dataString.length() -2);
            ArrayList<XYChart.Data<Integer, Integer>> data = new ArrayList<>();
            int i = 0;
            for(String val : dataString.split(",")) {
                data.add(new XYChart.Data(i, Integer.valueOf(val)));
                i++;
            }
            System.out.println("size: " + data.size());
            dataSize = data.size() - 1;


            // Deletes the empty space on the right
            xAxis.setAutoRanging(false);
            xAxis.setUpperBound(data.size());
            xAxis.setLowerBound(0);
            xAxis.setTickUnit(data.size() / 20);

            // add positional markers
            XYChart.Series line = new XYChart.Series();
            line.getData().add(new XYChart.Data(0, -100));
            line.getData().add(new XYChart.Data(0, 100));

            XYChart.Series line2 = new XYChart.Series();
            line2.getData().add(new XYChart.Data(data.size() -1, -100));
            line2.getData().add(new XYChart.Data(data.size() - 1, 100));

            Platform.runLater(() -> {
                XYChart.Series series = new XYChart.Series();
                series.getData().addAll(data);
                // deletes previous data and sets new
                chartData = chart.getData();
                chartData.setAll(series);
                chartData.add(line);
                chartData.add(line2);
                leftPointer = ((XYChart.Series)chartData.get(1)).getData();
                rightPointer = ((XYChart.Series)chartData.get(2)).getData();
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setLeftLine(double seconds) {
        Platform.runLater(() -> {
            leftPointer.set(0, new XYChart.Data(seconds, -100));
            leftPointer.set(1, new XYChart.Data(seconds, 100));
        });
    }

    public void setRightLine(double seconds) {
        Platform.runLater(() -> {
            rightPointer.set(0, new XYChart.Data(seconds, -100));
            rightPointer.set(1, new XYChart.Data(seconds, 100));
        });
    }

    public LineChart getChart() {
        return chart;
    }

    public NumberAxis getxAxis() {
        return xAxis;
    }
}
