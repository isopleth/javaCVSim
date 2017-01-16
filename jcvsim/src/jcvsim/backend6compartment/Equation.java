package jcvsim.backend6compartment;

import static jcvsim.backend6compartment.Data_vector.CompartmentIndexes.*;
import static java.lang.Math.PI;
import static jcvsim.backendCommon.Maths.cos;
import static jcvsim.backendCommon.Maths.sin;

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
 * Last modified October 19, 2005
 */
// Converted to Java Jason Leake December 2016
class Equation {


// Modifies ONLY Data_vector
    public static void elastance_ptr(Data_vector p, Reflex_vector r, Parameter_vector theta) {
        double Elv = 0.0, Erv = 0.0;
        double dElv = 0.0, dErv = 0.0;

        // ventricular timing variables
        double Tvsys = p.time[4];
        double v_time = p.time[6];

        // right and left ventricular diastolic and systolic compliances.
        double Crdias = theta.get(PVName.RV_DIASTOLIC_COMPLIANCE);
        double sigCr = p.compliance[PULMONARY_ARTERIAL_CPI];

        double Cldias = theta.get(PVName.LV_DIASTOLIC_COMPLIANCE);
        double sigCl = p.compliance[PULMONARY_VENOUS_CPI];

        // Ventricular contraction. PR-interval has not yet passed.
        if (v_time <= 0.0) {
            Elv = 1 / Cldias;
            Erv = 1 / Crdias;
            dElv = 0.0;
            dErv = 0.0;
        } // Ventricular contraction.
        else if ((0 < v_time) && (v_time <= Tvsys)) {
            Elv = 0.5 * (1 / sigCl - 1 / Cldias) * (1 - cos(PI * v_time / Tvsys)) + 1 / Cldias;
            Erv = 0.5 * (1 / sigCr - 1 / Crdias) * (1 - cos(PI * v_time / Tvsys)) + 1 / Crdias;
            dElv = 0.5 * PI * (1 / sigCl - 1 / Cldias) * sin(PI * v_time / Tvsys) / Tvsys;
            dErv = 0.5 * PI * (1 / sigCr - 1 / Crdias) * sin(PI * v_time / Tvsys) / Tvsys;
        } // Early ventricular relaxation.
        else if ((Tvsys < v_time) && (v_time <= 1.5 * Tvsys)) {
            Elv = 0.5 * (1 / sigCl - 1 / Cldias) * (1 + cos(2.0 * PI * (v_time - Tvsys) / Tvsys))
                    + 1 / Cldias;
            Erv = 0.5 * (1 / sigCr - 1 / Crdias) * (1 + cos(2.0 * PI * (v_time - Tvsys) / Tvsys))
                    + 1 / Crdias;
            dElv = -1.0 * PI * (1 / sigCl - 1 / Cldias) * sin(2.0 * PI * (v_time - Tvsys) / Tvsys) / Tvsys;
            dErv = -1.0 * PI * (1 / sigCr - 1 / Crdias) * sin(2.0 * PI * (v_time - Tvsys) / Tvsys) / Tvsys;
        } // Ventricular diastole.
        else if (v_time > 1.5 * Tvsys) {
            Elv = 1 / Cldias;
            Erv = 1 / Crdias;
            dElv = 0.0;
            dErv = 0.0;
        }

        p.compliance[ARTERIAL_CPI] = 1 / Erv;
        p.compliance[RIGHT_VENTRICULAR_CPI] = 1 / Elv;

        p.dComplianceDt[ARTERIAL_CPI] = -1.0 / (Erv * Erv) * dErv;
        p.dComplianceDt[RIGHT_VENTRICULAR_CPI] = -1.0 / (Elv * Elv) * dElv;
    }


