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
// converted to Java Jason Leake December 2016
public class System_parameters {

    /*
     * Structure containing the system parameters blood volume, heart rate, and
     * intra-thoracic pressure.
     */
    // These are arrays [1][4]
    // Element [0][0] is the value
    // Element [0][1] is the standard error
    // Element [0][2] is an alternative value, which is set but apparently never used
    // Element [0][3] is another alternative value, which again is set but apparently never used
    public double[][] totalBloodVolume;       // Total system blood volume
    public double[][] nominalHeartRate;       // Heart rate
    public double[][] intraThoracicPressure;      // Intra-thoracic pressure
    public double[][] height;        // Height of body
    public double[][] weight;        // Width of body
    public double[][] bodySurfaceArea;      // Body surface area

    // This is an array [3][4] of timings, presumably from the start
    // of the cardiac contraction cycle
    // As with other 4 element arrays, the first element is the value, the second
    // the standard error and the third and fourth are alternative values, possibly
    // representing different simulated individuals.
    // Only the first value and the standard error is used in the simulation.
    // T[0][] PR-interval
    // T[1][] Arterial systole
    // T[2][] Ventricular systole
    public double[][] T;

    public System_parameters() {

        totalBloodVolume = new double[1][];
        totalBloodVolume[0] = new double[4];
        nominalHeartRate = new double[1][];
        nominalHeartRate[0] = new double[4];
        intraThoracicPressure = new double[1][];
        intraThoracicPressure[0] = new double[4];
        height = new double[1][];
        height[0] = new double[4];
        weight = new double[1][];
        weight[0] = new double[4];
        bodySurfaceArea = new double[1][];
        bodySurfaceArea[0] = new double[4];
        T = new double[3][];
        T[0] = new double[4];
        T[1] = new double[4];
        T[2] = new double[4];
    }
}
