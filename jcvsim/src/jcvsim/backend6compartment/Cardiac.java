package jcvsim.backend6compartment;

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

/*
 * Definition of the compartment structure which is used to store the nominal
 * parameter values and their variances for the four physical parameters
 * describing each compartment: compliance, volume, resistance, and length.
 * Entries v[2][.] contain the volume limits and their variances for the
 * non-linear compartments; otherwise they are set to zero.
 */
public class Cardiac {

    public double[][] c_sys;
    public double[][] c_dias;
    public double[][] v;
    public double[][] r;

    public Cardiac() {
        c_sys = new double[2][];
        c_dias = new double[2][];
        v = new double[2][];
        r = new double[1][];

        c_sys[0] = new double[4];
        c_sys[1] = new double[4];
        c_dias[0] = new double[4];
        c_dias[1] = new double[4];
        v[0] = new double[4];
        v[1] = new double[4];
        r[0] = new double[4];
    }
}
