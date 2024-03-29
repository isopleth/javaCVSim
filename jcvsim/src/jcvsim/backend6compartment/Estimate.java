package jcvsim.backend6compartment;

import static jcvsim.backend6compartment.Data_vector.CompartmentIndex.*;
import static jcvsim.backend6compartment.Data_vector.ComplianceIndex.*;
import static jcvsim.backend6compartment.Data_vector.N_GRAV;
import static jcvsim.backendCommon.Maths.fabs;
import static jcvsim.backendCommon.Maths.sqrt;

/*
 * This file contains the subroutines to estimate the 23 intial pressures
 * (17 systemic, 2 mean atrial, 2 diastolic ventricular, and 2 systolic
 * ventricular pressures) needed to start the integration routine. It does so
 * in two steps. In the first step, all compartments are assumed linear and an
 * initial estimate of the pressures is obtained solving 23 linear equations
 * corresponding to a DC version of the hemodynamic system. In the second step,
 * these estimates are improved iteratively after introducing the three
 * non-linear vascular compartments. The equation for total blood volume is
 * used to monitor the accuracy of the pressure estimates.
 *
 * Thomas Heldt March 2nd, 2002
 * Last modified October 18, 2005
 */
// Converted to Java Jason Leake December 2016
public class Estimate {

    private static final int ARRAY_SIZE = 8;

