package jcvsim.backend6compartment;

import java.util.HashMap;

/*
 * This is the top-level header file for the entire program. Here we include
 * the standard libraries and define the global structures Parameter_vector
 * and Output_vector which are passed between the simulation and the estimation
 * modules. All other header files in this program include this header file.
 *
 * Thomas Heldt January 25th, 2002
 * Last modified March 27th, 2004
 */

// Converted to Java Jason Leake December 2016
public class Parameter_vector extends HashMap<PVName, Double> {

    /*
     * N_PARAMETER is the number of parameters of the model. It determines the
     * length of the parameter vector defined in the structure Parameter_vector.
     */
    /*
    private static final int N_PARAMETER = 168;
    public double[] vec;

    public Parameter_vector() {
        vec = new double[N_PARAMETER];
    }

    public double getVec(int index) {
        return vec[index];
    }
    */
}
