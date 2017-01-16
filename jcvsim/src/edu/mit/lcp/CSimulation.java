package edu.mit.lcp;

import java.util.List;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Comparator;

public abstract class CSimulation {

    public static final String COMPRESSION = "COMPRESSION";
    public static final String ABREFLEX = "ABREFLEX";
    public static final String CPREFLEX = "CPREFLEX";

    // Count the number of simulation steps
    protected long steps;

    // dataCompressionFactor is the number of steps/values the
    // simulation uses to generate the single output value, value of 1
    // means each step() proceeds 1ms through the simulation?
    private int _dataCompressionFactor = 10;

    // these parameters control the simulations
    private boolean _ABReflex;
    private boolean _CPReflex;

    public abstract void reset();

    public abstract void step();

    public abstract double getOutput(int i);

    public abstract void updatePressure(int i, double d);

    public abstract List<Parameter> getParameterList();

    public abstract Parameter getParameterByName(String name);

    // methods to change the simulation settings
    public void setDataCompressionFactor(int compressionFactor) {
        int old = getDataCompressionFactor();
        _dataCompressionFactor = compressionFactor;
        firePropertyChange(COMPRESSION, old, compressionFactor);
        System.out.println("DataCompressionFactor = " + compressionFactor);
    }

    public int getDataCompressionFactor() {
        return _dataCompressionFactor;
    }

    public void setABReflex(boolean enable) {
        boolean oldState = getABReflex();
        _ABReflex = enable;
        firePropertyChange(ABREFLEX, oldState, enable);
    }

    public boolean getABReflex() {
        return _ABReflex;
    }

    public void setCPReflex(boolean enable) {
        boolean oldState = getCPReflex();
        _CPReflex = enable;
        firePropertyChange(CPREFLEX, oldState, enable);
    }

    public boolean getCPReflex() {
        return _CPReflex;
    }

    public double calculateTotalZeroPressureFillingVolume() {
        double tzpfv = 0;
        for (Parameter p : getParameterList()) {
            if (p.getName().contains("Zero-Pressure Filling Volume")) {
                tzpfv += p.getValue();
            }
        }
        String str = String.format("tzpfv: %.4f", tzpfv);
        System.out.println(str);
        return tzpfv;
    }

    ///////////////////////////////
    // Support for property changes
    private final PropertyChangeSupport _propChangeListeners = new PropertyChangeSupport(this);
    // Add Support for Property Changes
    PropertyChangeListener SimulationParameterChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            firePropertyChange(new PropertyChangeEvent(e.getSource(),
                    e.getPropertyName() + ((Parameter) e.getSource()).getName(),
                    e.getOldValue(),
                    e.getNewValue()));
        }
    };

    /**
     * The specified PropertyChangeListeners propertyChange method will be
     * called each time the value of any bound property is changed. The
     * PropertyListener object is addded to a list of PropertyChangeListeners
     * managed by this button, it can be removed with
     * removePropertyChangeListener. Note: the JavaBeans specification does not
     * require PropertyChangeListeners to run in any particular order.
     *
     * @see #removePropertyChangeListener
     * @param l the PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propChangeListeners.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _propChangeListeners.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Remove this PropertyChangeListener from the buttons internal list. If the
     * PropertyChangeListener isn't on the list, silently do nothing.
     *
     * @see #addPropertyChangeListener
     * @param l the PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propChangeListeners.removePropertyChangeListener(l);
    }

    protected void firePropertyChange(String property, Object old, Object current) {
        _propChangeListeners.firePropertyChange(property, old, current);
    }

    protected void firePropertyChange(PropertyChangeEvent e) {
        _propChangeListeners.firePropertyChange(e);
    }

    // Listener Interface for ChangeEvents (data changes)
    //
    private final EventListenerList _changeListeners = new EventListenerList();

    public void addChangeListener(ChangeListener listener) {
        _changeListeners.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        _changeListeners.remove(ChangeListener.class, listener);
    }

    public void dataChanged() {
        fireChangeEvent();
    }

    private void fireChangeEvent() {
        ChangeEvent event = new ChangeEvent(this);

        // Guaranteed to return a non-null array
        Object[] listeners = _changeListeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int index = listeners.length - 2; index >= 0; index -= 2) {
            if (listeners[index] == ChangeListener.class) {
                ((ChangeListener) listeners[index + 1]).stateChanged(event);
            }
        }
    }

    // // // // 
    private final List<VariableRecorderInterface> varRecorders = new CopyOnWriteArrayList<>();

    public void addVariableRecorder(VariableRecorderInterface recorder) {
        varRecorders.add(recorder);
// 	System.out.println("CSimulation recorder total: " + varRecorders.size() + 
// 			   " (Added:   " + recorder + ", size " + recorder.getSize() + ")");
    }

    public void removeVariableRecorder(VariableRecorderInterface recorder) {
        varRecorders.remove(recorder);
// 	System.out.println("CSimulation recorder total: " + varRecorders.size() + 
// 			   " (Removed: " + recorder + ", size " + recorder.getSize() + ")");
    }

    protected List<VariableRecorderInterface> getVariableRecorders() {
        return varRecorders;
    }

    // Write new data out to the recorders
    public void updateRecorders() {
        for (VariableRecorderInterface recorder : getVariableRecorders()) {
            recorder.addMarkedDatum(getOutput(recorder.getOutputIndex()),
                    java.lang.Math.round(getOutput(0) * 100));
        }
    }

    // // // //
    protected List<SimulationOutputVariable> varList;

    protected List<SimulationOutputVariable> getOutputVariables() {
        return varList;
    }

    protected SimulationOutputVariable getOutputVariable(String name) {
        for (SimulationOutputVariable v : varList) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public class ComparatorX implements Comparator {

        @Override
        public int compare(Object p1, Object p2) {
            return ((((Parameter) p1).getName()).compareTo(((Parameter) p2).getName()));
        }
    }

}
