package jcvsim.backend21compartment;
import static jcvsim.backend21compartment.Data_vector.CompartmentIndex.*;
import static jcvsim.backend21compartment.Data_vector.TimeIndex.*;

/*
 * Simulator.c contains the top-level source code for the simulation routine.
 * The simulation routine reads the parameter vector and returns the
 * simulation output (which currently is an array of three arterial blood
 * pressure numerics [SAP, MAP, DAP], heart rate, and stroke volume) to the
 * main routine.
 *
 * Thomas Heldt January 29th, 2002
 * Last modified: September 28th, 2006
 */
// Converted to Java by Jason Leake December 2016
// This was in simulator.c but has been split out to make the static variables not
// clash with other ones
public class Simulator_numerics_new {

    static double abp_sys = 0.0, abp_mean = 0.0, abp_dias = 0.0;
    static double lvp_sys = 0.0, lvp_ed = 0.0, lvp_md = 0.0;
    static double rvp_sys = 0.0, rvp_ed = 0.0, rvp_md = 0.0;
    static double right_atrium = 0.0, left_atrium = 0.0, periph_vp = 0.0;
    static double pap_sys = 0.0, pap_mean = 0.0, pap_dias = 0.0;
    static double pvp_max = 0.0, pvp_mean = 0.0, pvp_min = 0.0;
    static double sv = 0.0;

    static double dtime = 1.0;

    static void numerics_new_ptr(Data_vector p, Reflex_vector r, double hdid, double[] out_vec) {

        // Initialize static variables between different calls to simulator()
        if (p.time[ABSOLUTE_TIME] < 1.0e-6) {
            abp_sys = abp_mean = 0.0;
            lvp_sys = lvp_ed = 0.0;
            rvp_sys = rvp_ed = 0.0;
            right_atrium = left_atrium = periph_vp = 0.0;
            pap_sys = pap_mean = 0.0;
            sv = 0.0;

            abp_dias = lvp_md = rvp_md = pap_dias = 5000.0;
        }

        if (r.step_cnt == 1) {
            out_vec[0] = abp_sys;
            out_vec[1] = abp_mean / dtime;
            out_vec[2] = abp_dias;

            out_vec[3] = lvp_sys;
            out_vec[4] = lvp_ed;
            out_vec[5] = lvp_md;

            out_vec[6] = rvp_sys;
            out_vec[7] = rvp_ed;
            out_vec[8] = rvp_md;

            out_vec[9] = right_atrium / dtime;
            out_vec[10] = left_atrium / dtime;
            out_vec[11] = periph_vp / dtime;

            out_vec[12] = pap_sys;
            out_vec[13] = pap_mean / dtime;
            out_vec[14] = pap_dias;

            out_vec[15] = sv;
            out_vec[16] = sv * r.hr[2];
            out_vec[17] = r.hr[2];

            out_vec[18] = pvp_max;
            out_vec[19] = pvp_mean / dtime;
            out_vec[20] = pvp_min;

            pap_sys = pap_mean = 0.0;
            abp_sys = abp_mean = 0.0;
            lvp_sys = lvp_ed = 0.0;
            rvp_sys = rvp_ed = 0.0;
            pvp_max = pvp_mean = 0.0;
            sv = 0.0;
            left_atrium = right_atrium = periph_vp = 0.0;

            abp_dias = lvp_md = rvp_md = pap_dias = pvp_min = 5000.0;
            dtime = 0.0;
        } else {
            // Systemic arterial pressure
            if (p.pressure[ASCENDING_AORTIC_CPI] > abp_sys) {
                abp_sys = p.pressure[ASCENDING_AORTIC_CPI];
            }
            if (p.pressure[ASCENDING_AORTIC_CPI] < abp_dias) {
                abp_dias = p.pressure[ASCENDING_AORTIC_CPI];
            }
            abp_mean += p.pressure[ASCENDING_AORTIC_CPI] * hdid;

            // Left ventricular pressure
            if (p.pressure[LEFT_VENTRICULAR_CPI] > lvp_sys) {
                lvp_sys = p.pressure[LEFT_VENTRICULAR_CPI];
            }
            if (p.pressure[LEFT_VENTRICULAR_CPI] < lvp_md) {
                lvp_md = p.pressure[LEFT_VENTRICULAR_CPI];
            }
            if ((r.step_cnt < 200) && (p.dPressureDt[LEFT_VENTRICULAR_CPI] < 20.0)) {
                lvp_ed = p.pressure[LEFT_VENTRICULAR_CPI];
            }

            // Right ventricular pressure
            if (p.pressure[RIGHT_VENTRICULAR_CPI] > rvp_sys) {
                rvp_sys = p.pressure[RIGHT_VENTRICULAR_CPI];
            }
            if (p.pressure[RIGHT_VENTRICULAR_CPI] < rvp_md) {
                rvp_md = p.pressure[RIGHT_VENTRICULAR_CPI];
            }
            if ((r.step_cnt < 200) && (p.dPressureDt[RIGHT_VENTRICULAR_CPI] < 20.0)) {
                rvp_ed = p.pressure[RIGHT_VENTRICULAR_CPI];
            }

            // Mean right and left atrial pressures and mean peripheral venous pressure
            right_atrium += p.pressure[RIGHT_ATRIAL_CPI] * hdid;
            left_atrium += p.pressure[LEFT_ATRIAL_CPI] * hdid;
            periph_vp += p.pressure[UPPER_BODY_VENOUS_CPI] * hdid;

            // Pulmonary artery pressure
            if (p.pressure[PULMONARY_ARTERIAL_CPI] > pap_sys) {
                pap_sys = p.pressure[PULMONARY_ARTERIAL_CPI];
            }
            if (p.pressure[PULMONARY_ARTERIAL_CPI] < pap_dias) {
                pap_dias = p.pressure[PULMONARY_ARTERIAL_CPI];
            }
            pap_mean += p.pressure[PULMONARY_ARTERIAL_CPI] * hdid;

            // Pulmonary artery pressure
            if (p.pressure[PULMONARY_VENOUS_CPI] > pvp_max) {
                pvp_max = p.pressure[PULMONARY_VENOUS_CPI];
            }
            if (p.pressure[PULMONARY_VENOUS_CPI] < pvp_min) {
                pvp_min = p.pressure[PULMONARY_VENOUS_CPI];
            }
            pvp_mean += p.pressure[PULMONARY_VENOUS_CPI] * hdid;

            // Stroke volume
            sv += p.flowRate[BIAS_3_PI] * hdid;

            dtime += hdid;
        }
    }
}
