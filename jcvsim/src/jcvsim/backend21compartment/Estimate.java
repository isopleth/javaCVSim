package jcvsim.backend21compartment;

import static java.lang.Math.PI;
import static jcvsim.backendCommon.Maths.atan;
import static jcvsim.backendCommon.Maths.fabs;
import static jcvsim.backendCommon.Maths.sqrt;
import static jcvsim.backend21compartment.Data_vector.CompartmentIndex.*;
import static jcvsim.backend21compartment.Data_vector.ComplianceIndex.*;
import static jcvsim.backend21compartment.Data_vector.TimeIndex.*;

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
 * Last modified February 17, 2003
 */
// Converted to Java Jason Leake December 2016
/*
 * Estimate() serves two purposes: it estimates the initial pressure vector
 * and initializes the reflex structure. The first purpose is acvieved by
 * reading the parameter vector, initializing the coefficient matrix a
 * and the lhs vector b, and calling the linear equation solver lineqns(). The
 * routine returns to the calling environment the estimated pressures and the
 * atrial and ventricular timing information (duration of PR interval, atrial
 * systole, and ventricular systole) as part of a structure of type
 * Data_vector.
 */
public class Estimate {

    private static final int ARRAY_SIZE = 23;

    public static void estimate_ptr(Data_vector out, Parameter_vector theta, Reflex_vector ref) {

        double[][] a = new double[ARRAY_SIZE][];// Coefficient matrix
        for (int index = 0; index < ARRAY_SIZE; index++) {
            a[index] = new double[ARRAY_SIZE];
        }

        double[] b = new double[ARRAY_SIZE];                // Solution vector
        double T = 0.0, Tdias = 0.0, Tsys = 0.0;     // RR-interval, systolic time,
        // and diastolic time
        double tmp = 5000.0;
        int i = 0;

        T = 60. / theta.get(PVName.NOMINAL_HEART_RATE);
        Tsys = theta.get(PVName.VENTRICULAR_SYSTOLE_INTERVAL) * sqrt(T);  // ventricular systolic time interval
        Tdias = T - Tsys;               // ventricular diastolic time interval

        // Linearized volume equation.
        a[0][0] = theta.get(PVName.ASCENDING_AORTA_COMPLIANCE);           // Aortic arch
        a[1][0] = theta.get(PVName.BRACH_ART_COMPLIANCE);           // Common Carotids
        a[2][0] = theta.get(PVName.UBODY_ART_COMPLIANCE);          // Cerebral Arteries
        a[3][0] = theta.get(PVName.UBODY_VEN_COMPLIANCE);           // Cerebral veins
        a[4][0] = theta.get(PVName.SVC_COMPLIANCE);           // Jugular veins
        a[5][0] = theta.get(PVName.THORACIC_AORTA_COMPLIANCE);           // Thoracic aorta
        a[6][0] = theta.get(PVName.ABDOM_AORTA_COMPLIANCE);          // Abdominal aorta
        a[7][0] = theta.get(PVName.RENAL_ART_COMPLIANCE);          // Renal arteries
        a[8][0] = theta.get(PVName.RENAL_VEN_COMPLIANCE);           // Renal veins
        a[9][0] = theta.get(PVName.SPLAN_ART_COMPLIANCE);          // Splanchnic arteries
        a[10][0] = theta.get(PVName.SPLAN_VEN_COMPLIANCE);           // Splanchnic veins
        a[11][0] = theta.get(PVName.LBODY_ART_COMPLIANCE);           // Leg arteries
        a[12][0] = theta.get(PVName.LBODY_VEN_COMPLIANCE);           // Leg veins
        a[13][0] = theta.get(PVName.ABDOM_VEN_COMPLIANCE);           // Abdominal veins
        a[14][0] = theta.get(PVName.IVC_COMPLIANCE);           // Vena cava inf. 
        a[15][0] = theta.get(PVName.RA_DIASTOLIC_COMPLIANCE);           // Right atrium
        a[16][0] = theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);           // Right ventricle diastole
        a[17][0] = 0.;                      // Right ventricle systole
        a[18][0] = theta.get(PVName.PULM_ART_COMPLIANCE);           // Pulmonary arteries
        a[19][0] = theta.get(PVName.PULM_VEN_COMPLIANCE);           // Pulmonary veins
        a[20][0] = theta.get(PVName.LA_DIASTOLIC_COMPLIANCE);           // Left atrium
        a[21][0] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);           // Left ventricle diastole
        a[22][0] = 0.;                      // Left ventricle systole

        // Left ventricular outflow = aortic arch outflow
        a[0][1] = 1. / theta.get(PVName.BRACH_ART_RESISTANCE) + 1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE) + Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE);
        a[1][1] = -1. / theta.get(PVName.BRACH_ART_RESISTANCE);
        a[2][1] = 0.;
        a[3][1] = 0.;
        a[4][1] = 0.;
        a[5][1] = -1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE);
        a[6][1] = 0.;
        a[7][1] = 0.;
        a[8][1] = 0.;
        a[9][1] = 0.;
        a[10][1] = 0.;
        a[11][1] = 0.;
        a[12][1] = 0.;
        a[13][1] = 0.;
        a[14][1] = 0.;
        a[15][1] = 0.;
        a[16][1] = 0.;
        a[17][1] = 0.;
        a[18][1] = 0.;
        a[19][1] = 0.;
        a[20][1] = 0.;
        a[21][1] = 0.;
        a[22][1] = -Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE);

        // Common carotid arterial flow = cerebral arterial flow 
        a[0][2] = -1. / theta.get(PVName.BRACH_ART_RESISTANCE);
        a[1][2] = 1. / theta.get(PVName.BRACH_ART_RESISTANCE) + 1. / theta.get(PVName.UBODY_ART_RESISTANCE);
        a[2][2] = -1. / theta.get(PVName.UBODY_ART_RESISTANCE);
        a[3][2] = 0.;
        a[4][2] = 0.;
        a[5][2] = 0.;
        a[6][2] = 0.;
        a[7][2] = 0.;
        a[8][2] = 0.;
        a[9][2] = 0.;
        a[10][2] = 0.;
        a[11][2] = 0.;
        a[12][2] = 0.;
        a[13][2] = 0.;
        a[14][2] = 0.;
        a[15][2] = 0.;
        a[16][2] = 0.;
        a[17][2] = 0.;
        a[18][2] = 0.;
        a[19][2] = 0.;
        a[20][2] = 0.;
        a[21][2] = 0.;
        a[22][2] = 0.;

        // Upper body microflow.
        a[0][3] = 0.;
        a[1][3] = -1. / theta.get(PVName.UBODY_ART_RESISTANCE);
        a[2][3] = 1. / theta.get(PVName.UBODY_ART_RESISTANCE) + 1. / theta.get(PVName.UBODY_MICRO_RESISTANCE);
        a[3][3] = -1. / theta.get(PVName.UBODY_MICRO_RESISTANCE);
        a[4][3] = 0.;
        a[5][3] = 0.;
        a[6][3] = 0.;
        a[7][3] = 0.;
        a[8][3] = 0.;
        a[9][3] = 0.;
        a[10][3] = 0.;
        a[11][3] = 0.;
        a[12][3] = 0.;
        a[13][3] = 0.;
        a[14][3] = 0.;
        a[15][3] = 0.;
        a[16][3] = 0.;
        a[17][3] = 0.;
        a[18][3] = 0.;
        a[19][3] = 0.;
        a[20][3] = 0.;
        a[21][3] = 0.;
        a[22][3] = 0.;

        // Upper body veins to superior vena cava
        a[0][4] = 0.;
        a[1][4] = 0.;
        a[2][4] = -1. / theta.get(PVName.UBODY_MICRO_RESISTANCE);
        a[3][4] = 1. / theta.get(PVName.UBODY_MICRO_RESISTANCE) + 1. / theta.get(PVName.UBODY_VEN_RESISTANCE);
        a[4][4] = -1. / theta.get(PVName.UBODY_VEN_RESISTANCE);
        a[5][4] = 0.;
        a[6][4] = 0.;
        a[7][4] = 0.;
        a[8][4] = 0.;
        a[9][4] = 0.;
        a[10][4] = 0.;
        a[11][4] = 0.;
        a[12][4] = 0.;
        a[13][4] = 0.;
        a[14][4] = 0.;
        a[15][4] = 0.;
        a[16][4] = 0.;
        a[17][4] = 0.;
        a[18][4] = 0.;
        a[19][4] = 0.;
        a[20][4] = 0.;
        a[21][4] = 0.;
        a[22][4] = 0.;

        // Thoracic aorta to abdominal aorta
        a[0][5] = -1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE);
        a[1][5] = 0.;
        a[2][5] = 0.;
        a[3][5] = 0.;
        a[4][5] = 0.;
        a[5][5] = 1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE) + 1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE);
        a[6][5] = -1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE);
        a[7][5] = 0.;
        a[8][5] = 0.;
        a[9][5] = 0.;
        a[10][5] = 0.;
        a[11][5] = 0.;
        a[12][5] = 0.;
        a[13][5] = 0.;
        a[14][5] = 0.;
        a[15][5] = 0.;
        a[16][5] = 0.;
        a[17][5] = 0.;
        a[18][5] = 0.;
        a[19][5] = 0.;
        a[20][5] = 0.;
        a[21][5] = 0.;
        a[22][5] = 0.;

        // Abdominal aorta to sytemics arteries
        a[0][6] = 0.;
        a[1][6] = 0.;
        a[2][6] = 0.;
        a[3][6] = 0.;
        a[4][6] = 0.;
        a[5][6] = -1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE);
        a[6][6] = 1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE) + 1. / theta.get(PVName.RENAL_ART_RESISTANCE) + 1. / theta.get(PVName.SPLAN_ART_RESISTANCE) + 1. / theta.get(PVName.LBODY_ART_RESISTANCE);
        a[7][6] = -1. / theta.get(PVName.RENAL_ART_RESISTANCE);
        a[8][6] = 0.;
        a[9][6] = -1. / theta.get(PVName.SPLAN_ART_RESISTANCE);
        a[10][6] = 0.;
        a[11][6] = -1. / theta.get(PVName.LBODY_ART_RESISTANCE);
        a[12][6] = 0.;
        a[13][6] = 0.;
        a[14][6] = 0.;
        a[15][6] = 0.;
        a[16][6] = 0.;
        a[17][6] = 0.;
        a[18][6] = 0.;
        a[19][6] = 0.;
        a[20][6] = 0.;
        a[21][6] = 0.;
        a[22][6] = 0.;

        // Abdominal aorta to renal vein
        a[0][7] = 0.;
        a[1][7] = 0.;
        a[2][7] = 0.;
        a[3][7] = 0.;
        a[4][7] = 0.;
        a[5][7] = 0.;
        a[6][7] = -1. / theta.get(PVName.RENAL_ART_RESISTANCE);
        a[7][7] = 1. / theta.get(PVName.RENAL_ART_RESISTANCE) + 1. / theta.get(PVName.RENAL_MICRO_RESISTANCE);
        a[8][7] = -1. / theta.get(PVName.RENAL_MICRO_RESISTANCE);
        a[9][7] = 0.;
        a[10][7] = 0.;
        a[11][7] = 0.;
        a[12][7] = 0.;
        a[13][7] = 0.;
        a[14][7] = 0.;
        a[15][7] = 0.;
        a[16][7] = 0.;
        a[17][7] = 0.;
        a[18][7] = 0.;
        a[19][7] = 0.;
        a[20][7] = 0.;
        a[21][7] = 0.;
        a[22][7] = 0.;

        // Renal artery to abdominal veins
        a[0][8] = 0.;
        a[1][8] = 0.;
        a[2][8] = 0.;
        a[3][8] = 0.;
        a[4][8] = 0.;
        a[5][8] = 0.;
        a[6][8] = 0.;
        a[7][8] = -1. / theta.get(PVName.RENAL_MICRO_RESISTANCE);
        a[8][8] = 1. / theta.get(PVName.RENAL_MICRO_RESISTANCE) + 1. / theta.get(PVName.RENAL_VEN_RESISTANCE);
        a[9][8] = 0.;
        a[10][8] = 0.;
        a[11][8] = 0.;
        a[12][8] = 0.;
        a[13][8] = -1. / theta.get(PVName.RENAL_VEN_RESISTANCE);
        a[14][8] = 0.;
        a[15][8] = 0.;
        a[16][8] = 0.;
        a[17][8] = 0.;
        a[18][8] = 0.;
        a[19][8] = 0.;
        a[20][8] = 0.;
        a[21][8] = 0.;
        a[22][8] = 0.;

        // Abdominal aorta to splanchnic veins
        a[0][9] = 0.;
        a[1][9] = 0.;
        a[2][9] = 0.;
        a[3][9] = 0.;
        a[4][9] = 0.;
        a[5][9] = 0.;
        a[6][9] = -1. / theta.get(PVName.SPLAN_ART_RESISTANCE);
        a[7][9] = 0.;
        a[8][9] = 0.;
        a[9][9] = 1. / theta.get(PVName.SPLAN_ART_RESISTANCE) + 1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE);
        a[10][9] = -1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE);
        a[11][9] = 0.;
        a[12][9] = 0.;
        a[13][9] = 0.;
        a[14][9] = 0.;
        a[15][9] = 0.;
        a[16][9] = 0.;
        a[17][9] = 0.;
        a[18][9] = 0.;
        a[19][9] = 0.;
        a[20][9] = 0.;
        a[21][9] = 0.;
        a[22][9] = 0.;

        // Flow through splanchnic compartment.
        a[0][10] = 0.;
        a[1][10] = 0.;
        a[2][10] = 0.;
        a[3][10] = 0.;
        a[4][10] = 0.;
        a[5][10] = 0.;
        a[6][10] = 0.;
        a[7][10] = 0.;
        a[8][10] = 0.;
        a[9][10] = -1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE);
        a[10][10] = 1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE) + 1. / theta.get(PVName.SPLAN_VEN_RESISTANCE);
        a[11][10] = 0.;
        a[12][10] = 0.;
        a[13][10] = -1. / theta.get(PVName.SPLAN_VEN_RESISTANCE);
        a[14][10] = 0.;
        a[15][10] = 0.;
        a[16][10] = 0.;
        a[17][10] = 0.;
        a[18][10] = 0.;
        a[19][10] = 0.;
        a[20][10] = 0.;
        a[21][10] = 0.;
        a[22][10] = 0.;

        // Abdominal aorta to leg arteries
        a[0][11] = 0.;
        a[1][11] = 0.;
        a[2][11] = 0.;
        a[3][11] = 0.;
        a[4][11] = 0.;
        a[5][11] = 0.;
        a[6][11] = -1. / theta.get(PVName.LBODY_ART_RESISTANCE);
        a[7][11] = 0.;
        a[8][11] = 0.;
        a[9][11] = 0.;
        a[10][11] = 0.;
        a[11][11] = 1. / theta.get(PVName.LBODY_ART_RESISTANCE) + 1. / theta.get(PVName.LBODY_MICRO_RESISTANCE);
        a[12][11] = -1. / theta.get(PVName.LBODY_MICRO_RESISTANCE);
        a[13][11] = 0.;
        a[14][11] = 0.;
        a[15][11] = 0.;
        a[16][11] = 0.;
        a[17][11] = 0.;
        a[18][11] = 0.;
        a[19][11] = 0.;
        a[20][11] = 0.;
        a[21][11] = 0.;
        a[22][11] = 0.;

        // Leg arteries to abdominal veins
        a[0][12] = 0.;
        a[1][12] = 0.;
        a[2][12] = 0.;
        a[3][12] = 0.;
        a[4][12] = 0.;
        a[5][12] = 0.;
        a[6][12] = 0.;
        a[7][12] = 0.;
        a[8][12] = 0.;
        a[9][12] = 0.;
        a[10][12] = 0.;
        a[11][12] = -1. / theta.get(PVName.LBODY_MICRO_RESISTANCE);
        a[12][12] = 1. / theta.get(PVName.LBODY_MICRO_RESISTANCE) + 1. / theta.get(PVName.LBODY_VEN_RESISTANCE);
        a[13][12] = -1. / theta.get(PVName.LBODY_VEN_RESISTANCE);
        a[14][12] = 0.;
        a[15][12] = 0.;
        a[16][12] = 0.;
        a[17][12] = 0.;
        a[18][12] = 0.;
        a[19][12] = 0.;
        a[20][12] = 0.;
        a[21][12] = 0.;
        a[22][12] = 0.;

        // Parallel venous outflow to abdominal veins
        a[0][13] = 0.;
        a[1][13] = 0.;
        a[2][13] = 0.;
        a[3][13] = 0.;
        a[4][13] = 0.;
        a[5][13] = 0.;
        a[6][13] = 0.;
        a[7][13] = 0.;
        a[8][13] = -1. / theta.get(PVName.RENAL_VEN_RESISTANCE);
        a[9][13] = 0.;
        a[10][13] = -1. / theta.get(PVName.SPLAN_VEN_RESISTANCE);
        a[11][13] = 0.;
        a[12][13] = -1. / theta.get(PVName.LBODY_VEN_RESISTANCE);
        a[13][13] = 1. / theta.get(PVName.RENAL_VEN_RESISTANCE) + 1. / theta.get(PVName.SPLAN_VEN_RESISTANCE) + 1. / theta.get(PVName.LBODY_VEN_RESISTANCE) + 1. / theta.get(PVName.ABDOM_VEN_RESISTANCE);
        a[14][13] = -1. / theta.get(PVName.ABDOM_VEN_RESISTANCE);
        a[15][13] = 0.;
        a[16][13] = 0.;
        a[17][13] = 0.;
        a[18][13] = 0.;
        a[19][13] = 0.;
        a[20][13] = 0.;
        a[21][13] = 0.;
        a[22][13] = 0.;

        // Abdominal veins to right atrium
        a[0][14] = 0.;
        a[1][14] = 0.;
        a[2][14] = 0.;
        a[3][14] = 0.;
        a[4][14] = 0.;
        a[5][14] = 0.;
        a[6][14] = 0.;
        a[7][14] = 0.;
        a[8][14] = 0.;
        a[9][14] = 0.;
        a[10][14] = 0.;
        a[11][14] = 0.;
        a[12][14] = 0.;
        a[13][14] = -1. / theta.get(PVName.ABDOM_VEN_RESISTANCE);
        a[14][14] = 1. / theta.get(PVName.ABDOM_VEN_RESISTANCE) + 1. / theta.get(PVName.IVC_RESISTANCE);
        a[15][14] = -1. / theta.get(PVName.IVC_RESISTANCE);
        a[16][14] = 0.;
        a[17][14] = 0.;
        a[18][14] = 0.;
        a[19][14] = 0.;
        a[20][14] = 0.;
        a[21][14] = 0.;
        a[22][14] = 0.;

        // Right atrial inflow to right ventricle
        a[0][15] = 0.;
        a[1][15] = 0.;
        a[2][15] = 0.;
        a[3][15] = 0.;
        a[4][15] = -1. / theta.get(PVName.SVC_RESISTANCE);
        a[5][15] = 0.;
        a[6][15] = 0.;
        a[7][15] = 0.;
        a[8][15] = 0.;
        a[9][15] = 0.;
        a[10][15] = 0.;
        a[11][15] = 0.;
        a[12][15] = 0.;
        a[13][15] = 0.;
        a[14][15] = -1. / theta.get(PVName.IVC_RESISTANCE);
        a[15][15] = Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE) + 1. / theta.get(PVName.IVC_RESISTANCE) + 1. / theta.get(PVName.SVC_RESISTANCE);
        a[16][15] = -Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE);
        a[17][15] = 0.;
        a[18][15] = 0.;
        a[19][15] = 0.;
        a[20][15] = 0.;
        a[21][15] = 0.;
        a[22][15] = 0.;

        // Tricuspid flow to pulmonary artery
        a[0][16] = 0.;
        a[1][16] = 0.;
        a[2][16] = 0.;
        a[3][16] = 0.;
        a[4][16] = 0.;
        a[5][16] = 0.;
        a[6][16] = 0.;
        a[7][16] = 0.;
        a[8][16] = 0.;
        a[9][16] = 0.;
        a[10][16] = 0.;
        a[11][16] = 0.;
        a[12][16] = 0.;
        a[13][16] = 0.;
        a[14][16] = 0.;
        a[15][16] = -Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE);
        a[16][16] = Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE);
        a[17][16] = Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[18][16] = -Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[19][16] = 0.;
        a[20][16] = 0.;
        a[21][16] = 0.;
        a[22][16] = 0.;

        // Left atrial inflow.
        a[0][17] = 0.;
        a[1][17] = 0.;
        a[2][17] = 0.;
        a[3][17] = 0.;
        a[4][17] = 0.;
        a[5][17] = 0.;
        a[6][17] = 0.;
        a[7][17] = 0.;
        a[8][17] = 0.;
        a[9][17] = 0.;
        a[10][17] = 0.;
        a[11][17] = 0.;
        a[12][17] = 0.;
        a[13][17] = 0.;
        a[14][17] = 0.;
        a[15][17] = 0.;
        a[16][17] = theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);
        a[17][17] = -theta.get(PVName.RV_SYSTOLIC_COMPLIANCE) - Tsys / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[18][17] = Tsys / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[19][17] = 0.;
        a[20][17] = 0.;
        a[21][17] = 0.;
        a[22][17] = 0.;

        // Left atrial inflow.
        a[0][18] = 0.;
        a[1][18] = 0.;
        a[2][18] = 0.;
        a[3][18] = 0.;
        a[4][18] = 0.;
        a[5][18] = 0.;
        a[6][18] = 0.;
        a[7][18] = 0.;
        a[8][18] = 0.;
        a[9][18] = 0.;
        a[10][18] = 0.;
        a[11][18] = 0.;
        a[12][18] = 0.;
        a[13][18] = 0.;
        a[14][18] = 0.;
        a[15][18] = 0.;
        a[16][18] = 0.;
        a[17][18] = -Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[18][18] = 1. / theta.get(PVName.PULM_MICRO_RESISTANCE) + Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[19][18] = -1. / theta.get(PVName.PULM_MICRO_RESISTANCE);
        a[20][18] = 0.;
        a[21][18] = 0.;
        a[22][18] = 0.;

        // Left atrial inflow.
        a[0][19] = 0.;
        a[1][19] = 0.;
        a[2][19] = 0.;
        a[3][19] = 0.;
        a[4][19] = 0.;
        a[5][19] = 0.;
        a[6][19] = 0.;
        a[7][19] = 0.;
        a[8][19] = 0.;
        a[9][19] = 0.;
        a[10][19] = 0.;
        a[11][19] = 0.;
        a[12][19] = 0.;
        a[13][19] = 0.;
        a[14][19] = 0.;
        a[15][19] = 0.;
        a[16][19] = 0.;
        a[17][19] = 0.;
        a[18][19] = -1. / theta.get(PVName.PULM_MICRO_RESISTANCE);
        a[19][19] = 1. / theta.get(PVName.PULM_MICRO_RESISTANCE) + 1. / theta.get(PVName.PULM_VEN_RESISTANCE);
        a[20][19] = -1. / theta.get(PVName.PULM_VEN_RESISTANCE);
        a[21][19] = 0.;
        a[22][19] = 0.;

        // Left atrial inflow.
        a[0][20] = 0.;
        a[1][20] = 0.;
        a[2][20] = 0.;
        a[3][20] = 0.;
        a[4][20] = 0.;
        a[5][20] = 0.;
        a[6][20] = 0.;
        a[7][20] = 0.;
        a[8][20] = 0.;
        a[9][20] = 0.;
        a[10][20] = 0.;
        a[11][20] = 0.;
        a[12][20] = 0.;
        a[13][20] = 0.;
        a[14][20] = 0.;
        a[15][20] = 0.;
        a[16][20] = 0.;
        a[17][20] = 0.;
        a[18][20] = 0.;
        a[19][20] = -1. / theta.get(PVName.PULM_VEN_RESISTANCE);
        a[20][20] = 1. / theta.get(PVName.PULM_VEN_RESISTANCE) + Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        a[21][20] = -Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        a[22][20] = 0.;

        // Left atrial inflow.
        a[0][21] = Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE);
        a[1][21] = 0.;
        a[2][21] = 0.;
        a[3][21] = 0.;
        a[4][21] = 0.;
        a[5][21] = 0.;
        a[6][21] = 0.;
        a[7][21] = 0.;
        a[8][21] = 0.;
        a[9][21] = 0.;
        a[10][21] = 0.;
        a[11][21] = 0.;
        a[12][21] = 0.;
        a[13][21] = 0.;
        a[14][21] = 0.;
        a[15][21] = 0.;
        a[16][21] = 0.;
        a[17][21] = 0.;
        a[18][21] = 0.;
        a[19][21] = 0.;
        a[20][21] = Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        a[21][21] = -Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        a[22][21] = -Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE);

        // Left atrial inflow.
        a[0][22] = Tsys / theta.get(PVName.AORTIC_VALVE_RESISTANCE);
        a[1][22] = 0.;
        a[2][22] = 0.;
        a[3][22] = 0.;
        a[4][22] = 0.;
        a[5][22] = 0.;
        a[6][22] = 0.;
        a[7][22] = 0.;
        a[8][22] = 0.;
        a[9][22] = 0.;
        a[10][22] = 0.;
        a[11][22] = 0.;
        a[12][22] = 0.;
        a[13][22] = 0.;
        a[14][22] = 0.;
        a[15][22] = 0.;
        a[16][22] = 0.;
        a[17][22] = 0.;
        a[18][22] = 0.;
        a[19][22] = 0.;
        a[20][22] = 0.;
        a[21][22] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);
        a[22][22] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE) - Tsys / theta.get(PVName.AORTIC_VALVE_RESISTANCE);

        b[0] = theta.get(PVName.TOTAL_BLOOD_VOLUME)
                - (theta.get(PVName.ASCENDING_AORTA_VOLUME) + theta.get(PVName.BRACH_ART_ZPFV) + theta.get(PVName.THORACIC_AORTA_ZPFV) + theta.get(PVName.UBODY_ART_ZPFV)
                + theta.get(PVName.ABDOM_AORTA_ZPFV) + theta.get(PVName.RENAL_ART_ZPFV) + theta.get(PVName.SPLAN_ART_ZPFV) + theta.get(PVName.LBODY_ART_ZPFV)
                + theta.get(PVName.UBODY_VEN_ZPFV) + theta.get(PVName.RENAL_VEN_ZPFV) + theta.get(PVName.SPLAN_VEN_ZPFV) + theta.get(PVName.LBODY_VEN_ZPFV)
                + theta.get(PVName.ABDOM_VEN_ZPFV) + theta.get(PVName.IVC_ZPFV) + theta.get(PVName.SVC_ZPFV) + theta.get(PVName.RA_ZPFV)
                + theta.get(PVName.RV_ZPFV) + theta.get(PVName.PULM_ART_ZPFV) + theta.get(PVName.PULN_VEN_ZPFV) + theta.get(PVName.LA_ZPFV)
                + theta.get(PVName.LV_ZPFV))
                + theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LA_DIASTOLIC_COMPLIANCE) + theta.get(PVName.RA_DIASTOLIC_COMPLIANCE) + theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) + theta.get(PVName.RV_DIASTOLIC_COMPLIANCE)
                + theta.get(PVName.PULM_ART_COMPLIANCE) + theta.get(PVName.PULM_VEN_COMPLIANCE) + theta.get(PVName.IVC_COMPLIANCE) + theta.get(PVName.SVC_COMPLIANCE)
                + theta.get(PVName.ASCENDING_AORTA_COMPLIANCE) + theta.get(PVName.BRACH_ART_COMPLIANCE) + theta.get(PVName.THORACIC_AORTA_COMPLIANCE));
        b[1] = 0.;
        b[2] = 0.;
        b[3] = 0.;
        b[4] = 0.;
        b[5] = 0.;
        b[6] = 0.;
        b[7] = 0.;
        b[8] = 0.;
        b[9] = 0.;
        b[10] = 0.;
        b[11] = 0.;
        b[12] = 0.;
        b[13] = 0.;
        b[14] = 0.;
        b[15] = 0.;
        b[16] = 0.;
        b[17] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.RV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.RV_SYSTOLIC_COMPLIANCE));
        b[18] = 0.;
        b[19] = 0.;
        b[20] = 0.;
        b[21] = 0.;
        b[22] = theta.get(PVName.INTRA_THORACIC_PRESSURE) * (theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE));

        //  for (i=0;i<ARRAY_SIZE; i++)
        //    printf("%e\n", b[i]);
        //  printf("\n");
        // Previously
        // lineqs(a, b, ARRAY_SIZE);     
        // Changed to be compatible w/ GCC versions 4.0 and higher
        // Modified by C. Dunn
        lineqs(a, b, ARRAY_SIZE);             // Call the linear equation solver
        // to solve for the initial pressure
        // estimate.

        // Call the modified Newton-Raphson routine for iterative improvement of
        // the pressure estimates. Terminate when the absolute blood volume error
        // is less than an arbitrarily pre-set error bound.
        // MODIFIED BY BRANDON PIERQUET
        tmp = fabs(mnewt_ptr(theta, b));
        while (tmp > 10.0e-12) {
            tmp = fabs(mnewt_ptr(theta, b));
            //    printf("Absolute volume error = %e\n", tmp);
        }

        // Initialize the first 21 entries of the pressure vector to the end of 
        // ventricular diastole.
        out.pressure[ASCENDING_AORTIC_CPI] = b[0];
        out.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] = b[1];
        out.pressure[UPPER_BODY_ARTERIAL_CPI] = b[2];
        out.pressure[UPPER_BODY_VENOUS_CPI] = b[3];
        out.pressure[SUPERIOR_VENA_CAVA_CPI] = b[4];
        out.pressure[THORACIC_AORTIC_CPI] = b[5];
        out.pressure[ABDOMINAL_AORTIC_CPI] = b[6];
        out.pressure[RENAL_ARTERIAL_CPI] = b[7];
        out.pressure[RENAL_VENOUS_CPI] = b[8];
        out.pressure[SPLANCHNIC_ARTERIAL_CPI] = b[9];
        out.pressure[SPLANCHNIC_VENOUS_CPI] = b[10];
        out.pressure[LBODY_ARTERIAL_CPI] = b[11];
        out.pressure[LBODY_VENOUS_CPI] = b[12];
        out.pressure[ABDOMINAL_VENOUS_CPI] = b[13];
        out.pressure[INFERIOR_VENA_CAVA_CPI] = b[14];
        out.pressure[RIGHT_ATRIAL_CPI] = b[15];
        out.pressure[RIGHT_VENTRICULAR_CPI] = b[16];
        out.pressure[PULMONARY_ARTERIAL_CPI] = b[18];
        out.pressure[PULMONARY_VENOUS_CPI] = b[19];
        out.pressure[LEFT_ATRIAL_CPI] = b[20];
        out.pressure[LEFT_VENTRICULAR_CPI] = b[21];

        // temporary
        out.compliance[RV_END_SYSTOLIC_COMPL] = theta.get(PVName.RV_SYSTOLIC_COMPLIANCE);
        out.compliance[LV_END_SYSTOLIC_COMPL] = theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);

        // Bias pressures and intrathoracic pressure.
        out.pressure[BIAS_1_CPI] = out.dPressureDt[BIAS_1_CPI] = 0.0;
        out.pressure[BIAS_2_CPI] = out.dPressureDt[BIAS_2_CPI] = 0.0;
        out.pressure[BIAS_3_CPI] = out.dPressureDt[BIAS_3_CPI] = 0.0;
        out.pressure[INTRA_THORACIC_CPI] = theta.get(PVName.INTRA_THORACIC_PRESSURE);
        out.dPressureDt[INTRA_THORACIC_CPI] = 0.0;

        for (i = 0; i < out.grav.length; i++) {
            out.grav[i] = 0.0;
        }

        // Initialize the timing variables: absolute time, cardiac time, PR interval,
        // atrial, and ventricular systolic time interval, respectively.
        out.time[ABSOLUTE_TIME] = 0.0;
        out.time[CARDIAC_TIME] = 0.0;
        out.time[PR_DELAY_TIME] = theta.get(PVName.PR_INTERVAL) * sqrt(T);   // Intialization of PR-interval
        out.time[ATRIAL_SYSTOLE_TIME] = theta.get(PVName.ATRIAL_SYSTOLE_INTERVAL) * sqrt(T);   // Initialization of atrial systolic
        // time interval.
        out.time[VENTRICULAR_SYSTOLE_TIME] = theta.get(PVName.VENTRICULAR_SYSTOLE_INTERVAL) * sqrt(T);   // Initialization of ventricular 
        // systolic time interval.
        // The next line is experimental as of Feb 20th, 2002. It initializes
        // "modified absolute time" needed for the orthostatic stress simulations.
        out.time[MODIFIED_ABSOLUTE_TIME] = 0.0;
        out.time[VENTRICULAR_TIME] = -out.time[PR_DELAY_TIME];  // Initialization of ventricular time to
        // negative PR-interval; added on 08/11/04

        // Initialize the timing variables: absolute time, cardiac time, PR interval,
        // atrial, and ventricular systolic time interval, respectively.
        out.time_new[ABSOLUTE_TIME] = 0.0;
        out.time_new[CARDIAC_TIME] = 0.0;
        out.time_new[PR_DELAY_TIME] = theta.get(PVName.PR_INTERVAL) * sqrt(T); // Intialization of PR-interval
        out.time_new[ATRIAL_SYSTOLE_TIME] = theta.get(PVName.ATRIAL_SYSTOLE_INTERVAL) * sqrt(T); // Initialization of atrial
        // systolic time interval.
        out.time_new[VENTRICULAR_SYSTOLE_TIME] = theta.get(PVName.VENTRICULAR_SYSTOLE_INTERVAL) * sqrt(T); // Initialization of ventricular 
        // systolic time interval.
        out.time_new[MODIFIED_ABSOLUTE_TIME] = -out.time_new[PR_DELAY_TIME];       // 
        out.time_new[VENTRICULAR_TIME] = -out.time_new[PR_DELAY_TIME];       // Initialize ventricular time

        // The following lines initialize the reflex structure entires to their
        // initial values.
        ref.hr[0] = theta.get(PVName.NOMINAL_HEART_RATE);
        ref.hr[1] = theta.get(PVName.NOMINAL_HEART_RATE);        // currently assigned to cum_HR;
        ref.hr[2] = theta.get(PVName.NOMINAL_HEART_RATE);

        ref.step_cnt = 1;                 // number of integration steps taken
        // in current cardiac cycle.

        ref.compliance[0] = theta.get(PVName.RV_SYSTOLIC_COMPLIANCE);
        ref.compliance[1] = theta.get(PVName.LV_SYSTOLIC_COMPLIANCE);

        ref.resistance[0] = theta.get(PVName.UBODY_MICRO_RESISTANCE);
        ref.resistance[1] = theta.get(PVName.RENAL_MICRO_RESISTANCE);
        ref.resistance[2] = theta.get(PVName.SPLAN_MICRO_RESISTANCE);
        ref.resistance[3] = theta.get(PVName.LBODY_MICRO_RESISTANCE);

        ref.volume[0] = theta.get(PVName.UBODY_VEN_ZPFV);
        ref.volume[1] = theta.get(PVName.RENAL_VEN_ZPFV);
        ref.volume[2] = theta.get(PVName.SPLAN_VEN_ZPFV);
        ref.volume[3] = theta.get(PVName.LBODY_VEN_ZPFV);

    }


    /*
     * Mnewt() is an implmentation of a modified Newton method for finding
     * the common roots of a set of non-linear equations. See Numerical
     * Recipes, 2nd ed., p. 379 for the general idea. The non-linearities
     * of the splanchnic, leg, and abdominal vascular beds necessitate an
     * iterative improvement of the initial pressures estimated in estimate()
     * to account properly for total blood volume.
     */
    static double mnewt_ptr(Parameter_vector theta, double b[]) {

        double[] F = new double[ARRAY_SIZE];
        double[][] a = new double[ARRAY_SIZE][];
        for (int index = 0; index < ARRAY_SIZE; index++) {
            a[index] = new double[ARRAY_SIZE];
        }

        double Vsp = 0.0, Vll = 0.0, Vab = 0.0;     // non-linear p-v relations
        double Csp = 0.0, Cll = 0.0, Cab = 0.0;     // non-linear compliances
        double T = 0.0, Tdias = 0.0, Tsys = 0.0;    // cardiac interval variables 
        double con = 0.0, vol_error = 0.0;          // temporary variables

        T = 60. / theta.get(PVName.NOMINAL_HEART_RATE);
        Tsys = 0.3 * sqrt(T);
        Tdias = T - Tsys;

        con = PI * theta.get(PVName.SPLAN_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL);
        Csp = theta.get(PVName.SPLAN_VEN_COMPLIANCE) / (1.0 + con * con * b[10] * b[10]);
        Vsp = 2.0 * theta.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL) * atan(con * b[10]) / PI;

        con = PI * theta.get(PVName.LBODY_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL);
        Cll = theta.get(PVName.LBODY_VEN_COMPLIANCE) / (1.0 + con * con * b[12] * b[12]);
        Vll = 2.0 * theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL) * atan(con * b[12]) / PI;

        con = PI * theta.get(PVName.ABDOM_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_ABDOM_DISTENDING_VOL);
        Cab = theta.get(PVName.ABDOM_VEN_COMPLIANCE) / (1.0 + con * con * b[13] * b[13]);
        Vab = 2.0 * theta.get(PVName.MAX_INCREASE_IN_ABDOM_DISTENDING_VOL) * atan(con * b[13]) / PI;

        F[0] = theta.get(PVName.TOTAL_BLOOD_VOLUME)
                - (theta.get(PVName.ASCENDING_AORTA_VOLUME) + theta.get(PVName.BRACH_ART_ZPFV) + theta.get(PVName.THORACIC_AORTA_ZPFV) + theta.get(PVName.UBODY_ART_ZPFV)
                + theta.get(PVName.ABDOM_AORTA_ZPFV) + theta.get(PVName.RENAL_ART_ZPFV) + theta.get(PVName.SPLAN_ART_ZPFV) + theta.get(PVName.LBODY_ART_ZPFV)
                + theta.get(PVName.UBODY_VEN_ZPFV) + theta.get(PVName.RENAL_VEN_ZPFV) + theta.get(PVName.SPLAN_VEN_ZPFV) + theta.get(PVName.LBODY_VEN_ZPFV)
                + theta.get(PVName.ABDOM_VEN_ZPFV) + theta.get(PVName.IVC_ZPFV) + theta.get(PVName.SVC_ZPFV) + theta.get(PVName.RA_ZPFV)
                + theta.get(PVName.RV_ZPFV) + theta.get(PVName.PULM_ART_ZPFV) + theta.get(PVName.PULN_VEN_ZPFV) + theta.get(PVName.LA_ZPFV)
                + theta.get(PVName.LV_ZPFV))
                + (theta.get(PVName.LA_DIASTOLIC_COMPLIANCE) + theta.get(PVName.RA_DIASTOLIC_COMPLIANCE) + theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) + theta.get(PVName.RV_DIASTOLIC_COMPLIANCE) + theta.get(PVName.PULM_ART_COMPLIANCE)
                + theta.get(PVName.PULM_VEN_COMPLIANCE) + theta.get(PVName.IVC_COMPLIANCE) + theta.get(PVName.SVC_COMPLIANCE) + theta.get(PVName.ASCENDING_AORTA_COMPLIANCE) + theta.get(PVName.BRACH_ART_COMPLIANCE)
                + theta.get(PVName.THORACIC_AORTA_COMPLIANCE)) * theta.get(PVName.INTRA_THORACIC_PRESSURE)
                - (theta.get(PVName.UBODY_VEN_COMPLIANCE) * b[3] + theta.get(PVName.RENAL_VEN_COMPLIANCE) * b[8] + Vsp + Vll + Vab + theta.get(PVName.IVC_COMPLIANCE) * b[14]
                + theta.get(PVName.SVC_COMPLIANCE) * b[4] + theta.get(PVName.RA_DIASTOLIC_COMPLIANCE) * b[15] + theta.get(PVName.RV_DIASTOLIC_COMPLIANCE) * b[16]
                + theta.get(PVName.PULM_ART_COMPLIANCE) * b[18] + theta.get(PVName.PULM_VEN_COMPLIANCE) * b[19] + theta.get(PVName.LA_DIASTOLIC_COMPLIANCE) * b[20]
                + theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) * b[21] + theta.get(PVName.ASCENDING_AORTA_COMPLIANCE) * b[0] + theta.get(PVName.BRACH_ART_COMPLIANCE) * b[1]
                + theta.get(PVName.UBODY_ART_COMPLIANCE) * b[2] + theta.get(PVName.THORACIC_AORTA_COMPLIANCE) * b[5] + theta.get(PVName.ABDOM_AORTA_COMPLIANCE) * b[6]
                + theta.get(PVName.RENAL_ART_COMPLIANCE) * b[7] + theta.get(PVName.SPLAN_ART_COMPLIANCE) * b[9] + theta.get(PVName.LBODY_ART_COMPLIANCE) * b[11]);

        F[1] = (-1. / theta.get(PVName.BRACH_ART_RESISTANCE) - 1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE) - Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE)) * b[0]
                + b[1] / theta.get(PVName.BRACH_ART_RESISTANCE) + b[5] / theta.get(PVName.THORACIC_AORTA_RESISTANCE) + Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE) * b[22];

        F[2] = (-1. / theta.get(PVName.BRACH_ART_RESISTANCE) - 1. / theta.get(PVName.UBODY_ART_RESISTANCE)) * b[1] + b[0] / theta.get(PVName.BRACH_ART_RESISTANCE)
                + b[2] / theta.get(PVName.UBODY_ART_RESISTANCE);

        F[3] = (-1. / theta.get(PVName.UBODY_ART_RESISTANCE) - 1. / theta.get(PVName.UBODY_MICRO_RESISTANCE)) * b[2] + b[1] / theta.get(PVName.UBODY_ART_RESISTANCE)
                + b[3] / theta.get(PVName.UBODY_MICRO_RESISTANCE);

        F[4] = (-1. / theta.get(PVName.UBODY_MICRO_RESISTANCE) - 1. / theta.get(PVName.UBODY_VEN_RESISTANCE)) * b[3] + b[2] / theta.get(PVName.UBODY_MICRO_RESISTANCE)
                + b[4] / theta.get(PVName.UBODY_VEN_RESISTANCE);

        F[5] = (-1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE) - 1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE)) * b[5] + b[0] / theta.get(PVName.THORACIC_AORTA_RESISTANCE)
                + b[6] / theta.get(PVName.ABDOM_AORTA_RESISTANCE);

        F[6] = b[7] / theta.get(PVName.RENAL_ART_RESISTANCE) + b[9] / theta.get(PVName.SPLAN_ART_RESISTANCE) + b[11] / theta.get(PVName.LBODY_ART_RESISTANCE)
                - (1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE) + 1. / theta.get(PVName.RENAL_ART_RESISTANCE) + 1. / theta.get(PVName.SPLAN_ART_RESISTANCE) + 1. / theta.get(PVName.LBODY_ART_RESISTANCE))
                * b[6] + b[5] / theta.get(PVName.ABDOM_AORTA_RESISTANCE);

        F[7] = (-1. / theta.get(PVName.RENAL_ART_RESISTANCE) - 1. / theta.get(PVName.RENAL_MICRO_RESISTANCE)) * b[7] + b[6] / theta.get(PVName.RENAL_ART_RESISTANCE)
                + b[8] / theta.get(PVName.RENAL_MICRO_RESISTANCE);

        F[8] = (-1. / theta.get(PVName.RENAL_MICRO_RESISTANCE) - 1. / theta.get(PVName.RENAL_VEN_RESISTANCE)) * b[8] + b[7] / theta.get(PVName.RENAL_MICRO_RESISTANCE)
                + b[13] / theta.get(PVName.RENAL_VEN_RESISTANCE);

        F[9] = (-1. / theta.get(PVName.SPLAN_ART_RESISTANCE) - 1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE)) * b[9] + b[6] / theta.get(PVName.SPLAN_ART_RESISTANCE)
                + b[10] / theta.get(PVName.SPLAN_MICRO_RESISTANCE);

        F[10] = (-1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE) - 1. / theta.get(PVName.SPLAN_VEN_RESISTANCE)) * b[10] + b[9] / theta.get(PVName.SPLAN_MICRO_RESISTANCE)
                + b[13] / theta.get(PVName.SPLAN_VEN_RESISTANCE);

        F[11] = (-1. / theta.get(PVName.LBODY_ART_RESISTANCE) - 1. / theta.get(PVName.LBODY_MICRO_RESISTANCE)) * b[11] + b[6] / theta.get(PVName.LBODY_ART_RESISTANCE)
                + b[12] / theta.get(PVName.LBODY_MICRO_RESISTANCE);

        F[12] = (-1. / theta.get(PVName.LBODY_MICRO_RESISTANCE) - 1. / theta.get(PVName.LBODY_VEN_RESISTANCE)) * b[12] + b[11] / theta.get(PVName.LBODY_MICRO_RESISTANCE)
                + b[13] / theta.get(PVName.LBODY_VEN_RESISTANCE);

        F[13] = b[12] / theta.get(PVName.LBODY_VEN_RESISTANCE) + b[10] / theta.get(PVName.SPLAN_VEN_RESISTANCE) + b[8] / theta.get(PVName.RENAL_VEN_RESISTANCE)
                - (1. / theta.get(PVName.RENAL_VEN_RESISTANCE) + 1. / theta.get(PVName.SPLAN_VEN_RESISTANCE) + 1. / theta.get(PVName.LBODY_VEN_RESISTANCE) + 1. / theta.get(PVName.ABDOM_VEN_RESISTANCE)) * b[13]
                + b[14] / theta.get(PVName.ABDOM_VEN_RESISTANCE);

        F[14] = (-1. / theta.get(PVName.ABDOM_VEN_RESISTANCE) - 1. / theta.get(PVName.IVC_RESISTANCE)) * b[14] + b[13] / theta.get(PVName.ABDOM_VEN_RESISTANCE)
                + b[15] / theta.get(PVName.IVC_RESISTANCE);

        F[15] = (-Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE) - 1. / theta.get(PVName.IVC_RESISTANCE) - 1. / theta.get(PVName.SVC_RESISTANCE)) * b[15]
                + b[4] / theta.get(PVName.SVC_RESISTANCE) + b[14] / theta.get(PVName.IVC_RESISTANCE) + Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE) * b[16];

        F[16] = -Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE) * (b[16] - b[15]) - Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE)
                * (b[17] - b[18]);

        F[17] = (theta.get(PVName.RV_SYSTOLIC_COMPLIANCE) + Tsys / theta.get(PVName.PUMONIC_VALVE_RESISTANCE)) * b[17] - theta.get(PVName.RV_SYSTOLIC_COMPLIANCE) * theta.get(PVName.INTRA_THORACIC_PRESSURE)
                - theta.get(PVName.RV_DIASTOLIC_COMPLIANCE) * b[16] - Tsys / theta.get(PVName.PUMONIC_VALVE_RESISTANCE) * b[18] + theta.get(PVName.INTRA_THORACIC_PRESSURE) * theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);

        F[18] = (1. / theta.get(PVName.PULM_MICRO_RESISTANCE) + Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE)) * b[18] - Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE)
                * b[17] - b[19] / theta.get(PVName.PULM_MICRO_RESISTANCE);

        F[19] = (-1. / theta.get(PVName.PULM_MICRO_RESISTANCE) - 1. / theta.get(PVName.PULM_VEN_RESISTANCE)) * b[19] + b[18] / theta.get(PVName.PULM_MICRO_RESISTANCE)
                + b[20] / theta.get(PVName.PULM_VEN_RESISTANCE);

        F[20] = (-1. / theta.get(PVName.PULM_VEN_RESISTANCE) - Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE)) * b[20] + b[19] / theta.get(PVName.PULM_VEN_RESISTANCE)
                + b[21] * Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);

        F[21] = -Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE) * (b[20] - b[21]) + Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE)
                * (b[22] - b[0]);

        F[22] = (theta.get(PVName.LV_SYSTOLIC_COMPLIANCE) + Tsys / theta.get(PVName.AORTIC_VALVE_RESISTANCE)) * b[22] - theta.get(PVName.LV_SYSTOLIC_COMPLIANCE) * theta.get(PVName.INTRA_THORACIC_PRESSURE)
                - Tsys / theta.get(PVName.AORTIC_VALVE_RESISTANCE) * b[0] - theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) * (b[21] - theta.get(PVName.INTRA_THORACIC_PRESSURE));

        // Linearized volume equation.
        a[0][0] = theta.get(PVName.ASCENDING_AORTA_COMPLIANCE);           // Aortic arch
        a[1][0] = theta.get(PVName.BRACH_ART_COMPLIANCE);           // Common Carotids
        a[2][0] = theta.get(PVName.UBODY_ART_COMPLIANCE);           // Cerebral Arteries
        a[3][0] = theta.get(PVName.UBODY_VEN_COMPLIANCE);           // Cerebral veins
        a[4][0] = theta.get(PVName.SVC_COMPLIANCE);           // Jugular veins
        a[5][0] = theta.get(PVName.THORACIC_AORTA_COMPLIANCE);           // Thoracic aorta
        a[6][0] = theta.get(PVName.ABDOM_AORTA_COMPLIANCE);           // Abdominal aorta
        a[7][0] = theta.get(PVName.RENAL_ART_COMPLIANCE);           // Renal arteries
        a[8][0] = theta.get(PVName.RENAL_VEN_COMPLIANCE);           // Renal veins
        a[9][0] = theta.get(PVName.SPLAN_ART_COMPLIANCE);           // Splanchnic arteries
        //  a[10][0] = theta.get(PVName.PV38);           // Splanchnic veins
        a[10][0] = Csp;           // Splanchnic veins
        a[11][0] = theta.get(PVName.LBODY_ART_COMPLIANCE);                     // Leg arteries
        a[12][0] = Cll;                     // Leg veins
        a[13][0] = Cab;                     // Abdominal veins
        a[14][0] = theta.get(PVName.IVC_COMPLIANCE);           // Vena cava inf. 
        a[15][0] = theta.get(PVName.RA_DIASTOLIC_COMPLIANCE);           // Right atrium
        a[16][0] = theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);           // Right ventricle diastole
        a[17][0] = 0.;                      // Right ventricle systole
        a[18][0] = theta.get(PVName.PULM_ART_COMPLIANCE);           // Pulmonary arteries
        a[19][0] = theta.get(PVName.PULM_VEN_COMPLIANCE);           // Pulmonary veins
        a[20][0] = theta.get(PVName.LA_DIASTOLIC_COMPLIANCE);           // Left atrium
        a[21][0] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);           // Left ventricle diastole
        a[22][0] = 0.;                      // Left ventricle systole

        // Left ventricular outflow = aortic arch outflow
        a[0][1] = 1. / theta.get(PVName.BRACH_ART_RESISTANCE) + 1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE) + Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE);
        a[1][1] = -1. / theta.get(PVName.BRACH_ART_RESISTANCE);
        a[2][1] = 0.;
        a[3][1] = 0.;
        a[4][1] = 0.;
        a[5][1] = -1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE);
        a[6][1] = 0.;
        a[7][1] = 0.;
        a[8][1] = 0.;
        a[9][1] = 0.;
        a[10][1] = 0.;
        a[11][1] = 0.;
        a[12][1] = 0.;
        a[13][1] = 0.;
        a[14][1] = 0.;
        a[15][1] = 0.;
        a[16][1] = 0.;
        a[17][1] = 0.;
        a[18][1] = 0.;
        a[19][1] = 0.;
        a[20][1] = 0.;
        a[21][1] = 0.;
        a[22][1] = -Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE);

        // Common carotid arterial flow = cerebral arterial flow 
        a[0][2] = -1. / theta.get(PVName.BRACH_ART_RESISTANCE);
        a[1][2] = 1. / theta.get(PVName.UBODY_ART_RESISTANCE) + 1. / theta.get(PVName.BRACH_ART_RESISTANCE);
        a[2][2] = -1. / theta.get(PVName.UBODY_ART_RESISTANCE);
        a[3][2] = 0.;
        a[4][2] = 0.;
        a[5][2] = 0.;
        a[6][2] = 0.;
        a[7][2] = 0.;
        a[8][2] = 0.;
        a[9][2] = 0.;
        a[10][2] = 0.;
        a[11][2] = 0.;
        a[12][2] = 0.;
        a[13][2] = 0.;
        a[14][2] = 0.;
        a[15][2] = 0.;
        a[16][2] = 0.;
        a[17][2] = 0.;
        a[18][2] = 0.;
        a[19][2] = 0.;
        a[20][2] = 0.;
        a[21][2] = 0.;
        a[22][2] = 0.;

        // Upper body microflow.
        a[0][3] = 0.;
        a[1][3] = -1. / theta.get(PVName.UBODY_ART_RESISTANCE);
        a[2][3] = 1. / theta.get(PVName.UBODY_ART_RESISTANCE) + 1. / theta.get(PVName.UBODY_MICRO_RESISTANCE);
        a[3][3] = -1. / theta.get(PVName.UBODY_MICRO_RESISTANCE);
        a[4][3] = 0.;
        a[5][3] = 0.;
        a[6][3] = 0.;
        a[7][3] = 0.;
        a[8][3] = 0.;
        a[9][3] = 0.;
        a[10][3] = 0.;
        a[11][3] = 0.;
        a[12][3] = 0.;
        a[13][3] = 0.;
        a[14][3] = 0.;
        a[15][3] = 0.;
        a[16][3] = 0.;
        a[17][3] = 0.;
        a[18][3] = 0.;
        a[19][3] = 0.;
        a[20][3] = 0.;
        a[21][3] = 0.;
        a[22][3] = 0.;

        // Upper body veins to superior vena cava
        a[0][4] = 0.;
        a[1][4] = 0.;
        a[2][4] = -1. / theta.get(PVName.UBODY_MICRO_RESISTANCE);
        a[3][4] = 1. / theta.get(PVName.UBODY_MICRO_RESISTANCE) + 1. / theta.get(PVName.UBODY_VEN_RESISTANCE);
        a[4][4] = -1. / theta.get(PVName.UBODY_VEN_RESISTANCE);
        a[5][4] = 0.;
        a[6][4] = 0.;
        a[7][4] = 0.;
        a[8][4] = 0.;
        a[9][4] = 0.;
        a[10][4] = 0.;
        a[11][4] = 0.;
        a[12][4] = 0.;
        a[13][4] = 0.;
        a[14][4] = 0.;
        a[15][4] = 0.;
        a[16][4] = 0.;
        a[17][4] = 0.;
        a[18][4] = 0.;
        a[19][4] = 0.;
        a[20][4] = 0.;
        a[21][4] = 0.;
        a[22][4] = 0.;

        // Thoracic aorta to abdominal aorta
        a[0][5] = -1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE);
        a[1][5] = 0.;
        a[2][5] = 0.;
        a[3][5] = 0.;
        a[4][5] = 0.;
        a[5][5] = 1. / theta.get(PVName.THORACIC_AORTA_RESISTANCE) + 1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE);
        a[6][5] = -1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE);
        a[7][5] = 0.;
        a[8][5] = 0.;
        a[9][5] = 0.;
        a[10][5] = 0.;
        a[11][5] = 0.;
        a[12][5] = 0.;
        a[13][5] = 0.;
        a[14][5] = 0.;
        a[15][5] = 0.;
        a[16][5] = 0.;
        a[17][5] = 0.;
        a[18][5] = 0.;
        a[19][5] = 0.;
        a[20][5] = 0.;
        a[21][5] = 0.;
        a[22][5] = 0.;

        // Abdominal aorta to sytemics arteries
        a[0][6] = 0.;
        a[1][6] = 0.;
        a[2][6] = 0.;
        a[3][6] = 0.;
        a[4][6] = 0.;
        a[5][6] = -1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE);
        a[6][6] = 1. / theta.get(PVName.ABDOM_AORTA_RESISTANCE) + 1. / theta.get(PVName.RENAL_ART_RESISTANCE) + 1. / theta.get(PVName.SPLAN_ART_RESISTANCE) + 1. / theta.get(PVName.LBODY_ART_RESISTANCE);
        a[7][6] = -1. / theta.get(PVName.RENAL_ART_RESISTANCE);
        a[8][6] = 0.;
        a[9][6] = -1. / theta.get(PVName.SPLAN_ART_RESISTANCE);
        a[10][6] = 0.;
        a[11][6] = -1. / theta.get(PVName.LBODY_ART_RESISTANCE);
        a[12][6] = 0.;
        a[13][6] = 0.;
        a[14][6] = 0.;
        a[15][6] = 0.;
        a[16][6] = 0.;
        a[17][6] = 0.;
        a[18][6] = 0.;
        a[19][6] = 0.;
        a[20][6] = 0.;
        a[21][6] = 0.;
        a[22][6] = 0.;

        // Abdominal aorta to renal vein
        a[0][7] = 0.;
        a[1][7] = 0.;
        a[2][7] = 0.;
        a[3][7] = 0.;
        a[4][7] = 0.;
        a[5][7] = 0.;
        a[6][7] = -1. / theta.get(PVName.RENAL_ART_RESISTANCE);
        a[7][7] = 1. / theta.get(PVName.RENAL_ART_RESISTANCE) + 1. / theta.get(PVName.RENAL_MICRO_RESISTANCE);
        a[8][7] = -1. / theta.get(PVName.RENAL_MICRO_RESISTANCE);
        a[9][7] = 0.;
        a[10][7] = 0.;
        a[11][7] = 0.;
        a[12][7] = 0.;
        a[13][7] = 0.;
        a[14][7] = 0.;
        a[15][7] = 0.;
        a[16][7] = 0.;
        a[17][7] = 0.;
        a[18][7] = 0.;
        a[19][7] = 0.;
        a[20][7] = 0.;
        a[21][7] = 0.;
        a[22][7] = 0.;

        // Renal artery to abdominal veins
        a[0][8] = 0.;
        a[1][8] = 0.;
        a[2][8] = 0.;
        a[3][8] = 0.;
        a[4][8] = 0.;
        a[5][8] = 0.;
        a[6][8] = 0.;
        a[7][8] = -1. / theta.get(PVName.RENAL_MICRO_RESISTANCE);
        a[8][8] = 1. / theta.get(PVName.RENAL_MICRO_RESISTANCE) + 1. / theta.get(PVName.RENAL_VEN_RESISTANCE);
        a[9][8] = 0.;
        a[10][8] = 0.;
        a[11][8] = 0.;
        a[12][8] = 0.;
        a[13][8] = -1. / theta.get(PVName.RENAL_VEN_RESISTANCE);
        a[14][8] = 0.;
        a[15][8] = 0.;
        a[16][8] = 0.;
        a[17][8] = 0.;
        a[18][8] = 0.;
        a[19][8] = 0.;
        a[20][8] = 0.;
        a[21][8] = 0.;
        a[22][8] = 0.;

        // Abdominal aorta to splanchnic veins
        a[0][9] = 0.;
        a[1][9] = 0.;
        a[2][9] = 0.;
        a[3][9] = 0.;
        a[4][9] = 0.;
        a[5][9] = 0.;
        a[6][9] = -1. / theta.get(PVName.SPLAN_ART_RESISTANCE);
        a[7][9] = 0.;
        a[8][9] = 0.;
        a[9][9] = 1. / theta.get(PVName.SPLAN_ART_RESISTANCE) + 1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE);
        a[10][9] = -1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE);
        a[11][9] = 0.;
        a[12][9] = 0.;
        a[13][9] = 0.;
        a[14][9] = 0.;
        a[15][9] = 0.;
        a[16][9] = 0.;
        a[17][9] = 0.;
        a[18][9] = 0.;
        a[19][9] = 0.;
        a[20][9] = 0.;
        a[21][9] = 0.;
        a[22][9] = 0.;

        // Flow through splanchnic compartment.
        a[0][10] = 0.;
        a[1][10] = 0.;
        a[2][10] = 0.;
        a[3][10] = 0.;
        a[4][10] = 0.;
        a[5][10] = 0.;
        a[6][10] = 0.;
        a[7][10] = 0.;
        a[8][10] = 0.;
        a[9][10] = -1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE);
        a[10][10] = 1. / theta.get(PVName.SPLAN_MICRO_RESISTANCE) + 1. / theta.get(PVName.SPLAN_VEN_RESISTANCE);
        a[11][10] = 0.;
        a[12][10] = 0.;
        a[13][10] = -1. / theta.get(PVName.SPLAN_VEN_RESISTANCE);
        a[14][10] = 0.;
        a[15][10] = 0.;
        a[16][10] = 0.;
        a[17][10] = 0.;
        a[18][10] = 0.;
        a[19][10] = 0.;
        a[20][10] = 0.;
        a[21][10] = 0.;
        a[22][10] = 0.;

        // Abdominal aorta to leg arteries
        a[0][11] = 0.;
        a[1][11] = 0.;
        a[2][11] = 0.;
        a[3][11] = 0.;
        a[4][11] = 0.;
        a[5][11] = 0.;
        a[6][11] = -1. / theta.get(PVName.LBODY_ART_RESISTANCE);
        a[7][11] = 0.;
        a[8][11] = 0.;
        a[9][11] = 0.;
        a[10][11] = 0.;
        a[11][11] = 1. / theta.get(PVName.LBODY_ART_RESISTANCE) + 1. / theta.get(PVName.LBODY_MICRO_RESISTANCE);
        a[12][11] = -1. / theta.get(PVName.LBODY_MICRO_RESISTANCE);
        a[13][11] = 0.;
        a[14][11] = 0.;
        a[15][11] = 0.;
        a[16][11] = 0.;
        a[17][11] = 0.;
        a[18][11] = 0.;
        a[19][11] = 0.;
        a[20][11] = 0.;
        a[21][11] = 0.;
        a[22][11] = 0.;

        // Leg arteries to abdominal veins
        a[0][12] = 0.;
        a[1][12] = 0.;
        a[2][12] = 0.;
        a[3][12] = 0.;
        a[4][12] = 0.;
        a[5][12] = 0.;
        a[6][12] = 0.;
        a[7][12] = 0.;
        a[8][12] = 0.;
        a[9][12] = 0.;
        a[10][12] = 0.;
        a[11][12] = -1. / theta.get(PVName.LBODY_MICRO_RESISTANCE);
        a[12][12] = 1. / theta.get(PVName.LBODY_MICRO_RESISTANCE) + 1. / theta.get(PVName.LBODY_VEN_RESISTANCE);
        a[13][12] = -1. / theta.get(PVName.LBODY_VEN_RESISTANCE);
        a[14][12] = 0.;
        a[15][12] = 0.;
        a[16][12] = 0.;
        a[17][12] = 0.;
        a[18][12] = 0.;
        a[19][12] = 0.;
        a[20][12] = 0.;
        a[21][12] = 0.;
        a[22][12] = 0.;

        // Parallel venous outflow to abdominal veins
        a[0][13] = 0.;
        a[1][13] = 0.;
        a[2][13] = 0.;
        a[3][13] = 0.;
        a[4][13] = 0.;
        a[5][13] = 0.;
        a[6][13] = 0.;
        a[7][13] = 0.;
        a[8][13] = -1. / theta.get(PVName.RENAL_VEN_RESISTANCE);
        a[9][13] = 0.;
        a[10][13] = -1. / theta.get(PVName.SPLAN_VEN_RESISTANCE);
        a[11][13] = 0.;
        a[12][13] = -1. / theta.get(PVName.LBODY_VEN_RESISTANCE);
        a[13][13] = 1. / theta.get(PVName.RENAL_VEN_RESISTANCE) + 1. / theta.get(PVName.SPLAN_VEN_RESISTANCE) + 1. / theta.get(PVName.LBODY_VEN_RESISTANCE) + 1. / theta.get(PVName.ABDOM_VEN_RESISTANCE);
        a[14][13] = -1. / theta.get(PVName.ABDOM_VEN_RESISTANCE);
        a[15][13] = 0.;
        a[16][13] = 0.;
        a[17][13] = 0.;
        a[18][13] = 0.;
        a[19][13] = 0.;
        a[20][13] = 0.;
        a[21][13] = 0.;
        a[22][13] = 0.;

        // Abdominal veins to right atrium
        a[0][14] = 0.;
        a[1][14] = 0.;
        a[2][14] = 0.;
        a[3][14] = 0.;
        a[4][14] = 0.;
        a[5][14] = 0.;
        a[6][14] = 0.;
        a[7][14] = 0.;
        a[8][14] = 0.;
        a[9][14] = 0.;
        a[10][14] = 0.;
        a[11][14] = 0.;
        a[12][14] = 0.;
        a[13][14] = -1. / theta.get(PVName.ABDOM_VEN_RESISTANCE);
        a[14][14] = 1. / theta.get(PVName.ABDOM_VEN_RESISTANCE) + 1. / theta.get(PVName.IVC_RESISTANCE);
        a[15][14] = -1. / theta.get(PVName.IVC_RESISTANCE);
        a[16][14] = 0.;
        a[17][14] = 0.;
        a[18][14] = 0.;
        a[19][14] = 0.;
        a[20][14] = 0.;
        a[21][14] = 0.;
        a[22][14] = 0.;

        // Right atrial inflow to right ventricle
        a[0][15] = 0.;
        a[1][15] = 0.;
        a[2][15] = 0.;
        a[3][15] = 0.;
        a[4][15] = -1. / theta.get(PVName.SVC_RESISTANCE);
        a[5][15] = 0.;
        a[6][15] = 0.;
        a[7][15] = 0.;
        a[8][15] = 0.;
        a[9][15] = 0.;
        a[10][15] = 0.;
        a[11][15] = 0.;
        a[12][15] = 0.;
        a[13][15] = 0.;
        a[14][15] = -1. / theta.get(PVName.IVC_RESISTANCE);
        a[15][15] = Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE) + 1. / theta.get(PVName.IVC_RESISTANCE) + 1. / theta.get(PVName.SVC_RESISTANCE);
        a[16][15] = -Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE);
        a[17][15] = 0.;
        a[18][15] = 0.;
        a[19][15] = 0.;
        a[20][15] = 0.;
        a[21][15] = 0.;
        a[22][15] = 0.;

        // Tricuspid flow to pulmonary artery
        a[0][16] = 0.;
        a[1][16] = 0.;
        a[2][16] = 0.;
        a[3][16] = 0.;
        a[4][16] = 0.;
        a[5][16] = 0.;
        a[6][16] = 0.;
        a[7][16] = 0.;
        a[8][16] = 0.;
        a[9][16] = 0.;
        a[10][16] = 0.;
        a[11][16] = 0.;
        a[12][16] = 0.;
        a[13][16] = 0.;
        a[14][16] = 0.;
        a[15][16] = -Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE);
        a[16][16] = Tdias / T / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE);
        a[17][16] = Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[18][16] = -Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[19][16] = 0.;
        a[20][16] = 0.;
        a[21][16] = 0.;
        a[22][16] = 0.;

        // Left atrial inflow.
        a[0][17] = 0.;
        a[1][17] = 0.;
        a[2][17] = 0.;
        a[3][17] = 0.;
        a[4][17] = 0.;
        a[5][17] = 0.;
        a[6][17] = 0.;
        a[7][17] = 0.;
        a[8][17] = 0.;
        a[9][17] = 0.;
        a[10][17] = 0.;
        a[11][17] = 0.;
        a[12][17] = 0.;
        a[13][17] = 0.;
        a[14][17] = 0.;
        a[15][17] = 0.;
        a[16][17] = theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);
        a[17][17] = -theta.get(PVName.RV_SYSTOLIC_COMPLIANCE) - Tsys / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[18][17] = Tsys / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[19][17] = 0.;
        a[20][17] = 0.;
        a[21][17] = 0.;
        a[22][17] = 0.;

        // Left atrial inflow.
        a[0][18] = 0.;
        a[1][18] = 0.;
        a[2][18] = 0.;
        a[3][18] = 0.;
        a[4][18] = 0.;
        a[5][18] = 0.;
        a[6][18] = 0.;
        a[7][18] = 0.;
        a[8][18] = 0.;
        a[9][18] = 0.;
        a[10][18] = 0.;
        a[11][18] = 0.;
        a[12][18] = 0.;
        a[13][18] = 0.;
        a[14][18] = 0.;
        a[15][18] = 0.;
        a[16][18] = 0.;
        a[17][18] = Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        a[18][18] = -Tsys / T / theta.get(PVName.PUMONIC_VALVE_RESISTANCE) - 1. / theta.get(PVName.PULM_MICRO_RESISTANCE);
        a[19][18] = 1. / theta.get(PVName.PULM_MICRO_RESISTANCE);
        a[20][18] = 0.;
        a[21][18] = 0.;
        a[22][18] = 0.;

        // Left atrial inflow.
        a[0][19] = 0.;
        a[1][19] = 0.;
        a[2][19] = 0.;
        a[3][19] = 0.;
        a[4][19] = 0.;
        a[5][19] = 0.;
        a[6][19] = 0.;
        a[7][19] = 0.;
        a[8][19] = 0.;
        a[9][19] = 0.;
        a[10][19] = 0.;
        a[11][19] = 0.;
        a[12][19] = 0.;
        a[13][19] = 0.;
        a[14][19] = 0.;
        a[15][19] = 0.;
        a[16][19] = 0.;
        a[17][19] = 0.;
        a[18][19] = -1. / theta.get(PVName.PULM_MICRO_RESISTANCE);
        a[19][19] = 1. / theta.get(PVName.PULM_MICRO_RESISTANCE) + 1. / theta.get(PVName.PULM_VEN_RESISTANCE);
        a[20][19] = -1. / theta.get(PVName.PULM_VEN_RESISTANCE);
        a[21][19] = 0.;
        a[22][19] = 0.;

        // Left atrial inflow.
        a[0][20] = 0.;
        a[1][20] = 0.;
        a[2][20] = 0.;
        a[3][20] = 0.;
        a[4][20] = 0.;
        a[5][20] = 0.;
        a[6][20] = 0.;
        a[7][20] = 0.;
        a[8][20] = 0.;
        a[9][20] = 0.;
        a[10][20] = 0.;
        a[11][20] = 0.;
        a[12][20] = 0.;
        a[13][20] = 0.;
        a[14][20] = 0.;
        a[15][20] = 0.;
        a[16][20] = 0.;
        a[17][20] = 0.;
        a[18][20] = 0.;
        a[19][20] = -1. / theta.get(PVName.PULM_VEN_RESISTANCE);
        a[20][20] = 1. / theta.get(PVName.PULM_VEN_RESISTANCE) + Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        a[21][20] = -Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        a[22][20] = 0.;

        // Left atrial inflow.
        a[0][21] = Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE);
        a[1][21] = 0.;
        a[2][21] = 0.;
        a[3][21] = 0.;
        a[4][21] = 0.;
        a[5][21] = 0.;
        a[6][21] = 0.;
        a[7][21] = 0.;
        a[8][21] = 0.;
        a[9][21] = 0.;
        a[10][21] = 0.;
        a[11][21] = 0.;
        a[12][21] = 0.;
        a[13][21] = 0.;
        a[14][21] = 0.;
        a[15][21] = 0.;
        a[16][21] = 0.;
        a[17][21] = 0.;
        a[18][21] = 0.;
        a[19][21] = 0.;
        a[20][21] = Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        a[21][21] = -Tdias / T / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        a[22][21] = -Tsys / T / theta.get(PVName.AORTIC_VALVE_RESISTANCE);

        // Left atrial inflow.
        a[0][22] = Tsys / theta.get(PVName.AORTIC_VALVE_RESISTANCE);
        a[1][22] = 0.;
        a[2][22] = 0.;
        a[3][22] = 0.;
        a[4][22] = 0.;
        a[5][22] = 0.;
        a[6][22] = 0.;
        a[7][22] = 0.;
        a[8][22] = 0.;
        a[9][22] = 0.;
        a[10][22] = 0.;
        a[11][22] = 0.;
        a[12][22] = 0.;
        a[13][22] = 0.;
        a[14][22] = 0.;
        a[15][22] = 0.;
        a[16][22] = 0.;
        a[17][22] = 0.;
        a[18][22] = 0.;
        a[19][22] = 0.;
        a[20][22] = 0.;
        a[21][22] = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);
        a[22][22] = -theta.get(PVName.LV_SYSTOLIC_COMPLIANCE) - Tsys / theta.get(PVName.AORTIC_VALVE_RESISTANCE);

        // Previously
        // lineqs(a, F, ARRAY_SIZE);     
        // Changed to be compatible w/ GCC versions 4.0 and higher
        // Modified by C. Dunn
        lineqs(a, F, ARRAY_SIZE);

        b[0] += F[0];
        b[1] += F[1];
        b[2] += F[2];
        b[3] += F[3];
        b[4] += F[4];
        b[5] += F[5];
        b[6] += F[6];
        b[7] += F[7];
        b[8] += F[8];
        b[9] += F[9];
        b[10] += F[10];
        b[11] += F[11];
        b[12] += F[12];
        b[13] += F[13];
        b[14] += F[14];
        b[15] += F[15];
        b[16] += F[16];
        b[17] += F[17];
        b[18] += F[18];
        b[19] += F[19];
        b[20] += F[20];
        b[21] += F[21];
        b[22] += F[22];

        // Update the non-linear pressure-volume elements and return the updated
        // volume error to the calling environment.
        con = PI * theta.get(PVName.SPLAN_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL);
        Csp = theta.get(PVName.SPLAN_VEN_COMPLIANCE) / (1.0 + con * con * b[10] * b[10]);
        Vsp = 2.0 * theta.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL) * atan(con * b[10]) / PI;

        con = PI * theta.get(PVName.LBODY_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL);
        Cll = theta.get(PVName.LBODY_VEN_COMPLIANCE) / (1.0 + con * con * b[12] * b[12]);
        Vll = 2.0 * theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL) * atan(con * b[12]) / PI;

        con = PI * theta.get(PVName.ABDOM_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_ABDOM_DISTENDING_VOL);
        Cab = theta.get(PVName.ABDOM_VEN_COMPLIANCE) / (1.0 + con * con * b[13] * b[13]);
        Vab = 2.0 * theta.get(PVName.MAX_INCREASE_IN_ABDOM_DISTENDING_VOL) * atan(con * b[13]) / PI;

        vol_error = theta.get(PVName.TOTAL_BLOOD_VOLUME)
                - (theta.get(PVName.ASCENDING_AORTA_VOLUME) + theta.get(PVName.BRACH_ART_ZPFV) + theta.get(PVName.THORACIC_AORTA_ZPFV) + theta.get(PVName.UBODY_ART_ZPFV)
                + theta.get(PVName.ABDOM_AORTA_ZPFV) + theta.get(PVName.RENAL_ART_ZPFV) + theta.get(PVName.SPLAN_ART_ZPFV) + theta.get(PVName.LBODY_ART_ZPFV)
                + theta.get(PVName.UBODY_VEN_ZPFV) + theta.get(PVName.RENAL_VEN_ZPFV) + theta.get(PVName.SPLAN_VEN_ZPFV) + theta.get(PVName.LBODY_VEN_ZPFV)
                + theta.get(PVName.ABDOM_VEN_ZPFV) + theta.get(PVName.IVC_ZPFV) + theta.get(PVName.SVC_ZPFV) + theta.get(PVName.RA_ZPFV)
                + theta.get(PVName.RV_ZPFV) + theta.get(PVName.PULM_ART_ZPFV) + theta.get(PVName.PULN_VEN_ZPFV) + theta.get(PVName.LA_ZPFV)
                + theta.get(PVName.LV_ZPFV))
                + (theta.get(PVName.LA_DIASTOLIC_COMPLIANCE) + theta.get(PVName.RA_DIASTOLIC_COMPLIANCE) + theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) + theta.get(PVName.RV_DIASTOLIC_COMPLIANCE) + theta.get(PVName.PULM_ART_COMPLIANCE)
                + theta.get(PVName.PULM_VEN_COMPLIANCE) + theta.get(PVName.IVC_COMPLIANCE) + theta.get(PVName.SVC_COMPLIANCE) + theta.get(PVName.ASCENDING_AORTA_COMPLIANCE) + theta.get(PVName.BRACH_ART_COMPLIANCE) + theta.get(PVName.THORACIC_AORTA_COMPLIANCE)) * theta.get(PVName.INTRA_THORACIC_PRESSURE)
                - (theta.get(PVName.UBODY_VEN_COMPLIANCE) * b[3] + theta.get(PVName.RENAL_VEN_COMPLIANCE) * b[8] + Vsp + Vll + Vab + theta.get(PVName.IVC_COMPLIANCE) * b[14]
                + theta.get(PVName.SVC_COMPLIANCE) * b[4] + theta.get(PVName.RA_DIASTOLIC_COMPLIANCE) * b[15] + theta.get(PVName.RV_DIASTOLIC_COMPLIANCE) * b[16]
                + theta.get(PVName.PULM_ART_COMPLIANCE) * b[18] + theta.get(PVName.PULM_VEN_COMPLIANCE) * b[19] + theta.get(PVName.LA_DIASTOLIC_COMPLIANCE) * b[20]
                + theta.get(PVName.LV_DIASTOLIC_COMPLIANCE) * b[21] + theta.get(PVName.ASCENDING_AORTA_COMPLIANCE) * b[0] + theta.get(PVName.BRACH_ART_COMPLIANCE) * b[1]
                + theta.get(PVName.UBODY_ART_COMPLIANCE) * b[2] + theta.get(PVName.THORACIC_AORTA_COMPLIANCE) * b[5] + theta.get(PVName.ABDOM_AORTA_COMPLIANCE) * b[6]
                + theta.get(PVName.RENAL_ART_COMPLIANCE) * b[7] + theta.get(PVName.SPLAN_ART_COMPLIANCE) * b[9] + theta.get(PVName.LBODY_ART_COMPLIANCE) * b[11]);

        return vol_error;
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
