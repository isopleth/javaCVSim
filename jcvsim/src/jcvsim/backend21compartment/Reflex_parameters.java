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

// These are baroreflex response parameters
public class Reflex_parameters {

    // set[0] is the arterial set-point
    // set[1] is the arterial baroreflex scaling factor
    public double[][] set;
    
    // rr[0] is the RR-sympathetic gain
    // rr[1] is the RR-parasympathetic gain
    public double[][] rr;
    
    // Arterial baroreflex resistance gains in the following compartments
    // res[0][] - upper body arterial compartment
    // res[1][] - kidney arterial compartment
    // res[2][] - splanchnic arterial compartment
    // res[3][] - leg aerterial compartment
    public double[][] res;
    
    // Arterial baroreflex VT gains in the following compartments
    // vt[0][] - upper body arterial compartment
    // vt[1][] - kidney arterial compartment
    // vt[2][] - splanchnic arterial compartment
    // vt[3][] - leg arterial compartment
    public double[][] vt;
    
    // c[0][] - Arterial baroreflex RV contractility gain
    // c[1][] - Arterial baroreflex LV contractility gain
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
