package edu.mit.lcp;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import jcvsim.backend6compartment.Output;
import jcvsim.backend6compartment.Main;
import jcvsim.backend6compartment.PVName;
import jcvsim.backend6compartment.Parameter_vector;

public class CSimulation6C extends CSimulation {

    // C structure which holds the results of the last simulation step
    private final Output output;
    
    // Simulation Parameters for the C code
    private final CSimulation6CParameters simParameters;

    public CSimulation6C() {
	super();
	System.out.println("CSimulation6C()");	

	// create a new instance of the output class which provides
	// access to the data structures in the loaded C library
	output = new Output();
      
	simParameters = new CSimulation6CParameters();

	// Create output variable list
	varList = new ArrayList<>();

	varList.add(new SimulationOutputVariable<>
		     (0, "TIME", "Time", "s", "Systemic Parameters", "Time",
		      new Range<>(0.0, 0.0)) );

	// Left Ventricle
	varList.add(new SimulationOutputVariable<>
		     (1, "LVP", "Left Ventricle Pressure", "mmHg", "Left Heart", "Pressure",
		      new Range<>(0.0, 200.0)) ); // X0
	
	varList.add(new SimulationOutputVariable<>
		     (2, "LVQ", "Left Ventricle Flow", "mL/s", "Left Heart", "Flow", 
		      new Range<>(0.0, 1500.0)) ); // Q0

	varList.add(new SimulationOutputVariable<>
		     (3, "LVV", "Left Ventricle Volume", "mL", "Left Heart", "Volume", 
		      new Range<>(0.0, 200.0)) ); // V0

	// Arterial 
	varList.add(new SimulationOutputVariable<>
		     (4, "AP", "Arterial Pressure", "mmHg", "Systemic Arteries", "Pressure",
		      new Range<>(0.0, 200.0)) ); // X1

	varList.add(new SimulationOutputVariable<>
		     (5, "AQ", "Arterial Flow", "mL/s", "Systemic Arteries", "Flow",
		      new Range<>(0.0, 150.0)) ); // Q1

	varList.add(new SimulationOutputVariable<>
		     (6, "AV",  "Arterial Volume", "mL", "Systemic Arteries", "Volume", 
		      new Range<>(0.0, 1250.0)) ); //V1
	
	// Central Venous
	varList.add(new SimulationOutputVariable<>
		     (7, "CVP", "Central Venous Pressure", "mmHg", "Systemic Veins", "Pressure", 
		      new Range<>(0.0, 200.0)) ); // X2
	
	varList.add(new SimulationOutputVariable<>
		     (8, "CVQ", "Central Venous Flow", "mL/s", "Systemic Veins", "Flow", 
		      new Range<>(0.0, 250.0)) ); // Q2

	varList.add(new SimulationOutputVariable<>
		     (9, "CVV", "Central Venous Volume", "mL", "Systemic Veins", "Volume", 
		      new Range<>(0.0, 3500.0)) ); // V2
	
	// Right Ventricle
	varList.add(new SimulationOutputVariable<>
		     (10, "RVP", "Right Ventricle Pressure", "mmHg", "Right Heart", "Pressure", 
		      new Range<>(0.0, 200.0)) ); // X3
	
	varList.add(new SimulationOutputVariable<>
		     (11, "RVQ", "Right Ventricle Flow", "mL/s", "Right Heart", "Flow", 
		      new Range<>(0.0, 1000.0)) ); // Q3
	
	varList.add(new SimulationOutputVariable<>
		     (12, "RVV", "Right Ventricle Volume", "mL", "Right Heart", "Volume",
		      new Range<>(0.0, 200.0)) ); // V3	

	// Pulmonary Arterial
	varList.add(new SimulationOutputVariable<>
		     (13, "PAP", "Pulmonary Arterial Pressure", "mmHg", "Pulmonary Arteries", "Pressure",
		      new Range<>(0.0, 200.0)) ); // X4

	varList.add(new SimulationOutputVariable<>
		     (14, "PAQ", "Pulmonary Arterial Flow", "mL/s", "Pulmonary Arteries", "Flow", 
		      new Range<>(0.0, 225.0)) ); // Q4

	varList.add(new SimulationOutputVariable<>
		     (15, "PAV", "Pulmonary Arterial Volume", "mL", "Pulmonary Arteries", "Volume", 
		      new Range<>(0.0, 225.0)) ); // V4

	
	// Pulmonary Venous
	varList.add(new SimulationOutputVariable<>
		     (16, "PVP", "Pulmonary Venous Pressure", "mmHg", "Pulmonary Veins", "Pressure",
		      new Range<>(0.0, 200.0)) ); // X5
	
	varList.add(new SimulationOutputVariable<>
		     (17, "PVQ", "Pulmonary Venous Flow", "mL/s", "Pulmonary Veins", "Flow", 
		      new Range<>(0.0, 2000.0)) ); // Q5
	
	varList.add(new SimulationOutputVariable<>
		     (18, "PVV", "Pulmonary Venous Volume", "mL", "Pulmonary Veins", "Volume",
		      new Range<>(0.0, 1000.0)) ); // V5
    

	// Reflex Outputs (Controlled Variables)
	varList.add(new SimulationOutputVariable<> 
		    (19, "HR", "Heart Rate", "beats/min", "Systemic Parameters", "Reflex",
		     new Range<>(0.0, 100.0)) ); // Heart Rate
	
	varList.add(new SimulationOutputVariable<> 
		    (20, "AR", "Arteriolar Resistance", "PRU", "Systemic Arteries", "Reflex",
		     new Range<>(0.0, 10.0)) ); // Arterial Resistance
    
	varList.add(new SimulationOutputVariable<>
		    (21, "VT", "Venous Tone", "mL", "Systemic Veins", "Reflex",
		     new Range<>(0.0, 2500.0)) ); // Venous Tone
	
	varList.add(new SimulationOutputVariable<>
		    (22, "RVC", "Right Ventricle Contractility", "mL/mmHg", "Right Heart", "Reflex",
		     new Range<>(0.0, 10.0)) );

	varList.add(new SimulationOutputVariable<>
		    (23, "LVC", "Left Ventricle Contractility", "mL/mmHg", "Left Heart", "Reflex",
		     new Range<>(0.0, 10.0)) );

	// Total Blood Volume 
	varList.add(new SimulationOutputVariable<>
		    (24, "TBV", "Total Blood Volume", "mL", "Systemic Parameters", "Volume",
		     new Range<>(0.0, 5500.0)) );

	varList.add(new SimulationOutputVariable<>
		    (25, "PTH", "Intra-thoracic Pressure", "mmHg", "Systemic Parameters", "Pressure",
		     new Range<>(-10.0, 10.0)) );

    }

