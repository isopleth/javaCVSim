package jcvsim.backend21compartment;

import static java.lang.Math.PI;
import jcvsim.backendCommon.Turning;
import static jcvsim.backend21compartment.Output_vector.N_SAMPLES;
import static jcvsim.backendCommon.Maths.tan;
import static jcvsim.backend21compartment.Data_vector.CompartmentIndexes.*;
import static jcvsim.backend21compartment.Data_vector.TimeIndexes.*;

/*
 * This file was created in order to run the simulation from the Java GUI.
 * The functions in this file combine the main() function in main.c and
 * the simulator() function in simulator.c. The simulation was broken up into
 * into three parts: an init_sim() function to initialize the simulation,
 * a step_sim() function to advance the simulation one timestep, and a
 * reset_sim()
 * function to reset variables to their initial values.
 *
 * Catherine Dunn July 10, 2006
 * Last modified July 10, 2006
 */
// Converted to Java Jason Leake December 2016
public class Main {

    int N_OUT = 21;           // Number of output variables in the steady-state
    // analysis
    private double[] result;           // output vector containing the output variables.
    private double[] target;

// Structure handling the flow of data within the simulation.c routine.
// See simulator.h for the definition.
    Data_vector pressure = new Data_vector();

// Structure for the reflex variables.
    Reflex_vector reflex_vector = new Reflex_vector();

// Structure for the impulse response functions
    Impulse_vector imp = new Impulse_vector();

// Simulator related definitions
    Output_vector out = new Output_vector();

    // The following definitions pertain to the adaptive stepsize integration
    // routine. See Numerical Recipes in C (p. ???) for details.
    // The single element arrays are used to allow the RK method to set multiple
    // return values.
    double[] hdid = {0.0};     // hdid[0] is set by the RK method to the stepsize that was accomplished
    double htry = 0.00001;
    double yscale[] = {1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1.};
    double[] hnext = {0.0};    // hnext[0] is set to the RK method to the estimated next stepsize 

    private static Main theInstance;

    private static final int N_SIGNALS = 6;

    static public Main instance() {
        if (theInstance == null) {
            theInstance = new Main();
        }
        return theInstance;
    }

    // Singleton, so prevent constructor from being called from outside of the class
    private Main() {

    }

