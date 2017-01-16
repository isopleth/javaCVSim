package jcvsim.backend6compartment;

import static jcvsim.backend6compartment.Data_vector.CompartmentIndexes.*;
import jcvsim.backendCommon.Turning;

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

    int N_OUT = 21;      // Number of output variables in the steady-state
    // analysis

// Structure handling the flow of data within the simulation.c routine.
// See simulator.h for the definition.
    Data_vector pressure = new Data_vector();

// Structure for the reflex variables.
    Reflex_vector reflex_vector = new Reflex_vector();

// Structure for the impulse response functions
    Impulse_vector imp = new Impulse_vector();

// Output structure - defined in main.h
    Output_vector out = new Output_vector();

// The following definitions pertain to the adaptive stepsize integration
// routine. See Numerical Recipes in C (p. ???) for details.
    double[] hdid = {0.0};
    double htry = 0.00001;
    double yscale[] = {1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1.};
    double[] hnext = {0.0};

    double[] result;           // output vector containing the output variables.
    private static Main theInstance;

    static public Main instance() {
        if (theInstance == null) {
            theInstance = new Main();
        }
        return theInstance;
    }

    private Main() {

    }

// init_sim(): Initializes the simulation
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
        Equation.elastance_ptr(pressure, reflex_vector, a);

        Equation.fixvolume_ptr(pressure, reflex_vector, a);     // fix the total blood volume

        Equation.eqns_ptr(pressure, a, reflex_vector); // Initialize pressure derivatives

        Reflex.makeImp(imp, a);                    // Set up the impulse resp. arrays

        Simulator_numerics.numerics(pressure, reflex_vector, out, a); // Initialize the ouput array???

    }

// This function is needed to update pressure.x[] from the Java code.
// The output struct copies the values in pressure.x and makes them available to 
// the Java code. The output struct is in a sense read-only. If the values in the 
// output struct are changed, the change is not reflected in pressure.x. However, 
// occasionally (as in the case of parameter updates), information must pass from
// the GUI to the simulation variables. This function was created to allow 
// the changes to output.x to be reflected in pressure.x.
    public void updatePressure(int i, double value) {
        pressure.pressure[i] = value;
    }