    @Override
    public void reset() {
	Main.instance().reset_sim();
    }

    // This method steps the simulation
    @Override
    public void step() {
	    
	// run simulation and get updated model measurements
	Main.instance().step_sim(output, simParameters.getVector(),
			       getDataCompressionFactor(),
			       getABReflex(), 
			       getCPReflex());
	// increment the step count
	steps++;

	// Save the new data
	updateRecorders();

	// Flag that data has changed
	dataChanged();

    } // end step

    @Override
    public double getOutput(int index) {
	double value;
	switch (index) {
	case 0  : value = output.time; break; // Time 
	    // Left Ventrical
	case 1  : value = output.leftVentricularPressure; break;   // LVP
	case 2  : value = output.leftVentricularFlowRate; break;   // LVQ
	case 3  : value = output.leftVentricularVolume; break;   // LVV
	    // Arterial
	case 4  : value = output.arterialPressure; break;   // AP
	case 5  : value = output.arterialFlowRate; break;   // AQ
	case 6  : value = output.arterialVolume; break;   // AV
	    // Central Venous
	case 7  : value = output.centralVenousPressure; break;   // CVP
	case 8  : value = output.centralVenousFlowRate; break;   // CVQ
	case 9  : value = output.centralVenousVolume; break;   // CVV
	    // Right Ventrical
	case 10 : value = output.rightVentricularPressure; break;   // RVP
	case 11 : value = output.rightVentricularFlowRate; break;   // RVQ
	case 12 : value = output.rightVentricularVolume; break;   // RVV
	    // Pulmonary Arterial
	case 13 : value = output.pulmonaryArterialPressure; break;   // PAP
	case 14 : value = output.pulmonaryArterialFlowRate; break;   // PAQ
	case 15 : value = output.pulmonaryArterialVolume; break;   // PAV
	    // Pulmonary Venous
	case 16 : value = output.pulmonaryVenousPressure; break;   // PVP
	case 17 : value = output.pulmonaryVenousFlowRate; break;   // PVQ
	case 18 : value = output.pulmonaryVenousVolume; break;   // PVV

	case 19 : value = output.heartRate; break; // HR
	case 20 : value = output.arteriolarResistance; break; // AR
	case 21 : value = output.venousTone; break; // VT
	case 22 : value = output.rightVentricleContractility; break; // RVC
	case 23 : value = output.leftVentricleContractility; break; // LVC

	case 24 : value = output.totalBloodVolume; break; // TBV
	case 25 : value = output.IntraThoracicPressure; break; // Pth

	default: value = 0.0; break;
	}
	
	return value;
    }

    @Override
    public void updatePressure(int i, double d) {
	Main.instance().updatePressure(i, d);
    }   