    public void init_sim(Parameter_vector a) {

        // Declare parameter value structures.
        Hemo[] hemo = new Hemo[17];
        for (int index = 0; index < hemo.length; index++) {
            hemo[index] = new Hemo();
        }
        Cardiac[] cardiac = new Cardiac[2];
        cardiac[0] = new Cardiac();
        cardiac[1] = new Cardiac();
        Reflex_parameters[] reflex = new Reflex_parameters[2];
        reflex[0] = new Reflex_parameters();
        reflex[1] = new Reflex_parameters();
        Micro_r micro_r = new Micro_r();
        System_parameters system = new System_parameters();
        Timing timing = new Timing();

        // Allocate memory for data structures.
        target = new double[N_SIGNALS * N_SAMPLES];
        result = new double[N_OUT];

        // Initialize the parameter structures and the parameter vector.
        Initial.initial_ptr(hemo, cardiac, micro_r, system, reflex, timing);
        Initial.mapping_ptr(hemo, cardiac, micro_r, system, reflex, timing, a);

        // The following function calls initialize the data vector. Estimate()
        // initializes the pressure.x[] and pressure.time[] arrays. Elastance()
        // computes the cardiac compliances and their derivatives and stores their
        // values in pressure.c[] and pressure.dcdt[], respectively. Finally,
        // eqns() computes the pressure derivatives and stores them in 
        // pressure.dxdt[].
        Estimate.estimate_ptr(pressure, a, reflex_vector);      // estimate initial pressures

        // Set up the values for the time-varying capacitance values
        Equation.elastance_ptr(pressure, a);

        Equation.fixvolume_ptr(pressure, reflex_vector, a);     // fix the total blood volume

        Equation.eqns_ptr(pressure, a, reflex_vector, false, 0, 0); // Initialize pressure derivatives

        Reflex.makeImp(imp, a);                    // Set up the impulse resp. arrays

        Simulator_numerics.numerics(pressure, reflex_vector, out, a);

    }

// This function is needed to update pressure.x[] from the Java code.
// The output struct copies the values in pressure.x and makes them available to 
// the Java code. The output struct is in a sense read-only. If the values in the 
// output struct are changed, the change is not reflected in pressure.x. However, 
// occasionally (as in the case of parameter updates), information must pass from
// the GUI to the simulation variables. This function was created to allow 
// the changes to output.x to be reflected in pressure.x.
    public void updatePressure(int index, double value) {
        pressure.pressure[index] = value;
    }

// step_sim(): Advances the simulation one timestep
// By varying the dataCompressionFactor the data can be compressed before 
// being passed to the GUI. A dataCompressionFactor of 10 means one piece 
// of output data is passed to the GUI for every 10 timesteps. A 
// dataCompressionFactor of 1 means every piece of data is passed to the GUI.
// (One piece of data for every timestep.)
    public void step_sim(Output stepout, Parameter_vector pvec, int dataCompressionFactor,
            boolean ABReflexOn, boolean CPReflexOn,
            boolean tiltTestOn, double tiltStartTime, double tiltStopTime) {
        double[] ascendingAorticPressure = new double[dataCompressionFactor];
        double[] brachiocephalicArterialPressure = new double[dataCompressionFactor];
        double[] upperBodyArterialPressure = new double[dataCompressionFactor];
        double[] upperBodyVenousPressure = new double[dataCompressionFactor];
        double[] superiorVenaCavaPressure = new double[dataCompressionFactor];
        double[] thoracicAorticPressure = new double[dataCompressionFactor];
        double[] abdominalAorticPressure = new double[dataCompressionFactor];
        double[] renalArterialPressure = new double[dataCompressionFactor];
        double[] renalVenousPressure = new double[dataCompressionFactor];
        double[] splanchnicArterialPressure = new double[dataCompressionFactor];
        double[] splanchnicVenousPressure = new double[dataCompressionFactor];
        double[] lowerBodyArterialPressure = new double[dataCompressionFactor];
        double[] lowerBodyVenousPressure = new double[dataCompressionFactor];
        double[] abdominalVenousPressure = new double[dataCompressionFactor];
        double[] inferiorVenaCavaPressure = new double[dataCompressionFactor];
        double[] rightAtrialPressure = new double[dataCompressionFactor];
        double[] rightVentricularPressure = new double[dataCompressionFactor];
        double[] pulmonaryArterialPressure = new double[dataCompressionFactor];
        double[] pulmonaryVenousPressure = new double[dataCompressionFactor];
        double[] leftAtrialPressure = new double[dataCompressionFactor];
        double[] leftVentricularPressure = new double[dataCompressionFactor];

        double[] ascendingAorticFlow = new double[dataCompressionFactor];
        double[] brachiocephalicArterialFlow = new double[dataCompressionFactor];
        double[] upperBodyArterialFlow = new double[dataCompressionFactor];
        double[] upperBodyVenousFlow = new double[dataCompressionFactor];
        double[] superiorVenaCavaFlow = new double[dataCompressionFactor];
        double[] thoracicAorticFlow = new double[dataCompressionFactor];
        double[] abdominalAorticFlow = new double[dataCompressionFactor];
        double[] renalArterialFlow = new double[dataCompressionFactor];
        double[] renalVenousFlow = new double[dataCompressionFactor];
        double[] splanchnicArterialFlow = new double[dataCompressionFactor];
        double[] splanchnicVenousFlow = new double[dataCompressionFactor];
        double[] lowerBodyArterialFlow = new double[dataCompressionFactor];
        double[] lowerBodyVenousFlow = new double[dataCompressionFactor];
        double[] abdominalVenousFlow = new double[dataCompressionFactor];
        double[] inferiorVenaCavaFlow = new double[dataCompressionFactor];
        double[] rightAtrialFlow = new double[dataCompressionFactor];
        double[] rightVentricularFlow = new double[dataCompressionFactor];
        double[] pulmonaryArterialFlow = new double[dataCompressionFactor];
        double[] pulmonaryVenousFlow = new double[dataCompressionFactor];
        double[] leftAtrialFlow = new double[dataCompressionFactor];
        double[] leftVentricularFlow = new double[dataCompressionFactor];

        double[] ascendingAorticVolume = new double[dataCompressionFactor];
        double[] brachiocephalicArterialVolume = new double[dataCompressionFactor];
        double[] upperBodyArterialVolume = new double[dataCompressionFactor];
        double[] upperBodyVenousVolume = new double[dataCompressionFactor];
        double[] superiorVenaCavaVolume = new double[dataCompressionFactor];
        double[] thoracicAorticVolume = new double[dataCompressionFactor];
        double[] abdominalAorticVolume = new double[dataCompressionFactor];
        double[] renalArterialVolume = new double[dataCompressionFactor];
        double[] renalVenousVolume = new double[dataCompressionFactor];
        double[] splanchnicArterialVolume = new double[dataCompressionFactor];
        double[] splanchnicVenousVolume = new double[dataCompressionFactor];
        double[] lowerBodyArterialVolume = new double[dataCompressionFactor];
        double[] lowerBodyVenousVolume = new double[dataCompressionFactor];
        double[] abdominalVenousVolume = new double[dataCompressionFactor];
        double[] inferiorVenaCavaVolume = new double[dataCompressionFactor];
        double[] rightAtrialVolume = new double[dataCompressionFactor];
        double[] rightVentricularVolume = new double[dataCompressionFactor];
        double[] pulmonaryArterialVolume = new double[dataCompressionFactor];
        double[] pulmonaryVenousVolume = new double[dataCompressionFactor];
        double[] leftAtrialVolume = new double[dataCompressionFactor];
        double[] leftVentricularVolume = new double[dataCompressionFactor];

        double[] hr = new double[dataCompressionFactor];
        double[] ar = new double[dataCompressionFactor];
        double[] vt = new double[dataCompressionFactor];
        double[] rvc = new double[dataCompressionFactor];
        double[] lvc = new double[dataCompressionFactor];

        double[] totalBloodVolume = new double[dataCompressionFactor];
        double[] intraThoracicPressure = new double[dataCompressionFactor];

        double[] tiltAngle = new double[dataCompressionFactor];

        // Calculate output values
        for (int index = 0; index < dataCompressionFactor; index++) {

            Rkqc.rkqc_ptr(pressure, reflex_vector, pvec, htry, 0.001, yscale, hdid, hnext,
                    tiltTestOn, tiltStartTime, tiltStopTime);

            pressure.time[CARDIAC_TIME] = Reflex.sanode(pressure, reflex_vector, pvec, hdid[0]);
            Equation.elastance_ptr(pressure, pvec);
            Equation.eqns_ptr(pressure, pvec, reflex_vector, tiltTestOn, tiltStartTime, tiltStopTime);
            pressure.time[ABSOLUTE_TIME] += hdid[0];

            //htry = (hnext > 0.001 ? 0.001 : hnext);
            htry = 0.001;
            Reflex.queue_ptr(pressure, imp, reflex_vector, pvec, hdid[0], ABReflexOn, CPReflexOn);
            Equation.fixvolume_ptr(pressure, reflex_vector, pvec);

            Simulator_numerics_new.numerics_new_ptr(pressure, reflex_vector, hdid[0], result);
            Simulator_numerics.numerics(pressure, reflex_vector, out, pvec);

            // simulation time
            stepout.time = pressure.time[ABSOLUTE_TIME];

            ascendingAorticPressure[index] = pressure.pressure[ASCENDING_AORTIC_CPI];
            brachiocephalicArterialPressure[index] = pressure.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI];
            upperBodyArterialPressure[index] = pressure.pressure[UPPER_BODY_ARTERIAL_CPI];
            upperBodyVenousPressure[index] = pressure.pressure[UPPER_BODY_VENOUS_CPI];
            superiorVenaCavaPressure[index] = pressure.pressure[SUPERIOR_VENA_CAVA_CPI];
            thoracicAorticPressure[index] = pressure.pressure[THORACIC_AORTIC_CPI];
            abdominalAorticPressure[index] = pressure.pressure[ABDOMINAL_AORTIC_CPI];
            renalArterialPressure[index] = pressure.pressure[RENAL_ARTERIAL_CPI];
            renalVenousPressure[index] = pressure.pressure[RENAL_VENOUS_CPI];
            splanchnicArterialPressure[index] = pressure.pressure[SPLANCHNIC_ARTERIAL_CPI];
            splanchnicVenousPressure[index] = pressure.pressure[SPLANCHNIC_VENOUS_CPI];
            lowerBodyArterialPressure[index] = pressure.pressure[LOWER_BODY_ARTERIAL_CPI];
            lowerBodyVenousPressure[index] = pressure.pressure[LOWER_BODY_VENOUS_CPI];
            abdominalVenousPressure[index] = pressure.pressure[ABDOMINAL_VENOUS_CPI];
            inferiorVenaCavaPressure[index] = pressure.pressure[INFERIOR_VENA_CAVA_CPI];
            rightAtrialPressure[index] = pressure.pressure[RIGHT_ATRIAL_CPI];
            rightVentricularPressure[index] = pressure.pressure[RIGHT_VENTRICULAR_CPI];
            pulmonaryArterialPressure[index] = pressure.pressure[PULMONARY_ARTERIAL_CPI];
            pulmonaryVenousPressure[index] = pressure.pressure[PULMONARY_VENOUS_CPI];
            leftAtrialPressure[index] = pressure.pressure[LEFT_ATRIAL_CPI];
            leftVentricularPressure[index] = pressure.pressure[LEFT_VENTRICULAR_CPI];

            ascendingAorticFlow[index] = pressure.flowRate[ASCENDING_AORTIC_CPI];
            brachiocephalicArterialFlow[index] = pressure.flowRate[BRACHIOCEPHALIC_ARTERIAL_CPI];
            upperBodyArterialFlow[index] = pressure.flowRate[UPPER_BODY_ARTERIAL_CPI];
            upperBodyVenousFlow[index] = pressure.flowRate[UPPER_BODY_VENOUS_CPI];
            superiorVenaCavaFlow[index] = pressure.flowRate[SUPERIOR_VENA_CAVA_CPI];
            thoracicAorticFlow[index] = pressure.flowRate[THORACIC_AORTIC_CPI];
            abdominalAorticFlow[index] = pressure.flowRate[ABDOMINAL_AORTIC_CPI];
            renalArterialFlow[index] = pressure.flowRate[RENAL_ARTERIAL_CPI];
            renalVenousFlow[index] = pressure.flowRate[RENAL_VENOUS_CPI];
            splanchnicArterialFlow[index] = pressure.flowRate[SPLANCHNIC_ARTERIAL_CPI];
            splanchnicVenousFlow[index] = pressure.flowRate[SPLANCHNIC_VENOUS_CPI];
            lowerBodyArterialFlow[index] = pressure.flowRate[LOWER_BODY_ARTERIAL_CPI];
            lowerBodyVenousFlow[index] = pressure.flowRate[LOWER_BODY_VENOUS_CPI];
            abdominalVenousFlow[index] = pressure.flowRate[ABDOMINAL_VENOUS_CPI];
            inferiorVenaCavaFlow[index] = pressure.flowRate[INFERIOR_VENA_CAVA_CPI];
            rightAtrialFlow[index] = pressure.flowRate[RIGHT_ATRIAL_CPI];
            rightVentricularFlow[index] = pressure.flowRate[RIGHT_VENTRICULAR_CPI];
            pulmonaryArterialFlow[index] = pressure.flowRate[PULMONARY_ARTERIAL_CPI];
            pulmonaryVenousFlow[index] = pressure.flowRate[PULMONARY_VENOUS_CPI];
            leftAtrialFlow[index] = pressure.flowRate[LEFT_ATRIAL_CPI];
            leftVentricularFlow[index] = pressure.flowRate[LEFT_VENTRICULAR_CPI];

            ascendingAorticVolume[index] = pressure.volume[ASCENDING_AORTIC_CPI];
            brachiocephalicArterialVolume[index] = pressure.volume[BRACHIOCEPHALIC_ARTERIAL_CPI];
            upperBodyArterialVolume[index] = pressure.volume[UPPER_BODY_ARTERIAL_CPI];
            upperBodyVenousVolume[index] = pressure.volume[UPPER_BODY_VENOUS_CPI];
            superiorVenaCavaVolume[index] = pressure.volume[SUPERIOR_VENA_CAVA_CPI];
            thoracicAorticVolume[index] = pressure.volume[THORACIC_AORTIC_CPI];
            abdominalAorticVolume[index] = pressure.volume[ABDOMINAL_AORTIC_CPI];
            renalArterialVolume[index] = pressure.volume[RENAL_ARTERIAL_CPI];
            renalVenousVolume[index] = pressure.volume[RENAL_VENOUS_CPI];
            splanchnicArterialVolume[index] = pressure.volume[SPLANCHNIC_ARTERIAL_CPI];
            splanchnicVenousVolume[index] = pressure.volume[SPLANCHNIC_VENOUS_CPI];
            lowerBodyArterialVolume[index] = pressure.volume[LOWER_BODY_ARTERIAL_CPI];
            lowerBodyVenousVolume[index] = pressure.volume[LOWER_BODY_VENOUS_CPI];
            abdominalVenousVolume[index] = pressure.volume[ABDOMINAL_VENOUS_CPI];
            inferiorVenaCavaVolume[index] = pressure.volume[INFERIOR_VENA_CAVA_CPI];
            rightAtrialVolume[index] = pressure.volume[RIGHT_ATRIAL_CPI];
            rightVentricularVolume[index] = pressure.volume[RIGHT_VENTRICULAR_CPI];
            pulmonaryArterialVolume[index] = pressure.volume[PULMONARY_ARTERIAL_CPI];
            pulmonaryVenousVolume[index] = pressure.volume[PULMONARY_VENOUS_CPI];
            leftAtrialVolume[index] = pressure.volume[LEFT_ATRIAL_CPI];
            leftVentricularVolume[index] = pressure.volume[LEFT_VENTRICULAR_CPI];

            hr[index] = reflex_vector.hr[2];
            ar[index] = reflex_vector.resistance[0];
            vt[index] = reflex_vector.volume[0];
            rvc[index] = reflex_vector.compliance[0];
            lvc[index] = reflex_vector.compliance[1];

            totalBloodVolume[index] = pvec.get(PVName.TOTAL_BLOOD_VOLUME);
            intraThoracicPressure[index] = pvec.get(PVName.INTRA_THORACIC_PRESSURE);

            tiltAngle[index] = pressure.tilt_angle;

        } // end for

