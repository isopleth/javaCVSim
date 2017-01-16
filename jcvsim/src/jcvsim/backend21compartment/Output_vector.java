package jcvsim.backend21compartment;

/*
 * This is the top-level header file for the entire program. Here we include
 * the standard libraries and define the global structures Parameter_vector
 * and Output_vector which are passed between the simulation and the estimation
 * modules. All other header files in this program include this header file.
 *
 * Thomas Heldt January 25th, 2002
 * Last modified March 27th, 2004
 */
// Convert to Java Jason Leake December 2016 
public class Output_vector {

    /*
     * The simulation subroutine generates observables (pressures, flows, etc.)
     * over hundreds of seconds at a granularity of the integration stepsize.
     * The
     * following definitions pertain to the sampling of these high-resolution
     * data
     * streams and the storage of these samples in the output structure (which
     * is
     * defined below).
     *
     * T_BASELINE is the amount of time (in seconds) of supine baseline included
     * in
     * the output stream prior to initiation of the orthostatic stress.
     * T_TRANSIENT
     * is the amount of time (in seconds) after onset of orthostatic stress
     * included in the output stream. T_SAMP is the sampling period with which
     * the
     * observables are being sampled. Finally, N_SAMPLES is the number of
     * samples
     * in the output stream. It is given by (T_BASELINE+T_TRANSIENT)/T_SAMP.
     * Currently, we sample at 2 Hz so we get 261 samples for 130 seconds of
     * data
     * (including the initial point corresponding to time zero).
     */
    static final double T_BASELINE = 60.0;
    static final double T_TRANSIENT = 240.0;
    static final double T_SAMP = 0.5;

    static final int N_SAMPLES = (int) ((T_BASELINE + T_TRANSIENT) / T_SAMP);
//#define N_SAMPLES (int) ((T_BASELINE + T_TRANSIENT)/T_SAMP)

    /*
     * Definition of the output vector structure to be returned as the final
     * output
     * from the simulation routine. It currently contains heart rate, systolic,
     * mean, and diastolic arterial pressures, and stroke volume.
     */
    public double[] hr;     // Heart rate
    public double[] sap;    // Systolic arterial pressure
    public double[] map;    // Mean arterial pressure
    public double[] dap;    // Diastolic arterial pressure
    public double[] cvp;    // Central venous pressure?
    public double[] sv;     // Cardiac stroke volume - left ventricle?

    public Output_vector() {
        hr = new double[N_SAMPLES];
        sap = new double[N_SAMPLES];
        map = new double[N_SAMPLES];
        dap = new double[N_SAMPLES];
        cvp = new double[N_SAMPLES];
        sv = new double[N_SAMPLES];
    }

}
