package jcvsim.backendCommon;

import static java.lang.Double.isNaN;

/**
 * This class provides maths routines. It is here so than NaN etc errors can
 * be detected on values returned from Java Math routines. Functions which can
 * take any number as input will still return NaN if presented with NaN, so it
 * it worth checking this for all of them.
 *
 * @author Jason Leake
 */
public class Maths {

    public static final double fabs(double value) {
        double returnValue = Math.abs(value);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in fabs - input value " + value);
        }
        return returnValue;
    }

    public static double pow(double value1, double value2) {
        double returnValue = Math.pow(value1, value2);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in pow - input value "
                    + value1 + " and " + value2);
        }
        return returnValue;
    }

    public static double log(double value) {
        double returnValue = Math.log(value);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in log - input value " + value);
        }
        return returnValue;
    }

    public static double exp(double value) {
        double returnValue = Math.exp(value);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in exp - input value " + value);
        }
        return returnValue;
    }

    public static final double sqrt(double value) {
        double returnValue = Math.sqrt(value);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in sqrt - input value " + value);
        }
        return returnValue;
    }

    public static final double atan(double value) {
        double returnValue = Math.atan(value);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in atan - input value " + value);
        }
        return returnValue;
    }

    public static final double rint(double value) {
        double returnValue = Math.round(value);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in round - input value " + value);
        }
        return returnValue;
    }

    public static final double tan(double radians) {
        double returnValue = Math.tan(radians);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in tan - input value " + radians);
        }
        return returnValue;
    }

    public static final double sin(double radians) {
        double returnValue = Math.sin(radians);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in sin - input value " + radians);
        }
        return returnValue;
    }

    public static final double cos(double radians) {
        double returnValue = Math.cos(radians);
        if (isNaN(returnValue)) {
            System.out.println("isnan error in cos - input value " + radians);
        }
        return returnValue;
    }

}