        // Compress the output by applying turning algorithm
        stepout.ascendingAorticPressure = Turning.turning(ascendingAorticPressure, dataCompressionFactor);
        stepout.brachiocephalicArterialPressure = Turning.turning(brachiocephalicArterialPressure, dataCompressionFactor);
        stepout.upperBodyArterialPressure = Turning.turning(upperBodyArterialPressure, dataCompressionFactor);
        stepout.upperBodyVenousPressure = Turning.turning(upperBodyVenousPressure, dataCompressionFactor);
        stepout.superiorVenaCavaPressure = Turning.turning(superiorVenaCavaPressure, dataCompressionFactor);
        stepout.thoracicAorticPressure = Turning.turning(thoracicAorticPressure, dataCompressionFactor);
        stepout.abdominalAorticPressure = Turning.turning(abdominalAorticPressure, dataCompressionFactor);
        stepout.renalArterialPressure = Turning.turning(renalArterialPressure, dataCompressionFactor);
        stepout.renalVenousPressure = Turning.turning(renalVenousPressure, dataCompressionFactor);
        stepout.splanchnicArterialPressure = Turning.turning(splanchnicArterialPressure, dataCompressionFactor);
        stepout.splanchnicVenousPressure = Turning.turning(splanchnicVenousPressure, dataCompressionFactor);
        stepout.lowerBodyArterialPressure = Turning.turning(lowerBodyArterialPressure, dataCompressionFactor);
        stepout.lowerBodyVenousPressure = Turning.turning(lowerBodyVenousPressure, dataCompressionFactor);
        stepout.abdominalVenousPressure = Turning.turning(abdominalVenousPressure, dataCompressionFactor);
        stepout.inferiorVenaCavaPressure = Turning.turning(inferiorVenaCavaPressure, dataCompressionFactor);
        stepout.rightAtrialPressure = Turning.turning(rightAtrialPressure, dataCompressionFactor);
        stepout.rightVentricularPressure = Turning.turning(rightVentricularPressure, dataCompressionFactor);
        stepout.pulmonaryArterialPressure = Turning.turning(pulmonaryArterialPressure, dataCompressionFactor);
        stepout.pulmonaryVenousPressure = Turning.turning(pulmonaryVenousPressure, dataCompressionFactor);
        stepout.leftAtrialPressure = Turning.turning(leftAtrialPressure, dataCompressionFactor);
        stepout.leftVentricularPressure = Turning.turning(leftVentricularPressure, dataCompressionFactor);

