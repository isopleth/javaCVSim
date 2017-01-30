package edu.mit.lcp;

import jcvsim.backend21compartment.Output;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import jcvsim.backend21compartment.Main;
import jcvsim.backend21compartment.PVName;
import jcvsim.backend21compartment.Parameter_vector;

public class CSimulation21C extends CSimulation {

    // C structure which holds the results of the last simulation step
    public Output output;

    // Simulation Parameters for the C code
    public CSimulation21CParameters simParameters;

    public CSimulation21C() {
        super();
        System.out.println("CSimulation21C()");

        // create a new instance of the output class which provides
        // access to the data structures in the loaded C library
        output = new Output();

        simParameters = new CSimulation21CParameters();

        // Create output variable list
        varList = new ArrayList<>();

        varList.add(new SimulationOutputVariable<>(0, "TIME", "Time", "s", "Other", "Time",
                new Range<>(0.0, 0.0)));

        // Ascending Aorta
        varList.add(new SimulationOutputVariable<>(1, "AAP", "Ascending Aortic Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X0

        varList.add(new SimulationOutputVariable<>(2, "AAF", "Ascending Aortic Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q0

        varList.add(new SimulationOutputVariable<>(3, "AAV", "Ascending Aortic Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V0

        // Brachiocephalic Arteries
        varList.add(new SimulationOutputVariable<>(4, "BAP", "Brachiocephalic Arterial Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X1

        varList.add(new SimulationOutputVariable<>(5, "BAF", "Brachiocephalic Arterial Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q1

        varList.add(new SimulationOutputVariable<>(6, "BAV", "Brachiocephalic Arterial Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V1

        // Upper Body Arteries 
        varList.add(new SimulationOutputVariable<>(7, "UBAP", "Upper Body Arterial Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X2

        varList.add(new SimulationOutputVariable<>(8, "UBAF", "Upper Body Arterial Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q2

        varList.add(new SimulationOutputVariable<>(9, "UBAV", "Upper Body Arterial Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V2

        // Lower Body Arteries
        varList.add(new SimulationOutputVariable<>(10, "UBVP", "Upper Body Venous Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X3

        varList.add(new SimulationOutputVariable<>(11, "UBVF", "Upper Body Venous Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q3

        varList.add(new SimulationOutputVariable<>(12, "UBVV", "Upper Body Venous Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V3

        // Superior Vena Cava 
        varList.add(new SimulationOutputVariable<>(13, "SVCP", "Superior Vena Cava Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X4

        varList.add(new SimulationOutputVariable<>(14, "SVCF", "Superior Vena Cava Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q4

        varList.add(new SimulationOutputVariable<>(15, "SVCV", "Superior Vena Cava Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V4

        // Thoracic Aorta
        varList.add(new SimulationOutputVariable<>(16, "TAP", "Thoracic Aortic Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X5

        varList.add(new SimulationOutputVariable<>(17, "TAF", "Thoracic Aortic Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q5

        varList.add(new SimulationOutputVariable<>(18, "TAV", "Thoracic Aortic Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V5

        // Abdominal Aorta
        varList.add(new SimulationOutputVariable<>(19, "AAP", "Abdominal Aortic Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X6

        varList.add(new SimulationOutputVariable<>(20, "AAF", "Abdominal Aortic Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q6

        varList.add(new SimulationOutputVariable<>(21, "AAV", "Abdominal Aortic Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V6

        // Renal Arteries
        varList.add(new SimulationOutputVariable<>(22, "RAP", "Renal Arterial Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X7

        varList.add(new SimulationOutputVariable<>(23, "RAF", "Renal Arterial Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q7

        varList.add(new SimulationOutputVariable<>(24, "RAV", "Renal Arterial Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V7

        // Renal Veins
        varList.add(new SimulationOutputVariable<>(25, "RVP", "Renal Venous Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X8

        varList.add(new SimulationOutputVariable<>(26, "RVF", "Renal Venous Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q8

        varList.add(new SimulationOutputVariable<>(27, "RVV", "Renal Venous Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V8

        // Splanchnic Arteries
        varList.add(new SimulationOutputVariable<>(28, "SAP", "Splanchnic Arterial Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X9

        varList.add(new SimulationOutputVariable<>(29, "SAF", "Splanchnic Arterial Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q9

        varList.add(new SimulationOutputVariable<>(30, "SAV", "Splanchnic Arterial Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V9

        // Splanchnic Veins
        varList.add(new SimulationOutputVariable<>(31, "SVP", "Splanchnic Venous Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X10

        varList.add(new SimulationOutputVariable<>(32, "SVF", "Splanchnic Venous Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q10

        varList.add(new SimulationOutputVariable<>(33, "SVV", "Splanchnic Venous Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V10

        // Lower Body Arteries
        varList.add(new SimulationOutputVariable<>(34, "LBAP", "Lower Body Arterial Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X11

        varList.add(new SimulationOutputVariable<>(35, "LBAF", "Lower Body Arterial Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q11

        varList.add(new SimulationOutputVariable<>(36, "LBAV", "Lower Body Arterial Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V11

        // Lower Body Veins
        varList.add(new SimulationOutputVariable<>(37, "LBVP", "Lower Body Venous Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X12

        varList.add(new SimulationOutputVariable<>(38, "LBVF", "Lower Body Venous Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q12

        varList.add(new SimulationOutputVariable<>(39, "LBVV", "Lower Body Venous Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V12

        // Abdominal Veins
        varList.add(new SimulationOutputVariable<>(40, "AVP", "Abdominal Venous Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X13

        varList.add(new SimulationOutputVariable<>(41, "AVF", "Abdominal Venous Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q13

        varList.add(new SimulationOutputVariable<>(42, "AVV", "Abdominal Venous Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V13

        // Inferior Vena Cava
        varList.add(new SimulationOutputVariable<>(43, "IVCP", "Inferior Vena Cava Pressure", "mmHg", "Peripheral", "Pressure",
                new Range<>(0.0, 200.0))); // X14

        varList.add(new SimulationOutputVariable<>(44, "IVCF", "Inferior Vena Cava Flow", "mL/s", "Peripheral", "Flow",
                new Range<>(0.0, 200.0))); // Q14

        varList.add(new SimulationOutputVariable<>(45, "IVCV", "Inferior Vena Cava Volume", "mL", "Peripheral", "Volume",
                new Range<>(0.0, 200.0))); // V14

        // Right Atrium
        varList.add(new SimulationOutputVariable<>(46, "RAP", "Right Atrial Pressure", "mmHg", "Cardiac", "Pressure",
                new Range<>(0.0, 200.0))); // X15

        varList.add(new SimulationOutputVariable<>(47, "RAF", "Right Atrial Flow", "mL/s", "Cardiac", "Flow",
                new Range<>(0.0, 200.0))); // Q15

        varList.add(new SimulationOutputVariable<>(48, "RAV", "Right Atrial Volume", "mL", "Cardiac", "Volume",
                new Range<>(0.0, 200.0))); // V15

        // Right Ventricle
        varList.add(new SimulationOutputVariable<>(49, "RVP", "Right Ventricular Pressure", "mmHg", "Cardiac", "Pressure",
                new Range<>(0.0, 200.0))); // X16

        varList.add(new SimulationOutputVariable<>(50, "RVF", "Right Ventricular Flow", "mL/s", "Cardiac", "Flow",
                new Range<>(0.0, 200.0))); // Q16

        varList.add(new SimulationOutputVariable<>(51, "RVV", "Right Ventricular Volume", "mL", "Cardiac", "Volume",
                new Range<>(0.0, 200.0))); // V16

        // Pulmonary Arteries
        varList.add(new SimulationOutputVariable<>(52, "PAP", "Pulmonary Arterial Pressure", "mmHg", "Pulmonary", "Pressure",
                new Range<>(0.0, 200.0))); // X17

        varList.add(new SimulationOutputVariable<>(53, "PAF", "Pulmonary Arterial Flow", "mL/s", "Pulmonary", "Flow",
                new Range<>(0.0, 200.0))); // Q17

        varList.add(new SimulationOutputVariable<>(54, "PAV", "Pulmonary Arterial Volume", "mL", "Pulmonary", "Volume",
                new Range<>(0.0, 200.0))); // V17

        // Pulmonary Veins
        varList.add(new SimulationOutputVariable<>(55, "PVP", "Pulmonary Venous Pressure", "mmHg", "Pulmonary", "Pressure",
                new Range<>(0.0, 200.0))); // X18

        varList.add(new SimulationOutputVariable<>(56, "PVF", "Pulmonary Venous Flow", "mL/s", "Pulmonary", "Flow",
                new Range<>(0.0, 200.0))); // Q18

        varList.add(new SimulationOutputVariable<>(57, "PVV", "Pulmonary Venous Volume", "mL", "Pulmonary", "Volume",
                new Range<>(0.0, 200.0))); // V18

        // Left Atrium
        varList.add(new SimulationOutputVariable<>(58, "LAP", "Left Atrial Pressure", "mmHg", "Cardiac", "Pressure",
                new Range<>(0.0, 200.0))); // X19

        varList.add(new SimulationOutputVariable<>(59, "LAF", "Left Atrial Flow", "mL/s", "Cardiac", "Flow",
                new Range<>(0.0, 200.0))); // Q19

        varList.add(new SimulationOutputVariable<>(60, "LAV", "Left Atrial Volume", "mL", "Cardiac", "Volume",
                new Range<>(0.0, 200.0))); // V19

        // Left Ventricle
        varList.add(new SimulationOutputVariable<>(61, "LVP", "Left Ventricular Pressure", "mmHg", "Cardiac", "Pressure",
                new Range<>(0.0, 200.0))); // X20

        varList.add(new SimulationOutputVariable<>(62, "LVF", "Left Ventricular Flow", "mL/s", "Cardiac", "Flow",
                new Range<>(0.0, 200.0))); // Q20

        varList.add(new SimulationOutputVariable<>(63, "LVV", "Left Ventricular Volume", "mL", "Cardiac", "Volume",
                new Range<>(0.0, 200.0))); // V20

        // Reflex Outputs (Controlled Variables)
        varList.add(new SimulationOutputVariable<>(64, "HR", "Heart Rate", "beats/min", "Systemic", "Reflex",
                new Range<>(0.0, 100.0))); // Heart Rate

        varList.add(new SimulationOutputVariable<>(65, "AR", "Arteriolar Resistance", "PRU", "Systemic", "Reflex",
                new Range<>(0.0, 10.0))); // Arterial Resistance

        varList.add(new SimulationOutputVariable<>(66, "VT", "Venous Tone", "mL", "Systemic", "Reflex",
                new Range<>(0.0, 2500.0))); // Venous Tone

        varList.add(new SimulationOutputVariable<>(67, "RVC", "Right Ventricle Contractility", "mL/mmHg", "Systemic", "Reflex",
                new Range<>(0.0, 10.0)));

        varList.add(new SimulationOutputVariable<>(68, "LVC", "Left Ventricle Contractility", "mL/mmHg", "Systemic", "Reflex",
                new Range<>(0.0, 10.0)));

        varList.add(new SimulationOutputVariable<>(69, "TBV", "Total Blood Volume", "mL", "Systemic", "Volume",
                new Range<>(0.0, 5500.0)));

        varList.add(new SimulationOutputVariable<>(70, "PTH", "Intra-thoracic Pressure", "mmHg", "Systemic", "Pressure",
                new Range<>(-10.0, 10.0)));

        varList.add(new SimulationOutputVariable<>(71, "TA", "Tilt Angle", "Degrees", "Experiment", "Angle",
                new Range<>(0.0, 90.0)));

    }

    @Override
    public void reset() {
        Main.instance().reset_sim();
    }

    // This method steps the simulation
    @Override
    public void step() {

        // run simulation and get updated model measurements
        Main.instance().step_sim(output,
                simParameters.getVector(),
                getDataCompressionFactor(),
                getABReflex(),
                getCPReflex(),
                CVSim.gui.tiltTestFrame.getTiltTest(),
                CVSim.gui.tiltTestFrame.getTiltStartTime(),
                CVSim.gui.tiltTestFrame.getTiltStopTime());

        // increment the step count
        steps++;

        // Save the new data
        updateRecorders();

        // Flag that data has changed
        dataChanged();

    } // end step

    @Override
    public double getOutput(int index) {
        double value;
        switch (index) {
            case 0:
                value = output.time;
                break; // Time 
            // 
            case 1:
                value = output.ascendingAorticPressure;
                break;   // 
            case 2:
                value = output.ascendingAorticFlow;
                break;   // 
            case 3:
                value = output.ascendingAorticVolume;
                break;   // 
            // 
            case 4:
                value = output.brachiocephalicArterialPressure;
                break;   // 
            case 5:
                value = output.brachiocephalicArterialFlow;
                break;   // 
            case 6:
                value = output.brachiocephalicArterialVolume;
                break;   // 
            // 
            case 7:
                value = output.upperBodyArterialPressure;
                break;   // 
            case 8:
                value = output.upperBodyArterialFlow;
                break;   // 
            case 9:
                value = output.upperBodyArterialVolume;
                break;   // 
            // 
            case 10:
                value = output.upperBodyVenousPressure;
                break;   // 
            case 11:
                value = output.upperBodyVenousFlow;
                break;   // 
            case 12:
                value = output.upperBodyVenousVolume;
                break;   // 
            // 
            case 13:
                value = output.superiorVenaCavaPressure;
                break;   // 
            case 14:
                value = output.superiorVenaCavaFlow;
                break;   // 
            case 15:
                value = output.superiorVenaCavaVolume;
                break;   // 
            // 
            case 16:
                value = output.thoracicAorticPressure;
                break;   // 
            case 17:
                value = output.thoracicAorticFlow;
                break;   // 
            case 18:
                value = output.thoracicAorticVolume;
                break;   // 
            // 
            case 19:
                value = output.abdominalAorticPressure;
                break;   // 
            case 20:
                value = output.abdominalAorticFlow;
                break;   // 
            case 21:
                value = output.abdominalAorticVolume;
                break;   // 
            // 
            case 22:
                value = output.renalArterialPressure;
                break;   // 
            case 23:
                value = output.renalArterialFlow;
                break;   // 
            case 24:
                value = output.renalArterialVolume;
                break;   // 
            // 
            case 25:
                value = output.renalVenousPressure;
                break;   // 
            case 26:
                value = output.renalVenousFlow;
                break;   // 
            case 27:
                value = output.renalVenousVolume;
                break;   // 
            // 
            case 28:
                value = output.splanchnicArterialPressure;
                break;   // 
            case 29:
                value = output.splanchnicArterialFlow;
                break;   // 
            case 30:
                value = output.splanchnicArterialVolume;
                break;   // 
            // 
            case 31:
                value = output.splanchnicVenousPressure;
                break;   // 
            case 32:
                value = output.splanchnicVenousFlow;
                break;   // 
            case 33:
                value = output.splanchnicVenousVolume;
                break;   // 
            // 
            case 34:
                value = output.lowerBodyArterialPressure;
                break;   // 
            case 35:
                value = output.lowerBodyArterialFlow;
                break;   // 
            case 36:
                value = output.lowerBodyArterialVolume;
                break;   // 
            // 
            case 37:
                value = output.lowerBodyVenousPressure;
                break;   // 
            case 38:
                value = output.lowerBodyVenousFlow;
                break;   // 
            case 39:
                value = output.lowerBodyVenousVolume;
                break;   // 
            // 
            case 40:
                value = output.abdominalVenousPressure;
                break;   // 
            case 41:
                value = output.abdominalVenousFlow;
                break;   // 
            case 42:
                value = output.abdominalVenousVolume;
                break;   // 
            // 
            case 43:
                value = output.inferiorVenaCavaPressure;
                break;   // 
            case 44:
                value = output.inferiorVenaCavaFlow;
                break;   // 
            case 45:
                value = output.inferiorVenaCavaVolume;
                break;   // 
            // 
            case 46:
                value = output.rightAtrialPressure;
                break;   // 
            case 47:
                value = output.rightAtrialFlow;
                break;   // 
            case 48:
                value = output.rightAtrialVolume;
                break;   // 
            // 
            case 49:
                value = output.rightVentricularPressure;
                break;   // 
            case 50:
                value = output.rightVentricularFlow;
                break;   // 
            case 51:
                value = output.rightVentricularVolume;
                break;   // 
            // 
            case 52:
                value = output.pulmonaryArterialPressure;
                break;   // 
            case 53:
                value = output.pulmonaryArterialFlow;
                break;   // 
            case 54:
                value = output.pulmonaryArterialVolume;
                break;   // 
            // 
            case 55:
                value = output.pulmonaryVenousPressure;
                break;   // 
            case 56:
                value = output.pulmonaryVenousFlow;
                break;   // 
            case 57:
                value = output.pulmonaryVenousVolume;
                break;   // 
            // 
            case 58:
                value = output.leftAtrialPressure;
                break;   // 
            case 59:
                value = output.leftAtrialFlow;
                break;   // 
            case 60:
                value = output.leftAtrialVolume;
                break;   // 
            // 
            case 61:
                value = output.leftVentricularPressure;
                break;   // 
            case 62:
                value = output.leftVentricularFlow;
                break;   // 
            case 63:
                value = output.leftVentricularVolume;
                break;   // 

            case 64:
                value = output.HR;
                break; // HR
            case 65:
                value = output.AR;
                break; // AR
            case 66:
                value = output.VT;
                break; // VT
            case 67:
                value = output.RVC;
                break; // RVC
            case 68:
                value = output.LVC;
                break; // LVC

            case 69:
                value = output.totalBloodVolume;
                break; // TBV 
            case 70:
                value = output.intraThoracicPressure;
                break; // Pth 

            case 71:
                value = output.tiltAngle;
                break; // tilt angle    

            default:
                value = 0.0;
                break;
        }

        return value;
    }

    @Override
    public void updatePressure(int i, double d) {
        Main.instance().updatePressure(i, d);
    }

    @Override
    public List<Parameter> getParameterList() {
        return simParameters.getParameterList();
    }

    @Override
    public Parameter getParameterByName(String name) {
        return simParameters.getParameterByName(name);
    }

    public Parameter_vector getParameterVector() {
        return simParameters.getVector();
    }

    ///////////////////////////////////////
    ///////////////////////////////////////
    public class CSimulation21CParameters {

        private final Parameter_vector pvec;
        private final List<Parameter> plist;

        CSimulation21CParameters() {
            pvec = new Parameter_vector();
            // initialize the simulation with the variables
            Main.instance().init_sim(pvec);

            plist = createParameterList();
        }

        public Parameter_vector getVector() {
            return pvec;
        }

        public List<Parameter> getParameterList() {
            return plist;
        }

        public Parameter getParameterByName(String name) {
            for (Parameter p : plist) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
            return null;
        }

        // creates and initializes parameters
        private List<Parameter> createParameterList() {
            List<Parameter> list = new ArrayList<>();

            // Hello, magic numbers!  
            // The index of each parameter is its location in the 
            // tmp->vec structure in the mapping() function in initial.c. 
            // Use the comments in mapping() to locate each parameter.
            // For example, if you want to know the index of the
            // pulmonary arterial compliance parameter, look for 
            // something resembling that description in the comments in the 
            // mapping function. In this case you will find:
            // tmp -> vec[47] = (*hemo)[8].c[0][0]; // C pul. arteries
            // Therefore, 47 is the index for pulmonary arterial compliance.
            // To find the minimum and maximum values for each parameter, 
            // cross-reference the right side of the assignment statements 
            // in mapping() with the declarations in the initial(). The minimum
            // is located in the second column of each multi-dimensional array. The
            // maximum is located in the third column. Again, using pulmonary arterial
            // compliance as an example:
            // tmp -> vec[47] = (*hemo)[8].c[0][0]; // C pul. arteries
            // In this case, you would need to find the entry for 
            // (*hemo)[8].c[0][2] (the minimum) and  (*hemo)[8].c[0][3]
            // (the maximum) in initial.c.
            // (*hemo)[8].c[0][2] = 1.5;   (*hemo)[8].c[0][3] = 7.2;
            // Is this a terrible way to do things? Absolutely. But
            // doing things the right way would require rewriting
            // a significant part of the C code, and there is not enough 
            // time to do that.
            // Cardiac
            list.add(new Parameter21C(pvec, PVName.LA_DIASTOLIC_COMPLIANCE,
                    "Left Heart", "Compliance", "Left Atrium Diastolic Compliance",
                    "mL/mmHg", 1.0, 4.3));
            list.add(new Parameter21C(pvec, PVName.LA_SYSTOLIC_COMPLIANCE,
                    "Left Heart", "Compliance", "Left Atrium Systolic Compliance",
                    "mL/mmHg", 0.8, 3.2));
            list.add(new Parameter21C(pvec, PVName.MITRAL_VALVE_RESISTANCE,
                    "Left Heart", "Resistance", "Mitral Valve Resistance",
                    "PRU", 0.007, 0.013));
            list.add(new Parameter21C(pvec, PVName.LA_ZPFV,
                    "Left Heart", "Volume", "Left Atrium Zero-Pressure Filling Volume",
                    "mL", 10.0, 38.0));

            list.add(new Parameter21C(pvec, PVName.RA_DIASTOLIC_COMPLIANCE,
                    "Right Heart", "Compliance", "Right Atrium Diastolic Compliance",
                    "mL/mmHg", 1.5, 6.0));
            list.add(new Parameter21C(pvec, PVName.RA_SYSTOLIC_COMPLIANCE,
                    "Right Heart", "Compliance", "Right Atrium Systolic Compliance",
                    "mL/mmHg", 0.6, 2.7));
            list.add(new Parameter21C(pvec, PVName.TRICUSPID_VALVE_RESISTANCE,
                    "Right Heart", "Resistance", "Tricuspid Valve Resistance",
                    "PRU", 0.0, 0.015));
            list.add(new Parameter21C(pvec, PVName.RA_ZPFV,
                    "Right Heart", "Volume", "Right Atrium Zero-Pressure Filling Volume",
                    "mL", 10.0, 18.0));

            list.add(new Parameter21C(pvec, PVName.LV_DIASTOLIC_COMPLIANCE,
                    "Left Heart", "Compliance", "Left Ventricle Diastolic Compliance",
                    "mL/mmHg", 3.88, 15.11));
            list.add(new Parameter21C(pvec, PVName.LV_SYSTOLIC_COMPLIANCE,
                    "Left Heart", "Compliance", "Left Ventricle Systolic Compliance",
                    "mL/mmHg", 0.2, 0.77));
            list.add(new Parameter21C(pvec, PVName.AORTIC_VALVE_RESISTANCE,
                    "Left Heart", "Resistance", "Aortic Valve Resistance",
                    "PRU", 0.0, 0.013));
            list.add(new Parameter21C(pvec, PVName.LV_ZPFV,
                    "Left Heart", "Volume", "Left Ventricle Zero-Pressure Filling Volume",
                    "mL", 25.0, 85.0));

            list.add(new Parameter21C(pvec, PVName.RV_DIASTOLIC_COMPLIANCE,
                    "Right Heart", "Compliance", "Right Ventricle Diastolic Compliance",
                    "mL/mmHg", 7.0, 29.0));
            list.add(new Parameter21C(pvec, PVName.RV_SYSTOLIC_COMPLIANCE,
                    "Right Heart", "Compliance", "Right Ventricle Systolic Compliance",
                    "mL/mmHg", 0.3, 2.0));
            list.add(new Parameter21C(pvec, PVName.PUMONIC_VALVE_RESISTANCE,
                    "Right Heart", "Resistance", "Pulmonic Valve Resistance",
                    "PRU", 0.0, 0.015));
            list.add(new Parameter21C(pvec, PVName.RV_ZPFV,
                    "Right Heart", "Volume", "Right Ventricle Zero-Pressure Filling Volume",
                    "mL", 10.0, 82.0));

            // Peripheral Circulation
            list.add(new Parameter21C(pvec, PVName.ABDOM_AORTA_COMPLIANCE,
                    "Abdominal Aorta", "Compliance", "Abdominal Aorta Compliance",
                    "mL/mmHg", 0.07, 0.13));
            list.add(new Parameter21C(pvec, PVName.ABDOM_AORTA_RESISTANCE,
                    "Abdominal Aorta", "Resistance", "Abdominal Aorta Resistance",
                    "PRU", 0.0, 0.02));
            list.add(new Parameter21C(pvec, PVName.ABDOM_AORTA_ZPFV,
                    "Abdominal Aorta", "Volume", "Abdominal Aorta Zero-Pressure Filling Volume",
                    "mL", 7.0, 13.0));

            list.add(new Parameter21C(pvec, PVName.ABDOM_VEN_COMPLIANCE,
                    "Abdominal Veins", "Compliance", "Abdominal Veins Compliance",
                    "mL/mmHg", 1.0, 1.6));
            list.add(new Parameter21C(pvec, PVName.ABDOM_VEN_RESISTANCE,
                    "Abdominal Veins", "Resistance", "Abdominal Veins Resistance",
                    "PRU", 0.0, 0.04));
            list.add(new Parameter21C(pvec, PVName.ABDOM_VEN_ZPFV,
                    "Abdominal Veins", "Volume", "Abdominal Veins Zero-Pressure Filling Volume",
                    "mL", 49.0, 109.0));

            list.add(new Parameter21C(pvec, PVName.ASCENDING_AORTA_COMPLIANCE,
                    "Ascending Aorta", "Compliance", "Ascending Aorta Compliance",
                    "mL/mmHg", 0.16, 0.4));
            list.add(new Parameter21C(pvec, PVName.ASCENDING_AORTA_VOLUME,
                    "Ascending Aorta", "Volume",
                    "Ascending Aorta Zero-Pressure Filling Volume",
                    "mL", 10.0, 32.0));

            list.add(new Parameter21C(pvec, PVName.BRACH_ART_COMPLIANCE,
                    "Brachiocephalic Arteries", "Compliance",
                    "Brachiocephalic Arteries Compliance",
                    "mL/mmHg", 0.07, 0.2));
            list.add(new Parameter21C(pvec, PVName.BRACH_ART_RESISTANCE,
                    "Brachiocephalic Arteries", "Resistance",
                    "Brachiocephalic Arteries Resistance",
                    "PRU", 0.002, 0.026));
            list.add(new Parameter21C(pvec, PVName.BRACH_ART_ZPFV,
                    "Brachiocephalic Arteries", "Volume",
                    "Brachiocephalic Arteries Zero-Pressure Filling Volume",
                    "PRU", 2.0, 8.0));

            list.add(new Parameter21C(pvec, PVName.IVC_COMPLIANCE,
                    "Inferior Vena Cava", "Compliance", "Inferior Vena Cava Compliance",
                    "mL/mmHg", 0.2, 0.8));
            list.add(new Parameter21C(pvec, PVName.IVC_RESISTANCE,
                    "Inferior Vena Cava", "Resistance", "Inferior Vena Cava Resistance",
                    "PRU", 0.0, 0.017));
            list.add(new Parameter21C(pvec, PVName.IVC_ZPFV,
                    "Inferior Vena Cava", "Volume",
                    "Inferior Vena Cava Zero-Pressure Filling Volume",
                    "mL", 21.0, 45.0));

            list.add(new Parameter21C(pvec, PVName.LBODY_ART_COMPLIANCE,
                    "Lower Body Arteries", "Compliance", "Lower Body Arteries Compliance",
                    "mL/mmHg", 0.1, 0.7));
            list.add(new Parameter21C(pvec, PVName.LBODY_MICRO_RESISTANCE,
                    "Lower Body Microcirculation", "Resistance",
                    "Lower Body Microcirculation Resistance",
                    "PRU", 4.0, 10.3));
            list.add(new Parameter21C(pvec, PVName.LBODY_ART_ZPFV,
                    "Lower Body Arteries", "Volume",
                    "Lower Body Arteries Zero-Pressure Filling Volume",
                    "mL", 140.0, 260.0));

            list.add(new Parameter21C(pvec, PVName.LBODY_VEN_COMPLIANCE,
                    "Lower Body Veins", "Compliance", "Lower Body Veins Compliance",
                    "mL/mmHg", 11.0, 29.0));
            list.add(new Parameter21C(pvec, PVName.LBODY_VEN_RESISTANCE,
                    "Lower Body Veins", "Resistance", "Lower Body Veins Resistance",
                    "PRU", 0.0, 0.25));
            list.add(new Parameter21C(pvec, PVName.LBODY_VEN_ZPFV,
                    "Lower Body Veins", "Volume",
                    "Lower Body Veins Zero-Pressure Filling Volume",
                    "mL", 666.0, 866.0));

            list.add(new Parameter21C(pvec, PVName.RENAL_ART_COMPLIANCE,
                    "Renal Arteries", "Compliance", "Renal Arteries Compliance",
                    "mL/mmHg", 0.1, 0.3));
            list.add(new Parameter21C(pvec, PVName.RENAL_ART_RESISTANCE,
                    "Renal Arteries", "Resistance", "Renal Arteries Resistance",
                    "PRU", 0.0, 0.25));
            list.add(new Parameter21C(pvec, PVName.RENAL_ART_ZPFV,
                    "Renal Arteries", "Volume",
                    "Renal Arteries Zero-Pressure Filling Volume",
                    "mL", 5.0, 35.0));

            list.add(new Parameter21C(pvec, PVName.RENAL_VEN_COMPLIANCE,
                    "Renal Veins", "Compliance", "Renal Veins Compliance",
                    "mL/mmHg", 2.0, 8.0));
            list.add(new Parameter21C(pvec, PVName.RENAL_VEN_RESISTANCE,
                    "Renal Veins", "Resistance", "Renal Veins Resistance",
                    "PRU", 0.0, 0.26));
            list.add(new Parameter21C(pvec, PVName.RENAL_VEN_ZPFV,
                    "Renal Veins", "Volume", "Renal Veins Zero-Pressure Filling Volume",
                    "mL", 10.0, 60.0));

            list.add(new Parameter21C(pvec, PVName.SPLAN_ART_COMPLIANCE,
                    "Splanchnic Arteries", "Compliance", "Splanchnic Arteries Compliance",
                    "mL/mmHg", 0.10, 0.70));
            list.add(new Parameter21C(pvec, PVName.SPLAN_ART_RESISTANCE,
                    "Splanchnic Arteries", "Resistance", "Splanchnic Arteries Resistance",
                    "PRU", 0.0, 0.19));
            list.add(new Parameter21C(pvec, PVName.SPLAN_ART_ZPFV,
                    "Splanchnic Arteries", "Volume",
                    "Splanchnic Arteries Zero-Pressure Filling Volume",
                    "mL", 150.0, 450.0));

            list.add(new Parameter21C(pvec, PVName.SPLAN_VEN_COMPLIANCE,
                    "Splanchnic Veins", "Compliance", "Splanchnic Veins Compliance",
                    "mL/mmHg", 27.5, 72.5));
            list.add(new Parameter21C(pvec, PVName.SPLAN_VEN_RESISTANCE,
                    "Splanchnic Veins", "Resistance", "Splanchnic Veins Resistance",
                    "PRU", 0.0, 0.19));
            list.add(new Parameter21C(pvec, PVName.SPLAN_VEN_ZPFV,
                    "Splanchnic Veins", "Volume",
                    "Splanchnic Veins Zero-Pressure Filling Volume",
                    "mL", 850.0, 1450.0));

            list.add(new Parameter21C(pvec, PVName.SVC_COMPLIANCE,
                    "Superior Vena Cava", "Compliance",
                    "Superior Vena Cava Compliance",
                    "mL/mmHg", 1.0, 1.6));
            list.add(new Parameter21C(pvec, PVName.SVC_RESISTANCE,
                    "Superior Vena Cava", "Resistance",
                    "Superior Vena Cava Resistance",
                    "PRU", 0.0, 0.056));
            list.add(new Parameter21C(pvec, PVName.SVC_ZPFV,
                    "Superior Vena Cava", "Volume",
                    "Superior Vena Cava Zero-Pressure Filling Volume",
                    "mL", 4.0, 28.0));

            list.add(new Parameter21C(pvec, PVName.THORACIC_AORTA_COMPLIANCE,
                    "Thoracic Aorta", "Compliance", "Thoracic Aorta Compliance",
                    "mL/mmHg", 0.05, 0.30));
            list.add(new Parameter21C(pvec, PVName.THORACIC_AORTA_RESISTANCE,
                    "Thoracic Aorta", "Resistance", "Thoracic Aorta Resistance",
                    "PRU", 0.005, 0.017));
            list.add(new Parameter21C(pvec, PVName.THORACIC_AORTA_ZPFV,
                    "Thoracic Aorta", "Volume",
                    "Thoracic Aorta Zero-Pressure Filling Volume",
                    "mL", 80.0, 320.0));

            list.add(new Parameter21C(pvec, PVName.UBODY_ART_COMPLIANCE,
                    "Upper Body Arteries", "Compliance",
                    "Upper Body Arteries Compliance",
                    "mL/mmHg", 0.1, 0.7));
            list.add(new Parameter21C(pvec, PVName.UBODY_ART_RESISTANCE,
                    "Upper Body Arteries", "Resistance",
                    "Upper Body Arteries Resistance",
                    "PRU", 3.3, 6.5));
            list.add(new Parameter21C(pvec, PVName.UBODY_ART_ZPFV,
                    "Upper Body Arteries", "Volume",
                    "Upper Body Arteries Zero-Pressure Filling Volume",
                    "mL", 10.0, 32.0));

            list.add(new Parameter21C(pvec, PVName.UBODY_VEN_COMPLIANCE,
                    "Upper Body Veins", "Compliance",
                    "Upper Body Veins Compliance",
                    "mL/mmHg", 1.0, 13.0));
            list.add(new Parameter21C(pvec, PVName.UBODY_VEN_RESISTANCE,
                    "Upper Body Veins", "Resistance",
                    "Upper Body Veins Resistance",
                    "PRU", 0.0, 0.26));
            list.add(new Parameter21C(pvec, PVName.UBODY_VEN_ZPFV,
                    "Upper Body Veins", "Volume",
                    "Upper Body Veins Zero-Pressure Filling Volume",
                    "mL", 425.0, 765.0));

            // Pulmonary 
            list.add(new Parameter21C(pvec, PVName.PULM_ART_COMPLIANCE,
                    "Pulmonary Arteries", "Compliance",
                    "Pulmonary Arterial Compliance",
                    "mL/mmHg", 1.5, 7.2));
            list.add(new Parameter21C(pvec, PVName.PULM_ART_ZPFV,
                    "Pulmonary Arteries", "Volume",
                    "Pulmonary Arterial Zero-Pressure Filling Volume",
                    "mL", 100.0, 220.0));

            list.add(new Parameter21C(pvec, PVName.PULM_VEN_COMPLIANCE,
                    "Pulmonary Veins", "Compliance",
                    "Pulmonary Venous Compliance",
                    "mL/mmHg", 5.3, 12.7));
            list.add(new Parameter21C(pvec, PVName.PULM_VEN_RESISTANCE,
                    "Pulmonary Veins", "Resistance",
                    "Pulmonary Venous Resistance",
                    "PRU", 0.0, 0.015));
            list.add(new Parameter21C(pvec, PVName.PULN_VEN_ZPFV,
                    "Pulmonary Veins", "Volume",
                    "Pulmonary Venous Zero-Pressure Filling Volume",
                    "mL", 180.0, 580.0));

            // Microvascular Resistance
            list.add(new Parameter21C(pvec, PVName.UBODY_MICRO_RESISTANCE,
                    "Upper Body Microcirculation", "Resistance",
                    "Upper Body Microcirculation Resistance",
                    "PRU", 3.3, 6.5));
            list.add(new Parameter21C(pvec, PVName.SPLAN_MICRO_RESISTANCE,
                    "Splanchnic Microcirculation", "Resistance",
                    "Splanchnic Microcirculation Resistance",
                    "PRU", 2.3, 4.3));
            list.add(new Parameter21C(pvec, PVName.RENAL_MICRO_RESISTANCE,
                    "Renal Microcirculation", "Resistance",
                    "Renal Microcirculation Resistance",
                    "PRU", 3.2, 6.2));
            list.add(new Parameter21C(pvec, PVName.LBODY_MICRO_RESISTANCE,
                    "Lower Body Microcirculation", "Resistance",
                    "Lower Body Microcirculation Resistance",
                    "PRU", 4.0, 10.3));
            list.add(new Parameter21C(pvec, PVName.PULM_MICRO_RESISTANCE,
                    "Pulmonary Microcirculation", "Resistance",
                    "Pulmonary Microcirculation Resistance",
                    "PRU", 0.0, 0.19));

            // System Parameters
            list.add(new Parameter21C(pvec, PVName.INTRA_THORACIC_PRESSURE,
                    "System Parameters", "Pressure", "Intra-thoracic Pressure",
                    "mmHg", -20.0, 40.0));
            list.add(new Parameter21C(pvec, PVName.NOMINAL_HEART_RATE,
                    "System Parameters", "Heart Rate", "Nominal Heart Rate",
                    "beats/min", 50.0, 85.0));
            list.add(new Parameter21C(pvec, PVName.TOTAL_BLOOD_VOLUME,
                    "System Parameters", "Volume", "Total Blood Volume",
                    "mL", 4041.0, 6460.0));

            // Control System Parameters    
            // Arterial Baroreflex 
            list.add(new Parameter21C(pvec, PVName.ABR_SET_POINT,
                    "Arterial Baroreflex", "Set Point",
                    "ABR Set Point", "mmHg", 89.0, 105.0));

            list.add(new Parameter21C(pvec, PVName.ABR_HR_SYMPATHETIC_GAIN,
                    "Arterial Baroreflex", "Gain",
                    "ABR Heart Rate Sympathetic Gain", "ms/mmHg", 0.005, 0.017));

            list.add(new Parameter21C(pvec, PVName.ABR_HR_PARASYMPATHETIC_GAIN,
                    "Arterial Baroreflex", "Gain",
                    "ABR Heart Rate Parasympathetic Gain", "ms/mmHg", 0.005, 0.017));

            list.add(new Parameter21C(pvec, PVName.ABR_VEN_TONE_SYMPATHETIC_GAIN_TO_UPPER_BODY,
                    "Arterial Baroreflex", "Gain",
                    "ABR Venous Tone Sympathetic Gain to Upper Body", "ms/mmHg", 3.6, 6.15));

            list.add(new Parameter21C(pvec, PVName.ABR_VEN_TONE_SYMPATHETIC_GAIN_TO_KIDNEY,
                    "Arterial Baroreflex", "Gain",
                    "ABR Venous Tone Sympathetic Gain to Kidney", "ms/mmHg", 0.7, 2.0));

            list.add(new Parameter21C(pvec, PVName.ABR_VEN_TONE_SYMPATHETIC_GAIN_TO_SPLANCHNIC,
                    "Arterial Baroreflex", "Gain",
                    "ABR Venous Tone Sympathetic Gain to Splanchnic", "ms/mmHg", 9.0, 17.6));

            list.add(new Parameter21C(pvec, PVName.ABR_VEN_TONE_SYMPATHETIC_GAIN_TO_LOWER_BODY,
                    "Arterial Baroreflex", "Gain",
                    "ABR Venous Tone Sympathetic Gain to Lower Body", "ms/mmHg", 4.5, 9.0));

            list.add(new Parameter21C(pvec, PVName.ABR_ART_RES_SYMPATHETIC_GAIN_TO_UPPER_BODY,
                    "Arterial Baroreflex", "Gain",
                    "ABR Arterial Resistance Sympathetic Gain to Upper Body",
                    "mL/mmHg^2", -0.15, -0.05));

            list.add(new Parameter21C(pvec, PVName.ABR_ART_RES_SYMPATHETIC_GAIN_TO_KIDNEY,
                    "Arterial Baroreflex", "Gain",
                    "ABR Arterial Resistance Sympathetic Gain to Kidney",
                    "mL/mmHg^2", -0.15, -0.05));

            list.add(new Parameter21C(pvec, PVName.ABR_ART_RES_SYMPATHETIC_GAIN_TO_SPLANCHNIC,
                    "Arterial Baroreflex", "Gain",
                    "ABR Arterial Resistance Sympathetic Gain to Splanchnic",
                    "mL/mmHg^2", -0.15, -0.05));

            list.add(new Parameter21C(pvec, PVName.ABR_ART_RES_SYMPATHETIC_GAIN_TO_LOWER_BODY,
                    "Arterial Baroreflex", "Gain",
                    "ABR Arterial Resistance Sympathetic Gain to Lower Body",
                    "mL/mmHg^2", -0.15, -0.05));

            list.add(new Parameter21C(pvec, PVName.ABR_RV_CONTRACTILITY_SYMPATHETIC_GAIN,
                    "Arterial Baroreflex", "Gain",
                    "ABR Contractility Right Ventricle Sympathetic Gain",
                    "mL/mmHg^2", 0.007, 0.03));
            list.add(new Parameter21C(pvec, PVName.ABR_LV_CONTRACTILITY_SYMPATHETIC_GAIN,
                    "Arterial Baroreflex", "Gain",
                    "ABR Contractility Left Ventricle Sympathetic Gain",
                    "mL/mmHg^2", 0.004, 0.014));

            // Cardiopulmonary Reflex
            list.add(new Parameter21C(pvec, PVName.CPR_SET_POINT,
                    "Cardiopulmonary Reflex", "Set Point",
                    "CPR Set Point", "mmHg", 4.0, 10.0));
            list.add(new Parameter21C(pvec, PVName.CPR_VEN_SYMPATHETIC_GAIN_TO_UBODY,
                    "Cardiopulmonary Reflex", "Gain",
                    "CPR Venous Tone Sympathetic Gain to Upper Body", "mL/mmHg^2", 8.1, 19.0));
            list.add(new Parameter21C(pvec, PVName.CPR_VEN_SYMPATHETIC_GAIN_TO_KIDNEY,
                    "Cardiopulmonary Reflex", "Gain",
                    "CPR Venous Tone Sympathetic Gain to Kidney",
                    "mL/mmHg^2", 2.2, 3.2));
            list.add(new Parameter21C(pvec, PVName.CPR_VEN_SYMPATHETIC_GAIN_TO_SPLANCHNIC,
                    "Cardiopulmonary Reflex", "Gain",
                    "CPR Venous Tone Sympathetic Gain to Splanchnic",
                    "mL/mmHg^2", 38.4, 90.0));
            list.add(new Parameter21C(pvec, PVName.CPR_VEN_SYMPATHETIC_GAIN_TO_LBODY,
                    "Cardiopulmonary Reflex", "Gain",
                    "CPR Venous Tone Sympathetic Gain to Lower Body",
                    "mL/mmHg^2", 18.0, 42.0));
            list.add(new Parameter21C(pvec, PVName.CPR_ART_RES_SYMPATHETIC_GAIN_TO_UBODY,
                    "Cardiopulmonary Reflex", "Gain",
                    "CPR Arterial Resistance Sympathetic Gain to Upper Body",
                    "mL/mmHg^2", -0.4, -0.2));
            list.add(new Parameter21C(pvec, PVName.CPR_ART_RES_SYMPATHETIC_GAIN_TO_KIDNEY,
                    "Cardiopulmonary Reflex", "Gain",
                    "CPR Arterial Resistance Sympathetic Gain to Kidney",
                    "mL/mmHg^2", -0.4, -0.2));
            list.add(new Parameter21C(pvec, PVName.CPR_ART_RES_SYMPATHETIC_GAIN_TO_SPLANCHNIC,
                    "Cardiopulmonary Reflex", "Gain",
                    "CPR Arterial Resistance Sympathetic Gain to Splanchnic",
                    "mL/mmHg^2", -0.4, -0.2));
            list.add(new Parameter21C(pvec, PVName.CPR_ART_RES_SYMPATHETIC_GAIN_TO_LBODY,
                    "Cardiopulmonary Reflex", "Gain",
                    "CPR Arterial Resistance Sympathetic Gain to Lower Body",
                    "mL/mmHg^2", -0.4, -0.2));

            // register listeners for each parameter in the list, so that
            // we can notify our listeners of parameter changes.
            for (Parameter p : list) {
                p.addPropertyChangeListener(SimulationParameterChangeListener);
            }

            // alphabetize list by Parameter name	    
            Parameter[] array = new Parameter[list.size()];
            ComparatorX c = new ComparatorX();
            list.toArray(array);
            Arrays.sort(array, c);
            list = Arrays.asList(array);

            return list;
        }
    }
}
