package jcvsim.backend21compartment;

import java.util.Arrays;

/*
 * Header file for the file simulator.c which is the top-level file for the
 * simulation module.
 *
 * Thomas Heldt March 2nd, 2002
 * Last modified April 11th, 2002
 */
// Converted to Java   Jason Leake December 2016

public class Reflex_vector {

    /*
     * N_COMP defines the number of cardiac chambers affected by the
     * contractility
     * feedback loop; N_RES and N_VOL are the number of compartments affected by
     * the resistance and venous tone feedback loops, respectively. N_HR has
     * three
     * entries: the first one is for storage of the afferent (reflex)
     * instantaneous
     * heart rate signal, the second stores the cummulative heart rate signal
     * in a beat (intra-cardiac cycle time), and the third one stores the actual
     * beat-by-beat heart rate value.
     */
    static private final int N_HR = 4;
    static private final int N_COMP = 2;
    static private final int N_RES = 4;
    static private final int N_VOL = 4;

    /*
     * Definition of the reflex data structure. This structure contains the
     * values
     * of the parameters affected by the reflex response, namely heart rate,
     * right
     * and left ventricular end-systolic compliances, arteriolar resistances,
     * and
     * four systemic zero pressure filling volumes.
     */
    public double hr[];
    public double compliance[];
    public double resistance[];
    public double volume[];
    public int step_cnt;

    public Reflex_vector() {
        hr = new double[N_HR];
        compliance = new double[N_COMP];
        resistance = new double[N_RES];
        volume = new double[N_VOL];
    }

    public void copyFrom(Reflex_vector other) {
        hr = Arrays.copyOf(other.hr, other.hr.length);
        compliance = Arrays.copyOf(other.compliance, other.compliance.length);
        resistance = Arrays.copyOf(other.resistance, other.resistance.length);
        volume = Arrays.copyOf(other.volume, other.volume.length);
        step_cnt = other.step_cnt;

    }

    public Reflex_vector(Reflex_vector other) {
        super();
        copyFrom(other);
    }
}