        stepout.ascendingAorticFlow = Turning.turning(ascendingAorticFlow, dataCompressionFactor);
        stepout.brachiocephalicArterialFlow = Turning.turning(brachiocephalicArterialFlow, dataCompressionFactor);
        stepout.upperBodyArterialFlow = Turning.turning(upperBodyArterialFlow, dataCompressionFactor);
        stepout.upperBodyVenousFlow = Turning.turning(upperBodyVenousFlow, dataCompressionFactor);
        stepout.superiorVenaCavaFlow = Turning.turning(superiorVenaCavaFlow, dataCompressionFactor);
        stepout.thoracicAorticFlow = Turning.turning(thoracicAorticFlow, dataCompressionFactor);
        stepout.abdominalAorticFlow = Turning.turning(abdominalAorticFlow, dataCompressionFactor);
        stepout.renalArterialFlow = Turning.turning(renalArterialFlow, dataCompressionFactor);
        stepout.renalVenousFlow = Turning.turning(renalVenousFlow, dataCompressionFactor);
        stepout.splanchnicArterialFlow = Turning.turning(splanchnicArterialFlow, dataCompressionFactor);
        stepout.splanchnicVenousFlow = Turning.turning(splanchnicVenousFlow, dataCompressionFactor);
        stepout.lowerBodyArterialFlow = Turning.turning(lowerBodyArterialFlow, dataCompressionFactor);
        stepout.lowerBodyVenousFlow = Turning.turning(lowerBodyVenousFlow, dataCompressionFactor);
        stepout.abdominalVenousFlow = Turning.turning(abdominalVenousFlow, dataCompressionFactor);
        stepout.inferiorVenaCavaFlow = Turning.turning(inferiorVenaCavaFlow, dataCompressionFactor);
        stepout.rightAtrialFlow = Turning.turning(rightAtrialFlow, dataCompressionFactor);
        stepout.rightVentricularFlow = Turning.turning(rightVentricularFlow, dataCompressionFactor);
        stepout.pulmonaryArterialFlow = Turning.turning(pulmonaryArterialFlow, dataCompressionFactor);
        stepout.pulmonaryVenousFlow = Turning.turning(pulmonaryVenousFlow, dataCompressionFactor);
        stepout.leftAtrialFlow = Turning.turning(leftAtrialFlow, dataCompressionFactor);
        stepout.leftVentricularFlow = Turning.turning(leftVentricularFlow, dataCompressionFactor);

