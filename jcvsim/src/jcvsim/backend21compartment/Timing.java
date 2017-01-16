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
// Converted from C to Java Jason Leake December 2016
/*
 * Structures containing the reflex parameters. Reflex contains the set-point
 * values and static gain values. Timing contains the information needed to
 * set up the impulse response functions.
 */

// Baroreflex timing
public class Timing {

    public double[][] para;
    public double[][] beta;
    public double[][] alpha_r;
    public double[][] alpha_v;
    public double[][] alpha_cpr;
    public double[][] alpha_cpv;

    public Timing() {
        para = new double[3][];
        beta = new double[3][];
        alpha_r = new double[3][];
        alpha_v = new double[3][];
        alpha_cpr = new double[3][];
        alpha_cpv = new double[3][];

        for (int index = 0; index < 3; index++) {
            para[index] = new double[4];
            beta[index] = new double[4];
            alpha_r[index] = new double[4];
            alpha_v[index] = new double[4];
            alpha_cpr[index] = new double[4];
            alpha_cpv[index] = new double[4];
        }
    }
}