// step_sim(): Advances the simulation one timestep
// By varying the dataCompressionFactor the data can be compressed before 
// being passed to the GUI. A dataCompressionFactor of 10 means one piece 
// of output data is passed to the GUI for every 10 timesteps. A 
// dataCompressionFactor of 1 means every piece of data is passed to the GUI.
// (One piece of data for every timestep.)
    public void step_sim(Output stepout, Parameter_vector a, int dataCompressionFactor,
            boolean ABReflexOn, boolean CPReflexOn) {
        double[] x0 = new double[dataCompressionFactor];
        double[] x1 = new double[dataCompressionFactor];
        double[] x2 = new double[dataCompressionFactor];
        double[] x3 = new double[dataCompressionFactor];
        double[] x4 = new double[dataCompressionFactor];
        double[] x5 = new double[dataCompressionFactor];
        double[] q0 = new double[dataCompressionFactor];
        double[] q1 = new double[dataCompressionFactor];
        double[] q2 = new double[dataCompressionFactor];
        double[] q3 = new double[dataCompressionFactor];
        double[] q4 = new double[dataCompressionFactor];
        double[] q5 = new double[dataCompressionFactor];
        double[] v0 = new double[dataCompressionFactor];
        double[] v1 = new double[dataCompressionFactor];
        double[] v2 = new double[dataCompressionFactor];
        double[] v3 = new double[dataCompressionFactor];
        double[] v4 = new double[dataCompressionFactor];
        double[] v5 = new double[dataCompressionFactor];
        double[] hr = new double[dataCompressionFactor];
        double[] ar = new double[dataCompressionFactor];
        double[] vt = new double[dataCompressionFactor];
        double[] rvc = new double[dataCompressionFactor];
        double[] lvc = new double[dataCompressionFactor];
        double[] tbv = new double[dataCompressionFactor];
        double[] pth = new double[dataCompressionFactor];

        // Calculate output values
        int i;
        for (i = 0; i < dataCompressionFactor; i++) {
            Rkqc.rkqc_ptr(pressure, reflex_vector, a, htry, 0.001, yscale, hdid, hnext);

            pressure.time[1] = Reflex.sanode(pressure, reflex_vector, a, hdid[0]);

            Equation.elastance_ptr(pressure, reflex_vector, a);

            Equation.eqns_ptr(pressure, a, reflex_vector);

            Reflex.queue_ptr(pressure, imp, reflex_vector, a, hdid[0], ABReflexOn, CPReflexOn);

            pressure.time[0] += hdid[0];

            htry = 0.001;

            Equation.fixvolume_ptr(pressure, reflex_vector, a);

            Simulator_numerics_new.numerics_new_ptr(pressure, reflex_vector, hdid[0], result);

            Simulator_numerics.numerics(pressure, reflex_vector, out, a);

            // simulation time
            stepout.time = pressure.time[0];

            x0[i] = pressure.pressure[LEFT_VENTRICULAR_CPI];
            x1[i] = pressure.pressure[ARTERIAL_CPI];
            x2[i] = pressure.pressure[CENTRAL_VENOUS_CPI];
            x3[i] = pressure.pressure[RIGHT_VENTRICULAR_CPI];
            x4[i] = pressure.pressure[PULMONARY_ARTERIAL_CPI];
            x5[i] = pressure.pressure[PULMONARY_VENOUS_CPI];

            q0[i] = pressure.flowRate[LEFT_VENTRICULAR_CPI];
            q1[i] = pressure.flowRate[ARTERIAL_CPI];
            q2[i] = pressure.flowRate[CENTRAL_VENOUS_CPI];
            q3[i] = pressure.flowRate[RIGHT_VENTRICULAR_CPI];
            q4[i] = pressure.flowRate[PULMONARY_ARTERIAL_CPI];
            q5[i] = pressure.flowRate[PULMONARY_VENOUS_CPI];

            v0[i] = pressure.volume[LEFT_VENTRICULAR_CPI];
            v1[i] = pressure.volume[ARTERIAL_CPI];
            v2[i] = pressure.volume[CENTRAL_VENOUS_CPI];
            v3[i] = pressure.volume[RIGHT_VENTRICULAR_CPI];
            v4[i] = pressure.volume[PULMONARY_ARTERIAL_CPI];
            v5[i] = pressure.volume[PULMONARY_VENOUS_CPI];

            hr[i] = reflex_vector.hr[2];
            ar[i] = reflex_vector.resistance[0];
            vt[i] = reflex_vector.volume[0];
            rvc[i] = reflex_vector.compliance[0];
            lvc[i] = reflex_vector.compliance[1];
            tbv[i] = a.get(PVName.TOTAL_BLOOD_VOLUME);
            pth[i] = a.get(PVName.INTRA_THORACIC_PRESSURE);

            // Check TBV
            //double total = 0;
            //int k;
            //for (k=0; k < 25; k++)
            // total += pressure.v[k]; 
            //System.out.printf("TBV: %.2f, %.2f\n", a.get(PVName.PV70), total);
            // Check TZPFV
            //double total = 0;
            //int k;
            //for (k=162; k < 168; k++)
            //  total += a.get(k);
            //System.out.printf("TZPFV: %.2f, %.2f\n", a.get(PVName.PV75), total);
        } // end for

        // Compress the output by applying turning algorithm
        stepout.leftVentricularPressure = Turning.turning(x0, dataCompressionFactor);
        stepout.arterialPressure = Turning.turning(x1, dataCompressionFactor);
        stepout.centralVenousPressure = Turning.turning(x2, dataCompressionFactor);
        stepout.rightVentricularPressure = Turning.turning(x3, dataCompressionFactor);
        stepout.pulmonaryArterialPressure = Turning.turning(x4, dataCompressionFactor);
        stepout.pulmonaryVenousPressure = Turning.turning(x5, dataCompressionFactor);

        stepout.leftVentricularFlowRate = Turning.turning(q0, dataCompressionFactor);
        stepout.arterialFlowRate = Turning.turning(q1, dataCompressionFactor);
        stepout.centralVenousFlowRate = Turning.turning(q2, dataCompressionFactor);
        stepout.rightVentricularFlowRate = Turning.turning(q3, dataCompressionFactor);
        stepout.pulmonaryArterialFlowRate = Turning.turning(q4, dataCompressionFactor);
        stepout.pulmonaryVenousFlowRate = Turning.turning(q5, dataCompressionFactor);

        stepout.leftVentricularVolume = Turning.turning(v0, dataCompressionFactor);
        stepout.arterialVolume = Turning.turning(v1, dataCompressionFactor);
        stepout.centralVenousVolume = Turning.turning(v2, dataCompressionFactor);
        stepout.rightVentricularVolume = Turning.turning(v3, dataCompressionFactor);
        stepout.pulmonaryArterialVolume = Turning.turning(v4, dataCompressionFactor);
        stepout.pulmonaryVenousVolume = Turning.turning(v5, dataCompressionFactor);

        stepout.heartRate = Turning.turning(hr, dataCompressionFactor);
        stepout.arteriolarResistance = Turning.turning(ar, dataCompressionFactor);
        stepout.venousTone = Turning.turning(vt, dataCompressionFactor);
        stepout.rightVentricleContractility = Turning.turning(rvc, dataCompressionFactor);
        stepout.leftVentricleContractility = Turning.turning(lvc, dataCompressionFactor);
        stepout.totalBloodVolume = Turning.turning(tbv, dataCompressionFactor);
        stepout.IntraThoracicPressure = Turning.turning(pth, dataCompressionFactor);
    }

    public void reset_sim() {
        // The following lines either reset variables to their initial values or
        // call functions that do the same in other files. This resets the entire
        // simulation subroutine to its initial state such that two consecutive
        // simulations start with the same numeric parameters.
        //  numerics_reset();
        Reflex.queue_reset();
    }

