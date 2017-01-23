package jcvsim.backend6compartment;

import java.util.Arrays;

/*
 * Header file for the file simulator.c which is the top-level file for the
 * simulation module.
 *
 * Thomas Heldt March 2nd, 2002
 * Last modified April 11th, 2002
 */
// Converted to Java   Jason Leake December 2016
public class Data_vector {

    /*
     * N_X is the number of relevant pressures in the cardiovascular system. It
     * consists of the 14 pressure nodes of the hemodynamic model plus the three
     * external bias pressures. N_X determines the size of the pressure vector x
     * and the vector of the pressure derivatives dxdt. N_C is the number of
     * contracting cardiac chambers in the model. It determines the size of the
     * arrays c and dcdt which contain the values of the time-varying
     * compliances
     * and their derivatives, respectively. N_T determines the size of the time
     * vector. It currently consists of 6 entries. Absolute time, cardiac time
     * (i.e. time elapsed since the onset of the last atrial contraction), are
     * the
     * first two entries. The next three entries are PR_delay, Tasys, and Tvsys.
     * The last entry is "absolute modified time" which is used in the tilt()
     * and
     * lbnp() routines (see those routines for the definition of this time
     * variable). The tilt array stores the blood volume loss in the 0 component
     * and the carotid sinus offset pressure in the 1 component.
     */
    public static final int N_X = 25;
    public static final int N_Q = 24;
    public static final int N_V = 21;
    public static final int N_C = 7;
    public static final int N_TIME = 7;
    public static final int N_TILT = 2;
    public static final int N_GRAV = 15;

    /*
     * Definition of the data structure. It contains the pressure vector, x[],
     * the vector of pressure derivatives, dxdt[], the time-varying compliance
     * vector, c[], the vector of the compliance derivatives, dcdt[], and
     * finally
     * the time vector, time[].
     */
    public double[] pressure;       // Pressures - use indexes in CompartmentIndexes
    public double[] dPressureDt;       // Pressure derivatives - use indexes in CompartmentIndexes
    public double[] flowRate;    // Flow rates - CompartmentIndexes subset
    public double[] volume;       // volumes - CompartmentIndexes subset
        //
    // Heart chamber compliance and rate of change of compliance
    // Use ComplianceIndexes for these
    public double[] compliance;
    public double[] dComplianceDt;
     //
    // Times   
    public double time[];
    public double time_new[];
    public double tilt[];
    public double grav[];
    double tilt_angle; // instantaneous tilt angle in degrees

    public Data_vector() {
        pressure = new double[N_X];
        dPressureDt = new double[N_X];
        flowRate = new double[N_Q];
        volume = new double[N_V];
        compliance = new double[N_C];
        dComplianceDt = new double[N_C];
        time = new double[N_TIME];
        time_new = new double[N_TIME];
        tilt = new double[N_TILT];
        grav = new double[N_GRAV];
    }

    public final void copyFrom(Data_vector other) {

        pressure = Arrays.copyOf(other.pressure, other.pressure.length);
        dPressureDt = Arrays.copyOf(other.dPressureDt, other.dPressureDt.length);
        flowRate = Arrays.copyOf(other.flowRate, other.flowRate.length);
        volume = Arrays.copyOf(other.volume, other.volume.length);
        compliance = Arrays.copyOf(other.compliance, other.compliance.length);
        dComplianceDt = Arrays.copyOf(other.dComplianceDt, other.dComplianceDt.length);
        time = Arrays.copyOf(other.time, other.time.length);
        time_new = Arrays.copyOf(other.time_new, other.time_new.length);
        tilt = Arrays.copyOf(other.tilt, other.tilt.length);
        grav = Arrays.copyOf(other.grav, other.grav.length);
    }

    public Data_vector(Data_vector other) {
        super();
        copyFrom(other);
    }
    
     public static class CompartmentIndex {

        // These indexes (up to and including LEFT_VENTRICULAR_CPI) are used by
        // pressure, dPressureDt, flowRate and volume
         // CPI suffix means "compartment_index"
        public static final int LEFT_VENTRICULAR_CPI = 0;
        public static final int ARTERIAL_CPI = 1;
        public static final int CENTRAL_VENOUS_CPI = 2;
        public static final int RIGHT_VENTRICULAR_CPI = 3;
        public static final int PULMONARY_ARTERIAL_CPI = 4;
        public static final int PULMONARY_VENOUS_CPI = 5;
        // these are used for pressure, dPressureDt and flowRate but not
        // volume
        public static final int BIAS_1_CPI = 21;
        public static final int BIAS_2_CPI = 22;
        public static final int BIAS_3_CPI = 23;
        // This index is used for pressure, dPressureDt, but not for
        // flowRate or volume
        public static final int INTRA_THORACIC_CPI = 24;
    };
     
    public static class ComplianceIndex {

        // This class defines the indexes for the compliance array compliance
        // and the rate of change of  compliance dComplianceDt
        // LA, LV, RQ, RV mean left and right atrial/ventrical. The ventriular
        // ones aren't used - but they are in the 21 compartment model
        public static int RA_COMPL_NOT_USED = 0;
        public static int RV_COMPL = 1;
        public static int LA_COMPL_NOT_USED = 2;
        public static int LV_COMPL = 3;
        // The last two indexes are only used by compliance, and not by dComplianceDt
        public static int RV_END_SYSTOLIC_COMPL = 4;
        public static int LV_END_SYSTOLIC_COMPL = 5;
    };
}
