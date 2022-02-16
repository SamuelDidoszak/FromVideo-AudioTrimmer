package org.didoszak.audiocutter.CustomClasses;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.gillius.jfxutils.EventHandlerManager;
import org.gillius.jfxutils.chart.*;

/**
 * Custom ChartZoomManager Class.
 * Supports zooming limit and is optimized for the use in my program
 */
public class ChartZoomManagerCustom {
    public static final EventHandler<MouseEvent> DEFAULT_FILTER = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                mouseEvent.consume();
            }

        }
    };
    private final SimpleDoubleProperty rectX = new SimpleDoubleProperty();
    private final SimpleDoubleProperty rectY = new SimpleDoubleProperty();
    private final SimpleBooleanProperty selecting = new SimpleBooleanProperty(false);
    private final DoubleProperty zoomDurationMillis = new SimpleDoubleProperty(750.0D);
    private final BooleanProperty zoomAnimated = new SimpleBooleanProperty(true);
    private final BooleanProperty mouseWheelZoomAllowed = new SimpleBooleanProperty(true);
    private AxisConstraint zoomMode;
    private AxisConstraintStrategy axisConstraintStrategy;
    private AxisConstraintStrategy mouseWheelAxisConstraintStrategy;
    private EventHandler<? super MouseEvent> mouseFilter;
    private final EventHandlerManager handlerManager;
    private final Rectangle selectRect;
    private final ValueAxis<?> xAxis;
    private final ValueAxis<?> yAxis;
    private final XYChartInfo chartInfo;
    private final Timeline zoomAnimation;

    private double lowerBound;
    private double upperBound;

    public ChartZoomManagerCustom(Pane chartPane, Rectangle selectRect, XYChart<?, ?> chart) {
        this.zoomMode = AxisConstraint.None;
        this.axisConstraintStrategy = AxisConstraintStrategies.getIgnoreOutsideChart();
        this.mouseWheelAxisConstraintStrategy = AxisConstraintStrategies.getDefault();
        this.mouseFilter = DEFAULT_FILTER;
        this.zoomAnimation = new Timeline();
        this.selectRect = selectRect;
        this.xAxis = (ValueAxis)chart.getXAxis();
        this.yAxis = (ValueAxis)chart.getYAxis();
        this.chartInfo = new XYChartInfo(chart, chartPane);
        this.handlerManager = new EventHandlerManager(chartPane);
        this.handlerManager.addEventHandler(false, ScrollEvent.ANY, new ChartZoomManagerCustom.MouseWheelZoomHandler());
    }

    public AxisConstraintStrategy getAxisConstraintStrategy() {
        return this.axisConstraintStrategy;
    }

    public void setAxisConstraintStrategy(AxisConstraintStrategy axisConstraintStrategy) {
        this.axisConstraintStrategy = axisConstraintStrategy;
    }

    public AxisConstraintStrategy getMouseWheelAxisConstraintStrategy() {
        return this.mouseWheelAxisConstraintStrategy;
    }

    public void setMouseWheelAxisConstraintStrategy(AxisConstraintStrategy mouseWheelAxisConstraintStrategy) {
        this.mouseWheelAxisConstraintStrategy = mouseWheelAxisConstraintStrategy;
    }

    public boolean isZoomAnimated() {
        return this.zoomAnimated.get();
    }

    public BooleanProperty zoomAnimatedProperty() {
        return this.zoomAnimated;
    }

    public void setZoomAnimated(boolean zoomAnimated) {
        this.zoomAnimated.set(zoomAnimated);
    }

    public double getZoomDurationMillis() {
        return this.zoomDurationMillis.get();
    }

    public DoubleProperty zoomDurationMillisProperty() {
        return this.zoomDurationMillis;
    }

    public void setZoomDurationMillis(double zoomDurationMillis) {
        this.zoomDurationMillis.set(zoomDurationMillis);
    }

    public boolean isMouseWheelZoomAllowed() {
        return this.mouseWheelZoomAllowed.get();
    }

    public BooleanProperty mouseWheelZoomAllowedProperty() {
        return this.mouseWheelZoomAllowed;
    }

    public void setMouseWheelZoomAllowed(boolean allowed) {
        this.mouseWheelZoomAllowed.set(allowed);
    }

    public EventHandler<? super MouseEvent> getMouseFilter() {
        return this.mouseFilter;
    }

    public void setMouseFilter(EventHandler<? super MouseEvent> mouseFilter) {
        this.mouseFilter = mouseFilter;
    }

    public void start() {
        this.handlerManager.addAllHandlers();
        this.selectRect.widthProperty().bind(this.rectX.subtract(this.selectRect.translateXProperty()));
        this.selectRect.heightProperty().bind(this.rectY.subtract(this.selectRect.translateYProperty()));
        this.selectRect.visibleProperty().bind(this.selecting);
    }

    public void stop() {
        this.handlerManager.removeAllHandlers();
        this.selecting.set(false);
        this.selectRect.widthProperty().unbind();
        this.selectRect.heightProperty().unbind();
        this.selectRect.visibleProperty().unbind();
    }

    private boolean passesFilter(MouseEvent event) {
        if (this.mouseFilter != null) {
            MouseEvent cloned = (MouseEvent)event.clone();
            this.mouseFilter.handle(cloned);
            if (cloned.isConsumed()) {
                return false;
            }
        }

        return true;
    }

    private static double getBalance(double val, double min, double max) {
        if (val <= min) {
            return 0.0D;
        } else {
            return val >= max ? 1.0D : (val - min) / (max - min);
        }
    }


    /**
     * Restrict bounds of zooming.
     * @param leftBound
     * @param rightBound
     */
    public void restrictPanning(double leftBound, double rightBound) {
        this.lowerBound = leftBound;
        this.upperBound = rightBound;
    }

    private class MouseWheelZoomHandler implements EventHandler<ScrollEvent> {
        private boolean ignoring;

        private MouseWheelZoomHandler() {
            this.ignoring = false;
        }

        public void handle(ScrollEvent event) {
            EventType<? extends Event> eventType = event.getEventType();
            if (eventType == ScrollEvent.SCROLL_STARTED) {
                this.ignoring = true;
            } else if (eventType == ScrollEvent.SCROLL_FINISHED) {
                this.ignoring = false;
            } else if (eventType == ScrollEvent.SCROLL && !this.ignoring && !event.isInertia() && event.getDeltaY() != 0.0D && event.getTouchCount() == 0) {
                double eventX = event.getX();
                double eventY = event.getY();
                DefaultChartInputContext context = new DefaultChartInputContext(ChartZoomManagerCustom.this.chartInfo, eventX, eventY);
                AxisConstraint zoomMode = ChartZoomManagerCustom.this.mouseWheelAxisConstraintStrategy.getConstraint(context);

                ChartZoomManagerCustom.this.zoomAnimation.stop();
                Point2D dataCoords = ChartZoomManagerCustom.this.chartInfo.getDataCoordinates(eventX, eventY);
                double xZoomBalance = ChartZoomManagerCustom.getBalance(dataCoords.getX(), ChartZoomManagerCustom.this.xAxis.getLowerBound(), ChartZoomManagerCustom.this.xAxis.getUpperBound());
                double direction = -Math.signum(event.getDeltaY());
                double zoomAmount = 0.2D * direction;
                double yZoomDelta;
                if (zoomMode == AxisConstraint.Horizontal) {
                    yZoomDelta = (ChartZoomManagerCustom.this.xAxis.getUpperBound() - ChartZoomManagerCustom.this.xAxis.getLowerBound()) * zoomAmount;

                    double tempLowerBound = ChartZoomManagerCustom.this.xAxis.getLowerBound() - yZoomDelta * xZoomBalance;
                    double tempUpperBound = ChartZoomManagerCustom.this.xAxis.getUpperBound() + yZoomDelta * (1.0D - xZoomBalance);

                    // restricts zooming area
                    if(direction == 1) {
                        if(tempLowerBound <= lowerBound)
                            tempLowerBound = lowerBound;
                        if(tempUpperBound >= upperBound)
                            tempUpperBound  =upperBound;
                    }

                    ChartZoomManagerCustom.this.xAxis.setLowerBound(tempLowerBound);
                    ChartZoomManagerCustom.this.xAxis.setUpperBound(tempUpperBound);
                }
            }

        }
    }
}