    @Override
    public List<Parameter> getParameterList() {
	return simParameters.getParameterList();
    }
    @Override
    public Parameter getParameterByName(String name) {
	return simParameters.getParameterByName(name);
    }

    ///////////////////////////////////////
    ///////////////////////////////////////
    public class CSimulation6CParameters {

	private final Parameter_vector pvec;
	private final List<Parameter> plist;

	CSimulation6CParameters() {
	    pvec = new Parameter_vector();
	    // initialize the simulation with the variables
	    Main.instance().init_sim(pvec);

	    plist = createParameterList();
	}

	public Parameter_vector getVector() {
	    return pvec;
	}
	public List<Parameter> getParameterList() {
	    return plist;
	}
// 	public Parameter getParameterByIndex(int index) {
// 	    for (Parameter p: plist) {
// 		if (p.getIndex() == index)
// 		    return p;
// 	    }
// 	    return null;
// 	}
	public Parameter getParameterByName(String name) {
	    for (Parameter p: plist) {
		if (p.getName().equals(name))
		    return p;
	    }
	    return null;
	}

	// creates and initializes parameters
	private List<Parameter> createParameterList() {
	    
	    // Hello, magic numbers!  
	    
            // The index of each parameter is its location in the 
	    // tmp->vec structure in the mapping() function in initial.c. 
	    // Use the comments in mapping() to locate each parameter.

	    // For example, if you want to know the index of the
	    // pulmonary arterial compliance parameter, look for 
	    // something resembling that description in the comments in the 
	    // mapping function. In this case you will find:

	    // tmp -> vec[47] = (*hemo)[8].c[0][0]; // C pul. arteries

	    // Therefore, 47 is the index for pulmonary arterial compliance.

	    // The min and max values are from the cvlib/parameter.db.safe file
	    // in Tim Davis's code.

	    // Is this a terrible way to do things? Absolutely. But
	    // doing things the right way would require rewriting
	    // a significant part of the C code, and there is not enough 
	    // time to do that.

	    List<Parameter> list = new ArrayList<>();

	    // Compliance
	    list.add(new Parameter6C(pvec, PVName.LV_DIASTOLIC_COMPLIANCE, "Left Heart", "Compliance",
				   "Left Ventricle Diastolic Compliance",
				     "mL/mmHg", 0.2, 20.0));
	
	    list.add(new Parameter6C(pvec, PVName.LV_SYSTOLIC_COMPLIANCE, "Left Heart", "Compliance",
				   "Left Ventricle Systolic Compliance",
				     "mL/mmHg", 0.1, 20.0));
	
	    list.add(new Parameter6C(pvec, PVName.RV_DIASTOLIC_COMPLIANCE, "Right Heart", "Compliance",
				   "Right Ventricle Diastolic Compliance",
				     "mL/mmHg", 0.2, 40.0));
	
	    list.add(new Parameter6C(pvec, PVName.RV_SYSTOLIC_COMPLIANCE, "Right Heart", "Compliance",
				   "Right Ventricle Systolic Compliance",
				     "mL/mmHg", 0.2, 40.0));
	
	    list.add(new Parameter6C(pvec, PVName.PULM_ART_COMPLIANCE, "Pulmonary Arteries", "Compliance",
				   "Pulmonary Arterial Compliance",
				     "mL/mmHg", 0.1, 20.0));
	
	    list.add(new Parameter6C(pvec, PVName.PULM_VEN_COMPLIANCE, "Pulmonary Veins", "Compliance",
				   "Pulmonary Venous Compliance",
				     "mL/mmHg", 0.1, 40.0));
	
	    list.add(new Parameter6C(pvec, PVName.ART_COMPLIANCE, "Systemic Arteries", "Compliance",
				     "Arterial Compliance",
				     "mL/mmHg", 0.1, 20.0));
	
	    list.add(new Parameter6C(pvec, PVName.VEN_COMPLIANCE, "Systemic Veins", "Compliance",
				   "Venous Compliance",
				     "mL/mmHg", 0.1, 500.0));

	    // Resistance
	    list.add(new Parameter6C(pvec, PVName.AORTIC_VALVE_RESISTANCE, "Left Heart",  "Resistance",
				   "Aortic Valve Resistance", 
				     "PRU", 0.001, 2.0));

	    list.add(new Parameter6C(pvec, PVName.PULMONIC_VALVE_RESISTANCE, "Right Heart",  "Resistance",
				   "Pulmonic Valve Resistance",
				     "PRU", 0.001, 2.0));

	    list.add(new Parameter6C(pvec, PVName.PULM_MICRO_RESISTANCE, "Pulmonary Microcirculation", "Resistance",
				     "Pulmonary Microcirculation Resistance",
				     "PRU", 0.01, 2.0));

	    list.add(new Parameter6C(pvec, PVName.PULM_VEN_RESISTANCE, "Pulmonary Veins", "Resistance",
				   "Pulmonary Venous Resistance",
				     "PRU", 0.01, 2.0));

	    list.add(new Parameter6C(pvec, PVName.TOTAL_PERIPHERAL_RESISTANCE, "Systemic Microcirculation", "Resistance",
				   "Total Peripheral Resistance",
				     "PRU", 0.01, 10.0));

	    list.add(new Parameter6C(pvec, PVName.VEN_RESISTANCE, "Systemic Veins", "Resistance",
				   "Venous Resistance",
				     "PRU", 0.01, 2.0));

	    // Systemic
	    list.add(new Parameter6C(pvec, PVName.INTRA_THORACIC_PRESSURE, "Systemic Parameters",  "Pressure",
				   "Intra-thoracic Pressure",
				     "mmHg", -20.0, 20.0));
	    
	    list.add(new Parameter6C(pvec, PVName.TOTAL_BLOOD_VOLUME, "Systemic Parameters",  "Volume",
				   "Total Blood Volume",
				     "mL", 100.0, 10000.0));

	    list.add(new Parameter6C(pvec, PVName.TOTAL_ZPFV, "Systemic Parameters",  "Volume",
				   "Total Zero-Pressure Filling Volume",
				     "mL", 0, 10000.0));

	    list.add(new Parameter6C(pvec, PVName.NOMINAL_HEART_RATE, "Systemic Parameters",  "Heart Rate",
				     "Nominal Heart Rate",
				     "beats/min", 20.0, 250.0));

	    // Control System Parameters

	    // Arterial Baroreflex
	    list.add(new Parameter6C(pvec, PVName.ABR_SET_POINT, "Arterial Baroreflex", "Set Point", 
				     "ABR Set Point",
				     "mmHg", 89.0, 105.0));

	    list.add(new Parameter6C(pvec, PVName.ABR_HR_PARASYMPATHETIC_GAIN, "Arterial Baroreflex", "Gain", 
				   "ABR Heart Rate Parasympathetic Gain",
				     "ms/mmHg", 0.005, 0.017));

	    list.add(new Parameter6C(pvec, PVName.ABR_HR_SYMPATHETIC_GAIN, "Arterial Baroreflex", "Gain", 
				   "ABR Heart Rate Sympathetic Gain",
				     "ms/mmHg", 0.005, 0.017));
	
	    list.add(new Parameter6C(pvec, PVName.ABR_VEN_TONE_SYMPATHETIC_GAIN, "Arterial Baroreflex", "Gain", 
				   "ABR Venous Tone Sympathetic Gain",
				   "mL/mmHg"));

	    list.add(new Parameter6C(pvec, PVName.ABR_ART_RESISTANCE_SYMP_GAIN, "Arterial Baroreflex", "Gain",
				   "ABR Arterial Resistance Sympathetic Gain",
				   "PRU/mmHg"));

	    list.add(new Parameter6C(pvec, PVName.ABR_CONTRACT_RV_SYMP_GAIN, "Arterial Baroreflex", "Gain",
				   "ABR Contractility Right Ventricle Sympathetic Gain",
				     "mL/mmHg^2", 0.007, 0.030));
	
	    list.add(new Parameter6C(pvec, PVName.ABR_CONTRACT_LV_SYMP_GAIN, "Arterial Baroreflex", "Gain",
				   "ABR Contractility Left Ventricle Sympathetic Gain",
				     "mL/mmHg^2", 0.004, 0.014));

	    // Cardiopulmonary Reflex
	    list.add(new Parameter6C(pvec, PVName.CPR_SET_POINT, "Cardiopulmonary Reflex", "Set Point",
				   "CPR Set Point",
				     "mmHg", 4.0, 10.0));

	    list.add(new Parameter6C(pvec, PVName.CPR_VEN_TONE_SYMP_GAIN, "Cardiopulmonary Reflex", "Gain",
				   "CPR Venous Tone Sympathetic Gain",
				   "mL/mmHg"));


	    list.add(new Parameter6C(pvec, PVName.CPR_ART_RESISTANCE_SYMP_GAIN, "Cardiopulmonary Reflex", "Gain",
				   "CPR Arterial Resistance Sympathetic Gain",
				   "PRU/mmHg"));

	    // register listeners for each parameter in the list, so that
	    // we can notify our listeners of parameter changes.
	    for (Parameter p: list) {
		p.addPropertyChangeListener(SimulationParameterChangeListener);
	    }

	    // alphabetize list by Parameter name	    
	    Parameter[] array = new Parameter[list.size()];
	    ComparatorX c = new ComparatorX();
	    list.toArray(array);
	    Arrays.sort(array, c);
	    list = Arrays.asList(array);

	    return list;
	}
    }
}
