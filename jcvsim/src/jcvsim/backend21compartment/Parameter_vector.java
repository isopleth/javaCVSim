package jcvsim.backend21compartment;

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

// converted to Java Jason Leake December 2016
// This was originally an array with magic number indices; now a hashmap with
// named parameters

public class Parameter_vector extends HashMap<PVName, Double> {

}
