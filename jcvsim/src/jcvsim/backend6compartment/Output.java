package jcvsim.backend6compartment;

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
    public double leftVentricularPressure;
    public double arterialPressure;
    public double centralVenousPressure;
    public double rightVentricularPressure;
    public double pulmonaryArterialPressure;
    public double pulmonaryVenousPressure;
    public double leftVentricularFlowRate;
    public double arterialFlowRate;
    public double centralVenousFlowRate;
    public double rightVentricularFlowRate;
    public double pulmonaryArterialFlowRate;
    public double pulmonaryVenousFlowRate;
    public double leftVentricularVolume;
    public double arterialVolume;
    public double centralVenousVolume;
    public double rightVentricularVolume;
    public double pulmonaryArterialVolume;
    public double pulmonaryVenousVolume;
    public double heartRate;  // heart rate (reflex.hr[2])
    public double arteriolarResistance;  // arterial resistance (reflex.resistance[0])
    public double venousTone;  // venous tone (reflex.volume[0])
    public double rightVentricleContractility; // right ventricle contractility (reflex.compliance[0])
    public double leftVentricleContractility; // left ventricle contractility (reflex.compliance[1])
    public double totalBloodVolume; // total blood volume
    public double IntraThoracicPressure; // intrathoracic pressure
}
