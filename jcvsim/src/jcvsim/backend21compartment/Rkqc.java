package jcvsim.backend21compartment;

import static jcvsim.backendCommon.Maths.exp;
import static jcvsim.backendCommon.Maths.fabs;
import static jcvsim.backendCommon.Maths.log;
import static jcvsim.backendCommon.Maths.pow;
import static jcvsim.backend21compartment.Data_vector.CompartmentIndex.*;
import static jcvsim.backend21compartment.Data_vector.TimeIndex.*;

/*
 * This file contains the two routines needed to advance the solution of the
 * differential equations by one step. We are currently using a fourth-order
 * Runge-Kutta adaptive stepsize integration routine adapted from the Numerical
 * Recipes in C.
 *
 * Thomas Heldt January 31st, 2002
 * Last modified July 13th, 2002
 */
// converted to Java Jason Leake December 2016
public class Rkqc {

    private static final double PGROW = -0.20;
    private static final double PSHRNK = -0.25;
    private static final double SAFETY = 0.9;
    private static final double ERRCON = 6.0e-4;


    /*
     * rk4() is a fourth-order Runge Kutta integrator routine that is based on a
     * similar code in the Numerical Recipies for C. When the function is
     * called,
     * on fourth-order step is taken and the values of the dependent variables
     * are
     * returned upon function exit.
     *
     * On entry:
     *
     * q Data_vector
     * q is the data structure that contains the dependent variables as
     * the q.x array, the time index in the q.time array, and the
     * derivative information in the q.dxdt array.
     *
     * r Reflex_vector
     * r contains the information about the state of the reflex system. In
     * particular, it contains the information about the cummulative heart
     * rate and therefore the onset time for the next cardiac beat.
     *
     * c Parameter_vector
     * c contains the values of the parameters which are needed when
     * calling the subroutines eqns() and elastance().
     *
     * h double
     * h is the size of the step to be taken by the integrator.
     *
     *
     * On return:
     *
     * q the values of the dependent variables in the q.x array have been
     * replaced by the values at the time t_0+h where t_0 was the
     * simulation time before the rk4 routine was called.
     *
     * r the cummulative heart rate entry and the step count entry of the
     * reflex vector are updated upon function exit.
     *
     * Thomas Heldt January 20, 2002
     * Last modified July 13th, 2002
     */
    public static void rk4_ptr(Data_vector q, Reflex_vector r, Parameter_vector c, double h,
            boolean tiltTestOn, double tiltStartTime, double tiltStopTime) {
        Reflex_vector t = new Reflex_vector(r);
        Reflex_vector s = new Reflex_vector(r);       // temporary reflex vectors
        Data_vector q_local = new Data_vector(q);
        Data_vector p_local = new Data_vector(q);  // temporary data vectors

        double hh = h * 0.5, h6 = h / 6.0;          // see NRC for explanation of fourth-
        // order RK integration algorithm

        //  printf("Entering rk4\n");
        //  printf("%e %e %e %e %e\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        // Take first step of size hh to the mid-point of the interval.
        q_local.pressure[ASCENDING_AORTIC_CPI] += hh * q.dPressureDt[ASCENDING_AORTIC_CPI];
        q_local.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] += hh * q.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI];
        q_local.pressure[UPPER_BODY_ARTERIAL_CPI] += hh * q.dPressureDt[UPPER_BODY_ARTERIAL_CPI];
        q_local.pressure[UPPER_BODY_VENOUS_CPI] += hh * q.dPressureDt[UPPER_BODY_VENOUS_CPI];
        q_local.pressure[SUPERIOR_VENA_CAVA_CPI] += hh * q.dPressureDt[SUPERIOR_VENA_CAVA_CPI];
        q_local.pressure[THORACIC_AORTIC_CPI] += hh * q.dPressureDt[THORACIC_AORTIC_CPI];
        q_local.pressure[ABDOMINAL_AORTIC_CPI] += hh * q.dPressureDt[ABDOMINAL_AORTIC_CPI];
        q_local.pressure[RENAL_ARTERIAL_CPI] += hh * q.dPressureDt[RENAL_ARTERIAL_CPI];
        q_local.pressure[RENAL_VENOUS_CPI] += hh * q.dPressureDt[RENAL_VENOUS_CPI];
        q_local.pressure[SPLANCHNIC_ARTERIAL_CPI] += hh * q.dPressureDt[SPLANCHNIC_ARTERIAL_CPI];
        q_local.pressure[SPLANCHNIC_VENOUS_CPI] += hh * q.dPressureDt[SPLANCHNIC_VENOUS_CPI];
        q_local.pressure[LBODY_ARTERIAL_CPI] += hh * q.dPressureDt[LBODY_ARTERIAL_CPI];
        q_local.pressure[LBODY_VENOUS_CPI] += hh * q.dPressureDt[LBODY_VENOUS_CPI];
        q_local.pressure[ABDOMINAL_VENOUS_CPI] += hh * q.dPressureDt[ABDOMINAL_VENOUS_CPI];
        q_local.pressure[INFERIOR_VENA_CAVA_CPI] += hh * q.dPressureDt[INFERIOR_VENA_CAVA_CPI];
        q_local.pressure[RIGHT_ATRIAL_CPI] += hh * q.dPressureDt[RIGHT_ATRIAL_CPI];
        q_local.pressure[RIGHT_VENTRICULAR_CPI] += hh * q.dPressureDt[RIGHT_VENTRICULAR_CPI];
        q_local.pressure[PULMONARY_ARTERIAL_CPI] += hh * q.dPressureDt[PULMONARY_ARTERIAL_CPI];
        q_local.pressure[PULMONARY_VENOUS_CPI] += hh * q.dPressureDt[PULMONARY_VENOUS_CPI];
        q_local.pressure[LEFT_ATRIAL_CPI] += hh * q.dPressureDt[LEFT_ATRIAL_CPI];
        q_local.pressure[LEFT_VENTRICULAR_CPI] += hh * q.dPressureDt[LEFT_VENTRICULAR_CPI];

        // Check whether a new beat should be initiated given that we have propagated
        // the pressure vector over the time interval hh.
        q_local.time[CARDIAC_TIME] = Reflex.sanode(q_local, s, c, hh);

        // Update the time-varying elastance values and their derivatives.
        Equation.elastance_ptr(q_local, c);

        // Update the derivatives for the current copy of the pressure vector.
        Equation.eqns_ptr(q_local, c, s, tiltTestOn, tiltStartTime, tiltStopTime);

        //  printf("%e %e %e %e %e\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        // Take second step (also of size hh) to the mid-point of the interval.
        q_local.pressure[ASCENDING_AORTIC_CPI] = q.pressure[ASCENDING_AORTIC_CPI] + hh * q_local.dPressureDt[ASCENDING_AORTIC_CPI];
        q_local.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] = q.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] + hh * q_local.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI];
        q_local.pressure[UPPER_BODY_ARTERIAL_CPI] = q.pressure[UPPER_BODY_ARTERIAL_CPI] + hh * q_local.dPressureDt[UPPER_BODY_ARTERIAL_CPI];
        q_local.pressure[UPPER_BODY_VENOUS_CPI] = q.pressure[UPPER_BODY_VENOUS_CPI] + hh * q_local.dPressureDt[UPPER_BODY_VENOUS_CPI];
        q_local.pressure[SUPERIOR_VENA_CAVA_CPI] = q.pressure[SUPERIOR_VENA_CAVA_CPI] + hh * q_local.dPressureDt[SUPERIOR_VENA_CAVA_CPI];
        q_local.pressure[THORACIC_AORTIC_CPI] = q.pressure[THORACIC_AORTIC_CPI] + hh * q_local.dPressureDt[THORACIC_AORTIC_CPI];
        q_local.pressure[ABDOMINAL_AORTIC_CPI] = q.pressure[ABDOMINAL_AORTIC_CPI] + hh * q_local.dPressureDt[ABDOMINAL_AORTIC_CPI];
        q_local.pressure[RENAL_ARTERIAL_CPI] = q.pressure[RENAL_ARTERIAL_CPI] + hh * q_local.dPressureDt[RENAL_ARTERIAL_CPI];
        q_local.pressure[RENAL_VENOUS_CPI] = q.pressure[RENAL_VENOUS_CPI] + hh * q_local.dPressureDt[RENAL_VENOUS_CPI];
        q_local.pressure[SPLANCHNIC_ARTERIAL_CPI] = q.pressure[SPLANCHNIC_ARTERIAL_CPI] + hh * q_local.dPressureDt[SPLANCHNIC_ARTERIAL_CPI];
        q_local.pressure[SPLANCHNIC_VENOUS_CPI] = q.pressure[SPLANCHNIC_VENOUS_CPI] + hh * q_local.dPressureDt[SPLANCHNIC_VENOUS_CPI];
        q_local.pressure[LBODY_ARTERIAL_CPI] = q.pressure[LBODY_ARTERIAL_CPI] + hh * q_local.dPressureDt[LBODY_ARTERIAL_CPI];
        q_local.pressure[LBODY_VENOUS_CPI] = q.pressure[LBODY_VENOUS_CPI] + hh * q_local.dPressureDt[LBODY_VENOUS_CPI];
        q_local.pressure[ABDOMINAL_VENOUS_CPI] = q.pressure[ABDOMINAL_VENOUS_CPI] + hh * q_local.dPressureDt[ABDOMINAL_VENOUS_CPI];
        q_local.pressure[INFERIOR_VENA_CAVA_CPI] = q.pressure[INFERIOR_VENA_CAVA_CPI] + hh * q_local.dPressureDt[INFERIOR_VENA_CAVA_CPI];
        q_local.pressure[RIGHT_ATRIAL_CPI] = q.pressure[RIGHT_ATRIAL_CPI] + hh * q_local.dPressureDt[RIGHT_ATRIAL_CPI];
        q_local.pressure[RIGHT_VENTRICULAR_CPI] = q.pressure[RIGHT_VENTRICULAR_CPI] + hh * q_local.dPressureDt[RIGHT_VENTRICULAR_CPI];
        q_local.pressure[PULMONARY_ARTERIAL_CPI] = q.pressure[PULMONARY_ARTERIAL_CPI] + hh * q_local.dPressureDt[PULMONARY_ARTERIAL_CPI];
        q_local.pressure[PULMONARY_VENOUS_CPI] = q.pressure[PULMONARY_VENOUS_CPI] + hh * q_local.dPressureDt[PULMONARY_VENOUS_CPI];
        q_local.pressure[LEFT_ATRIAL_CPI] = q.pressure[LEFT_ATRIAL_CPI] + hh * q_local.dPressureDt[LEFT_ATRIAL_CPI];
        q_local.pressure[LEFT_VENTRICULAR_CPI] = q.pressure[LEFT_VENTRICULAR_CPI] + hh * q_local.dPressureDt[LEFT_VENTRICULAR_CPI];

        // Compute the derivatives.
        p_local.copyFrom(q_local);
        Equation.eqns_ptr(p_local, c, s, tiltTestOn, tiltStartTime, tiltStopTime);
        p_local.time[CARDIAC_TIME] = q.time[CARDIAC_TIME];
        p_local.time[VENTRICULAR_TIME] = q.time[VENTRICULAR_TIME];
        p_local.time_new[ABSOLUTE_TIME] = q.time_new[ABSOLUTE_TIME];
        p_local.time_new[MODIFIED_ABSOLUTE_TIME] = q.time_new[MODIFIED_ABSOLUTE_TIME];

        //  printf("%e %e %e %e %e\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        // Take step of size h to the end of the interval.
        p_local.pressure[ASCENDING_AORTIC_CPI] = q.pressure[ASCENDING_AORTIC_CPI] + h * p_local.dPressureDt[ASCENDING_AORTIC_CPI];
        p_local.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] = q.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] + h * p_local.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI];
        p_local.pressure[UPPER_BODY_ARTERIAL_CPI] = q.pressure[UPPER_BODY_ARTERIAL_CPI] + h * p_local.dPressureDt[UPPER_BODY_ARTERIAL_CPI];
        p_local.pressure[UPPER_BODY_VENOUS_CPI] = q.pressure[UPPER_BODY_VENOUS_CPI] + h * p_local.dPressureDt[UPPER_BODY_VENOUS_CPI];
        p_local.pressure[SUPERIOR_VENA_CAVA_CPI] = q.pressure[SUPERIOR_VENA_CAVA_CPI] + h * p_local.dPressureDt[SUPERIOR_VENA_CAVA_CPI];
        p_local.pressure[THORACIC_AORTIC_CPI] = q.pressure[THORACIC_AORTIC_CPI] + h * p_local.dPressureDt[THORACIC_AORTIC_CPI];
        p_local.pressure[ABDOMINAL_AORTIC_CPI] = q.pressure[ABDOMINAL_AORTIC_CPI] + h * p_local.dPressureDt[ABDOMINAL_AORTIC_CPI];
        p_local.pressure[RENAL_ARTERIAL_CPI] = q.pressure[RENAL_ARTERIAL_CPI] + h * p_local.dPressureDt[RENAL_ARTERIAL_CPI];
        p_local.pressure[RENAL_VENOUS_CPI] = q.pressure[RENAL_VENOUS_CPI] + h * p_local.dPressureDt[RENAL_VENOUS_CPI];
        p_local.pressure[SPLANCHNIC_ARTERIAL_CPI] = q.pressure[SPLANCHNIC_ARTERIAL_CPI] + h * p_local.dPressureDt[SPLANCHNIC_ARTERIAL_CPI];
        p_local.pressure[SPLANCHNIC_VENOUS_CPI] = q.pressure[SPLANCHNIC_VENOUS_CPI] + h * p_local.dPressureDt[SPLANCHNIC_VENOUS_CPI];
        p_local.pressure[LBODY_ARTERIAL_CPI] = q.pressure[LBODY_ARTERIAL_CPI] + h * p_local.dPressureDt[LBODY_ARTERIAL_CPI];
        p_local.pressure[LBODY_VENOUS_CPI] = q.pressure[LBODY_VENOUS_CPI] + h * p_local.dPressureDt[LBODY_VENOUS_CPI];
        p_local.pressure[ABDOMINAL_VENOUS_CPI] = q.pressure[ABDOMINAL_VENOUS_CPI] + h * p_local.dPressureDt[ABDOMINAL_VENOUS_CPI];
        p_local.pressure[INFERIOR_VENA_CAVA_CPI] = q.pressure[INFERIOR_VENA_CAVA_CPI] + h * p_local.dPressureDt[INFERIOR_VENA_CAVA_CPI];
        p_local.pressure[RIGHT_ATRIAL_CPI] = q.pressure[RIGHT_ATRIAL_CPI] + h * p_local.dPressureDt[RIGHT_ATRIAL_CPI];
        p_local.pressure[RIGHT_VENTRICULAR_CPI] = q.pressure[RIGHT_VENTRICULAR_CPI] + h * p_local.dPressureDt[RIGHT_VENTRICULAR_CPI];
        p_local.pressure[PULMONARY_ARTERIAL_CPI] = q.pressure[PULMONARY_ARTERIAL_CPI] + h * p_local.dPressureDt[PULMONARY_ARTERIAL_CPI];
        p_local.pressure[PULMONARY_VENOUS_CPI] = q.pressure[PULMONARY_VENOUS_CPI] + h * p_local.dPressureDt[PULMONARY_VENOUS_CPI];
        p_local.pressure[LEFT_ATRIAL_CPI] = q.pressure[LEFT_ATRIAL_CPI] + h * p_local.dPressureDt[LEFT_ATRIAL_CPI];
        p_local.pressure[LEFT_VENTRICULAR_CPI] = q.pressure[LEFT_VENTRICULAR_CPI] + h * p_local.dPressureDt[LEFT_VENTRICULAR_CPI];

        q_local.dPressureDt[ASCENDING_AORTIC_CPI] += p_local.dPressureDt[ASCENDING_AORTIC_CPI];
        q_local.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI] += p_local.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI];
        q_local.dPressureDt[UPPER_BODY_ARTERIAL_CPI] += p_local.dPressureDt[UPPER_BODY_ARTERIAL_CPI];
        q_local.dPressureDt[UPPER_BODY_VENOUS_CPI] += p_local.dPressureDt[UPPER_BODY_VENOUS_CPI];
        q_local.dPressureDt[SUPERIOR_VENA_CAVA_CPI] += p_local.dPressureDt[SUPERIOR_VENA_CAVA_CPI];
        q_local.dPressureDt[THORACIC_AORTIC_CPI] += p_local.dPressureDt[THORACIC_AORTIC_CPI];
        q_local.dPressureDt[ABDOMINAL_AORTIC_CPI] += p_local.dPressureDt[ABDOMINAL_AORTIC_CPI];
        q_local.dPressureDt[RENAL_ARTERIAL_CPI] += p_local.dPressureDt[RENAL_ARTERIAL_CPI];
        q_local.dPressureDt[RENAL_VENOUS_CPI] += p_local.dPressureDt[RENAL_VENOUS_CPI];
        q_local.dPressureDt[SPLANCHNIC_ARTERIAL_CPI] += p_local.dPressureDt[SPLANCHNIC_ARTERIAL_CPI];
        q_local.dPressureDt[SPLANCHNIC_VENOUS_CPI] += p_local.dPressureDt[SPLANCHNIC_VENOUS_CPI];
        q_local.dPressureDt[LBODY_ARTERIAL_CPI] += p_local.dPressureDt[LBODY_ARTERIAL_CPI];
        q_local.dPressureDt[LBODY_VENOUS_CPI] += p_local.dPressureDt[LBODY_VENOUS_CPI];
        q_local.dPressureDt[ABDOMINAL_VENOUS_CPI] += p_local.dPressureDt[ABDOMINAL_VENOUS_CPI];
        q_local.dPressureDt[INFERIOR_VENA_CAVA_CPI] += p_local.dPressureDt[INFERIOR_VENA_CAVA_CPI];
        q_local.dPressureDt[RIGHT_ATRIAL_CPI] += p_local.dPressureDt[RIGHT_ATRIAL_CPI];
        q_local.dPressureDt[RIGHT_VENTRICULAR_CPI] += p_local.dPressureDt[RIGHT_VENTRICULAR_CPI];
        q_local.dPressureDt[PULMONARY_ARTERIAL_CPI] += p_local.dPressureDt[PULMONARY_ARTERIAL_CPI];
        q_local.dPressureDt[PULMONARY_VENOUS_CPI] += p_local.dPressureDt[PULMONARY_VENOUS_CPI];
        q_local.dPressureDt[LEFT_ATRIAL_CPI] += p_local.dPressureDt[LEFT_ATRIAL_CPI];
        q_local.dPressureDt[LEFT_VENTRICULAR_CPI] += p_local.dPressureDt[LEFT_VENTRICULAR_CPI];

        p_local.time[CARDIAC_TIME] = Reflex.sanode(p_local, t, c, h);
        Equation.elastance_ptr(p_local, c);
        Equation.eqns_ptr(p_local, c, t, tiltTestOn, tiltStartTime, tiltStopTime);

        //  printf("%e %e %e %e %e\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        p_local.pressure[ASCENDING_AORTIC_CPI] = q.pressure[ASCENDING_AORTIC_CPI] + h6 * (q.dPressureDt[ASCENDING_AORTIC_CPI] + p_local.dPressureDt[ASCENDING_AORTIC_CPI] + 2.0 * q_local.dPressureDt[ASCENDING_AORTIC_CPI]);
        p_local.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] = q.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] + h6 * (q.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI] + p_local.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI] + 2.0 * q_local.dPressureDt[BRACHIOCEPHALIC_ARTERIAL_CPI]);
        p_local.pressure[UPPER_BODY_ARTERIAL_CPI] = q.pressure[UPPER_BODY_ARTERIAL_CPI] + h6 * (q.dPressureDt[UPPER_BODY_ARTERIAL_CPI] + p_local.dPressureDt[UPPER_BODY_ARTERIAL_CPI] + 2.0 * q_local.dPressureDt[UPPER_BODY_ARTERIAL_CPI]);
        p_local.pressure[UPPER_BODY_VENOUS_CPI] = q.pressure[UPPER_BODY_VENOUS_CPI] + h6 * (q.dPressureDt[UPPER_BODY_VENOUS_CPI] + p_local.dPressureDt[UPPER_BODY_VENOUS_CPI] + 2.0 * q_local.dPressureDt[UPPER_BODY_VENOUS_CPI]);
        p_local.pressure[SUPERIOR_VENA_CAVA_CPI] = q.pressure[SUPERIOR_VENA_CAVA_CPI] + h6 * (q.dPressureDt[SUPERIOR_VENA_CAVA_CPI] + p_local.dPressureDt[SUPERIOR_VENA_CAVA_CPI] + 2.0 * q_local.dPressureDt[SUPERIOR_VENA_CAVA_CPI]);
        p_local.pressure[THORACIC_AORTIC_CPI] = q.pressure[THORACIC_AORTIC_CPI] + h6 * (q.dPressureDt[THORACIC_AORTIC_CPI] + p_local.dPressureDt[THORACIC_AORTIC_CPI] + 2.0 * q_local.dPressureDt[THORACIC_AORTIC_CPI]);
        p_local.pressure[ABDOMINAL_AORTIC_CPI] = q.pressure[ABDOMINAL_AORTIC_CPI] + h6 * (q.dPressureDt[ABDOMINAL_AORTIC_CPI] + p_local.dPressureDt[ABDOMINAL_AORTIC_CPI] + 2.0 * q_local.dPressureDt[ABDOMINAL_AORTIC_CPI]);
        p_local.pressure[RENAL_ARTERIAL_CPI] = q.pressure[RENAL_ARTERIAL_CPI] + h6 * (q.dPressureDt[RENAL_ARTERIAL_CPI] + p_local.dPressureDt[RENAL_ARTERIAL_CPI] + 2.0 * q_local.dPressureDt[RENAL_ARTERIAL_CPI]);
        p_local.pressure[RENAL_VENOUS_CPI] = q.pressure[RENAL_VENOUS_CPI] + h6 * (q.dPressureDt[RENAL_VENOUS_CPI] + p_local.dPressureDt[RENAL_VENOUS_CPI] + 2.0 * q_local.dPressureDt[RENAL_VENOUS_CPI]);
        p_local.pressure[SPLANCHNIC_ARTERIAL_CPI] = q.pressure[SPLANCHNIC_ARTERIAL_CPI] + h6 * (q.dPressureDt[SPLANCHNIC_ARTERIAL_CPI] + p_local.dPressureDt[SPLANCHNIC_ARTERIAL_CPI] + 2.0 * q_local.dPressureDt[SPLANCHNIC_ARTERIAL_CPI]);
        p_local.pressure[SPLANCHNIC_VENOUS_CPI] = q.pressure[SPLANCHNIC_VENOUS_CPI] + h6 * (q.dPressureDt[SPLANCHNIC_VENOUS_CPI] + p_local.dPressureDt[SPLANCHNIC_VENOUS_CPI] + 2.0 * q_local.dPressureDt[SPLANCHNIC_VENOUS_CPI]);
        p_local.pressure[LBODY_ARTERIAL_CPI] = q.pressure[LBODY_ARTERIAL_CPI] + h6 * (q.dPressureDt[LBODY_ARTERIAL_CPI] + p_local.dPressureDt[LBODY_ARTERIAL_CPI] + 2.0 * q_local.dPressureDt[LBODY_ARTERIAL_CPI]);
        p_local.pressure[LBODY_VENOUS_CPI] = q.pressure[LBODY_VENOUS_CPI] + h6 * (q.dPressureDt[LBODY_VENOUS_CPI] + p_local.dPressureDt[LBODY_VENOUS_CPI] + 2.0 * q_local.dPressureDt[LBODY_VENOUS_CPI]);
        p_local.pressure[ABDOMINAL_VENOUS_CPI] = q.pressure[ABDOMINAL_VENOUS_CPI] + h6 * (q.dPressureDt[ABDOMINAL_VENOUS_CPI] + p_local.dPressureDt[ABDOMINAL_VENOUS_CPI] + 2.0 * q_local.dPressureDt[ABDOMINAL_VENOUS_CPI]);
        p_local.pressure[INFERIOR_VENA_CAVA_CPI] = q.pressure[INFERIOR_VENA_CAVA_CPI] + h6 * (q.dPressureDt[INFERIOR_VENA_CAVA_CPI] + p_local.dPressureDt[INFERIOR_VENA_CAVA_CPI] + 2.0 * q_local.dPressureDt[INFERIOR_VENA_CAVA_CPI]);
        p_local.pressure[RIGHT_ATRIAL_CPI] = q.pressure[RIGHT_ATRIAL_CPI] + h6 * (q.dPressureDt[RIGHT_ATRIAL_CPI] + p_local.dPressureDt[RIGHT_ATRIAL_CPI] + 2.0 * q_local.dPressureDt[RIGHT_ATRIAL_CPI]);
        p_local.pressure[RIGHT_VENTRICULAR_CPI] = q.pressure[RIGHT_VENTRICULAR_CPI] + h6 * (q.dPressureDt[RIGHT_VENTRICULAR_CPI] + p_local.dPressureDt[RIGHT_VENTRICULAR_CPI] + 2.0 * q_local.dPressureDt[RIGHT_VENTRICULAR_CPI]);
        p_local.pressure[PULMONARY_ARTERIAL_CPI] = q.pressure[PULMONARY_ARTERIAL_CPI] + h6 * (q.dPressureDt[PULMONARY_ARTERIAL_CPI] + p_local.dPressureDt[PULMONARY_ARTERIAL_CPI] + 2.0 * q_local.dPressureDt[PULMONARY_ARTERIAL_CPI]);
        p_local.pressure[PULMONARY_VENOUS_CPI] = q.pressure[PULMONARY_VENOUS_CPI] + h6 * (q.dPressureDt[PULMONARY_VENOUS_CPI] + p_local.dPressureDt[PULMONARY_VENOUS_CPI] + 2.0 * q_local.dPressureDt[PULMONARY_VENOUS_CPI]);
        p_local.pressure[LEFT_ATRIAL_CPI] = q.pressure[LEFT_ATRIAL_CPI] + h6 * (q.dPressureDt[LEFT_ATRIAL_CPI] + p_local.dPressureDt[LEFT_ATRIAL_CPI] + 2.0 * q_local.dPressureDt[LEFT_ATRIAL_CPI]);
        p_local.pressure[LEFT_VENTRICULAR_CPI] = q.pressure[LEFT_VENTRICULAR_CPI] + h6 * (q.dPressureDt[LEFT_VENTRICULAR_CPI] + p_local.dPressureDt[LEFT_VENTRICULAR_CPI] + 2.0 * q_local.dPressureDt[LEFT_VENTRICULAR_CPI]);

        Equation.eqns_ptr(p_local, c, t, tiltTestOn, tiltStartTime, tiltStopTime);
        p_local.time[ABSOLUTE_TIME] += h;

        //  printf("%e %e %e %e %e\n\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        r.hr[1] = t.hr[1];
        r.step_cnt = t.step_cnt;

        q.copyFrom(p_local);
    }

