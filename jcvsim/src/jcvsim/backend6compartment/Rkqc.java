package jcvsim.backend6compartment;

import static jcvsim.backend6compartment.Data_vector.CompartmentIndexes.*;
import static jcvsim.backendCommon.Maths.exp;
import static jcvsim.backendCommon.Maths.fabs;
import static jcvsim.backendCommon.Maths.log;
import static jcvsim.backendCommon.Maths.pow;

/*
 * This file contains the two routines needed to advance the solution of the
 * differential equations by one step. We are currently using a fourth-order
 * Runge-Kutta adaptive stepsize integration routine adapted from the Numerical
 * Recipes in C.
 *
 * Thomas Heldt January 31st, 2002
 * Last modified July 13th, 2002
 */
// Converted to Java Jason Leake December 2016
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
// Modifies ONLY Reflex_vector
    public static void rk4_ptr(Data_vector q, Reflex_vector r, Parameter_vector c, double h) {

        Reflex_vector t = new Reflex_vector(r);
        Reflex_vector s = new Reflex_vector(r);       // temporary reflex vectors
        Data_vector q_local = new Data_vector(q);
        Data_vector p_local = new Data_vector(q);  // temporary data vectors

        double hh = h * 0.5, h6 = h / 6.0;          // see NRC for explanation of fourth-
        // order RK integration algorithm

        //  printf("Entering rk4\n");
        //  printf("%e %e %e %e %e\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        // Take first step of size hh to the mid-point of the interval.
        q_local.pressure[LEFT_VENTRICULAR_CPI] += hh * q.dPressureDt[LEFT_VENTRICULAR_CPI];
        q_local.pressure[ARTERIAL_CPI] += hh * q.dPressureDt[ARTERIAL_CPI];
        q_local.pressure[CENTRAL_VENOUS_CPI] += hh * q.dPressureDt[CENTRAL_VENOUS_CPI];
        q_local.pressure[RIGHT_VENTRICULAR_CPI] += hh * q.dPressureDt[RIGHT_VENTRICULAR_CPI];
        q_local.pressure[PULMONARY_ARTERIAL_CPI] += hh * q.dPressureDt[PULMONARY_ARTERIAL_CPI];
        q_local.pressure[PULMONARY_VENOUS_CPI] += hh * q.dPressureDt[PULMONARY_VENOUS_CPI];

        // Check whether a new beat should be initiated given that we have propagated
        // the pressure vector over the time interval hh.
        q_local.time[1] = Reflex.sanode(q_local, s, c, hh);

        // Update the time-varying elastance values and their derivatives.
        Equation.elastance_ptr(q_local, s, c);

        // Update the derivatives for the current copy of the pressure vector.
        Equation.eqns_ptr(q_local, c, s);

        //  printf("%e %e %e %e %e\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        // Take second step (also of size hh) to the mid-point of the interval.
        q_local.pressure[LEFT_VENTRICULAR_CPI] = q.pressure[LEFT_VENTRICULAR_CPI] + hh * q_local.dPressureDt[LEFT_VENTRICULAR_CPI];
        q_local.pressure[ARTERIAL_CPI] = q.pressure[ARTERIAL_CPI] + hh * q_local.dPressureDt[ARTERIAL_CPI];
        q_local.pressure[CENTRAL_VENOUS_CPI] = q.pressure[CENTRAL_VENOUS_CPI] + hh * q_local.dPressureDt[CENTRAL_VENOUS_CPI];
        q_local.pressure[RIGHT_VENTRICULAR_CPI] = q.pressure[RIGHT_VENTRICULAR_CPI] + hh * q_local.dPressureDt[RIGHT_VENTRICULAR_CPI];
        q_local.pressure[PULMONARY_ARTERIAL_CPI] = q.pressure[PULMONARY_ARTERIAL_CPI] + hh * q_local.dPressureDt[PULMONARY_ARTERIAL_CPI];
        q_local.pressure[PULMONARY_VENOUS_CPI] = q.pressure[PULMONARY_VENOUS_CPI] + hh * q_local.dPressureDt[PULMONARY_VENOUS_CPI];

        // Compute the derivatives.
        p_local.set(q_local);
        Equation.eqns_ptr(p_local, c, s);
        p_local.time[1] = q.time[1];
        p_local.time[6] = q.time[6];
        p_local.time_new[0] = q.time_new[0];
        p_local.time_new[5] = q.time_new[5];

        //  printf("%e %e %e %e %e\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        // Take step of size h to the end of the interval.
        p_local.pressure[LEFT_VENTRICULAR_CPI] = q.pressure[LEFT_VENTRICULAR_CPI] + h * p_local.dPressureDt[LEFT_VENTRICULAR_CPI];
        p_local.pressure[ARTERIAL_CPI] = q.pressure[ARTERIAL_CPI] + h * p_local.dPressureDt[ARTERIAL_CPI];
        p_local.pressure[CENTRAL_VENOUS_CPI] = q.pressure[CENTRAL_VENOUS_CPI] + h * p_local.dPressureDt[CENTRAL_VENOUS_CPI];
        p_local.pressure[RIGHT_VENTRICULAR_CPI] = q.pressure[RIGHT_VENTRICULAR_CPI] + h * p_local.dPressureDt[RIGHT_VENTRICULAR_CPI];
        p_local.pressure[PULMONARY_ARTERIAL_CPI] = q.pressure[PULMONARY_ARTERIAL_CPI] + h * p_local.dPressureDt[PULMONARY_ARTERIAL_CPI];
        p_local.pressure[PULMONARY_VENOUS_CPI] = q.pressure[PULMONARY_VENOUS_CPI] + h * p_local.dPressureDt[PULMONARY_VENOUS_CPI];

        q_local.dPressureDt[LEFT_VENTRICULAR_CPI] += p_local.dPressureDt[LEFT_VENTRICULAR_CPI];
        q_local.dPressureDt[ARTERIAL_CPI] += p_local.dPressureDt[ARTERIAL_CPI];
        q_local.dPressureDt[CENTRAL_VENOUS_CPI] += p_local.dPressureDt[CENTRAL_VENOUS_CPI];
        q_local.dPressureDt[RIGHT_VENTRICULAR_CPI] += p_local.dPressureDt[RIGHT_VENTRICULAR_CPI];
        q_local.dPressureDt[PULMONARY_ARTERIAL_CPI] += p_local.dPressureDt[PULMONARY_ARTERIAL_CPI];
        q_local.dPressureDt[PULMONARY_VENOUS_CPI] += p_local.dPressureDt[PULMONARY_VENOUS_CPI];

        p_local.time[1] = Reflex.sanode(p_local, t, c, h);
        Equation.elastance_ptr(p_local, t, c);
        Equation.eqns_ptr(p_local, c, t);

        //  printf("%e %e %e %e %e\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        p_local.pressure[LEFT_VENTRICULAR_CPI] = q.pressure[LEFT_VENTRICULAR_CPI] + h6 * (q.dPressureDt[LEFT_VENTRICULAR_CPI] + p_local.dPressureDt[LEFT_VENTRICULAR_CPI] + 2.0 * q_local.dPressureDt[LEFT_VENTRICULAR_CPI]);
        p_local.pressure[ARTERIAL_CPI] = q.pressure[ARTERIAL_CPI] + h6 * (q.dPressureDt[ARTERIAL_CPI] + p_local.dPressureDt[ARTERIAL_CPI] + 2.0 * q_local.dPressureDt[ARTERIAL_CPI]);
        p_local.pressure[CENTRAL_VENOUS_CPI] = q.pressure[CENTRAL_VENOUS_CPI] + h6 * (q.dPressureDt[CENTRAL_VENOUS_CPI] + p_local.dPressureDt[CENTRAL_VENOUS_CPI] + 2.0 * q_local.dPressureDt[CENTRAL_VENOUS_CPI]);
        p_local.pressure[RIGHT_VENTRICULAR_CPI] = q.pressure[RIGHT_VENTRICULAR_CPI] + h6 * (q.dPressureDt[RIGHT_VENTRICULAR_CPI] + p_local.dPressureDt[RIGHT_VENTRICULAR_CPI] + 2.0 * q_local.dPressureDt[RIGHT_VENTRICULAR_CPI]);
        p_local.pressure[PULMONARY_ARTERIAL_CPI] = q.pressure[PULMONARY_ARTERIAL_CPI] + h6 * (q.dPressureDt[PULMONARY_ARTERIAL_CPI] + p_local.dPressureDt[PULMONARY_ARTERIAL_CPI] + 2.0 * q_local.dPressureDt[PULMONARY_ARTERIAL_CPI]);
        p_local.pressure[PULMONARY_VENOUS_CPI] = q.pressure[PULMONARY_VENOUS_CPI] + h6 * (q.dPressureDt[PULMONARY_VENOUS_CPI] + p_local.dPressureDt[PULMONARY_VENOUS_CPI] + 2.0 * q_local.dPressureDt[PULMONARY_VENOUS_CPI]);

        Equation.eqns_ptr(p_local, c, t);
        p_local.time[0] += h;

        //  printf("%e %e %e %e %e\n\n", q.time[0], q_local.time[1], p_local.time[1], q_local.c[1], p_local.c[1]);
        r.hr[1] = t.hr[1];
        r.step_cnt = t.step_cnt;

        q.set(p_local);
    }

    public static int rkqc_ptr(Data_vector pres, Reflex_vector r, Parameter_vector theta,
            double htry, double eps, double yscal[], double[] hdid,
            double[] hnext) {
        Data_vector p = new Data_vector();
        Data_vector q = new Data_vector();
        Reflex_vector s = new Reflex_vector();
        Reflex_vector t = new Reflex_vector();

        double xsav = 0.0;          // dummy variable to save the initial time
        double hh = 0.0, h = 0.0;   // variables for step-sizes
        double temp = 0.0;          // dummy variable (see below) 
        double errmax = 0.0;        // dummy variable for maximal difference between
        // single step and double step results

        xsav = pres.time[1];
        h = htry;

        Equation.eqns_ptr(pres, theta, r); // Updated dxdt vector needed because of 
        // time-varying volume. Does not affect
        // model performace when bv is constant.

        for (;;) {
            p.set(pres);
            q.set(pres);
            s.set(r);
            t.set(r);
            hh = 0.5 * h;

            //    printf("Taking first step of size: %f\n", hh);
            Rkqc.rk4_ptr(p, s, theta, hh);    // Take first step of size h/2.
            //    printf("Taking second step of size: %f\n", hh);
            Rkqc.rk4_ptr(p, s, theta, hh);    // Take second setp of size hh.

            if (p.time[1] == xsav) {
                break;
            }

            //    printf("Taking step of size: %f\n", h);
            Rkqc.rk4_ptr(q, t, theta, h);     // Take one full step of size h
            errmax = 0.0;

            q.pressure[LEFT_VENTRICULAR_CPI] -= p.pressure[LEFT_VENTRICULAR_CPI];
            if (errmax < (temp = fabs(q.pressure[LEFT_VENTRICULAR_CPI] / yscal[0]))) {
                errmax = temp;
            }
            q.pressure[ARTERIAL_CPI] -= p.pressure[ARTERIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[ARTERIAL_CPI] / yscal[1]))) {
                errmax = temp;
            }
            q.pressure[CENTRAL_VENOUS_CPI] -= p.pressure[CENTRAL_VENOUS_CPI];
            if (errmax < (temp = fabs(q.pressure[CENTRAL_VENOUS_CPI] / yscal[2]))) {
                errmax = temp;
            }
            q.pressure[RIGHT_VENTRICULAR_CPI] -= p.pressure[RIGHT_VENTRICULAR_CPI];
            if (errmax < (temp = fabs(q.pressure[RIGHT_VENTRICULAR_CPI] / yscal[3]))) {
                errmax = temp;
            }
            q.pressure[PULMONARY_ARTERIAL_CPI] -= p.pressure[PULMONARY_ARTERIAL_CPI];
            if (errmax < (temp = fabs(q.pressure[PULMONARY_ARTERIAL_CPI] / yscal[4]))) {
                errmax = temp;
            }
            q.pressure[PULMONARY_VENOUS_CPI] -= p.pressure[PULMONARY_VENOUS_CPI];
            if (errmax < (temp = fabs(q.pressure[PULMONARY_VENOUS_CPI] / yscal[5]))) {
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

        pres.pressure[LEFT_VENTRICULAR_CPI] = p.pressure[LEFT_VENTRICULAR_CPI] += q.pressure[LEFT_VENTRICULAR_CPI] / 15.0;
        pres.pressure[ARTERIAL_CPI] = p.pressure[ARTERIAL_CPI] += q.pressure[ARTERIAL_CPI] / 15.0;
        pres.pressure[CENTRAL_VENOUS_CPI] = p.pressure[CENTRAL_VENOUS_CPI] += q.pressure[CENTRAL_VENOUS_CPI] / 15.0;
        pres.pressure[RIGHT_VENTRICULAR_CPI] = p.pressure[RIGHT_VENTRICULAR_CPI] += q.pressure[RIGHT_VENTRICULAR_CPI] / 15.0;
        pres.pressure[PULMONARY_ARTERIAL_CPI] = p.pressure[PULMONARY_ARTERIAL_CPI] += q.pressure[PULMONARY_ARTERIAL_CPI] / 15.0;
        pres.pressure[PULMONARY_VENOUS_CPI] = p.pressure[PULMONARY_VENOUS_CPI] += q.pressure[PULMONARY_VENOUS_CPI] / 15.0;

        return 0;
    }

}
