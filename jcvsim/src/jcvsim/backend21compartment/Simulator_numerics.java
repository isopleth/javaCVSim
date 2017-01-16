package jcvsim.backend21compartment;

import static jcvsim.backend21compartment.Output_vector.N_SAMPLES;
import static jcvsim.backend21compartment.Output_vector.T_BASELINE;
import static jcvsim.backend21compartment.Output_vector.T_SAMP;
import static jcvsim.backend21compartment.Data_vector.CompartmentIndexes.*;
import static jcvsim.backend21compartment.Data_vector.TimeIndexes.*;

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

public class Simulator_numerics {

    /*
     * The following subroutine does two things: (1) it extracts the
     * beat-by-beat
     * numerics for systolic, mean, and diastolic pressure, heart rate, CVP, and
     * stroke volume and (2) is samples these numerics every T_SAMP seconds
     * during
     * the orthostatic stress routine for queuing in the output structure. The
     * numerics are computed on a beat-by-beat basis. Linear interpolation is
     * used
     * to sample values between beats.
     */
    private static int k = 0;
    private static int l = 0;
    private static int m = 0;
    private static int n = 0;
    private static double tmp_map = 0.0, tmp_dap = 500.0, tmp_sap = 0.0;
    private static double tmp_sv = 0.0, tmp_cvp = 0.0;

    private static double sap = 0.0, sap_prev = 0.0;
    private static double map = 0.0, map_prev = 0.0;
    private static double dap = 0.0, dap_prev = 0.0;
    private static double cvp = 0.0, cvp_prev = 0.0;
    private static double sv = 0.0, sv_prev = 0.0;

    private static double sap_time_prev = 0.0, sap_time_next = 0.0, sap_time = 0.0;
    private static double map_time_prev = 0.0, map_time_next = 0.0, map_time = 0.0;
    private static double dap_time_prev = 0.0, dap_time_next = 0.0, dap_time = 0.0;

    private static double prev_time = 0.0, current_time = 0.0;
    private static double cum_dt = 0.0, cum_hr = 500.0;

    private static double onset = 0.0;
    private static double told = 0.0;
    private static double tnew = 0.0;
    private static double T_old = 0.0;
    private static double hr_old = 0.0;