    /*
     * Estimate() serves two purposes: it estimates the initial pressure vector
     * and initializes the reflex structure. The first purpose is acvieved by
     * reading the parameter vector, initializing the coefficient matrix a
     * and the lhs vector b, and calling the linear equation solver lineqns().
     * The
     * routine returns to the calling environment the estimated pressures and
     * the
     * atrial and ventricular timing information (duration of PR interval,
     * atrial
     * systole, and ventricular systole) as part of a structure of type
     * Data_vector.
     */
    public static void estimate_ptr(Data_vector out, Parameter_vector theta, Reflex_vector ref) {
        double[][] a = new double[ARRAY_SIZE][];// Coefficient matrix
        for (int index = 0; index < ARRAY_SIZE; index++) {
            a[index] = new double[ARRAY_SIZE];
        }

        double[] b = new double[ARRAY_SIZE];                // Solution vector
        double T = 0.0, Tdias = 0.0, Tsys = 0.0;     // RR-interval, systolic time,
        // and diastolic time
        //Data_vector out =  {{0.0}, {0.0}, {0.0}, {0.0}, {0.0}, {0.0}, {0.0}};
        int i = 0;

        T = 60. / theta.get(PVName.NOMINAL_HEART_RATE);
        Tsys = theta.get(PVName.PV120) * sqrt(T);  // ventricular systolic time interval
        Tdias = T - Tsys;               // ventricular diastolic time interval

        // Volume equation.
        a[0][0] = theta.get(PVName.ART_COMPLIANCE);
        a[1][0] = theta.get(PVName.VEN_COMPLIANCE);
        a[2][0] = 0.;
        a[3][0] = theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);
        a[4][0] = theta.get(PVName.PULM_ART_COMPLIANCE);
        a[5][0] = theta.get(PVName.PULM_VEN_COMPLIANCE);
        a[6][0] = 0.;
        a[7][0] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);

        // Systemic flow
        a[0][1] = -T / theta.get(PVName.TOTAL_PERIPHERAL_RESISTANCE);
        a[1][1] = T / theta.get(PVName.TOTAL_PERIPHERAL_RESISTANCE);
        a[2][1] = 0.;
        a[3][1] = 0.;
        a[4][1] = 0.;
        a[5][1] = 0.;
        a[6][1] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);
        a[7][1] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);

        // Right ventricular outflow
        a[0][2] = 0.;
        a[1][2] = 0.;
        a[2][2] = -Tsys / theta.get(PVName.PULMONIC_VALVE_RESISTANCE);
        a[3][2] = 0.;
        a[4][2] = Tsys / theta.get(PVName.PULMONIC_VALVE_RESISTANCE);
        a[5][2] = 0.;
        a[6][2] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);
        a[7][2] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);

        // Right ventricular inflow
        a[0][3] = 0.;
        a[1][3] = -Tdias / theta.get(PVName.VEN_RESISTANCE);
        a[2][3] = 0.;
        a[3][3] = Tdias / theta.get(PVName.VEN_RESISTANCE);
        a[4][3] = 0.;
        a[5][3] = 0.;
        a[6][3] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);
        a[7][3] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);

        // Pulmonary flow
        a[0][4] = 0.;
        a[1][4] = 0.;
        a[2][4] = 0.;
        a[3][4] = 0.;
        a[4][4] = -T / theta.get(PVName.PULM_MICRO_RESISTANCE);
        a[5][4] = T / theta.get(PVName.PULM_MICRO_RESISTANCE);
        a[6][4] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);
        a[7][4] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);

        // Left ventricular inflow
        a[0][5] = 0.;
        a[1][5] = 0.;
        a[2][5] = 0.;
        a[3][5] = 0.;
        a[4][5] = 0.;
        a[5][5] = -Tdias / theta.get(PVName.PULM_VEN_RESISTANCE);
        a[6][5] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);
        a[7][5] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) + Tdias / theta.get(PVName.PULM_VEN_RESISTANCE);

        // Left ventricular outflow
        a[0][6] = Tsys / theta.get(PVName.LV_OUTFLOW_RESISTANCE);
        a[1][6] = 0.;
        a[2][6] = 0.;
        a[3][6] = 0.;
        a[4][6] = 0.;
        a[5][6] = 0.;
        a[6][6] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE) - Tsys / theta.get(PVName.LV_OUTFLOW_RESISTANCE);
        a[7][6] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);

        // Stroke volume matching
        a[0][7] = 0.;
        a[1][7] = 0.;
        a[2][7] = theta.get(PVName.RV_SYSTOLIC_COMPLIANCE);
        a[3][7] = -theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);
        a[4][7] = 0.;
        a[5][7] = 0.;
        a[6][7] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);
        a[7][7] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);

        b[0] = theta.get(PVName.TOTAL_BLOOD_VOLUME) - theta.get(PVName.TOTAL_ZPFV) + theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) + theta.get(PVName.RV_DIASTOLIC_COMPLIANCE) + theta.get(PVName.PULM_ART_COMPLIANCE) + theta.get(PVName.PULM_VEN_COMPLIANCE));
        b[1] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE));
        b[2] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE));
        b[3] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE));
        b[4] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE));
        b[5] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE));
        b[6] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE));
        b[7] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE) - theta.get(PVName.RV_DIASTOLIC_COMPLIANCE) + theta.get(PVName.RV_SYSTOLIC_COMPLIANCE));


        /*
         * for (i=0;i<ARRAY_SIZE; i++) {
         * for (j=0; j<ARRAY_SIZE; j++)
         * printf("%e ", a[i][j]);
         * printf("\n");
         * }
         */
        // Previously
        // lineqs(a, b, ARRAY_SIZE);     
        // Changed to be compatible w/ GCC versions 4.0 and higher
        // Modified by C. Dunn
        lineqs(a, b, ARRAY_SIZE);             // Call the linear equation solver
        // to solve for the initial pressure
        // estimate.

        // Initialize the first 21 entries of the pressure vector to the end of 
        // ventricular diastole.
        out.pressure[LEFT_VENTRICULAR_CPI] = b[7];
        out.pressure[ARTERIAL_CPI] = b[0];
        out.pressure[CENTRAL_VENOUS_CPI] = b[1];
        out.pressure[RIGHT_VENTRICULAR_CPI] = b[3];
        out.pressure[PULMONARY_ARTERIAL_CPI] = b[4];
        out.pressure[PULMONARY_VENOUS_CPI] = b[5];

        // **************************** NOTE ***********************************
        // SOME STUFF BELOW HERE WAS NOT ADAPTED OR CLEANED UP FOR THE TLD CVSIM
        // VERSION THIS IS SUPPOSED TO BE.
        // *********************************************************************
        // temporary
        out.compliance[RV_END_SYSTOLIC_COMPL] = theta.get(PVName.RV_SYSTOLIC_COMPLIANCE);
        out.compliance[LV_END_SYSTOLIC_COMPL] = theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);

        // Bias pressures and intrathoracic pressure.
        out.pressure[BIAS_1_CPI] = out.dPressureDt[BIAS_1_CPI] = 0.0;
        out.pressure[BIAS_2_CPI] = out.dPressureDt[BIAS_2_CPI] = 0.0;
        out.pressure[BIAS_3_CPI] = out.dPressureDt[BIAS_3_CPI] = 0.0;
        out.pressure[INTRA_THORACIC_CPI] = theta.get(PVName.INTRA_THORACIC_PRESSURE);
        out.dPressureDt[INTRA_THORACIC_CPI] = 0.0;

        for (i = 0; i < N_GRAV; i++) {
            out.grav[i] = 0.0;
        }

        // Initialize the timing variables: absolute time, cardiac time, PR interval,
        // atrial, and ventricular systolic time interval, respectively.
        out.time[0] = 0.0;
        out.time[1] = 0.0;
        out.time[2] = theta.get(PVName.PV118) * sqrt(T);   // Intialization of PR-interval
        out.time[3] = theta.get(PVName.PV119) * sqrt(T);   // Initialization of atrial systolic
        // time interval.
        out.time[4] = theta.get(PVName.PV120) * sqrt(T);   // Initialization of ventricular 
        // systolic time interval.
        // The next line is experimental as of Feb 20th, 2002. It initializes
        // "modified absolute time" needed for the orthostatic stress simulations.
        out.time[5] = 0.0;
        out.time[6] = -out.time[2];  // Initialization of ventricular time to
        // negative PR-interval; added on 08/11/04

        // Initialize the timing variables: absolute time, cardiac time, PR interval,
        // atrial, and ventricular systolic time interval, respectively.
        out.time_new[0] = 0.0;
        out.time_new[1] = 0.0;
        out.time_new[2] = theta.get(PVName.PV118) * sqrt(T); // Intialization of PR-interval
        out.time_new[3] = theta.get(PVName.PV119) * sqrt(T); // Initialization of atrial
        // systolic time interval.
        out.time_new[4] = theta.get(PVName.PV120) * sqrt(T); // Initialization of ventricular 
        // systolic time interval.
        out.time_new[5] = -out.time_new[2];       // 
        out.time_new[6] = -out.time_new[2];       // Initialize ventricular time

        // The following lines initialize the reflex structure entires to their
        // initial values.
        ref.hr[0] = theta.get(PVName.NOMINAL_HEART_RATE);
        ref.hr[1] = theta.get(PVName.NOMINAL_HEART_RATE);        // currently assigned to cum_HR;
        ref.hr[2] = theta.get(PVName.NOMINAL_HEART_RATE);

        ref.step_cnt = 1;                 // number of integration steps taken
        // in current cardiac cycle.

        ref.compliance[0] = theta.get(PVName.RV_SYSTOLIC_COMPLIANCE);
        ref.compliance[1] = theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);

        ref.resistance[0] = theta.get(PVName.PV53);
        ref.resistance[1] = theta.get(PVName.KIDNEY_RESISTANCE);
        ref.resistance[2] = theta.get(PVName.SPLANCHNIC_RESISTANCE);
        ref.resistance[3] = theta.get(PVName.LBODY_RESISTANCE);

        ref.volume[0] = theta.get(PVName.KIDNEY_COMPARTMENT_ZPFV) + theta.get(PVName.SPLAN_COMPARTMENT_ZPFV) + theta.get(PVName.LBODY_COMPARTMENT_ZPFV) + theta.get(PVName.ABDOM_VEN_ZPFV) + theta.get(PVName.INFERIOR_VENA_CAVA_ZPFV) + theta.get(PVName.SUPERIOR_VENA_CAVA_ZPFV);
        ref.volume[1] = theta.get(PVName.KIDNEY_COMPARTMENT_ZPFV);
        ref.volume[2] = theta.get(PVName.SPLAN_COMPARTMENT_ZPFV);
        ref.volume[3] = theta.get(PVName.LBODY_COMPARTMENT_ZPFV);

    }

    /*
     * Lineqns() finds solution vector to a set of N simultaneous linear
     * equations using the Gauss-Jordan reduction algorithm with the diagonal
     * pivot strategy. NxN matrix A contains the coefficients; B contains
     * the right-hand side vector (both A and B are defined in estimate.c).
     * Based on Carnahan B., Luther H., Wilkes J.: Applied Numerical Methods,
     * 1969,
     * p. 276).
     */
