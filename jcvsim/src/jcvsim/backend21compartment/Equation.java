package jcvsim.backend21compartment;

import static java.lang.Math.PI;
import static jcvsim.backendCommon.Maths.atan;
import static jcvsim.backendCommon.Maths.cos;
import static jcvsim.backendCommon.Maths.exp;
import static jcvsim.backendCommon.Maths.sin;
import static jcvsim.backend21compartment.Data_vector.CompartmentIndexes.*;
import static jcvsim.backend21compartment.Data_vector.ComplianceIndexes.*;
import static jcvsim.backend21compartment.Data_vector.TimeIndexes.*;


/*
 * This file contains the subroutines necessary to evaluate the time-varying
 * capacitance values, their derivatives, the flows in the hemodynamic
 * system, and the pressure derivatives. Furthermore, it contains the volume
 * corrention routine.
 *
 * ALTHOUGH ELASTANCE() AND EQNS() ONLY REQUIRE A SMALL SUBSET OF ALL
 * PARAMETERS, CURRENTLY THE ENTIRE PARAMETER VECTOR IS PASSED TO THESE
 * ROUTINES AS ARGUMENTS. THIS CERTAINLY COMPROMISES PERFORMANCE AND
 * SHOULD BE RECONSIDERED IN FUTURE ITERATIONS OF THE PROGRAM.
 *
 *
 * Thomas Heldt March 30th, 2002
 * Last modified November 2, 2002
 */
// Converted to Java Jason Leake Decembr 2016
class Equation {

// Modifies ONLY Data_vector
    // Note by JLL :-
    // This method handles the pumping action of the heart.  In the model pumping
    // is implemented by dynamically changing the compliances of the four chambers
    // of the heart. It uses a set of different functions which are selected
    // according to the time into the cardiac cycle.  It actually works in terms
    // of elastances, and these are converted to compliances at the end of the
    // method.
    //
    // For the atria the elastances the functions are selected as follows:
    //
    // time < atrial systole time -- elastances appropriate for atrial contraction
    //
    // time < atrial systole time * 1.5 -- early atrial relaxation
    //
    // time > atrial systole time * 1.5 -- atrial relaxation
    //
    // And simultaneously for the ventricles the functions are selected as follows:
    //
    // time < ventricle contraction time -- fixed elastances
    //
    // time > venticle contraction time but less than ventricle systole time
    //
    // time > ventricle systole time until 1.5 x ventricle systole time
    //
    // time > 1.5 * ventricle systole time
    public static void elastance_ptr(Data_vector p, Parameter_vector theta) {

        // These are the elastances, and their rates of change, for the four
        // compartments. They are the outputs from the method (and copied into
        // the appropriate Data_vector elements when computed)
        double elastanceLA = 0.0; // LA elastanace
        double elastanceRA = 0.0;
        double elastanceLV = 0.0;
        double elastanceRV = 0.0;
        double dElastanceLA = 0.0; // Rate of change of LA elastance
        double dElastanceRA = 0.0;
        double dElastanceLV = 0.0;
        double dElastanceRV = 0.0;

        // These are the inputs used in the algorithm
        final double a_time = p.time[CARDIAC_TIME];
        //double PR_delay = p.time[2];  // unused
        final double timeAtrialSystole = p.time[ATRIAL_SYSTOLE_TIME];
        final double timeVentricleSystole = p.time[VENTRICULAR_SYSTOLE_TIME];

        final double complianceRAdiastole = theta.get(PVName.RA_DIASTOLIC_COMPLIANCE);
        final double complianceRAsystole = theta.get(PVName.RA_SYSTOLIC_COMPLIANCE);
        final double complianceRVdiastole = theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);
        final double sigCr = p.compliance[RV_END_SYSTOLIC_COMPL];