    /*
     * The following routine computes the flows and the pressure derivatives;
     * it is called from the Runge-Kutta integration routine.
     */
// Modifies ONLY Data_vector
    public static void eqns_ptr(Data_vector p, Parameter_vector theta, Reflex_vector r) {

        // Computing the flows in the system based on the pressures at the current
        // time step.
        // Left ventricular outflow
        if (p.pressure[LEFT_VENTRICULAR_CPI] > p.pressure[ARTERIAL_CPI]) {
            p.flowRate[LEFT_VENTRICULAR_CPI] = (p.pressure[LEFT_VENTRICULAR_CPI] - p.pressure[ARTERIAL_CPI]) / theta.get(PVName.AORTIC_VALVE_RESISTANCE);
        } else {
            p.flowRate[LEFT_VENTRICULAR_CPI] = 0.0;
        }

        // Systemic bloodflow 
        p.flowRate[ARTERIAL_CPI] = (p.pressure[ARTERIAL_CPI] - p.pressure[CENTRAL_VENOUS_CPI]) / r.resistance[0];

        // Right ventricular inflow
        if (p.pressure[CENTRAL_VENOUS_CPI] > p.pressure[RIGHT_VENTRICULAR_CPI]) {
            p.flowRate[CENTRAL_VENOUS_CPI] = (p.pressure[CENTRAL_VENOUS_CPI] - p.pressure[RIGHT_VENTRICULAR_CPI]) / theta.get(PVName.VEN_RESISTANCE);
        } else {
            p.flowRate[CENTRAL_VENOUS_CPI] = 0.0;
        }

        // Right ventricular outflow
        if (p.pressure[RIGHT_VENTRICULAR_CPI] > p.pressure[PULMONARY_ARTERIAL_CPI]) {
            p.flowRate[RIGHT_VENTRICULAR_CPI] = (p.pressure[RIGHT_VENTRICULAR_CPI] - p.pressure[PULMONARY_ARTERIAL_CPI]) / theta.get(PVName.PULMONIC_VALVE_RESISTANCE);
        } else {
            p.flowRate[RIGHT_VENTRICULAR_CPI] = 0.0;
        }

        // Pulmonary bloodflow
        p.flowRate[PULMONARY_ARTERIAL_CPI] = (p.pressure[PULMONARY_ARTERIAL_CPI] - p.pressure[PULMONARY_VENOUS_CPI]) / theta.get(PVName.PULM_MICRO_RESISTANCE);

        // Left ventricular inflow
        if (p.pressure[PULMONARY_VENOUS_CPI] > p.pressure[LEFT_VENTRICULAR_CPI]) {
            p.flowRate[PULMONARY_VENOUS_CPI] = (p.pressure[PULMONARY_VENOUS_CPI] - p.pressure[LEFT_VENTRICULAR_CPI]) / theta.get(PVName.PULM_VEN_RESISTANCE);
        } else {
            p.flowRate[PULMONARY_VENOUS_CPI] = 0.0;
        }

        // Computing the pressure derivatives based on the flows and compliance
        // values at the current time step.
        p.dPressureDt[LEFT_VENTRICULAR_CPI] = ((p.pressure[INTRA_THORACIC_CPI] - p.pressure[LEFT_VENTRICULAR_CPI]) * p.dComplianceDt[RIGHT_VENTRICULAR_CPI] + p.flowRate[PULMONARY_VENOUS_CPI] - p.flowRate[LEFT_VENTRICULAR_CPI])
                / p.compliance[RIGHT_VENTRICULAR_CPI] + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[ARTERIAL_CPI] = (p.flowRate[LEFT_VENTRICULAR_CPI] - p.flowRate[ARTERIAL_CPI]) / theta.get(PVName.ART_COMPLIANCE);
        p.dPressureDt[CENTRAL_VENOUS_CPI] = (p.flowRate[ARTERIAL_CPI] - p.flowRate[CENTRAL_VENOUS_CPI]) / theta.get(PVName.VEN_COMPLIANCE);
        p.dPressureDt[RIGHT_VENTRICULAR_CPI] = ((p.pressure[INTRA_THORACIC_CPI] - p.pressure[RIGHT_VENTRICULAR_CPI]) * p.dComplianceDt[ARTERIAL_CPI] + p.flowRate[CENTRAL_VENOUS_CPI] - p.flowRate[RIGHT_VENTRICULAR_CPI])
                / p.compliance[ARTERIAL_CPI] + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[PULMONARY_ARTERIAL_CPI] = (p.flowRate[RIGHT_VENTRICULAR_CPI] - p.flowRate[PULMONARY_ARTERIAL_CPI]) / theta.get(PVName.PULM_ART_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];
        p.dPressureDt[PULMONARY_VENOUS_CPI] = (p.flowRate[PULMONARY_ARTERIAL_CPI] - p.flowRate[PULMONARY_VENOUS_CPI]) / theta.get(PVName.PULM_VEN_COMPLIANCE) + p.dPressureDt[INTRA_THORACIC_CPI];

        // Computing the compartmental volumes.
        p.volume[LEFT_VENTRICULAR_CPI] = (p.pressure[LEFT_VENTRICULAR_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[RIGHT_VENTRICULAR_CPI] + theta.get(PVName.LV_ZPFV);
        p.volume[ARTERIAL_CPI] = p.pressure[ARTERIAL_CPI] * theta.get(PVName.ART_COMPLIANCE) + theta.get(PVName.SYSTEMIC_ART_ZPFV);
        p.volume[CENTRAL_VENOUS_CPI] = p.pressure[CENTRAL_VENOUS_CPI] * theta.get(PVName.VEN_COMPLIANCE) + theta.get(PVName.SYSTEMIC_VEN_ZPFV);
        p.volume[RIGHT_VENTRICULAR_CPI] = (p.pressure[RIGHT_VENTRICULAR_CPI] - p.pressure[INTRA_THORACIC_CPI]) * p.compliance[ARTERIAL_CPI] + theta.get(PVName.RV_ZPFV);
        p.volume[PULMONARY_ARTERIAL_CPI] = (p.pressure[PULMONARY_ARTERIAL_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.PULM_ART_COMPLIANCE) + theta.get(PVName.PULM_ART_ZPFV);
        p.volume[PULMONARY_VENOUS_CPI] = (p.pressure[PULMONARY_VENOUS_CPI] - p.pressure[INTRA_THORACIC_CPI]) * theta.get(PVName.PULM_VEN_COMPLIANCE) + theta.get(PVName.PULM_VEN_ZPFV);
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
// THIS FUNCTION DOESN'T EVEN DO ANYTHING!!
    public static int fixvolume_ptr(Data_vector p, Reflex_vector ref, Parameter_vector theta) {

        // This function doesn't do anthing, so just return 0 right away!
        return 0;

        // JLL The rest of the function is commented out because of the above line
/*
         * double diff = 0.0;
         *
         * diff = ((p.x[0]-p.x[24])*p.c[3] + (p.x[3]-p.x[24])*p.c[1] +
         * (p.x[4]-p.x[24])*theta.get(PVName.PV47) +
         * (p.x[5]-p.x[24])*theta.get(PVName.PV48) +
         * p.x[1]*theta.get(PVName.PV153) + p.x[2]*theta.get(PVName.PV154)) +
         * theta.get(PVName.PV75) - theta.get(PVName.PV70);
         *
         * // Correct for any difference in blood volume at the inferior vena
         * cava
         * // compartment.
         * // p . x[12] -= diff/theta.get(PVName.PV154);
         *
         * // printf("%e %e %e %e %e\n", p.time[0], p.time[1], p.c[3], p.c[1],
         * diff);
         *
         * return 0;
         */
    }
}