        stepout.ascendingAorticVolume = Turning.turning(ascendingAorticVolume, dataCompressionFactor);
        stepout.brachiocephalicArterialVolume = Turning.turning(brachiocephalicArterialVolume, dataCompressionFactor);
        stepout.upperBodyArterialVolume = Turning.turning(upperBodyArterialVolume, dataCompressionFactor);
        stepout.upperBodyVenousVolume = Turning.turning(upperBodyVenousVolume, dataCompressionFactor);
        stepout.superiorVenaCavaVolume = Turning.turning(superiorVenaCavaVolume, dataCompressionFactor);
        stepout.thoracicAorticVolume = Turning.turning(thoracicAorticVolume, dataCompressionFactor);
        stepout.abdominalAorticVolume = Turning.turning(abdominalAorticVolume, dataCompressionFactor);
        stepout.renalArterialVolume = Turning.turning(renalArterialVolume, dataCompressionFactor);
        stepout.renalVenousVolume = Turning.turning(renalVenousVolume, dataCompressionFactor);
        stepout.splanchnicArterialVolume = Turning.turning(splanchnicArterialVolume, dataCompressionFactor);
        stepout.splanchnicVenousVolume = Turning.turning(splanchnicVenousVolume, dataCompressionFactor);
        stepout.lowerBodyArterialVolume = Turning.turning(lowerBodyArterialVolume, dataCompressionFactor);
        stepout.lowerBodyVenousVolume = Turning.turning(lowerBodyVenousVolume, dataCompressionFactor);
        stepout.abdominalVenousVolume = Turning.turning(abdominalVenousVolume, dataCompressionFactor);
        stepout.inferiorVenaCavaVolume = Turning.turning(inferiorVenaCavaVolume, dataCompressionFactor);
        stepout.rightAtrialVolume = Turning.turning(rightAtrialVolume, dataCompressionFactor);
        stepout.rightVentricularVolume = Turning.turning(rightVentricularVolume, dataCompressionFactor);
        stepout.pulmonaryArterialVolume = Turning.turning(pulmonaryArterialVolume, dataCompressionFactor);
        stepout.pulmonaryVenousVolume = Turning.turning(pulmonaryVenousVolume, dataCompressionFactor);
        stepout.leftAtrialVolume = Turning.turning(leftAtrialVolume, dataCompressionFactor);
        stepout.leftVentricularVolume = Turning.turning(leftVentricularVolume, dataCompressionFactor);