// Total blood volume update constraint
// Pv,new = Pv,old + (Vtot,new - Vtot,old) / Cv
    public void updateTotalBloodVolume(double tbv_new, Parameter_vector a) {
        // venous compliance
        double Cv = a.get(PVName.VEN_COMPLIANCE);
        // central venous pressure
        double Pv_old = pressure.pressure[CENTRAL_VENOUS_CPI];
        // old total blood volume 
        double tbv_old = a.get(PVName.TOTAL_BLOOD_VOLUME);
        // new total blood volume
        a.put(PVName.TOTAL_BLOOD_VOLUME,tbv_new);
        // recalculate central venous pressure
        pressure.pressure[CENTRAL_VENOUS_CPI] = Pv_old + (tbv_new - tbv_old) / Cv;

        System.out.printf("Pv,new = Pv,old + (Vtot,new - Vtot,old) / Cv\n");
        System.out.printf("Pv,new: %.2f, Pv,old: %.2f, Vtot,new: %.2f, Vtot,old: %.2f, Cv: %.2f\n", pressure.pressure[CENTRAL_VENOUS_CPI], Pv_old, tbv_new, tbv_old, Cv);
    }

// Total zero pressure filling volume update constraint
// Pv,new = Pv,old + (V0,old - V0,new) / Cv
    public void updateTotalZeroPressureFillingVolume(double zpfv_new, Parameter_vector a) {
        // venous compliance
        double Cv = a.get(PVName.VEN_COMPLIANCE);
        // central venous pressure
        double Pv_old = pressure.pressure[CENTRAL_VENOUS_CPI];
        // old total zero pressure filling volume
        double zpfv_old = a.get(PVName.TOTAL_ZPFV);
        // new total zero pressure filling volume
        a.put(PVName.TOTAL_ZPFV,zpfv_new);
        // recalculate central venous pressure
        pressure.pressure[CENTRAL_VENOUS_CPI] = Pv_old + (zpfv_old - zpfv_new) / Cv;

        System.out.printf("Pv,new = Pv,old + (V0,old - V0,new) / Cv\n");
        System.out.printf("Pv,new: %.2f, Pv,old: %.2f, V0,old: %.2f, V0,new: %.2f, Cv: %.2f\n", pressure.pressure[CENTRAL_VENOUS_CPI], Pv_old, zpfv_old, zpfv_new, Cv);
    }

// Intra-thoracic pressure update constraint
// For all thoracic compartments.
// P,new = P,old + (Pth,new - Pth,old)
    public void updateIntrathoracicPressure(double Pth_new, Parameter_vector a) {
        double P_old;
        System.out.printf("P,new = P,old + (Pth,new - Pth,old)\n");

        // old intra-thoracic pressure
        double Pth_old = a.get(PVName.INTRA_THORACIC_PRESSURE);
        // new intra-thoracic pressure
        a.put(PVName.INTRA_THORACIC_PRESSURE,Pth_new);
        pressure.pressure[INTRA_THORACIC_CPI] = Pth_new;

        // left ventricle pressure
        P_old = pressure.pressure[LEFT_VENTRICULAR_CPI];
        // recalculate left ventricle pressure
        pressure.pressure[LEFT_VENTRICULAR_CPI] = P_old + Pth_new - Pth_old;
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n", pressure.pressure[LEFT_VENTRICULAR_CPI], P_old, Pth_new, Pth_old);

        // right ventricle pressure
        P_old = pressure.pressure[RIGHT_VENTRICULAR_CPI];
        // recalculate right ventricle pressure
        pressure.pressure[RIGHT_VENTRICULAR_CPI] = P_old + Pth_new - Pth_old;
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n", pressure.pressure[RIGHT_VENTRICULAR_CPI], P_old, Pth_new, Pth_old);

        // pulmonary arterial pressure
        P_old = pressure.pressure[PULMONARY_ARTERIAL_CPI];
        // recalculate pulmonary arterial pressure
        pressure.pressure[PULMONARY_ARTERIAL_CPI] = P_old + Pth_new - Pth_old;
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n", pressure.pressure[PULMONARY_ARTERIAL_CPI], P_old, Pth_new, Pth_old);

        // pulmonary venous pressure
        P_old = pressure.pressure[PULMONARY_VENOUS_CPI];
        // recalculate pulmonary venous pressure
        pressure.pressure[PULMONARY_VENOUS_CPI] = P_old + Pth_new - Pth_old;
        System.out.printf("P,new: %.2f, P,old: %.2f, Pth,new: %.2f, Pth,old: %.2f\n", pressure.pressure[PULMONARY_VENOUS_CPI], P_old, Pth_new, Pth_old);
    }

