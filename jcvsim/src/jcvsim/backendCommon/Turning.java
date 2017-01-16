package jcvsim.backendCommon;

/*
 * This file contains functions to implement the turning-point data
 * compression algorithm. By varying the dataCompressionFactor the data
 * can be compressed before being passed to the GUI. A dataCompressionFactor
 * of 10 means one piece of output data is passed to the GUI for every 10
 * timesteps.
 * A dataCompressionFactor of 1 means every piece of data is passed to the GUI.
 * (One piece of data for every timestep.)
 *
 * Catherine Dunn July 10, 2006
 * Last modified July 10, 2006
 */
// Converted to Java Jason Leake December 2016
public class Turning {

// Simple function to determine whether the slope between two points
// is positive, negative, or zero.
    public static int slope(double pt1, double pt2) {

        // if pt2 > pt1 return 1
        if (pt2 > pt1) {
            return 1;
        } // if pt1 > pt2 return -1
        else if (pt1 > pt2) {
            return -1;
        } // if pt1 = pt2, return 0 
        else if (pt1 == pt2) {
            return 0;
        }

        // This can/should never happen!
        System.out.println("Impossible state in slope()");
        System.out.println("Taking difference between " + pt1 + " and " + pt2);
        return 0;
    }

// Function to implement the turning-point data compression algorithm
    public static double turning(double d[], int dataCompressionFactor) {

        double pt1, pt2;
        int[] slopes = new int[dataCompressionFactor - 1];
        int i;

        // Determine if the slope between each pair of points 
        // is positive, negative, or zero
        for (i = 0; i < dataCompressionFactor - 1; i++) {
            pt1 = d[i];
            pt2 = d[i + 1];
            slopes[i] = slope(pt1, pt2);
        }

        // if all the slopes are positive, take the last point
        // if all the slopes are negative, take the last point
        // if there is peak, take the extremum
        //
        // for ( i=0; i < dataCompressionFactor-1; i++ ) { - This is wrong - range should be to -2
        for (i = 0; i < dataCompressionFactor - 2; i++) {
            if ((slopes[i] - slopes[i + 1]) != 0) {
                return d[i + 1];
            }
        }

        return d[dataCompressionFactor - 1];

    }
}