    static void numerics(Data_vector p, Reflex_vector r, Output_vector out, Parameter_vector theta) {

        double dt = 0.0, T_new = 0.0, hr_new = 0.0;

        // Initialize static variables between different calls to simulator.c
        if (p.time[ABSOLUTE_TIME] < 1.0e-6) {
            k = 0;
            l = 0;
            m = 0;
            n = 0;
            T_old = 0.0;
            onset = 0.0;

            sap = sap_prev = tmp_sap = sap_time_prev = sap_time_next = 0.0;
            map = map_prev = tmp_map = map_time_prev = map_time_next = 0.0;
            dap = dap_prev = tmp_dap = dap_time_prev = dap_time_next = 0.0;

            cvp = cvp_prev = tmp_cvp = 0.0;
            sv = sv_prev = tmp_sv = 0.0;
            hr_old = 0.0;

            sap_time = map_time = dap_time = theta.get(PVName.TILT_ONSET_TIME) - T_BASELINE;
            told = tnew = theta.get(PVName.TILT_ONSET_TIME) - T_BASELINE;

            tmp_dap = 500.0;
            cum_hr = 500.0;

            prev_time = current_time = cum_dt = 0.0;
        }

        // Detect onset of a new beat through change in cumulative heart rate signal
        // and compute new instantaneous heart rate signal sampled at 1/T_SAMP Hz.
        if (r.hr[1] < cum_hr) {
            T_new = p.time[ABSOLUTE_TIME];
            hr_new = r.hr[2];

            if ((p.time[ABSOLUTE_TIME] > theta.get(PVName.TILT_ONSET_TIME) - T_BASELINE) && (k < N_SAMPLES)) {
                while (((T_new - told) > T_SAMP) && (k < N_SAMPLES)) {
                    out.hr[k] = (hr_new - hr_old) * (tnew - T_old) / (T_new - T_old) + hr_old;
                    told = tnew;
                    tnew += T_SAMP;
                    k++;
                }
            }

            hr_old = hr_new;
            T_old = T_new;
        }

        // Update the local copy of the cummulative heart rate signal.
        cum_hr = r.hr[1];

        // The following piece of code computes the beat-by-beat numerics from the
        // pulsatile arterial pressure waveform and the stroke volume. It also re-
        // samples the numerics according to linear interpolation for output in the 
        // output array out.
        if (p.pressure[ASCENDING_AORTIC_CPI] > tmp_dap && p.pressure[ASCENDING_AORTIC_CPI] < tmp_sap) {
            current_time = p.time[ABSOLUTE_TIME];
            dt = current_time - prev_time;

            sap_prev = sap;
            map_prev = map;
            dap_prev = dap;
            sv_prev = sv;
            cvp_prev = cvp;

            map = tmp_map / cum_dt;
            sap = tmp_sap;
            dap = tmp_dap;
            sv = tmp_sv;
            cvp = tmp_cvp / cum_dt;

            map_time_prev = map_time_next;
            map_time_next = 0.5 * (dap_time_next + dap_time_prev);

            // Compute the systolic pressure numerics with granularity T_SAMP.
            if ((sap_time_next > theta.get(PVName.TILT_ONSET_TIME) - T_BASELINE) && (l < N_SAMPLES)) {
                out.sap[l] = (sap - sap_prev) * (sap_time - sap_time_prev)
                        / (sap_time_next - sap_time_prev) + sap_prev;

                while (((sap_time_next - sap_time) > T_SAMP) && ((l + 1) < N_SAMPLES)) {
                    sap_time += T_SAMP;
                    l++;
                    out.sap[l] = (sap - sap_prev) * (sap_time - sap_time_prev)
                            / (sap_time_next - sap_time_prev) + sap_prev;
                }
                sap_time += T_SAMP;
                l++;
            }

            // Compute the diastolic pressure numerics with granularity T_SAMP.
            if ((dap_time_next > theta.get(PVName.TILT_ONSET_TIME) - T_BASELINE) && (m < N_SAMPLES)) {
                out.dap[m] = (dap - dap_prev) * (dap_time - dap_time_prev)
                        / (dap_time_next - dap_time_prev) + dap_prev;
                while (((dap_time_next - dap_time) > T_SAMP) && ((m + 1) < N_SAMPLES)) {
                    dap_time += T_SAMP;
                    m++;
                    out.dap[m] = (dap - dap_prev) * (dap_time - dap_time_prev)
                            / (dap_time_next - dap_time_prev) + dap_prev;
                }
                dap_time += T_SAMP;
                m++;
            }

            // Compute the mean pressure and stroke volume numerics with granularity
            // T_SAMP. The value representing each beat is assigned to the midpoint
            // of the beat.
            if ((map_time_next > theta.get(PVName.TILT_ONSET_TIME) - T_BASELINE) && (n < N_SAMPLES)) {
                out.map[n] = (map - map_prev) * (map_time - map_time_prev)
                        / (map_time_next - map_time_prev) + map_prev;
                out.cvp[n] = (cvp - cvp_prev) * (map_time - map_time_prev)
                        / (map_time_next - map_time_prev) + cvp_prev;
                out.sv[n] = (sv - sv_prev) * (map_time - map_time_prev)
                        / (map_time_next - map_time_prev) + sv_prev;
                while (((map_time_next - map_time) > T_SAMP) && ((n + 1) < N_SAMPLES)) {
                    map_time += T_SAMP;
                    n++;
                    out.map[n] = (map - map_prev) * (map_time - map_time_prev)
                            / (map_time_next - map_time_prev) + map_prev;
                    out.cvp[n] = (cvp - cvp_prev) * (map_time - map_time_prev)
                            / (map_time_next - map_time_prev) + cvp_prev;
                    out.sv[n] = (sv - sv_prev) * (map_time - map_time_prev)
                            / (map_time_next - map_time_prev) + sv_prev;
                }
                map_time += T_SAMP;
                n++;
            }

            // Start search for onset of next beat.
            tmp_map = p.pressure[ASCENDING_AORTIC_CPI] * dt;
            tmp_cvp = (p.pressure[INFERIOR_VENA_CAVA_CPI] - theta.get(PVName.INTRA_THORACIC_PRESSURE)) * dt;
            tmp_sv = p.flowRate[ASCENDING_AORTIC_CPI] * dt;

            tmp_dap = 500.0;
            tmp_sap = 0.0;
            cum_dt = 0.0;
            onset = p.time[ABSOLUTE_TIME];

            dap_time_prev = dap_time_next;
            sap_time_prev = sap_time_next;
        } // Integrate the blood pressure and left ventricular outflow signals; also
        // detect highest and lowest values in blood pressure signal for systolic and
        // diastolic pressure detection along with the time of occurrence.
        else {
            current_time = p.time[ABSOLUTE_TIME];
            dt = current_time - prev_time;

            tmp_map += p.pressure[ASCENDING_AORTIC_CPI] * dt;
            tmp_cvp += (p.pressure[INFERIOR_VENA_CAVA_CPI] - theta.get(PVName.INTRA_THORACIC_PRESSURE)) * dt;
            tmp_sv += p.flowRate[ASCENDING_AORTIC_CPI] * dt;

            if (p.pressure[ASCENDING_AORTIC_CPI] < tmp_dap) {
                tmp_dap = p.pressure[ASCENDING_AORTIC_CPI];
                dap_time_next = current_time;
            }

            if (p.pressure[ASCENDING_AORTIC_CPI] > tmp_sap) {
                tmp_sap = p.pressure[ASCENDING_AORTIC_CPI];
                sap_time_next = current_time;
                tmp_dap = 500.0;
            }
        }

        // Update timing variables.
        cum_dt += current_time - prev_time;
        prev_time = current_time;

    }

}
