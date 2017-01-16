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
 * Structure for the microvascular resistance structure. Nominal values for
 * the microvascular resistances are stored in the first entry while their
 * respective variances are stored in the second column;
 */
public class Micro_r {

    // From reading Initial.java
    // micro_r.r[0][0]          R upper body
    // micro_r.r[1][0]          R kidney compartment
    // micro_r.r[2][0]          R splanchnic compartment
    // micro_r.r[3][0]          R lower body compartment
    // micro_r.r[4][0]          R pulmonary microcirculation
    //
    // Index [][0] is the value for the resistance
    // Index [][1] is the standard error for the resistance
    public double[][] r;

    public Micro_r() {
        r = new double[5][];
        for (int index = 0; index < 5; index++) {
            r[index] = new double[4];
        }
    }
}