// Pass *double as single element array - a bit of a bodge!
    public static void rkqc_ptr(Data_vector pres, Reflex_vector r, Parameter_vector theta,
            double htry, double eps, double[] yscal, double[] hdid,
            double[] hnext, boolean tiltTestOn, double tiltStartTime, double tiltStopTime) {
        Data_vector p = new Data_vector();
        Data_vector q = new Data_vector();
        Reflex_vector s = new Reflex_vector();
        Reflex_vector t = new Reflex_vector();

        double xsav = 0.0;          // dummy variable to save the initial time
        double hh = 0.0, h = 0.0;   // variables for step-sizes
        double temp = 0.0;          // dummy variable (see below) 
        double errmax = 0.0;        // dummy variable for maximal difference between
        // single step and double step results

        xsav = pres.time[CARDIAC_TIME];
        h = htry;

        // Updated dxdt vector needed because of time-varying volume. 
        // Does not affect model performace when bv is constant.
        Equation.eqns_ptr(pres, theta, r, tiltTestOn, tiltStartTime, tiltStopTime);

        for (;;) {
            p.copyFrom(pres);
            q.copyFrom(pres);
            s.copyFrom(r);
            t.copyFrom(r);
            hh = 0.5 * h;

            //    printf("Taking first step of size: %f\n", hh);
            // Take first step of size h/2.
            rk4_ptr(p, s, theta, hh, tiltTestOn, tiltStartTime, tiltStopTime);
            //    printf("Taking second step of size: %f\n", hh);
            // Take second setp of size hh.
            rk4_ptr(p, s, theta, hh, tiltTestOn, tiltStartTime, tiltStopTime);

            if (p.time[CARDIAC_TIME] == xsav) {
                break;
            }

            //    printf("Taking step of size: %f\n", h);
            // Take one full step of size h
            rk4_ptr(q, t, theta, h, tiltTestOn, tiltStartTime, tiltStopTime);
            errmax = 0.0;

            q.pressure[ASCENDING_AORTIC_CPI] -= p.pressure[ASCENDING_AORTIC_CPI];
            if (errmax < (temp = fabs(q.pressure[ASCENDING_AORTIC_CPI] / yscal[0]))) {
                errmax = temp;
            }
            q.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] -= p.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] / yscal[1]))) {
                errmax = temp;
            }
            q.pressure[UPPER_BODY_ARTERIAL_CPI] -= p.pressure[UPPER_BODY_ARTERIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[UPPER_BODY_ARTERIAL_CPI] / yscal[2]))) {
                errmax = temp;
            }
            q.pressure[UPPER_BODY_VENOUS_CPI] -= p.pressure[UPPER_BODY_VENOUS_CPI];
            if (errmax < (temp = fabs(q.pressure[UPPER_BODY_VENOUS_CPI] / yscal[3]))) {
                errmax = temp;
            }
            q.pressure[SUPERIOR_VENA_CAVA_CPI] -= p.pressure[SUPERIOR_VENA_CAVA_CPI];
            if (errmax < (temp = fabs(q.pressure[SUPERIOR_VENA_CAVA_CPI] / yscal[4]))) {
                errmax = temp;
            }
            q.pressure[THORACIC_AORTIC_CPI] -= p.pressure[THORACIC_AORTIC_CPI];
            if (errmax < (temp = fabs(q.pressure[THORACIC_AORTIC_CPI] / yscal[5]))) {
                errmax = temp;
            }
            q.pressure[ABDOMINAL_AORTIC_CPI] -= p.pressure[ABDOMINAL_AORTIC_CPI];
            if (errmax < (temp = fabs(q.pressure[ABDOMINAL_AORTIC_CPI] / yscal[6]))) {
                errmax = temp;
            }
            q.pressure[RENAL_ARTERIAL_CPI] -= p.pressure[RENAL_ARTERIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[RENAL_ARTERIAL_CPI] / yscal[7]))) {
                errmax = temp;
            }
            q.pressure[RENAL_VENOUS_CPI] -= p.pressure[RENAL_VENOUS_CPI];
            if (errmax < (temp = fabs(q.pressure[RENAL_VENOUS_CPI] / yscal[8]))) {
                errmax = temp;
            }
            q.pressure[SPLANCHNIC_ARTERIAL_CPI] -= p.pressure[SPLANCHNIC_ARTERIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[SPLANCHNIC_ARTERIAL_CPI] / yscal[9]))) {
                errmax = temp;
            }
            q.pressure[SPLANCHNIC_VENOUS_CPI] -= p.pressure[SPLANCHNIC_VENOUS_CPI];
            if (errmax < (temp = fabs(q.pressure[SPLANCHNIC_VENOUS_CPI] / yscal[10]))) {
                errmax = temp;
            }
            q.pressure[LBODY_ARTERIAL_CPI] -= p.pressure[LBODY_ARTERIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[LBODY_ARTERIAL_CPI] / yscal[11]))) {
                errmax = temp;
            }
            q.pressure[LBODY_VENOUS_CPI] -= p.pressure[LBODY_VENOUS_CPI];
            if (errmax < (temp = fabs(q.pressure[LBODY_VENOUS_CPI] / yscal[12]))) {
                errmax = temp;
            }
            q.pressure[ABDOMINAL_VENOUS_CPI] -= p.pressure[ABDOMINAL_VENOUS_CPI];
            if (errmax < (temp = fabs(q.pressure[ABDOMINAL_VENOUS_CPI] / yscal[13]))) {
                errmax = temp;
            }
            q.pressure[INFERIOR_VENA_CAVA_CPI] -= p.pressure[INFERIOR_VENA_CAVA_CPI];
            if (errmax < (temp = fabs(q.pressure[INFERIOR_VENA_CAVA_CPI] / yscal[14]))) {
                errmax = temp;
            }
            q.pressure[RIGHT_ATRIAL_CPI] -= p.pressure[RIGHT_ATRIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[RIGHT_ATRIAL_CPI] / yscal[15]))) {
                errmax = temp;
            }
            q.pressure[RIGHT_VENTRICULAR_CPI] -= p.pressure[RIGHT_VENTRICULAR_CPI];
            if (errmax < (temp = fabs(q.pressure[RIGHT_VENTRICULAR_CPI] / yscal[16]))) {
                errmax = temp;
            }
            q.pressure[PULMONARY_ARTERIAL_CPI] -= p.pressure[PULMONARY_ARTERIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[PULMONARY_ARTERIAL_CPI] / yscal[17]))) {
                errmax = temp;
            }
            q.pressure[PULMONARY_VENOUS_CPI] -= p.pressure[PULMONARY_VENOUS_CPI];
            if (errmax < (temp = fabs(q.pressure[PULMONARY_VENOUS_CPI] / yscal[18]))) {
                errmax = temp;
            }
            q.pressure[LEFT_ATRIAL_CPI] -= p.pressure[LEFT_ATRIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[LEFT_ATRIAL_CPI] / yscal[19]))) {
                errmax = temp;
            }
            q.pressure[LEFT_VENTRICULAR_CPI] -= p.pressure[LEFT_VENTRICULAR_CPI];
            if (errmax < (temp = fabs(q.pressure[LEFT_VENTRICULAR_CPI] / yscal[20]))) {
                errmax = temp;
            }

            if ((errmax /= eps) <= 1.0) {
                hdid[0] = h;
                hnext[0] = (errmax > ERRCON ? SAFETY * h * pow(errmax, PGROW) : 4.0 * h);
                break;
            }
            h = SAFETY * h * exp(PSHRNK * log(errmax));
            //    printf("Reducing stepsize to: %f\n\n", h);
        }

        pres.pressure[ASCENDING_AORTIC_CPI] = p.pressure[ASCENDING_AORTIC_CPI] += q.pressure[ASCENDING_AORTIC_CPI] / 15.0;
        pres.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] = p.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] += q.pressure[BRACHIOCEPHALIC_ARTERIAL_CPI] / 15.0;
        pres.pressure[UPPER_BODY_ARTERIAL_CPI] = p.pressure[UPPER_BODY_ARTERIAL_CPI] += q.pressure[UPPER_BODY_ARTERIAL_CPI] / 15.0;
        pres.pressure[UPPER_BODY_VENOUS_CPI] = p.pressure[UPPER_BODY_VENOUS_CPI] += q.pressure[UPPER_BODY_VENOUS_CPI] / 15.0;
        pres.pressure[SUPERIOR_VENA_CAVA_CPI] = p.pressure[SUPERIOR_VENA_CAVA_CPI] += q.pressure[SUPERIOR_VENA_CAVA_CPI] / 15.0;
        pres.pressure[THORACIC_AORTIC_CPI] = p.pressure[THORACIC_AORTIC_CPI] += q.pressure[THORACIC_AORTIC_CPI] / 15.0;
        pres.pressure[ABDOMINAL_AORTIC_CPI] = p.pressure[ABDOMINAL_AORTIC_CPI] += q.pressure[ABDOMINAL_AORTIC_CPI] / 15.0;
        pres.pressure[RENAL_ARTERIAL_CPI] = p.pressure[RENAL_ARTERIAL_CPI] += q.pressure[RENAL_ARTERIAL_CPI] / 15.0;
        pres.pressure[RENAL_VENOUS_CPI] = p.pressure[RENAL_VENOUS_CPI] += q.pressure[RENAL_VENOUS_CPI] / 15.0;
        pres.pressure[SPLANCHNIC_ARTERIAL_CPI] = p.pressure[SPLANCHNIC_ARTERIAL_CPI] += q.pressure[SPLANCHNIC_ARTERIAL_CPI] / 15.0;
        pres.pressure[SPLANCHNIC_VENOUS_CPI] = p.pressure[SPLANCHNIC_VENOUS_CPI] += q.pressure[SPLANCHNIC_VENOUS_CPI] / 15.0;
        pres.pressure[LBODY_ARTERIAL_CPI] = p.pressure[LBODY_ARTERIAL_CPI] += q.pressure[LBODY_ARTERIAL_CPI] / 15.0;
        pres.pressure[LBODY_VENOUS_CPI] = p.pressure[LBODY_VENOUS_CPI] += q.pressure[LBODY_VENOUS_CPI] / 15.0;
        pres.pressure[ABDOMINAL_VENOUS_CPI] = p.pressure[ABDOMINAL_VENOUS_CPI] += q.pressure[ABDOMINAL_VENOUS_CPI] / 15.0;
        pres.pressure[INFERIOR_VENA_CAVA_CPI] = p.pressure[INFERIOR_VENA_CAVA_CPI] += q.pressure[INFERIOR_VENA_CAVA_CPI] / 15.0;
        pres.pressure[RIGHT_ATRIAL_CPI] = p.pressure[RIGHT_ATRIAL_CPI] += q.pressure[RIGHT_ATRIAL_CPI] / 15.0;
        pres.pressure[RIGHT_VENTRICULAR_CPI] = p.pressure[RIGHT_VENTRICULAR_CPI] += q.pressure[RIGHT_VENTRICULAR_CPI] / 15.0;
        pres.pressure[PULMONARY_ARTERIAL_CPI] = p.pressure[PULMONARY_ARTERIAL_CPI] += q.pressure[PULMONARY_ARTERIAL_CPI] / 15.0;
        pres.pressure[PULMONARY_VENOUS_CPI] = p.pressure[PULMONARY_VENOUS_CPI] += q.pressure[PULMONARY_VENOUS_CPI] / 15.0;
        pres.pressure[LEFT_ATRIAL_CPI] = p.pressure[LEFT_ATRIAL_CPI] += q.pressure[LEFT_ATRIAL_CPI] / 15.0;
        pres.pressure[LEFT_VENTRICULAR_CPI] = p.pressure[LEFT_VENTRICULAR_CPI] += q.pressure[LEFT_VENTRICULAR_CPI] / 15.0;

    }
}
