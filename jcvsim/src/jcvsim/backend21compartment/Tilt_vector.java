package jcvsim.backend21compartment;

/*
 * Header file for the file simulator.c which is the top-level file for the
 * simulation module.
 *
 * Thomas Heldt March 2nd, 2002
 * Last modified April 11th, 2002
 */

// Converted to Java   Jason Leake December 2016

public class Tilt_vector {

    /*
     * N_FLOWS defines the number of leakage flows in the tilt routine. It is
     * currently set to three for the splanchnic, leg, and abdominal
     * compartments.
     */
    public static final int N_FLOWS = 3;

    /*
     * Definition of the tilt structure. It contains the leakage flow variables
     * that need to be passed from the tilt() or lbnp() routines to the eqns()
     * routine.
     */
    public double[] flow;

    Tilt_vector() {
        flow = new double[N_FLOWS];
    }

}