        stepout.HR = Turning.turning(hr, dataCompressionFactor);
        stepout.AR = Turning.turning(ar, dataCompressionFactor);
        stepout.VT = Turning.turning(vt, dataCompressionFactor);
        stepout.RVC = Turning.turning(rvc, dataCompressionFactor);
        stepout.LVC = Turning.turning(lvc, dataCompressionFactor);

        stepout.totalBloodVolume = Turning.turning(totalBloodVolume, dataCompressionFactor);
        stepout.intraThoracicPressure = Turning.turning(intraThoracicPressure, dataCompressionFactor);

        stepout.tiltAngle = Turning.turning(tiltAngle, dataCompressionFactor);

    }

    public void reset_sim() {
        // The following lines either reset variables to their initial values or
        // call functions that do the same in other files. This resets the entire
        // simulation subroutine to its initial state such that two consecutive
        // simulations start with the same numeric parameters.
        //  numerics_reset();
        Reflex.queue_reset();
    }

    // Total blood volume update equation
    // Delta P * C_splanchnicVenous = Delta V
    // where Delta P = tan ( Delta V * pi/2*V_max ) * (2*V_Max / pi * C_0 )
    public void updateTotalBloodVolume(double newTotalBloodVolume, Parameter_vector pvec) {
        // splanchnic venous compliance
        double C0 = pvec.get(PVName.SPLAN_VEN_COMPLIANCE);
        // old total blood volume 
        double tbv_old = pvec.get(PVName.TOTAL_BLOOD_VOLUME);
        // new total blood volume
        pvec.put(PVName.TOTAL_BLOOD_VOLUME, newTotalBloodVolume);
        // delta v
        double deltaV = newTotalBloodVolume - tbv_old;
        // Vmax 
        double Vmax = pvec.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL);
        // delta p
        double deltaP = tan((deltaV * PI) / (2 * Vmax)) * (2 * Vmax) / (PI * C0);
        // splanchnic pressure
        double P_old = pressure.pressure[SPLANCHNIC_VENOUS_CPI];
        // update splanchnic pressure
        pressure.pressure[SPLANCHNIC_VENOUS_CPI] = P_old + deltaP;
    }