// Previously
// int lineqs(double A[][ARRAY_SIZE], double B[], int N)
// Arrays of incomplete element type generate an error
// in GCC versions 4.0 and higher
// Modified by C. Dunn
// WAS int lineqs(double (*Ap)[ARRAY_SIZE][ARRAY_SIZE], double B[ARRAY_SIZE], int N)
    static int lineqs(double[][] A, double[] B, int N) {
        int i = 0, j = 0, k = 0;
        double EPS = 0.00001;

        for (k = 0; k < N; k++) {
            if (fabs(A[k][k]) < EPS) {
                System.out.println("Error in lineqs() located in sim/estimate.c\n");
                return (-1);
            }
            // normalize the pivot row
            for (j = k + 1; j < N; j++) {
                A[j][k] = A[j][k] / A[k][k];
            }
            B[k] = B[k] / A[k][k];
            A[k][k] = 1.;

            // eliminate k(th) column elements except for pivot
            for (i = 0; i < N; i++) {
                if ((i != k) & (A[k][i] != 0.)) {
                    for (j = k + 1; j < N; j++) {
                        A[j][i] -= A[k][i] * A[j][k];
                    }
                    B[i] -= A[k][i] * B[k];
                    A[k][i] = 0.;
                }
            }
        }
        return (0);
    }
}
