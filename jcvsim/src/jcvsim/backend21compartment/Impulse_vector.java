package jcvsim.backend21compartment;

/*
 * Header file for the file simulator.c which is the top-level file for the
 * simulation module.
 *
 * Thomas Heldt March 2nd, 2002
 * Last modified April 11th, 2002
 */
// Converted to Java   Jason Leake December 2016
public class Impulse_vector {

    /*
     * N_SLENGTH defines the number of entries in the vectors containing the
     * sympathetic and parasympathetic impulse response functions.
     */
    public static final int N_SLENGTH = 960;

    /*
     * Definition of the reflex impulse response function structure.
     */
    public double p[];
    public double s[];
    public double a[];
    public double v[];
    public double cpa[];
    public double cpv[];

    public Impulse_vector() {
        p = new double[N_SLENGTH];
        s = new double[N_SLENGTH];
        a = new double[N_SLENGTH];
        v = new double[N_SLENGTH];
        cpa = new double[N_SLENGTH];
        cpv = new double[N_SLENGTH];
    }

}
