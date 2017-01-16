package jcvsim.backend21compartment;


/*
 * This file was created in order to run the simulation from the Java GUI.
 * The functions in this file combine the main() function in main.c and
 * the simulator() function in simulator.c. The simulation was broken up into
 * into three parts: an init_sim() function to initialize the simulation,
 * a step_sim() function to advance the simulation one timestep, and a
 * reset_sim()
 * function to reset variables to their initial values. An output struct was
 * also
 * created to consolidate all the simulation output variables in one place.
 *
 * Catherine Dunn July 10, 2006
 * Last modified July 10, 2006
 */
// Converted to Java Jason Leake December 2016
public class Output {

    public double time;

    public double ascendingAorticPressure;
    public double brachiocephalicArterialPressure;
    public double upperBodyArterialPressure;
    public double upperBodyVenousPressure;
    public double superiorVenaCavaPressure;
    public double thoracicAorticPressure;
    public double abdominalAorticPressure;
    public double renalArterialPressure;
    public double renalVenousPressure;
    public double splanchnicArterialPressure;
    public double splanchnicVenousPressure;
    public double lowerBodyArterialPressure;
    public double lowerBodyVenousPressure;
    public double abdominalVenousPressure;
    public double inferiorVenaCavaPressure;
    public double rightAtrialPressure;
    public double rightVentricularPressure;
    public double pulmonaryArterialPressure;
    public double pulmonaryVenousPressure;
    public double leftAtrialPressure;
    public double leftVentricularPressure;

    public double ascendingAorticFlow;
    public double brachiocephalicArterialFlow;
    public double upperBodyArterialFlow;
    public double upperBodyVenousFlow;
    public double superiorVenaCavaFlow;
    public double thoracicAorticFlow;
    public double abdominalAorticFlow;
    public double renalArterialFlow;
    public double renalVenousFlow;
    public double splanchnicArterialFlow;
    public double splanchnicVenousFlow;
    public double lowerBodyArterialFlow;
    public double lowerBodyVenousFlow;
    public double abdominalVenousFlow;
    public double inferiorVenaCavaFlow;
    public double rightAtrialFlow;
    public double rightVentricularFlow;
    public double pulmonaryArterialFlow;
    public double pulmonaryVenousFlow;
    public double leftAtrialFlow;
    public double leftVentricularFlow;

    public double ascendingAorticVolume;
    public double brachiocephalicArterialVolume;
    public double upperBodyArterialVolume;
    public double upperBodyVenousVolume;
    public double superiorVenaCavaVolume;
    public double thoracicAorticVolume;
    public double abdominalAorticVolume;
    public double renalArterialVolume;
    public double renalVenousVolume;
    public double splanchnicArterialVolume;
    public double splanchnicVenousVolume;
    public double lowerBodyArterialVolume;
    public double lowerBodyVenousVolume;
    public double abdominalVenousVolume;
    public double inferiorVenaCavaVolume;
    public double rightAtrialVolume;
    public double rightVentricularVolume;
    public double pulmonaryArterialVolume;
    public double pulmonaryVenousVolume;
    public double leftAtrialVolume;
    public double leftVentricularVolume;

    public double HR;  // heart rate (reflex.hr[2])
    public double AR;  // arterial resistance (reflex.resistance[0])
    public double VT;  // venous tone (reflex.volume[0])
    public double RVC; // right ventricle contractility (reflex.compliance[0])
    public double LVC; // left ventricle contractility (reflex.compliance[1]) 

    public double totalBloodVolume; // total blood volume
    public double intraThoracicPressure; // intrathoracic pressure

    public double tiltAngle; // tilt angle

}