// Zero pressure filling volume update equation
// P,new = P,old + (V0,old - V0,new) / C
    /**
     * Update zero pressure filling volume for a specified compartment. Adjusts
     * the pressure in the compartment at the same time
     *
     * @param newZpfv new ZPFV
     * @param pvec data vector
     * @param zpfvName ZPFV name for this compartment in pvec
     * @param complianceName compliance name for this compartment in pvec
     * @param dataVectorIndex index of the pressure for this compartment in
     * (global) data vector
     */
    public void updateZeroPressureFillingVolume(double newZpfv,
            Parameter_vector pvec, PVName zpfvName,
            PVName complianceName, int dataVectorIndex) {
        // compliance
        double compliance = pvec.get(complianceName);
        // pressure
        double oldPressure = pressure.pressure[dataVectorIndex];
        // old zero pressure filling volume
        double oldZpfv = pvec.get(zpfvName);
        // new zero pressure filling volume
        pvec.put(zpfvName, newZpfv);
        // recalculate pressure
        pressure.pressure[dataVectorIndex] = oldPressure + (oldZpfv - newZpfv) / compliance;

        System.out.printf("P,new = P,old + (V0,old - V0,new) / C\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, V0,old: %.2f, V0,new: %.2f, C: %.2f\n",
                pressure.pressure[dataVectorIndex], oldPressure, oldZpfv, newZpfv, compliance);
    }

