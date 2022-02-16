package org.didoszak.audiocutter.CustomClasses;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import javafx.event.EventHandler;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import org.gillius.jfxutils.EventHandlerManager;
import org.gillius.jfxutils.chart.*;

/**
 * Custom ChartPanManager Class.
 * Supports panning limit and is optimized for the use in my program
 */
public class ChartPanManagerCustom {
    public static final EventHandler<MouseEvent> DEFAULT_FILTER;
    private final EventHandlerManager handlerManager;
    private final ValueAxis<?> xAxis;
    private final ValueAxis<?> yAxis;
    private final XYChartInfo chartInfo;
    private AxisConstraint panMode;
    private AxisConstraintStrategy axisConstraintStrategy;
    private EventHandler<? super MouseEvent> mouseFilter;
    private boolean dragging;
    private boolean wasXAnimated;
    private boolean wasYAnimated;
    private double lastX;
    private double lastY;

    private double lowerBound;
    private double upperBound;
    private boolean wasDragged;

    public ChartPanManagerCustom(XYChart<?, ?> chart) {
        this.panMode = AxisConstraint.None;
        this.axisConstraintStrategy = AxisConstraintStrategies.getDefault();
        this.mouseFilter = DEFAULT_FILTER;
        this.dragging = false;
        this.handlerManager = new EventHandlerManager(chart);
        this.xAxis = (ValueAxis)chart.getXAxis();
        this.yAxis = (ValueAxis)chart.getYAxis();
        this.chartInfo = new XYChartInfo(chart, chart);
        this.handlerManager.addEventHandler(false, MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if (ChartPanManagerCustom.this.passesFilter(mouseEvent)) {
                    ChartPanManagerCustom.this.startDrag(mouseEvent);
                }

            }
        });
        this.handlerManager.addEventHandler(false, MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                ChartPanManagerCustom.this.drag(mouseEvent);
            }
        });
        this.handlerManager.addEventHandler(false, MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                ChartPanManagerCustom.this.release();
            }
        });

        wasDragged = false;
    }

    public AxisConstraintStrategy getAxisConstraintStrategy() {
        return this.axisConstraintStrategy;
    }

    public void setAxisConstraintStrategy(AxisConstraintStrategy axisConstraintStrategy) {
        this.axisConstraintStrategy = axisConstraintStrategy;
    }

    public EventHandler<? super MouseEvent> getMouseFilter() {
        return this.mouseFilter;
    }

    public void setMouseFilter(EventHandler<? super MouseEvent> mouseFilter) {
        this.mouseFilter = mouseFilter;
    }

    public void start() {
        this.handlerManager.addAllHandlers();
    }

    public void stop() {
        this.handlerManager.removeAllHandlers();
        this.release();
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

    private void startDrag(MouseEvent event) {
        DefaultChartInputContext context = new DefaultChartInputContext(this.chartInfo, event.getX(), event.getY());
        this.panMode = this.axisConstraintStrategy.getConstraint(context);
        if (this.panMode != AxisConstraint.None) {
            this.lastX = event.getX();
            this.lastY = event.getY();
            this.dragging = true;
        }

    }

    /**
     * Restrict bounds of panning.
     * @param leftBound
     * @param rightBound
     */
    public void restrictPanning(double leftBound, double rightBound) {
        this.lowerBound = leftBound;
        this.upperBound = rightBound;
    }

    /**
     * Method modified to support dragging bounds
     */
    private void drag(MouseEvent event) {
        if (this.dragging) {
            double dY;
            if (this.panMode == AxisConstraint.Both || this.panMode == AxisConstraint.Horizontal) {
                dY = (event.getX() - this.lastX) / -this.xAxis.getScale();
                this.lastX = event.getX();
                // restrict dragging
                if(this.xAxis.getLowerBound() > lowerBound)
                    this.xAxis.setLowerBound(this.xAxis.getLowerBound() + dY);
                else
                    this.xAxis.setLowerBound(lowerBound);

                if(this.xAxis.getUpperBound() < upperBound)
                    this.xAxis.setUpperBound(this.xAxis.getUpperBound() + dY);
                else
                    this.xAxis.setUpperBound(upperBound);
            }
        }
    }

    private void release() {
        if (this.dragging) {
            this.dragging = false;
            this.xAxis.setAnimated(this.wasXAnimated);
            this.yAxis.setAnimated(this.wasYAnimated);

            wasDragged = true;
        }
    }

    /**
     * @return true if dragging event was called
     */
    public boolean wasDragged() {
        boolean temp = wasDragged;
        wasDragged = false;
        return temp;
    }

    static {
        DEFAULT_FILTER = ChartZoomManagerCustom.DEFAULT_FILTER;
    }
}
