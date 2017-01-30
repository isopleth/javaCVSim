package edu.mit.lcp;

//import edu.mit.lcp.C21_comp_backend.Main.instance();
//import edu.mit.lcp.C21_comp_backend.Parameter_vector;
import javax.swing.JOptionPane;
import jcvsim.backend21compartment.Main;
import jcvsim.backend21compartment.PVName;
import jcvsim.backend21compartment.Parameter_vector;

public class Parameter21C extends Parameter {

    private Parameter_vector paramVec;
    PVName pvName;

    public Parameter21C(Parameter_vector pvec, PVName parameterEnum,
            int index, String category, String name, String units) {
        this(pvec, parameterEnum, category, category, name, units);
    }

    public Parameter21C(Parameter_vector pvec, PVName parameterEnum,
            String category, String type, String name, String units) {
        super(category, type, name, units);
        paramVec = pvec;
        pvName = parameterEnum;
        setDefaultValue();
    }

    public Parameter21C(Parameter_vector pvec, PVName parameterEnum,
            String category, String type,
            String name, String units, double min, double max) {
        super(category, type, name, units, min, max);
        paramVec = pvec;
        pvName = parameterEnum;
        setDefaultValue();
    }

    @Override
    public void setValue(Double value) {
        Double oldVal = getValue();
        double tzpfv = 0;

        // Relational Constraints
        // Total Zero-Pressure Filling Volume cannot be greater than Total Blood Volume
        if (getName().contains("Zero-Pressure Filling Volume")) {
            tzpfv = CVSim.sim.calculateTotalZeroPressureFillingVolume();
            if ((value - oldVal + tzpfv) > CVSim.sim.getParameterByName("Total Blood Volume").getValue()) {
                String msg = String.format("The Total Zero-Pressure Filling Volume cannot be greater than the Total Blood Volume.");
                JOptionPane.showMessageDialog(MainWindow.frame, msg);
                return;
            }
        }
        if (getName().equals("Total Blood Volume")) {
            tzpfv = CVSim.sim.calculateTotalZeroPressureFillingVolume();
            if (value < tzpfv) {
                String msg = String.format("The Total Blood Volume cannot be less than the Total Zero-Pressure Filling Volume.");
                JOptionPane.showMessageDialog(MainWindow.frame, msg);
                return;
            }
        }

        // LV Systolic Compliance cannot be greater than LV Diastolic Compliance
        if (getName().equals("Left Ventricle Systolic Compliance")) {
            if (value > CVSim.sim.getParameterByName("Left Ventricle Diastolic Compliance").getValue()) {
                String msg = String.format("The Left Ventricle Systolic Compliance cannot be greater than the Left Ventricle Diastolic Compliance.");
                JOptionPane.showMessageDialog(MainWindow.frame, msg);
                return;
            }
        }
        if (getName().equals("Left Ventricle Diastolic Compliance")) {
            if (value < CVSim.sim.getParameterByName("Left Ventricle Systolic Compliance").getValue()) {
                String msg = String.format("The Left Ventricle Diastolic Compliance cannot be less than the Left Ventricle Systolic Compliance.");
                JOptionPane.showMessageDialog(MainWindow.frame, msg);
                return;
            }
        }

        // RV Systolic Compliance cannot be greater than RV Diastolic Compliance
        if (getName().equals("Right Ventricle Systolic Compliance")) {
            if (value > CVSim.sim.getParameterByName("Right Ventricle Diastolic Compliance").getValue()) {
                String msg = String.format("The Right Ventricle Systolic Compliance cannot be greater than the Right Ventricle Diastolic Compliance.");
                JOptionPane.showMessageDialog(MainWindow.frame, msg);
                return;
            }
        }
        if (getName().equals("Right Ventricle Diastolic Compliance")) {
            if (value < CVSim.sim.getParameterByName("Right Ventricle Systolic Compliance").getValue()) {
                String msg = String.format("The Right Ventricle Diastolic Compliance cannot be less than the Right Ventricle Systolic Compliance.");
                JOptionPane.showMessageDialog(MainWindow.frame, msg);
                return;
            }
        }

        // Check that value entered by user is not outside the valid parameter range
        if ((CVSim.gui.parameterPanel.getDisplayMode()).equals(ParameterPanel.DEFAULT)) {
            if (value < getMin()) {
                String msg = String.format("The value you entered for " + getName()
                        + " is outside \nthe allowed range. Please enter a value between %.3f and %.3f.",
                        getMin(), getMax());
                JOptionPane.showMessageDialog(MainWindow.frame, msg);
                value = getMin();
            } else if (value > getMax()) {
                String msg = String.format("The value you entered for " + getName()
                        + " is outside \nthe allowed range. Please enter a value between %.3f and %.3f.",
                        getMin(), getMax());
                JOptionPane.showMessageDialog(MainWindow.frame, msg);
                value = getMax();
            }
        }

        // Parameter Updates
        // Compliances inside the thorax
        if (getName().equals("Brachiocephalic Arteries Compliance")) {
            Main.instance().updateComplianceInsideThorax(value, paramVec, PVName.BRACH_ART_COMPLIANCE, 1);
        } else if (getName().equals("Superior Vena Cava Compliance")) {
            Main.instance().updateComplianceInsideThorax(value, paramVec, PVName.SVC_COMPLIANCE, 4);
        } else if (getName().equals("Inferior Vena Cava Compliance")) {
            Main.instance().updateComplianceInsideThorax(value, paramVec, PVName.IVC_COMPLIANCE, 14);
        } else if (getName().equals("Thoracic Aorta Compliance")) {
            Main.instance().updateComplianceInsideThorax(value, paramVec, PVName.THORACIC_AORTA_COMPLIANCE, 5);
        } else if (getName().equals("Ascending Aorta Compliance")) {
            Main.instance().updateComplianceInsideThorax(value, paramVec, PVName.ASCENDING_AORTA_COMPLIANCE, 0);
        } else if (getName().equals("Pulmonary Arterial Compliance")) {
            Main.instance().updateComplianceInsideThorax(value, paramVec, PVName.PULM_ART_COMPLIANCE, 17);
        } else if (getName().equals("Pulmonary Venous Compliance")) {
            Main.instance().updateComplianceInsideThorax(value, paramVec, PVName.PULM_VEN_COMPLIANCE, 18);
        } // Compliances outside the thorax
        else if (getName().equals("Abdominal Aorta Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.ABDOM_AORTA_COMPLIANCE, 6);
        } else if (getName().equals("Abdominal Veins Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.ABDOM_VEN_COMPLIANCE, 13);
        } else if (getName().equals("Lower Body Arteries Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.LBODY_ART_COMPLIANCE, 11);
        } else if (getName().equals("Lower Body Veins Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.LBODY_VEN_COMPLIANCE, 12);
        } else if (getName().equals("Renal Arteries Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.RENAL_ART_COMPLIANCE, 7);
        } else if (getName().equals("Renal Veins Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.RENAL_VEN_COMPLIANCE, 8);
        } else if (getName().equals("Splanchnic Arteries Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.SPLAN_ART_COMPLIANCE, 9);
        } else if (getName().equals("Splanchnic Veins Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.SPLAN_VEN_COMPLIANCE, 10);
        } else if (getName().equals("Upper Body Arteries Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.UBODY_ART_COMPLIANCE, 2);
        } else if (getName().equals("Upper Body Veins Compliance")) {
            Main.instance().updateComplianceOutsideThorax(value, paramVec, PVName.UBODY_VEN_COMPLIANCE, 3);
        } // Total Blood Volume
        else if (getName().equals("Total Blood Volume")) {
            Main.instance().updateTotalBloodVolume(value, paramVec);
        } // Intra-thoracic Pressure
        else if (getName().equals("Intra-thoracic Pressure")) {
            Main.instance().updateIntrathoracicPressure(value, paramVec);
        } // Zero-Pressure Filling Volume
        else if (getName().equals("Abdominal Aorta Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.ABDOM_AORTA_ZPFV, PVName.ABDOM_AORTA_COMPLIANCE, 6);
        } else if (getName().equals("Abdominal Veins Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.ABDOM_VEN_ZPFV, PVName.ABDOM_VEN_COMPLIANCE, 13);
        } else if (getName().equals("Ascending Aorta Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.ASCENDING_AORTA_VOLUME, PVName.ASCENDING_AORTA_COMPLIANCE, 0);
        } else if (getName().equals("Brachiocephalic Arteries Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.BRACH_ART_ZPFV, PVName.BRACH_ART_COMPLIANCE, 1);
        } else if (getName().equals("Inferior Vena Cava Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.IVC_ZPFV, PVName.IVC_COMPLIANCE, 14);
        } else if (getName().equals("Lower Body Arteries Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.LBODY_ART_ZPFV, PVName.LBODY_ART_COMPLIANCE, 11);
        } else if (getName().equals("Lower Body Veins Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.LBODY_VEN_ZPFV, PVName.LBODY_VEN_COMPLIANCE, 12);
        } else if (getName().equals("Renal Arteries Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.RENAL_ART_ZPFV, PVName.RENAL_ART_COMPLIANCE, 7);
        } else if (getName().equals("Renal Veins Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.RENAL_VEN_ZPFV, PVName.RENAL_VEN_COMPLIANCE, 8);
        } else if (getName().equals("Splanchnic Arteries Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.SPLAN_ART_ZPFV, PVName.SPLAN_ART_COMPLIANCE, 9);
        } else if (getName().equals("Splanchnic Veins Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.SPLAN_VEN_ZPFV, PVName.SPLAN_VEN_COMPLIANCE, 10);
        } else if (getName().equals("Superior Vena Cava Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.SVC_ZPFV, PVName.SVC_COMPLIANCE, 4);
        } else if (getName().equals("Thoracic Aorta Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.THORACIC_AORTA_ZPFV, PVName.THORACIC_AORTA_COMPLIANCE, 5);
        } else if (getName().equals("Upper Body Arteries Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.UBODY_ART_ZPFV, PVName.UBODY_ART_COMPLIANCE, 2);
        } else if (getName().equals("Upper Body Veins Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.UBODY_VEN_ZPFV, PVName.UBODY_VEN_COMPLIANCE, 3);
        } else if (getName().equals("Pulmonary Arterial Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.PULM_ART_ZPFV, PVName.PULM_ART_COMPLIANCE, 17);
        } else if (getName().equals("Pulmonary Venous Zero-Pressure Filling Volume")) {
            Main.instance().updateZeroPressureFillingVolume(value, paramVec, PVName.PULN_VEN_ZPFV, PVName.PULM_VEN_COMPLIANCE, 18);
        } // Everything else
        else {
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