// Compliance parameter update constraint
// P,new = P,old * C,old / C,new + Pth * (1 - C,old / C,new)
    public void updatePulmonaryArterialCompliance(double C_new, Parameter_vector a) {
        // old pulmonary arterial compliance
        double C_old = a.get(PVName.PULM_ART_COMPLIANCE);
        // new pulmonary arterial compliance
        a.put(PVName.PULM_ART_COMPLIANCE,C_new);
        // old intra-thoracic pressure
        double Pth = a.get(PVName.INTRA_THORACIC_PRESSURE);
        // old pulmonary arterial pressure
        double P_old = pressure.pressure[PULMONARY_ARTERIAL_CPI];
        // new pulomary arterial compliance
        pressure.pressure[PULMONARY_ARTERIAL_CPI] = P_old * C_old / C_new + Pth * (1 - C_old / C_new);
        System.out.printf("P,new = P,old * C,old / C,new\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, C,old: %.2f, C,new: %.2f\n", pressure.pressure[PULMONARY_ARTERIAL_CPI], P_old, C_old, C_new);
    }

// Compliance parameter update constraint
// P,new = P,old * C,old / C,new + Pth * (1 - C,old / C,new)
    public void updatePulmonaryVenousCompliance(double C_new, Parameter_vector a) {
        // old pulmonary arterial compliance
        double C_old = a.get(PVName.PULM_VEN_COMPLIANCE);
        // new pulmonary arterial compliance
        a.put(PVName.PULM_VEN_COMPLIANCE,C_new);
        // old intra-thoracic pressure
        double Pth = a.get(PVName.INTRA_THORACIC_PRESSURE);
        // old pulmonary arterial pressure
        double P_old = pressure.pressure[PULMONARY_VENOUS_CPI];
        // new pulomary arterial compliance
        pressure.pressure[PULMONARY_VENOUS_CPI] = P_old * C_old / C_new + Pth * (1 - C_old / C_new);
        System.out.printf("P,new = P,old * C,old / C,new\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, C,old: %.2f, C,new: %.2f\n", pressure.pressure[PULMONARY_VENOUS_CPI], P_old, C_old, C_new);
    }

// Compliance parameter update constraint
// P,new = P,old * C,old / C,new
    public void updateArterialCompliance(double C_new, Parameter_vector a) {
        // old pulmonary arterial compliance
        double C_old = a.get(PVName.ART_COMPLIANCE);
        // new pulmonary arterial compliance
        a.put(PVName.ART_COMPLIANCE,C_new);
        // old pulmonary arterial pressure
        double P_old = pressure.pressure[ARTERIAL_CPI];
        // new pulomary arterial compliance
        pressure.pressure[ARTERIAL_CPI] = P_old * C_old / C_new;
        System.out.printf("P,new = P,old * C,old / C,new\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, C,old: %.2f, C,new: %.2f\n", pressure.pressure[ARTERIAL_CPI], P_old, C_old, C_new);
    }

// Compliance parameter update constraint
// P,new = P,old * C,old / C,new
    public void updateVenousCompliance(double C_new, Parameter_vector a) {
        // old pulmonary arterial compliance
        double C_old = a.get(PVName.VEN_COMPLIANCE);
        // new pulmonary arterial compliance
        a.put(PVName.VEN_COMPLIANCE,C_new);
        // old pulmonary arterial pressure
        double P_old = pressure.pressure[CENTRAL_VENOUS_CPI];
        // new pulomary arterial compliance
        pressure.pressure[CENTRAL_VENOUS_CPI] = P_old * C_old / C_new;
        System.out.printf("P,new = P,old * C,old / C,new\n");
        System.out.printf("P,new: %.2f, P,old: %.2f, C,old: %.2f, C,new: %.2f\n", pressure.pressure[CENTRAL_VENOUS_CPI], P_old, C_old, C_new);
    }

// Update function for parameters with no update constraints
    public void updateParameter(double newValue, Parameter_vector a, PVName index) {
        a.put(index,newValue);
    }
}
