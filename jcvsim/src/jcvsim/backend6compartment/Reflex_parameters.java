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
// Converted from C to Java Jason Leake December 2016
public class Reflex_parameters {

    public double[][] set;
    public double[][] rr;
    public double[][] res;
    public double[][] vt;
    public double[][] c;

    public Reflex_parameters() {
        set = new double[2][];
        rr = new double[2][];
        res = new double[4][];
        vt = new double[4][];
        c = new double[2][];

        for (int index = 0; index < 2; index++) {
            set[index] = new double[4];
            rr[index] = new double[4];
            c[index] = new double[4];
        }
        for (int index = 0; index < 4; index++) {
            res[index] = new double[4];
            vt[index] = new double[4];
        }
    }
}
