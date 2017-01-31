package edu.mit.lcp;

import java.awt.geom.Point2D;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;
import javax.swing.ListModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListDataEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.beans.PropertyChangeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.List;
import java.util.ArrayList;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

// Custom plotter, extension of JPanel
public class PlotPanelStripChart extends PlotPanel {

    ///////////////////////
    // private variables
    //////////////////////
    private static final int NUM_TICKS = 5;

    private int traceBufferSize;
    private final TraceListModel traceList;
    private final JLabel titleLabel;
    private final JSlider speedSlider;
    private VariableRecorderInterface<Double> timeAxisBuffer;
    private double secondsPerUnit;

    private PlotComponent plot;

    public PlotPanelStripChart(TraceListModel model) {
        // store local reference to data model holding the traces
        traceList = model;
        //System.out.println("PlotPanelStripChart()");
        titleLabel = new JLabel("Plot Title");

        // register handler for changes in traceList
        traceList.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
            }
        });

        // register handler for changes in simulation compression (step size change)
        CVSim.sim.addPropertyChangeListener(CSimulation.COMPRESSION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                updateTraceBufferSizes();
            }
        });

        plot = new PlotComponent(traceList, PlotComponent.SCALE_X_DPI, PlotComponent.STRIPCHART);
        plot.setBorder(new CompoundBorder(new MatteBorder(14, 0, 14, 0, getBackground()),
                new LineBorder(Color.BLACK, 1)));

        // register handler for resize events
        plot.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateTraceBufferSizes();
            }
        });

        plot.addMouseListener(new PlotMouseListener());

        yScale = new YscaleComponent(traceList, 15, NUM_TICKS, this);

        // Setup Add widget and speed slider
        List<SimulationOutputVariable> tmpVarList = new ArrayList<>(CVSim.sim.getOutputVariables());
        tmpVarList.remove(CVSim.sim.getOutputVariable("TIME"));

        final JComboBox yTraceBox = new JComboBox(new SimulationOutputVariableListModel(tmpVarList));
        JButton addButton = new JButton("Add Trace");
        addButton.setMargin(new Insets(1, 2, 2, 2));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewTrace((SimulationOutputVariable) yTraceBox.getSelectedItem());
            }
        });

        speedSlider = new JSlider(1, 100, 5);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    System.out.println("New Paper Speed: " + (double) source.getValue());
                    setPaperSpeed((double) source.getValue() / 10.0);
                }
            }
        });

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                "Plot Speed");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        speedSlider.setBorder(titledBorder);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Y: "));
        controlPanel.add(yTraceBox);
        controlPanel.add(addButton);
        controlPanel.add(speedSlider);
        controlPanel.add(multipleYScalesCheckBox);
        controlPanel.setPreferredSize(new Dimension(25, 75));

        //add(xScale);
        this.setLayout(new BorderLayout());
        add(BorderLayout.PAGE_START, controlPanel);
        add(BorderLayout.LINE_START, yScale);
        add(BorderLayout.CENTER, plot);

        sourceDataChanged = new ChangeListener() {
            int count = 0;

            @Override
            public void stateChanged(ChangeEvent event) {
                if ((count++ % 4) == 0) {
                    updateTraceTransforms();
                    plot.repaint();
                }
            }
        };

        // Set default paper speed, and generate a default trace
        secondsPerUnit = 1;
        calculateTraceBufferSize();

    }

    @Override
    public void setMultipleYScales(boolean newValue) {
        boolean oldValue = getMultipleYScales();
        multipleYScales = newValue;
        yScale._changes.firePropertyChange(YscaleComponent.PROP_MULTISCALES, oldValue, newValue);
    }

    @Override
    public PlotComponent getPlot() {
        return plot;
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(yScale.getPreferredSize().width, yScale.getPreferredSize().width * 8);
    }

    private void createTimeAxisBuffer() {
        //System.out.println("PlotPanelStripChart() Creating timeAxisBuffer");
        timeAxisBuffer = new SimulationOutputVariableBuffer<>(traceBufferSize, CVSim.sim.getOutputVariable("TIME"), 0.0,
                SimulationOutputVariableBuffer.DATA_MONOTONIC_INCREASING);
        CVSim.sim.addVariableRecorder(timeAxisBuffer);
    }

    public void createNewTrace(SimulationOutputVariable var) {
        //System.out.println("createNewTrace(" + var + ")");
        if (timeAxisBuffer == null) {
            createTimeAxisBuffer();
        }
        VariableRecorderInterface<Double> y = new SimulationOutputVariableBuffer<>(traceBufferSize, var, 0.0);

        Trace<Double, Double> newTrace = new Trace<>(timeAxisBuffer, y, traceList.getNextColor());
        addTrace(newTrace);
    }

    @Override
    public void addTrace(Trace<?, ?> t) {
        //System.out.println("addTrace(" + t + ")");
        traceList.add(t);
        CVSim.sim.addVariableRecorder(t.getYVar());
    }

    @Override
    public void removeTrace(Trace<?, ?> t) {
        //System.out.println("removeTrace(" + t + ")");
        traceList.remove(t);
        CVSim.sim.removeVariableRecorder(t.getYVar());
        if (traceList.isEmpty() && (timeAxisBuffer != null)) {
            CVSim.sim.removeVariableRecorder(timeAxisBuffer);
            timeAxisBuffer = null;
        }
    }

    @Override
    public void removeAllTraces() {
        //System.out.println("removeAllTraces()");
        for (Trace t : traceList) {
            removeTrace(t);
        }
    }

    private int calculateTraceBufferSize() {
        int stepSize = CVSim.sim.getDataCompressionFactor();
        double plotTimeRange = secondsPerUnit * plot.getScaledPlotBounds().getWidth();
        traceBufferSize = (int) (1000 * plotTimeRange) / (stepSize);
        return traceBufferSize;
    }

    private SimulationOutputVariableBuffer resizeBuffer(SimulationOutputVariableBuffer b, int newSize) {
        SimulationOutputVariableBuffer<Double> newB;

        if (b.getSize() == newSize) {
            newB = b;
        } else {
            //System.out.println("resizeBuffer() " + b + " - from " + b.getSize() + " to " + newSize);	
            CVSim.sim.removeVariableRecorder(b);
            newB = new SimulationOutputVariableBuffer<>(traceBufferSize, b);
            CVSim.sim.addVariableRecorder(newB);
        }
        return newB;
    }

    private void updateTraceBufferSizes() {
        boolean wasRunning = false;

        int oldTraceBufferSize = traceBufferSize;
        calculateTraceBufferSize();
        if (oldTraceBufferSize != traceBufferSize) {
            //System.out.println("updateTraceBufferSizes() with " + traceBufferSize);
            //System.out.println(" -> " + (double)java.lang.Math.round((double)traceBufferSize/plot.getBounds().getWidth()*1000) /1000 + " data points per pixel");

            if (CVSim.simThread.isRunning()) {
                wasRunning = true;
                CVSim.simThread.stop();
            }

            if (timeAxisBuffer != null) {
                SimulationOutputVariableBuffer newTime = resizeBuffer((SimulationOutputVariableBuffer) timeAxisBuffer, traceBufferSize);
                timeAxisBuffer = newTime;
            }

            for (Trace tr : traceList) {
                tr.setXVar(timeAxisBuffer);
            }

            for (Trace tr : traceList) {
                SimulationOutputVariableBuffer newY = resizeBuffer((SimulationOutputVariableBuffer) tr.getYVar(), traceBufferSize);

                tr.setYVar(newY);
            }

            if (wasRunning) {
                CVSim.simThread.start();
            }
        }
    }

    public ListModel getListModel() {
        return traceList;
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    private void updateTraceTransforms() {
        AffineTransform plotTransform = plot.getPlotTransform();

        for (Trace<?, ?> trace : traceList) {
            AffineTransform at = trace.getTransform();
            if ((trace.isEnabled()) && (at != null)) {
                // start by setting the trace's transform to the
                //  value of the plot transform
                at.setTransform(plotTransform);

                // prepare scaling and translation values
                Range xrange = trace.getXDataRange();
                Range yrange = trace.getYRange();
                Double ydist = yrange.upper.doubleValue() - yrange.lower.doubleValue();

                // apply additional scaling for the data
                at.scale(-1 / secondsPerUnit, 1 / ydist);
                at.translate(-xrange.upper.doubleValue(),
                        -yrange.lower.doubleValue());

            }
        }
    }

    public void setPaperSpeed(double inchesPerSecond) {
        secondsPerUnit = 1 / inchesPerSecond;
        updateTraceBufferSizes();
    }

    class PlotMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // can only take measurement if sim is running
            if (!CVSim.simThread.isRunning()) {
                PlotPoints point = new PlotPoints(e.getX(), e.getY());
                Point2D screenPt, transPt;
                screenPt = e.getPoint();
                try {
                    if (getMultipleYScales()) {
                        Trace t0 = traceList.get(0);
                        transPt = getScaledPoint(t0.getTransform(), screenPt);
                        String str = String.format("%.2f, %.2f", transPt.getX(), transPt.getY());
                        point.add(str, t0.getColor());
                        for (Trace t : traceList) {
                            transPt = getScaledPoint(t.getTransform(), screenPt);
                            str = String.format("%.2f, %.2f", transPt.getX(), transPt.getY());
                            // don't want duplicate points
                            if (!(point.getString(0).equals(str))) {
                                point.add(str, t.getColor());
                            }
                        }
                    } else {
                        transPt = getScaledPoint(traceList.get(0).getTransform(), screenPt);
                        String str = String.format("%.2f, %.2f", transPt.getX(), transPt.getY());
                        point.add(str, Color.BLACK);
                    }

                    point.setEnabled(true);
                    plot.pointList.add(point);
                    plot.repaint();
                } catch (NoninvertibleTransformException ex) {
                    System.out.println("PlotPanel.java: NoninvertibleTransformException");
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent event) {
        }

        @Override
        public void mouseReleased(MouseEvent event) {
        }

        @Override
        public void mouseEntered(MouseEvent event) {
        }

        @Override
        public void mouseExited(MouseEvent event) {
        }
    }
} // end class PlotPanel