// Intrathoracic pressure update equation
// P,new = P,old + (Pth,new - Pth,old)
    /**
     * Update intra-thoracic pressure, adjusting the pressures of all of the
     * compartments in the thorax
     *
     * @param newIntraThoracicPressure
     * @param pvec parameter vector
     */
    public void updateIntrathoracicPressure(double newIntraThoracicPressure, Parameter_vector pvec) {
        double oldPressure;
        System.out.printf("updateIntrathoracicPressure()\n");
        System.out.printf("P,new = P,old + (Pth,new - Pth,old)\n");

        // old intra-thoracic pressure
        double oldIntraThoracicPressure = pvec.get(PVName.INTRA_THORACIC_PRESSURE);
        // new intra-thoracic pressure
        pvec.put(PVName.INTRA_THORACIC_PRESSURE, newIntraThoracicPressure);
        pressure.pressure[INTRA_THORACIC_CPI] = newIntraThoracicPressure;

        // update pressure for all compartments inside the thorax
        // ascending aorta
        oldPressure = pressure.pressure[ASCENDING_AORTIC_CPI];
        pressure.pressure[ASCENDING_AORTIC_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update ascending aortic pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[ASCENDING_AORTIC_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // brachiocephalic
        oldPressure = pressure.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI];
        pressure.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update brachiocephalic arterial pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // superior vena cava
        oldPressure = pressure.pressure[SUPERIOR_VENA_CAVA_CPI];
        pressure.pressure[SUPERIOR_VENA_CAVA_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update superior vena cava pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[SUPERIOR_VENA_CAVA_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // thoracic aorta
        oldPressure = pressure.pressure[THORACIC_AORTIC_CPI];
        pressure.pressure[THORACIC_AORTIC_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update thoracic aortic pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[THORACIC_AORTIC_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // inferior vena cava
        oldPressure = pressure.pressure[INFERIOR_VENA_CAVA_CPI];
        pressure.pressure[INFERIOR_VENA_CAVA_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update inferior vena cava pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[INFERIOR_VENA_CAVA_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // right atrial
        oldPressure = pressure.pressure[RIGHT_ATRIAL_CPI];
        pressure.pressure[RIGHT_ATRIAL_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update right atrial pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[RIGHT_ATRIAL_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // right ventricular
        oldPressure = pressure.pressure[RIGHT_VENTRICULAR_CPI];
        pressure.pressure[RIGHT_VENTRICULAR_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update right ventricular pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[RIGHT_VENTRICULAR_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // pulmonary arterial
        oldPressure = pressure.pressure[PULMONARY_ARTERIAL_CPI];
        pressure.pressure[PULMONARY_ARTERIAL_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update pulmonary arterial pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[PULMONARY_ARTERIAL_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // pulmonary venous
        oldPressure = pressure.pressure[PULMONARY_VENOUS_CPI];
        pressure.pressure[PULMONARY_VENOUS_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update pulmonary venous pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[PULMONARY_VENOUS_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // left atrial
        oldPressure = pressure.pressure[LEFT_ATRIAL_CPI];
        pressure.pressure[LEFT_ATRIAL_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update left atrial pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[LEFT_ATRIAL_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

        // left ventricular
        oldPressure = pressure.pressure[LEFT_VENTRICULAR_CPI];
        pressure.pressure[LEFT_VENTRICULAR_CPI] = oldPressure + newIntraThoracicPressure - oldIntraThoracicPressure;
        System.out.printf("Update left ventricular pressure\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n",
                pressure.pressure[LEFT_VENTRICULAR_CPI], oldPressure, newIntraThoracicPressure, oldIntraThoracicPressure);

    }

// Compliance update constraint for compartments inside the thorax
// P,new = P,old * C,old / C,new + Pth * (1 - C,old / C,new)
    /**
     * Adjust the compliance of a compartment located in the thorax. It
     * compensates the resulting pressure for the intra-thoracic compliance
     *
     * @param newCompliance new compliance value
     * @param pvec parameter vector
     * @param complianceName compartment compliance name
     * @param dataVectorIndex index of compartment pressure in data vector
     */
    public void updateComplianceInsideThorax(double newCompliance, Parameter_vector pvec,
            PVName complianceName, int dataVectorIndex) {
        // old compliance
        double oldCompliance = pvec.get(complianceName);
        // new compliance
        pvec.put(complianceName, newCompliance);
        // intra-thoracic pressure
        double intraThoracicPressure = pvec.get(PVName.INTRA_THORACIC_PRESSURE);
        // old pressure
        double oldPressure = pressure.pressure[dataVectorIndex];
        // new pressure
        pressure.pressure[dataVectorIndex] = oldPressure * oldCompliance / newCompliance + intraThoracicPressure * (1 - oldCompliance / newCompliance);
        System.out.printf("updateComplianceInsideThorax()\n");
        System.out.printf("P,new = P,old * C,old / C,new + Pth * (1 - C,old / C,new)\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, C,old: %.2f, C,new: %.2f, Pth: %.2f\n",
                pressure.pressure[dataVectorIndex], oldPressure, oldCompliance, newCompliance, intraThoracicPressure);
    }

// Compliance update constraint for compartments outside the thorax
// P,new = P,old * C,old / C,new
    /**
     * Adjust the compliance of a compartment located outside of the thorax. It
     * does not have to compensate the resulting pressure for the intra-thoracic
     * compliance
     *
     * @param newCompliance new compliance value
     * @param pvec parameter vector
     * @param complianceName compartment compliance name
     * @param dataVectorIndex index of compartment pressure in data vector
     */
    public void updateComplianceOutsideThorax(double newCompliance, Parameter_vector pvec,
            PVName complianceName, int dataVectorIndex) {
        // old compliance
        double oldCompliance = pvec.get(complianceName);
        // new compliance
        pvec.put(complianceName, newCompliance);
        // old pressure
        double oldPressure = pressure.pressure[dataVectorIndex];
        // new pressure
        pressure.pressure[dataVectorIndex] = oldPressure * oldCompliance / newCompliance;
        System.out.printf("updateComplianceOutsideThorax()\n");
        System.out.printf("P,new = P,old * C,old / C,new\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, C,old: %.2f, C,new: %.2f\n",
                pressure.pressure[dataVectorIndex], oldPressure, oldCompliance, newCompliance);
    }

// Update function for parameters with no update constraints
    public void updateParameter(double newValue, Parameter_vector pvec, PVName parameterName) {
        pvec.put(parameterName, newValue);
    }
}
