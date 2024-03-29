package edu.mit.lcp;

//import edu.mit.lcp.C6_comp_backend.main;
//import edu.mit.lcp.C6_comp_backend.Parameter_vector;
import javax.swing.JOptionPane;
import jcvsim.backend6compartment.Main;
import jcvsim.backend6compartment.PVName;
import jcvsim.backend6compartment.Parameter_vector;

public class Parameter6C extends Parameter {

    private Parameter_vector paramVec;
    PVName pvName;

    public Parameter6C(Parameter_vector pvec, PVName parameterEnum,
            String category, String name,
            String units) {
        this(pvec, parameterEnum, category, category, name, units);
    }

    public Parameter6C(Parameter_vector pvec, PVName parameterEnum,
            String category, String type,
            String name, String units) {
        super(category, type, name, units);
        paramVec = pvec;
        pvName = parameterEnum;
        setDefaultValue();
    }

    public Parameter6C(Parameter_vector pvec, PVName parameterEnum, String category, String type,
            String name, String units, double min, double max) {
        super(category, type, name, units, min, max);
        paramVec = pvec;
        pvName = parameterEnum;
        setDefaultValue();
    }

    @Override
    public void setValue(Double value) {
        Double oldVal = getValue();

        // Relational Constraints
        // Total Zero-Pressure Filling Volume cannot be greater than Total Blood Volume
        if (getName().equals("Total Zero-Pressure Filling Volume")) {
            if (value > CVSim.sim.getParameterByName("Total Blood Volume").getValue()) {
                String msg = String.format("The Total Zero-Pressure Filling Volume cannot be greater than the Total Blood Volume.");
                JOptionPane.showMessageDialog(CVSim.gui.frame, msg);
                return;
            }
        }
        if (getName().equals("Total Blood Volume")) {
            if (value < CVSim.sim.getParameterByName("Total Zero-Pressure Filling Volume").getValue()) {
                String msg = String.format("The Total Blood Volume cannot be less than the Total Zero-Pressure Filling Volume.");
                JOptionPane.showMessageDialog(CVSim.gui.frame, msg);
                return;
            }
        }

        // LV Systolic Compliance cannot be greater than LV Diastolic Compliance
        if (getName().equals("Left Ventricle Systolic Compliance")) {
            if (value > CVSim.sim.getParameterByName("Left Ventricle Diastolic Compliance").getValue()) {
                String msg = String.format("The Left Ventricle Systolic Compliance cannot be greater than the Left Ventricle Diastolic Compliance.");
                JOptionPane.showMessageDialog(CVSim.gui.frame, msg);
                return;
            }
        }
        if (getName().equals("Left Ventricle Diastolic Compliance")) {
            if (value < CVSim.sim.getParameterByName("Left Ventricle Systolic Compliance").getValue()) {
                String msg = String.format("The Left Ventricle Diastolic Compliance cannot be less than the Left Ventricle Systolic Compliance.");
                JOptionPane.showMessageDialog(CVSim.gui.frame, msg);
                return;
            }
        }

        // RV Systolic Compliance cannot be greater than RV Diastolic Compliance
        if (getName().equals("Right Ventricle Systolic Compliance")) {
            if (value > CVSim.sim.getParameterByName("Right Ventricle Diastolic Compliance").getValue()) {
                String msg = String.format("The Right Ventricle Systolic Compliance cannot be greater than the Right Ventricle Diastolic Compliance.");
                JOptionPane.showMessageDialog(CVSim.gui.frame, msg);
                return;
            }
        }
        if (getName().equals("Right Ventricle Diastolic Compliance")) {
            if (value < CVSim.sim.getParameterByName("Right Ventricle Systolic Compliance").getValue()) {
                String msg = String.format("The Right Ventricle Diastolic Compliance cannot be less than the Right Ventricle Systolic Compliance.");
                JOptionPane.showMessageDialog(CVSim.gui.frame, msg);
                return;
            }
        }

        // Check that value entered by user is not outside the valid parameter range
        if ((CVSim.gui.parameterPanel.getDisplayMode()).equals(ParameterPanel.DEFAULT)) {
            if (value < getMin()) {
                String msg = String.format("The value you entered for " + getName()
                        + " is outside \nthe allowed range. Please enter a value between %.3f and %.3f.",
                        getMin(), getMax());
                JOptionPane.showMessageDialog(CVSim.gui.frame, msg);
                value = getMin();
            } else if (value > getMax()) {
                String msg = String.format("The value you entered for " + getName()
                        + " is outside \nthe allowed range. Please enter a value between %.3f and %.3f.",
                        getMin(), getMax());
                JOptionPane.showMessageDialog(CVSim.gui.frame, msg);
                value = getMax();
            }
        }

        if (getName().equals("Pulmonary Arterial Compliance")) {
            Main.instance().updatePulmonaryArterialCompliance(value, paramVec);
        } else if (getName().equals("Pulmonary Venous Compliance")) {
            Main.instance().updatePulmonaryVenousCompliance(value, paramVec);
        } else if (getName().equals("Arterial Compliance")) {
            Main.instance().updateArterialCompliance(value, paramVec);
        } else if (getName().equals("Venous Compliance")) {
            Main.instance().updateVenousCompliance(value, paramVec);
        } else if (getName().equals("Total Blood Volume")) {
            Main.instance().updateTotalBloodVolume(value, paramVec);
        } else if (getName().equals("Intra-thoracic Pressure")) {
            Main.instance().updateIntrathoracicPressure(value, paramVec);
        } else if (getName().equals("Total Zero-Pressure Filling Volume")) {
            Main.instance().updateTotalZeroPressureFillingVolume(value, paramVec);
        } else {
            Main.instance().updateParameter(value, paramVec, pvName);
        }

        firePropertyChange("VALUE", oldVal, value);

        String str = String.format(getName() + " changed from %.3f to %.3f.",
                oldVal, getValue());
        System.out.println(str);

    }

    @Override
    public Double getValue() {
        return paramVec.get(pvName);
    }
}