        final double complianceLAdiastole = theta.get(PVName.LA_DIASTOLIC_COMPLIANCE);
        final double complianceLAsystole = theta.get(PVName.LA_SYSTOLIC_COMPLIANCE);
        final double complianceLVdiastole = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);
        final double sigCl = p.compliance[LV_END_SYSTOLIC_COMPL];

        final double v_time = p.time[VENTRICULAR_TIME];

        // Atrial contraction.
        if (a_time <= timeAtrialSystole) {
            elastanceLA = 0.5 * (1 / complianceLAsystole - 1 / complianceLAdiastole) * (1 - cos(PI * a_time / timeAtrialSystole)) + 1 / complianceLAdiastole;
            elastanceRA = 0.5 * (1 / complianceRAsystole - 1 / complianceRAdiastole) * (1 - cos(PI * a_time / timeAtrialSystole)) + 1 / complianceRAdiastole;
            dElastanceLA = 0.5 * PI * (1 / complianceLAsystole - 1 / complianceLAdiastole) * sin(PI * a_time / timeAtrialSystole) / timeAtrialSystole;
            dElastanceRA = 0.5 * PI * (1 / complianceRAsystole - 1 / complianceRAdiastole) * sin(PI * a_time / timeAtrialSystole) / timeAtrialSystole;
        } // Early atrial relaxation.
        else if ((timeAtrialSystole < a_time) && (a_time <= 1.5 * timeAtrialSystole)) {
            elastanceLA = 0.5 * (1 / complianceLAsystole - 1 / complianceLAdiastole) * (1 + cos(2.0 * PI * (a_time - timeAtrialSystole) / timeAtrialSystole))
                    + 1 / complianceLAdiastole;
            elastanceRA = 0.5 * (1 / complianceRAsystole - 1 / complianceRAdiastole) * (1 + cos(2.0 * PI * (a_time - timeAtrialSystole) / timeAtrialSystole))
                    + 1 / complianceRAdiastole;
            dElastanceLA = -1.0 * PI * (1 / complianceLAsystole - 1 / complianceLAdiastole) * sin(2.0 * PI * (a_time - timeAtrialSystole) / timeAtrialSystole) / timeAtrialSystole;
            dElastanceRA = -1.0 * PI * (1 / complianceRAsystole - 1 / complianceRAdiastole) * sin(2.0 * PI * (a_time - timeAtrialSystole) / timeAtrialSystole) / timeAtrialSystole;
        } // Atrial diastole.
        else if (1.5 * timeAtrialSystole < a_time) {
            elastanceLA = 1 / complianceLAdiastole;
            elastanceRA = 1 / complianceRAdiastole;
            dElastanceLA = 0.0;
            dElastanceRA = 0.0;
        }

        // Ventricular contraction. PR-interval has not yet passed.
        if (v_time <= 0.0) {
            elastanceLV = 1 / complianceLVdiastole;
            elastanceRV = 1 / complianceRVdiastole;
            dElastanceLV = 0.0;
            dElastanceRV = 0.0;
        } // Ventricular contraction.
        else if ((0 < v_time) && (v_time <= timeVentricleSystole)) {
            elastanceLV = 0.5 * (1 / sigCl - 1 / complianceLVdiastole) * (1 - cos(PI * v_time / timeVentricleSystole)) + 1 / complianceLVdiastole;
            elastanceRV = 0.5 * (1 / sigCr - 1 / complianceRVdiastole) * (1 - cos(PI * v_time / timeVentricleSystole)) + 1 / complianceRVdiastole;
            dElastanceLV = 0.5 * PI * (1 / sigCl - 1 / complianceLVdiastole) * sin(PI * v_time / timeVentricleSystole) / timeVentricleSystole;
            dElastanceRV = 0.5 * PI * (1 / sigCr - 1 / complianceRVdiastole) * sin(PI * v_time / timeVentricleSystole) / timeVentricleSystole;
        } // Early ventricular relaxation.
        else if ((timeVentricleSystole < v_time) && (v_time <= 1.5 * timeVentricleSystole)) {
            elastanceLV = 0.5 * (1 / sigCl - 1 / complianceLVdiastole) * (1 + cos(2.0 * PI * (v_time - timeVentricleSystole) / timeVentricleSystole))
                    + 1 / complianceLVdiastole;
            elastanceRV = 0.5 * (1 / sigCr - 1 / complianceRVdiastole) * (1 + cos(2.0 * PI * (v_time - timeVentricleSystole) / timeVentricleSystole))
                    + 1 / complianceRVdiastole;
            dElastanceLV = -1.0 * PI * (1 / sigCl - 1 / complianceLVdiastole) * sin(2.0 * PI * (v_time - timeVentricleSystole) / timeVentricleSystole) / timeVentricleSystole;
            dElastanceRV = -1.0 * PI * (1 / sigCr - 1 / complianceRVdiastole) * sin(2.0 * PI * (v_time - timeVentricleSystole) / timeVentricleSystole) / timeVentricleSystole;
        } // Ventricular diastole.
        else if (v_time > 1.5 * timeVentricleSystole) {
            elastanceLV = 1 / complianceLVdiastole;
            elastanceRV = 1 / complianceRVdiastole;
            dElastanceLV = 0.0;
            dElastanceRV = 0.0;
        }

        // Compliance is the reciprocal of the elastance
        p.compliance[RA_COMPL] = 1 / elastanceRA;
        p.compliance[RV_COMPL] = 1 / elastanceRV;
        p.compliance[LA_COMPL] = 1 / elastanceLA;
        p.compliance[LV_COMPL] = 1 / elastanceLV;

        p.dComplianceDt[RA_COMPL] = -1.0 / (elastanceRA * elastanceRA) * dElastanceRA;
        p.dComplianceDt[RV_COMPL] = -1.0 / (elastanceRV * elastanceRV) * dElastanceRV;
        p.dComplianceDt[LA_COMPL] = -1.0 / (elastanceLA * elastanceLA) * dElastanceLA;
        p.dComplianceDt[LV_COMPL] = -1.0 / (elastanceLV * elastanceLV) * dElastanceLV;

    }


    /*
     * The following routine computes the flows and and the pressure
     * derivatives.
     * Currently implemented features include the non-linear pressure-volume
     * relation for the splanchnic, the legs, and the abdominal venous
     * compartments. This routine calls the orthostatic stress routines [lbnp()
     * or
     * tilt()] which update the leakage flows and blood volume reductions.
     */
    public static void eqns_ptr(Data_vector p, Parameter_vector theta, Reflex_vector r,
            boolean tiltTestOn, double tiltStartTime, double tiltStopTime) {
        double Csp = 0.0, Cll = 0.0, Cab = 0.0;   // non-linear compliances
        double Vll = 0.0, Vsp = 0.0, Vab = 0.0;
        double con = 0.0;                         // temporary variable
        Tilt_vector t = new Tilt_vector();

        // call tilt function
        if (tiltTestOn) {
            tilt(p, theta, t, tiltStartTime, tiltStopTime);
        }

        // The following lines compute the non-linear venous compliance values and 
        // the corresponding vascular volumes.
        con = PI * theta.get(PVName.SPLAN_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL);
        Csp = theta.get(PVName.SPLAN_VEN_COMPLIANCE) / (1.0 + con * con * (p.pressure[SPLANCHNIC_VENOUS_CPI] - p.pressure[BIAS_1_PI]) * (p.pressure[SPLANCHNIC_VENOUS_CPI] - p.pressure[BIAS_1_PI]));
        Vsp = 2.0 * theta.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL) * atan(con * (p.pressure[SPLANCHNIC_VENOUS_CPI] - p.pressure[BIAS_1_PI])) / PI;

        if ((p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]) > 0.0) {
            con = PI * theta.get(PVName.LBODY_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL);
            Cll = theta.get(PVName.LBODY_VEN_COMPLIANCE) / (1.0 + con * con * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]) * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]));
            Vll = 2.0 * theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL) * atan(con * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI])) / PI;
        } else {
            con = PI * theta.get(PVName.LBODY_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL);
            Cll = theta.get(PVName.LBODY_VEN_COMPLIANCE) / (1.0 + con * con * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]) * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]));
        }

        con = PI * theta.get(PVName.ABDOM_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_ABDOM_DISTENDING_VOL);
        Cab = theta.get(PVName.ABDOM_VEN_COMPLIANCE) / (1.0 + con * con * (p.pressure[ABDOMINAL_VENOUS_CPI] - p.pressure[BIAS_1_PI]) * (p.pressure[ABDOMINAL_VENOUS_CPI] - p.pressure[BIAS_1_PI]));
        Vab = 2.0 * theta.get(PVName.MAX_INCREASE_IN_ABDOM_DISTENDING_VOL) * atan(con * (p.pressure[ABDOMINAL_VENOUS_CPI] - p.pressure[BIAS_1_PI])) / PI;

        // Computing the flows in the system based on the pressures at the current
        // time step.
        if (p.pressure[LEFT_VENTRICULAR_CPI] > (p.pressure[ASCENDING_AORTIC_CPI] + p.grav[0])) {
            p.flowRate[ASCENDING_AORTIC_CPI] = (p.pressure[LEFT_VENTRICULAR_CPI] - p.pressure[ASCENDING_AORTIC_CPI] - p.grav[0]) / theta.get(PVName.AORTIC_VALVE_RESISTANCE);
        } else {
            p.flowRate[ASCENDING_AORTIC_CPI] = 0.0;                        // left ventricular outflow
        }
        p.flowRate[BRACHIOCEPHALIC_ARTERIAL_CPI] = (p.pressure[ASCENDING_AORTIC_CPI] - p.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] - p.grav[1]) / theta.get(PVName.BRACH_ART_RESISTANCE);
        p.flowRate[UPPER_BODY_ARTERIAL_CPI] = (p.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] - p.pressure[UPPER_BODY_ARTERIAL_CPI] - p.grav[2]) / theta.get(PVName.UBODY_ART_RESISTANCE);
        p.flowRate[UPPER_BODY_VENOUS_CPI] = (p.pressure[UPPER_BODY_ARTERIAL_CPI] - p.pressure[UPPER_BODY_VENOUS_CPI]) / r.resistance[0];

        // Starling resistor defines the flow into the superior vena cava.
        if ((p.pressure[UPPER_BODY_VENOUS_CPI] + p.grav[3] > p.pressure[SUPERIOR_VENA_CAVA_CPI]) && (p.pressure[SUPERIOR_VENA_CAVA_CPI] > p.pressure[INTRA_THORACIC_CPI])) {
            p.flowRate[SUPERIOR_VENA_CAVA_CPI] = (p.pressure[UPPER_BODY_VENOUS_CPI] - p.pressure[SUPERIOR_VENA_CAVA_CPI] + p.grav[3]) / theta.get(PVName.UBODY_VEN_RESISTANCE);
        } else if ((p.pressure[UPPER_BODY_VENOUS_CPI] + p.grav[3] > p.pressure[INTRA_THORACIC_CPI]) && (p.pressure[INTRA_THORACIC_CPI] > p.pressure[SUPERIOR_VENA_CAVA_CPI])) {
            p.flowRate[SUPERIOR_VENA_CAVA_CPI] = (p.pressure[UPPER_BODY_VENOUS_CPI] - p.pressure[INTRA_THORACIC_CPI] + p.grav[3]) / theta.get(PVName.UBODY_VEN_RESISTANCE);
        } else if (p.pressure[INTRA_THORACIC_CPI] > p.pressure[UPPER_BODY_VENOUS_CPI] + p.grav[3]) {
            p.flowRate[SUPERIOR_VENA_CAVA_CPI] = 0.0;
        }

        p.flowRate[THORACIC_AORTIC_CPI] = (p.pressure[SUPERIOR_VENA_CAVA_CPI] - p.pressure[RIGHT_ATRIAL_CPI] + p.grav[4]) / theta.get(PVName.SVC_RESISTANCE);
        p.flowRate[ABDOMINAL_AORTIC_CPI] = (p.pressure[ASCENDING_AORTIC_CPI] - p.pressure[THORACIC_AORTIC_CPI] + p.grav[5]) / theta.get(PVName.THORACIC_AORTA_RESISTANCE);
        p.flowRate[RENAL_ARTERIAL_CPI] = (p.pressure[THORACIC_AORTIC_CPI] - p.pressure[ABDOMINAL_AORTIC_CPI] + p.grav[6]) / theta.get(PVName.ABDOM_AORTA_RESISTANCE);
        p.flowRate[RENAL_VENOUS_CPI] = (p.pressure[ABDOMINAL_AORTIC_CPI] - p.pressure[RENAL_ARTERIAL_CPI] + p.grav[7]) / theta.get(PVName.RENAL_ART_RESISTANCE);
        p.flowRate[SPLANCHNIC_ARTERIAL_CPI] = (p.pressure[RENAL_ARTERIAL_CPI] - p.pressure[RENAL_VENOUS_CPI]) / r.resistance[1];
        p.flowRate[SPLANCHNIC_VENOUS_CPI] = (p.pressure[RENAL_VENOUS_CPI] - p.pressure[ABDOMINAL_VENOUS_CPI] - p.grav[8]) / theta.get(PVName.RENAL_VEN_RESISTANCE);
        p.flowRate[LOWER_BODY_ARTERIAL_CPI] = (p.pressure[ABDOMINAL_AORTIC_CPI] - p.pressure[SPLANCHNIC_ARTERIAL_CPI] + p.grav[9]) / theta.get(PVName.SPLAN_ART_RESISTANCE);
        p.flowRate[LOWER_BODY_VENOUS_CPI] = (p.pressure[SPLANCHNIC_ARTERIAL_CPI] - p.pressure[SPLANCHNIC_VENOUS_CPI]) / r.resistance[2];
        p.flowRate[ABDOMINAL_VENOUS_CPI] = (p.pressure[SPLANCHNIC_VENOUS_CPI] - p.pressure[ABDOMINAL_VENOUS_CPI] - p.grav[10]) / theta.get(PVName.SPLAN_VEN_RESISTANCE);
        p.flowRate[INFERIOR_VENA_CAVA_CPI] = (p.pressure[ABDOMINAL_AORTIC_CPI] - p.pressure[LOWER_BODY_ARTERIAL_CPI] + p.grav[11]) / theta.get(PVName.LEG_ART_RESISTANCE);
        p.flowRate[RIGHT_ATRIAL_CPI] = (p.pressure[LOWER_BODY_ARTERIAL_CPI] - p.pressure[LOWER_BODY_VENOUS_CPI]) / r.resistance[3];

        if (p.pressure[LOWER_BODY_VENOUS_CPI] > (p.pressure[ABDOMINAL_VENOUS_CPI] + p.grav[12])) {
            p.flowRate[RIGHT_VENTRICULAR_CPI] = (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[ABDOMINAL_VENOUS_CPI] - p.grav[12]) / theta.get(PVName.LBODY_VEN_RESISTANCE);
        } else {
            p.flowRate[RIGHT_VENTRICULAR_CPI] = 0.0;
        }

        p.flowRate[PULMONARY_ARTERIAL_CPI] = (p.pressure[ABDOMINAL_VENOUS_CPI] - p.pressure[INFERIOR_VENA_CAVA_CPI] - p.grav[13]) / theta.get(PVName.ABDOM_VEN_RESISTANCE);
        p.flowRate[PULMONARY_VENOUS_CPI] = (p.pressure[INFERIOR_VENA_CAVA_CPI] - p.pressure[RIGHT_ATRIAL_CPI] - p.grav[14]) / theta.get(PVName.IVC_RESISTANCE);

        if (p.pressure[RIGHT_ATRIAL_CPI] > p.pressure[RIGHT_VENTRICULAR_CPI]) {
            p.flowRate[LEFT_ATRIAL_CPI] = (p.pressure[RIGHT_ATRIAL_CPI] - p.pressure[RIGHT_VENTRICULAR_CPI]) / theta.get(PVName.TRICUSPID_VALVE_RESISTANCE);
        } else {
            p.flowRate[LEFT_ATRIAL_CPI] = 0.0;
        }

        if (p.pressure[RIGHT_VENTRICULAR_CPI] > p.pressure[PULMONARY_ARTERIAL_CPI]) {
            p.flowRate[LEFT_VENTRICULAR_CPI] = (p.pressure[RIGHT_VENTRICULAR_CPI] - p.pressure[PULMONARY_ARTERIAL_CPI]) / theta.get(PVName.PUMONIC_VALVE_RESISTANCE);
        } else {
            p.flowRate[LEFT_VENTRICULAR_CPI] = 0.0;
        }

        p.flowRate[BIAS_1_PI] = (p.pressure[PULMONARY_ARTERIAL_CPI] - p.pressure[PULMONARY_VENOUS_CPI]) / theta.get(PVName.PULM_MICRO_RESISTANCE);
        p.flowRate[BIAS_2_PI] = (p.pressure[PULMONARY_VENOUS_CPI] - p.pressure[LEFT_ATRIAL_CPI]) / theta.get(PVName.PULM_VEN_RESISTANCE);

        if (p.pressure[LEFT_ATRIAL_CPI] > p.pressure[LEFT_VENTRICULAR_CPI]) {
            p.flowRate[BIAS_3_PI] = (p.pressure[LEFT_ATRIAL_CPI] - p.pressure[LEFT_VENTRICULAR_CPI]) / theta.get(PVName.MITRAL_VALVE_RESISTANCE);
        } else {
            p.flowRate[BIAS_3_PI] = 0.0;
        }

        // Computing the pressure derivatives based on the flows and compliance
        // values at the current time step.
        p.dPressureDt[ASCENDING_AORTIC_CPI] = (p.flowRate[ASCENDING_AORTIC_CPI] - p.flowRate[BRACHIOCEPHALIC_ARTERIAL_CPI] - p.flowRate[ABDOMINAL_AORTIC_CPI]) / theta.get(PVName.ASCENDING_AORTA_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI] = (p.flowRate[BRACHIOCEPHALIC_ARTERIAL_CPI] - p.flowRate[UPPER_BODY_ARTERIAL_CPI]) / theta.get(PVName.BRACH_ART_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[UPPER_BODY_ARTERIAL_CPI] = (p.flowRate[UPPER_BODY_ARTERIAL_CPI] - p.flowRate[UPPER_BODY_VENOUS_CPI]) / theta.get(PVName.UBODY_ART_COMPLIANCE);
        p.dPressureDt[UPPER_BODY_VENOUS_CPI] = (p.flowRate[UPPER_BODY_VENOUS_CPI] - p.flowRate[SUPERIOR_VENA_CAVA_CPI]) / theta.get(PVName.UBODY_VEN_COMPLIANCE);
        p.dPressureDt[SUPERIOR_VENA_CAVA_CPI] = (p.flowRate[SUPERIOR_VENA_CAVA_CPI] - p.flowRate[THORACIC_AORTIC_CPI]) / theta.get(PVName.SVC_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[THORACIC_AORTIC_CPI] = (p.flowRate[ABDOMINAL_AORTIC_CPI] - p.flowRate[RENAL_ARTERIAL_CPI]) / theta.get(PVName.THORACIC_AORTA_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[ABDOMINAL_AORTIC_CPI] = (p.flowRate[RENAL_ARTERIAL_CPI] - p.flowRate[RENAL_VENOUS_CPI] - p.flowRate[LOWER_BODY_ARTERIAL_CPI] - p.flowRate[INFERIOR_VENA_CAVA_CPI]) / theta.get(PVName.ABDOM_AORTA_COMPLIANCE) + p.dPressureDt[BIAS_1_PI];
        p.dPressureDt[RENAL_ARTERIAL_CPI] = (p.flowRate[RENAL_VENOUS_CPI] - p.flowRate[SPLANCHNIC_ARTERIAL_CPI]) / theta.get(PVName.RENAL_ART_COMPLIANCE) + p.dPressureDt[BIAS_1_PI];
        p.dPressureDt[RENAL_VENOUS_CPI] = (p.flowRate[SPLANCHNIC_ARTERIAL_CPI] - p.flowRate[SPLANCHNIC_VENOUS_CPI]) / theta.get(PVName.RENAL_VEN_COMPLIANCE) + p.dPressureDt[BIAS_1_PI];
        p.dPressureDt[SPLANCHNIC_ARTERIAL_CPI] = (p.flowRate[LOWER_BODY_ARTERIAL_CPI] - p.flowRate[LOWER_BODY_VENOUS_CPI]) / theta.get(PVName.SPLAN_ART_COMPLIANCE) + p.dPressureDt[BIAS_1_PI];
        p.dPressureDt[SPLANCHNIC_VENOUS_CPI] = (p.flowRate[LOWER_BODY_VENOUS_CPI] - p.flowRate[ABDOMINAL_VENOUS_CPI] - t.flow[0]) / Csp + p.dPressureDt[BIAS_1_PI];
        p.dPressureDt[LOWER_BODY_ARTERIAL_CPI] = (p.flowRate[INFERIOR_VENA_CAVA_CPI] - p.flowRate[RIGHT_ATRIAL_CPI]) / theta.get(PVName.LBODY_ART_COMPLIANCE) + p.dPressureDt[BIAS_2_PI];
        p.dPressureDt[LOWER_BODY_VENOUS_CPI] = (p.flowRate[RIGHT_ATRIAL_CPI] - p.flowRate[RIGHT_VENTRICULAR_CPI] - t.flow[1]) / Cll + p.dPressureDt[BIAS_2_PI];
        p.dPressureDt[ABDOMINAL_VENOUS_CPI] = (p.flowRate[SPLANCHNIC_VENOUS_CPI] + p.flowRate[ABDOMINAL_VENOUS_CPI] + p.flowRate[RIGHT_VENTRICULAR_CPI] - p.flowRate[PULMONARY_ARTERIAL_CPI] - t.flow[2]) / Cab + p.dPressureDt[BIAS_1_PI];
        p.dPressureDt[INFERIOR_VENA_CAVA_CPI] = (p.flowRate[PULMONARY_ARTERIAL_CPI] - p.flowRate[PULMONARY_VENOUS_CPI]) / theta.get(PVName.IVC_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[RIGHT_ATRIAL_CPI] = ((p.pressure[INTRA_THORACIC_CPI] - p.pressure[RIGHT_ATRIAL_CPI]) * p.dComplianceDt[RA_COMPL] + p.flowRate[THORACIC_AORTIC_CPI] + p.flowRate[PULMONARY_VENOUS_CPI] - p.flowRate[LEFT_ATRIAL_CPI])
                / p.compliance[RA_COMPL] + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[RIGHT_VENTRICULAR_CPI] = ((p.pressure[INTRA_THORACIC_CPI] - p.pressure[RIGHT_VENTRICULAR_CPI]) * p.dComplianceDt[RV_COMPL] + p.flowRate[LEFT_ATRIAL_CPI] - p.flowRate[LEFT_VENTRICULAR_CPI])
                / p.compliance[RV_COMPL] + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[PULMONARY_ARTERIAL_CPI] = (p.flowRate[LEFT_VENTRICULAR_CPI] - p.flowRate[BIAS_1_PI]) / theta.get(PVName.PULM_ART_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[PULMONARY_VENOUS_CPI] = (p.flowRate[BIAS_1_PI] - p.flowRate[BIAS_2_PI]) / theta.get(PVName.PULM_VEN_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[LEFT_ATRIAL_CPI] = ((p.pressure[INTRA_THORACIC_CPI] - p.pressure[LEFT_ATRIAL_CPI]) * p.dComplianceDt[LA_COMPL] + p.flowRate[BIAS_2_PI] - p.flowRate[BIAS_3_PI])
                / p.compliance[LA_COMPL] + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[LEFT_VENTRICULAR_CPI] = ((p.pressure[INTRA_THORACIC_CPI] - p.pressure[LEFT_VENTRICULAR_CPI]) * p.dComplianceDt[LV_COMPL] + p.flowRate[BIAS_3_PI] - p.flowRate[ASCENDING_AORTIC_CPI])
                / p.compliance[LV_COMPL] + p.dPressureDt[INTRA_THORACIC_CPI];

        // Computing the compartmental volumes.
        p.volume[ASCENDING_AORTIC_CPI] = (p.pressure[ASCENDING_AORTIC_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.ASCENDING_AORTA_COMPLIANCE) + theta.get(PVName.ASCENDING_AORTA_VOLUME);
        p.volume[BRACHIOCEPHALIC_ARTERIAL_CPI] = (p.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.BRACH_ART_COMPLIANCE) + theta.get(PVName.BRACH_ART_ZPFV);
        p.volume[UPPER_BODY_ARTERIAL_CPI] = p.pressure[UPPER_BODY_ARTERIAL_CPI] * theta.get(PVName.UBODY_ART_COMPLIANCE) + theta.get(PVName.UBODY_ART_ZPFV);
        p.volume[UPPER_BODY_VENOUS_CPI] = p.pressure[UPPER_BODY_VENOUS_CPI] * theta.get(PVName.UBODY_VEN_COMPLIANCE) + r.volume[0];
        p.volume[SUPERIOR_VENA_CAVA_CPI] = (p.pressure[SUPERIOR_VENA_CAVA_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.SVC_COMPLIANCE) + theta.get(PVName.SVC_ZPFV);
        p.volume[THORACIC_AORTIC_CPI] = (p.pressure[THORACIC_AORTIC_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.THORACIC_AORTA_COMPLIANCE) + theta.get(PVName.THORACIC_AORTA_ZPFV);
        p.volume[ABDOMINAL_AORTIC_CPI] = (p.pressure[ABDOMINAL_AORTIC_CPI] - p.pressure[BIAS_1_PI]) * theta.get(PVName.ABDOM_AORTA_COMPLIANCE) + theta.get(PVName.ABDOM_AORTA_ZPFV);
        p.volume[RENAL_ARTERIAL_CPI] = (p.pressure[RENAL_ARTERIAL_CPI] - p.pressure[BIAS_1_PI]) * theta.get(PVName.RENAL_ART_COMPLIANCE) + theta.get(PVName.RENAL_ART_ZPFV);
        p.volume[RENAL_VENOUS_CPI] = (p.pressure[RENAL_VENOUS_CPI] - p.pressure[BIAS_1_PI]) * theta.get(PVName.RENAL_VEN_COMPLIANCE) + r.volume[1];
        p.volume[SPLANCHNIC_ARTERIAL_CPI] = (p.pressure[SPLANCHNIC_ARTERIAL_CPI] - p.pressure[BIAS_1_PI]) * theta.get(PVName.SPLAN_ART_COMPLIANCE) + theta.get(PVName.SPLAN_ART_ZPFV);
        p.volume[SPLANCHNIC_VENOUS_CPI] = Vsp + r.volume[2];
        p.volume[LOWER_BODY_ARTERIAL_CPI] = (p.pressure[LOWER_BODY_ARTERIAL_CPI] - p.pressure[BIAS_2_PI]) * theta.get(PVName.LBODY_ART_COMPLIANCE) + theta.get(PVName.LBODY_ART_ZPFV);
        p.volume[LOWER_BODY_VENOUS_CPI] = Vll + r.volume[3];
        p.volume[ABDOMINAL_VENOUS_CPI] = Vab + theta.get(PVName.ABDOM_VEN_ZPFV);
        p.volume[INFERIOR_VENA_CAVA_CPI] = (p.pressure[INFERIOR_VENA_CAVA_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.IVC_COMPLIANCE) + theta.get(PVName.IVC_ZPFV);
        p.volume[RIGHT_ATRIAL_CPI] = (p.pressure[RIGHT_ATRIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[RA_COMPL] + theta.get(PVName.RA_ZPFV);
        p.volume[RIGHT_VENTRICULAR_CPI] = (p.pressure[RIGHT_VENTRICULAR_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[RV_COMPL] + theta.get(PVName.RV_ZPFV);
        p.volume[PULMONARY_ARTERIAL_CPI] = (p.pressure[PULMONARY_ARTERIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.PULM_ART_COMPLIANCE) + theta.get(PVName.PULM_ART_ZPFV);
        p.volume[PULMONARY_VENOUS_CPI] = (p.pressure[PULMONARY_VENOUS_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.PULM_VEN_COMPLIANCE) + theta.get(PVName.PULN_VEN_ZPFV);
        p.volume[LEFT_ATRIAL_CPI] = (p.pressure[LEFT_ATRIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[LA_COMPL] + theta.get(PVName.LA_ZPFV);
        p.volume[LEFT_VENTRICULAR_CPI] = (p.pressure[LEFT_VENTRICULAR_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[LV_COMPL] + theta.get(PVName.LV_ZPFV);

    }

    /*
     * The following routine ensures blood volume conservation after every
     * integration step. It computes the blood volume stored in each capacitor,
     * the total zero pressure filling volume, and the total blood volume loss
     * during an othostatic stress intervention and compares the sum of these
     * three to the total blood volume constant in the parameter vector. Any
     * difference between the two will be corrected for at the inferior vena
     * cava.
     */
    private static int i = 0;

    public static void fixvolume_ptr(Data_vector p, Reflex_vector ref, Parameter_vector theta) {
        double diff = 0.0, con = 0.0;           // temporary variables
        double Vsp = 0.0, Vll = 0.0, Vab = 0.0; // non-linear p-v relations
        double Cll = 0.0;

        con = PI * theta.get(PVName.SPLAN_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL);
        Vsp = 2.0 * theta.get(PVName.MAX_INCREASE_IN_SPLAN_DISTENDING_VOL) * atan(con * (p.pressure[SPLANCHNIC_VENOUS_CPI] - p.pressure[BIAS_1_PI])) / PI;

        if ((p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]) > 0.0) {
            con = PI * theta.get(PVName.LBODY_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL);
            Cll = theta.get(PVName.LBODY_VEN_COMPLIANCE) / (1.0 + con * con * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]) * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]));
            Vll = 2.0 * theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL) * atan(con * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI])) / PI;
        } else {
            con = PI * theta.get(PVName.LBODY_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL);
            Cll = theta.get(PVName.LBODY_VEN_COMPLIANCE) / (1.0 + con * con * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]) * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI]));
            Vll = 2.0 * theta.get(PVName.MAX_INCREASE_IN_LEG_DISTENDING_VOL) * atan(con * (p.pressure[LOWER_BODY_VENOUS_CPI] - p.pressure[BIAS_2_PI])) / PI;
        }

        con = PI * theta.get(PVName.ABDOM_VEN_COMPLIANCE) / 2.0 / theta.get(PVName.MAX_INCREASE_IN_ABDOM_DISTENDING_VOL);
        Vab = 2.0 * theta.get(PVName.MAX_INCREASE_IN_ABDOM_DISTENDING_VOL) * atan(con * (p.pressure[ABDOMINAL_VENOUS_CPI] - p.pressure[BIAS_1_PI])) / PI;

        diff = theta.get(PVName.TOTAL_BLOOD_VOLUME) - theta.get(PVName.ABDOM_VEN_ZPFV) - theta.get(PVName.IVC_ZPFV) - theta.get(PVName.SVC_ZPFV)
                - theta.get(PVName.RA_ZPFV) - theta.get(PVName.RV_ZPFV) - theta.get(PVName.PULM_ART_ZPFV)
                - theta.get(PVName.PULN_VEN_ZPFV) - theta.get(PVName.LA_ZPFV) - theta.get(PVName.LV_ZPFV) - ref.volume[0]
                - theta.get(PVName.ASCENDING_AORTA_VOLUME) - theta.get(PVName.BRACH_ART_ZPFV) - theta.get(PVName.THORACIC_AORTA_ZPFV) - theta.get(PVName.UBODY_ART_ZPFV)
                - theta.get(PVName.ABDOM_AORTA_ZPFV) - theta.get(PVName.RENAL_ART_ZPFV) - theta.get(PVName.SPLAN_ART_ZPFV) - theta.get(PVName.LBODY_ART_ZPFV)
                - ref.volume[1] - ref.volume[2] - ref.volume[3] - p.tilt[1]
                - ((p.pressure[ASCENDING_AORTIC_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.ASCENDING_AORTA_COMPLIANCE)
                + (p.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.BRACH_ART_COMPLIANCE)
                + (p.pressure[INFERIOR_VENA_CAVA_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.IVC_COMPLIANCE)
                + (p.pressure[RIGHT_ATRIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[RA_COMPL]
                + (p.pressure[RIGHT_VENTRICULAR_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[RV_COMPL]
                + (p.pressure[PULMONARY_ARTERIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.PULM_ART_COMPLIANCE)
                + (p.pressure[PULMONARY_VENOUS_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.PULM_VEN_COMPLIANCE)
                + (p.pressure[LEFT_ATRIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[LA_COMPL]
                + (p.pressure[LEFT_VENTRICULAR_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[LV_COMPL]
                + (p.pressure[SUPERIOR_VENA_CAVA_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.SVC_COMPLIANCE)
                + (p.pressure[THORACIC_AORTIC_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.THORACIC_AORTA_COMPLIANCE)
                + (p.pressure[ABDOMINAL_AORTIC_CPI] - p.pressure[BIAS_1_PI]) * theta.get(PVName.ABDOM_AORTA_COMPLIANCE) + Vsp + Vll + Vab
                + (p.pressure[RENAL_ARTERIAL_CPI] - p.pressure[BIAS_1_PI]) * theta.get(PVName.RENAL_ART_COMPLIANCE)
                + (p.pressure[RENAL_VENOUS_CPI] - p.pressure[BIAS_1_PI]) * theta.get(PVName.RENAL_VEN_COMPLIANCE)
                + (p.pressure[SPLANCHNIC_ARTERIAL_CPI] - p.pressure[BIAS_1_PI]) * theta.get(PVName.SPLAN_ART_COMPLIANCE)
                + p.pressure[UPPER_BODY_ARTERIAL_CPI] * theta.get(PVName.UBODY_ART_COMPLIANCE)
                + p.pressure[UPPER_BODY_VENOUS_CPI] * theta.get(PVName.UBODY_VEN_COMPLIANCE)
                + (p.pressure[LOWER_BODY_ARTERIAL_CPI] - p.pressure[BIAS_2_PI]) * theta.get(PVName.LBODY_ART_COMPLIANCE));

        // All volumes
        //  if ((i % 10) == 0)
        //    printf("%e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e\n", p.time[0], diff, (p.x[0]-p.x[24])*theta.get(PVName.PV97), (p.x[1] - p.x[24])*theta.get(PVName.PV98), p.x[2]*theta.get(PVName.PV100), p.x[3]*theta.get(PVName.PV36), (p.x[4]-p.x[24])*theta.get(PVName.PV42), (p.x[5]-p.x[24])*theta.get(PVName.PV99), p.x[6]*theta.get(PVName.PV101), p.x[7]*theta.get(PVName.PV102), p.x[8]*theta.get(PVName.PV37), p.x[9]*theta.get(PVName.PV103), Vsp, p.x[11]*theta.get(PVName.PV104), Vll, Vab, p.x[14]*theta.get(PVName.PV41), (p.x[15] - p.x[24])*p.c[0], (p.x[16] - p.x[24])*p.c[1], (p.x[17] - p.x[24])*theta.get(PVName.PV47), (p.x[18] - p.x[24])*theta.get(PVName.PV48), (p.x[19] - p.x[24])*p.c[2], (p.x[20] - p.x[24])*p.c[3], p.tilt[1]);  
        // All transmural pressures
        //  if ((i % 10) == 0)
        //    printf("%e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e\n", p.time[0], diff, p.x[0]-p.x[24], p.x[1]-p.x[24], p.x[2], p.x[3], p.x[4]-p.x[24], p.x[5]-p.x[24], p.x[6]-p.x[21], p.x[7]-p.x[21], p.x[8]-p.x[21], p.x[9]-p.x[21], p.x[10]-p.x[21], p.x[11]-p.x[22], p.x[12]-p.x[22], p.x[13]-p.x[21], p.x[14]-p.x[24], p.x[15]-p.x[24], p.x[16]-p.x[24], p.x[17]-p.x[24], p.x[18]-p.x[24], p.x[19]-p.x[24], p.x[20]-p.x[24]);
        // All flows
        //  printf("%e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e\n", p.time[0], diff, p.q[0], p.q[1], p.q[2], p.q[3], p.q[4], p.q[5], p.q[6], p.q[7], p.q[8], p.q[9], p.q[10], p.q[11], p.q[12], p.q[13], p.q[14], p.q[15], p.q[16], p.q[17], p.q[18], p.q[19], p.q[20], p.q[21], p.q[22], p.q[23]);
        // All luminal pressures (wrt atmospheric pressure).
        //  if ((i % 10) == 0)
        //    printf("%e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e\n", p.time[0], diff, p.x[0], p.x[1], p.x[2], p.x[3], p.x[4], p.x[5], p.x[6], p.x[7], p.x[8], p.x[9], p.x[10], p.x[11], p.x[12], p.x[13], p.x[14], p.x[15], p.x[16], p.x[17], p.x[18], p.x[19], p.x[20], p.x[21], p.x[22], p.x[23], p.x[24], Vll);
        // All gravitational pressure components
        //  printf("%e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e %e\n", p.time[0], diff, p.grav[0], p.grav[1], p.grav[2], p.grav[3], p.grav[4], p.grav[5], p.grav[6], p.grav[7], p.grav[8], p.grav[9], p.grav[10], p.grav[11], p.grav[12], p.grav[13], p.grav[14]);
        //  if ((i % 10) == 0)
        //    printf("%e %e %e %e %e %e %e %e %e %e\n", p.time[0], diff, p.x[0], ref.hr[2], ref.volume[0]+ref.volume[1]+ref.volume[2]+ref.volume[3], 1./(1./ref.resistance[0]+1./ref.resistance[1]+1./ref.resistance[2]+1./ref.resistance[3]), p.tilt[0], p.x[14]-p.x[24], p.x[0]-p.x[24], p.x[2]);
        //    printf("%e %e %e %e %e %e %e %e %e %e %e %e\n", p.time[0], diff, p.x[0], ref.hr[2], ref.volume[0]+ref.volume[1]+ref.volume[2]+ref.volume[3], 1./(1./ref.resistance[0]+1./ref.resistance[1]+1./ref.resistance[2]+1./ref.resistance[3]), p.x[10], p.x[12], p.x[14], p.x[14]-p.x[24], p.x[15]-p.x[24], p.c[3]);

        /*
         * if ((p.time[0] > 190.0) && (p.time[0] < 440.0))
         * if ((i % 100) == 0)
         * printf("%e %e %e %e %e %e %e %e\n", p.time[0], diff, p.x[0],
         * ref.hr[2], ref.volume[0]+ref.volume[1]+ref.volume[2]+ref.volume[3],
         * 1./(1./ref.resistance[0]+1./ref.resistance[1]+1./ref.resistance[2]+1./ref.resistance[3]),
         * p.tilt[0], p.x[15] - p.x[24]);
         */
        //  if (p.time[0] > 190.)
        //  printf("%e %e %e %e %e %e %e\n", p.time[0], diff, p.c[0], p.dcdt[0], p.c[1], p.dcdt[1], ref.hr[2]);
        // printf("%e %e\n", p.time[0], diff);
        //  printf("\n");
        //  if (fabs(diff) >= 0.01) printf("Volume correction error = %e\n", diff);
        // Correct for any difference in blood volume at the inferior vena cava
        // compartment.
        //  p . x[12] += diff/Cll;
        //  printf("%e %e %e %e %e\n", p.time[0], p.time[1], p.c[3], p.c[2], diff);
        i++;
    }

    /*
     * Three different orthostatic stresses (short radius centrifugation, tilt,
     * and lower body negative pressure) were implemented in the following three
     * subroutines. Each subroutine is called from eqns() and modifies selected
     * elements of the Data_vector and the Stress_vector which are supplied to
     * them.
     */

 /*
     * In the following subroutine the tilt simulation is implemented.
     */
    public static int tilt(Data_vector p, Parameter_vector theta, Tilt_vector tilt,
            double tiltStartTime, double tiltStopTime) {
        double alpha = 0.0;               // tilt angle converted to rads

        double act = 0.0;                 // activation function for intra-thoracic
        // and intra-abdominal pressures.
        double act_dt = 0.0;

        double gravity = 0.0;             // activation function of stress
        // onset and offset
        double gravity_dt = 0.0;

        double tilt_time = 0.0;          // time
        double tilt_angle = 0.0;         // instantaneous tilt angle in degrees

        // The following variables are introduced for computation of the blood 
        // volume sequestration into the interstitial fluid compartment.
        double V_loss = 0.0;                       // total blood volume loss as a
        // function of time

        double qsp_loss = 0.0, qll_loss = 0.0;     // leakage currents 
        double qab_loss = 0.0, q_loss = 0.0;
        double q_not = theta.get(PVName.MAXIMAL_BLOOD_VOLUME_LOSS_DURING_TILT) / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE);
        final double TAU = 276.0;        // time constant of interstitial fluid shifts

        // Page 49 of Thomas Heldt’s PhD thesis:
        // "In addition to changes in the luminal pressures described by Ph, Mead
        // and Gaensler [123] and Ferris and co-workers [124] showed that
        // intra-thoracic pressure, Pth, changed over similarly short periods of
        // time in response to gravitational stress. Mead and Gaensler reported
        // intra-thoracic pressure to drop by (3.1±1.0) mm Hg in response to
        // sitting up from the supine position. Ferris demonstrated a drop of
        // (3.5±0.7) mm Hg in response to head-up tilts to 90◦. We implemented
        // these changes in intra-thoracic pressure by assuming a time course
        // similar to the one described by Equation 2.4."
        final double INTRATHORACIC_PRESSURE_CHANGE_ON_TILT = -3.5;
        double con1 = INTRATHORACIC_PRESSURE_CHANGE_ON_TILT / 0.738;

        // Define certain dummy variables.
        alpha = Math.toRadians(theta.get(PVName.TILT_ANGLE));
        q_not *= sin(alpha) / sin(Math.toRadians(85.0));
        con1 *= sin(alpha);

        // Tilt from horizontal to head-up position.
        if ((p.time[MODIFIED_ABSOLUTE_TIME] >= tiltStartTime)
                && (p.time[MODIFIED_ABSOLUTE_TIME] <= (tiltStartTime + theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)))) {
            tilt_time = (p.time[MODIFIED_ABSOLUTE_TIME] - tiltStartTime);

            // calculate tilt angle in radians
            tilt_angle = alpha * (1.0 - cos(PI * tilt_time / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE))) / 2.0;
            gravity = 0.738 * sin(tilt_angle);
            gravity_dt = 0.738 * cos(tilt_angle) * alpha / 2.0 * sin(PI * tilt_time / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)) * PI / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE);

            p.pressure[INTRA_THORACIC_CPI] = theta.get(PVName.INTRA_THORACIC_PRESSURE) + con1 * gravity;
            p.dPressureDt[INTRA_THORACIC_CPI] = con1 * gravity_dt;

            p.grav[0] = theta.get(PVName.ASCENDING_AORTA_HEIGHT) / 2.0 * gravity;  // ascending aorta 
            p.grav[1] = theta.get(PVName.BRACHIOCEPHAL_ART_HEIGHT) / 2.0 * gravity;  // brachiocephal.
            p.grav[2] = theta.get(PVName.UBODY_ART_HEIGHT) / 2.0 * gravity;  // upper body
            p.grav[3] = theta.get(PVName.UBODY_VEN_HEIGHT) / 2.0 * gravity;  // upper body veins
            p.grav[4] = theta.get(PVName.SVC_HEIGHT) / 2.0 * gravity;  // SVC
            p.grav[5] = theta.get(PVName.DESCENDING_AORTA_HEIGHT) / 2.0 * gravity;  // descending aorta
            p.grav[6] = theta.get(PVName.ABDOM_AORTA_HEIGHT) / 3.0 * gravity;  // abdominal aorta
            p.grav[7] = theta.get(PVName.RENAL_ART_HEIGHT) / 2.0 * gravity;  // renal artieries
            p.grav[8] = theta.get(PVName.RENAL_VEN_HEIGHT) / 2.0 * gravity;  // renal veins
            p.grav[9] = theta.get(PVName.SPLAN_ART_HEIGHT) / 2.0 * gravity;  // splanchnic arteries
            p.grav[10] = theta.get(PVName.SPLAN_VEIN_HEIGHT) / 2.0 * gravity;  // splanchnic veins
            p.grav[11] = theta.get(PVName.LEG_ART_HEIGHT) / 3.0 * gravity;  // leg arteries
            p.grav[12] = theta.get(PVName.LEG_VEN_HEIGHT) / 3.0 * gravity;  // leg veins
            p.grav[13] = theta.get(PVName.ABDOM_IVC_HEIGHT) / 3.0 * gravity;  // abdominal IVC
            p.grav[14] = theta.get(PVName.THORACIC_IVC_HEIGHT) / 2.0 * gravity;  // thoracic IVC

            q_loss = q_not * (1.0 - exp(-tilt_time / TAU));
            qsp_loss = 7.0 / (63.0) * q_loss;
            qll_loss = 40.0 / (63.0) * q_loss;
            qab_loss = 16 / (63.0) * q_loss;

            V_loss = q_not * (tilt_time - TAU * (1.0 - exp(-tilt_time / TAU)));
        } // Head-up position
        else if ((p.time[MODIFIED_ABSOLUTE_TIME] > (theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) + tiltStartTime))
                && (p.time[MODIFIED_ABSOLUTE_TIME] <= (theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) + tiltStartTime
                + theta.get(PVName.DURATION_IN_UPRIGHT_POSTURE)))) {
            tilt_time = (p.time[MODIFIED_ABSOLUTE_TIME] - tiltStartTime - theta.get(PVName.TIME_TO_MAX_TILT_ANGLE));
            tilt_angle = alpha;
            gravity = 0.738 * sin(alpha);
            gravity_dt = 0.0;

            p.pressure[INTRA_THORACIC_CPI] = theta.get(PVName.INTRA_THORACIC_PRESSURE) + con1 * gravity;
            p.dPressureDt[INTRA_THORACIC_CPI] = con1 * gravity_dt;

            p.grav[0] = theta.get(PVName.ASCENDING_AORTA_HEIGHT) / 2.0 * gravity;  // ascending aorta
            p.grav[1] = theta.get(PVName.BRACHIOCEPHAL_ART_HEIGHT) / 2.0 * gravity;  // brachiocephal.
            p.grav[2] = theta.get(PVName.UBODY_ART_HEIGHT) / 2.0 * gravity;  // upper body
            p.grav[3] = theta.get(PVName.UBODY_VEN_HEIGHT) / 2.0 * gravity;  // upper body veins
            p.grav[4] = theta.get(PVName.SVC_HEIGHT) / 2.0 * gravity;  // SVC
            p.grav[5] = theta.get(PVName.DESCENDING_AORTA_HEIGHT) / 2.0 * gravity;  // descending aorta
            p.grav[6] = theta.get(PVName.ABDOM_AORTA_HEIGHT) / 3.0 * gravity;  // abdominal aorta
            p.grav[7] = theta.get(PVName.RENAL_ART_HEIGHT) / 2.0 * gravity;  // renal artieries
            p.grav[8] = theta.get(PVName.RENAL_VEN_HEIGHT) / 2.0 * gravity;  // renal veins
            p.grav[9] = theta.get(PVName.SPLAN_ART_HEIGHT) / 2.0 * gravity;  // splanchnic arteries
            p.grav[10] = theta.get(PVName.SPLAN_VEIN_HEIGHT) / 2.0 * gravity;  // splanchnic veins
            p.grav[11] = theta.get(PVName.LEG_ART_HEIGHT) / 3.0 * gravity;  // leg arteries
            p.grav[12] = theta.get(PVName.LEG_VEN_HEIGHT) / 3.0 * gravity;  // leg veins
            p.grav[13] = theta.get(PVName.ABDOM_IVC_HEIGHT) / 3.0 * gravity;  // abdominal IVC
            p.grav[14] = theta.get(PVName.THORACIC_IVC_HEIGHT) / 2.0 * gravity;  // thoracic IVC

            q_loss = q_not * (1.0 - exp(-theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) / TAU)) * exp(-tilt_time / TAU);
            qsp_loss = 7.0 / (63.0) * q_loss;
            qll_loss = 40.0 / (63.0) * q_loss;
            qab_loss = 16 / (63.0) * q_loss;

            V_loss = q_not * theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)
                    * (1.0 - TAU * (1.0 - exp(-theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) / TAU))
                    * exp(-tilt_time / TAU) / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE));
        } // Tilt back to the supine position
        else if ((p.time[MODIFIED_ABSOLUTE_TIME] >= tiltStopTime)
                && (p.time[MODIFIED_ABSOLUTE_TIME] < (tiltStopTime + theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)))) {
            tilt_time = (p.time[MODIFIED_ABSOLUTE_TIME] - tiltStartTime - theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)
                    - (tiltStopTime - tiltStartTime));

            tilt_angle = alpha * (1.0 - cos(PI * (1 - tilt_time / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)))) / 2.0;
            gravity = 0.738 * sin(tilt_angle);
            gravity_dt = 0.738 * cos(tilt_angle) * alpha / 2.0 * sin(PI * (1.0 - tilt_time / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE))) * PI / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) * (-1.0);

            p.pressure[INTRA_THORACIC_CPI] = theta.get(PVName.INTRA_THORACIC_PRESSURE) + con1 * gravity;
            p.dPressureDt[INTRA_THORACIC_CPI] = con1 * gravity_dt;

            p.grav[0] = theta.get(PVName.ASCENDING_AORTA_HEIGHT) / 2.0 * gravity;  // ascending aorta
            p.grav[1] = theta.get(PVName.BRACHIOCEPHAL_ART_HEIGHT) / 2.0 * gravity;  // brachiocephal.
            p.grav[2] = theta.get(PVName.UBODY_ART_HEIGHT) / 2.0 * gravity;  // upper body
            p.grav[3] = theta.get(PVName.UBODY_VEN_HEIGHT) / 2.0 * gravity;  // upper body veins
            p.grav[4] = theta.get(PVName.SVC_HEIGHT) / 2.0 * gravity;  // SVC
            p.grav[5] = theta.get(PVName.DESCENDING_AORTA_HEIGHT) / 2.0 * gravity;  // descending aorta
            p.grav[6] = theta.get(PVName.ABDOM_AORTA_HEIGHT) / 3.0 * gravity;  // abdominal aorta
            p.grav[7] = theta.get(PVName.RENAL_ART_HEIGHT) / 2.0 * gravity;  // renal artieries
            p.grav[8] = theta.get(PVName.RENAL_VEN_HEIGHT) / 2.0 * gravity;  // renal veins
            p.grav[9] = theta.get(PVName.SPLAN_ART_HEIGHT) / 2.0 * gravity;  // splanchnic arteries
            p.grav[10] = theta.get(PVName.SPLAN_VEIN_HEIGHT) / 2.0 * gravity;  // splanchnic veins
            p.grav[11] = theta.get(PVName.LEG_ART_HEIGHT) / 3.0 * gravity;  // leg arteries
            p.grav[12] = theta.get(PVName.LEG_VEN_HEIGHT) / 3.0 * gravity;  // leg veins
            p.grav[13] = theta.get(PVName.ABDOM_IVC_HEIGHT) / 3.0 * gravity;  // abdominal IVC
            p.grav[14] = theta.get(PVName.THORACIC_IVC_HEIGHT) / 2.0 * gravity;  // thoracic IVC

            q_loss = q_not * (1.0 + (1.0 - exp(-theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) / TAU))
                    * exp(-theta.get(PVName.DURATION_IN_UPRIGHT_POSTURE) / TAU)) * exp(-tilt_time / TAU) - q_not;
            qsp_loss = 7.0 / (63.0) * q_loss;
            qll_loss = 40.0 / (63.0) * q_loss;
            qab_loss = 16 / (63.0) * q_loss;

            V_loss = (1.0 + (1.0 - exp(-theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) / TAU)) * exp(-theta.get(PVName.DURATION_IN_UPRIGHT_POSTURE) / TAU))
                    * (1.0 - exp(-tilt_time / TAU)) * q_not * TAU
                    - q_not * TAU * (1.0 - exp(-theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) / TAU)) * exp(-theta.get(PVName.DURATION_IN_UPRIGHT_POSTURE) / TAU)
                    + q_not * theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) * (1.0 - tilt_time / theta.get(PVName.TIME_TO_MAX_TILT_ANGLE));
        } // Supine position
        else if (p.time[MODIFIED_ABSOLUTE_TIME] > (tiltStopTime + theta.get(PVName.TIME_TO_MAX_TILT_ANGLE))) {
            tilt_time = (p.time[MODIFIED_ABSOLUTE_TIME] - tiltStartTime - 2.0 * theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)
                    - (tiltStopTime - tiltStartTime));

            gravity = 0.0;
            gravity_dt = 0.0;
            tilt_angle = 0.0;
            p.pressure[INTRA_THORACIC_CPI] = theta.get(PVName.INTRA_THORACIC_PRESSURE) + con1 * gravity;
            p.dPressureDt[INTRA_THORACIC_CPI] = con1 * gravity_dt;

            p.grav[0] = theta.get(PVName.ASCENDING_AORTA_HEIGHT) / 2.0 * gravity;  // ascending aorta
            p.grav[1] = theta.get(PVName.BRACHIOCEPHAL_ART_HEIGHT) / 2.0 * gravity;  // brachiocephal.
            p.grav[2] = theta.get(PVName.UBODY_ART_HEIGHT) / 2.0 * gravity;  // upper body
            p.grav[3] = theta.get(PVName.UBODY_VEN_HEIGHT) / 2.0 * gravity;  // upper body veins
            p.grav[4] = theta.get(PVName.SVC_HEIGHT) / 2.0 * gravity;  // SVC
            p.grav[5] = theta.get(PVName.DESCENDING_AORTA_HEIGHT) / 2.0 * gravity;  // descending aorta
            p.grav[6] = theta.get(PVName.ABDOM_AORTA_HEIGHT) / 3.0 * gravity;  // abdominal aorta
            p.grav[7] = theta.get(PVName.RENAL_ART_HEIGHT) / 2.0 * gravity;  // renal artieries
            p.grav[8] = theta.get(PVName.RENAL_VEN_HEIGHT) / 2.0 * gravity;  // renal veins
            p.grav[9] = theta.get(PVName.SPLAN_ART_HEIGHT) / 2.0 * gravity;  // splanchnic arteries
            p.grav[10] = theta.get(PVName.SPLAN_VEIN_HEIGHT) / 2.0 * gravity;  // splanchnic veins
            p.grav[11] = theta.get(PVName.LEG_ART_HEIGHT) / 3.0 * gravity;  // leg arteries
            p.grav[12] = theta.get(PVName.LEG_VEN_HEIGHT) / 3.0 * gravity;  // leg veins
            p.grav[13] = theta.get(PVName.ABDOM_IVC_HEIGHT) / 3.0 * gravity;  // abdominal IVC
            p.grav[14] = theta.get(PVName.THORACIC_IVC_HEIGHT) / 2.0 * gravity;  // thoracic IVC

            q_loss = -q_not * (1.0 - exp(-theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) / TAU)) * exp(-tilt_time / TAU)
                    * (1.0 - exp(-(theta.get(PVName.DURATION_IN_UPRIGHT_POSTURE) + theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)) / TAU));
            qsp_loss = 7.0 / (63.0) * q_loss;
            qll_loss = 40.0 / (63.0) * q_loss;
            qab_loss = 16 / (63.0) * q_loss;

            V_loss = q_not * TAU * (1.0 - exp(-theta.get(PVName.TIME_TO_MAX_TILT_ANGLE) / TAU)) * exp(-tilt_time / TAU)
                    * (1.0 - exp(-(theta.get(PVName.DURATION_IN_UPRIGHT_POSTURE) + theta.get(PVName.TIME_TO_MAX_TILT_ANGLE)) / TAU));
        }

        // Write the computed values for the flows, the volume loss, and the carotid
        // sinus offset pressure to the respective structures so they can be passed
        // across subroutine boundaries.
        tilt.flow[0] = qsp_loss;
        tilt.flow[1] = qll_loss;
        tilt.flow[2] = qab_loss;

        p.tilt[0] = theta.get(PVName.SENSED_PRESSURE_OFFSET_DURING_TILT) * gravity;
        p.tilt[1] = V_loss;

        // convert tilt angle to degrees
        p.tilt_angle = Math.toDegrees(tilt_angle);

        return 0;
    }

}
