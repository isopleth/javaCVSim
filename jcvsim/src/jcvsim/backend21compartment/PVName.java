package jcvsim.backend21compartment;

/**
 * Attributes in the parameter vector
 *
 * Some abbreviations:
 *
 * ART = Arterial
 * CP = Cardiopulmonary
 * IVC = Inferior vena cava
 * LA = Left Atrium 
 * LBODY = Lower Body
 * LV = Left Ventricle 
 * MICRO = Microcirculation
 * PULM = Pulmonary Arterial
 * PULM = Pulmonary Venous
 * RA = Right Atrium 
 * RV = Right Ventricle
 * SPLAN = Splanchnic
 * SVC = Superior vena cava
 * UBODY = Upper Body
 * VEN = Venous
 * ZPFV = Zero point filling volume
 *
 *
 * PVxx means that they are set but never used.  Some of the names are 
 * 
 * @author Jason Leake
 */
public enum PVName {
    ABR_SET_POINT, // Original C code array index 0
    ABR_SCALING_FACTOR, // Original C code array index 1
    ABR_HR_SYMPATHETIC_GAIN, // Original C code array index 2
    ABR_HR_PARASYMPATHETIC_GAIN, // Original C code array index 3
    ABR_ART_RES_SYMPATHETIC_GAIN_TO_UPPER_BODY, // Original C code array index 4
    ABR_ART_RES_SYMPATHETIC_GAIN_TO_KIDNEY, // Original C code array index 5
    ABR_ART_RES_SYMPATHETIC_GAIN_TO_SPLANCHNIC, // Original C code array index 6
    ABR_ART_RES_SYMPATHETIC_GAIN_TO_LOWER_BODY, // Original C code array index 7
    ABR_VEN_TONE_SYMPATHETIC_GAIN_TO_UPPER_BODY, // Original C code array index 8
    ABR_VEN_TONE_SYMPATHETIC_GAIN_TO_KIDNEY, // Original C code array index 9
    ABR_VEN_TONE_SYMPATHETIC_GAIN_TO_SPLANCHNIC, // Original C code array index 10
    ABR_VEN_TONE_SYMPATHETIC_GAIN_TO_LOWER_BODY, // Original C code array index 11
    ABR_RV_CONTRACTILITY_SYMPATHETIC_GAIN, // Original C code array index 12
    ABR_LV_CONTRACTILITY_SYMPATHETIC_GAIN, // Original C code array index 13
    SENSED_PRESSURE_OFFSET_DURING_TILT, // Original C code array index 14
    CPR_SET_POINT, // Original C code array index 15
    CPR_SCALING_FACTOR, // Original C code array index 16
    CPR_ART_RES_SYMPATHETIC_GAIN_TO_UBODY, // Original C code array index 17
    CPR_ART_RES_SYMPATHETIC_GAIN_TO_KIDNEY, // Original C code array index 18
    CPR_ART_RES_SYMPATHETIC_GAIN_TO_SPLANCHNIC, // Original C code array index 19
    CPR_ART_RES_SYMPATHETIC_GAIN_TO_LBODY, // Original C code array index 20
    CPR_VEN_SYMPATHETIC_GAIN_TO_UBODY, // Original C code array index 21
    CPR_VEN_SYMPATHETIC_GAIN_TO_KIDNEY, // Original C code array index 22
    CPR_VEN_SYMPATHETIC_GAIN_TO_SPLANCHNIC, // Original C code array index 23
    CPR_VEN_SYMPATHETIC_GAIN_TO_LBODY, // Original C code array index 24
    ABR_DELAY_PARASYMP, // Original C code array index 25
    ABR_PEAK_PARASYMP, // Original C code array index 26
    ABR_END_PARASYMP, // Original C code array index 27
    ABR_DELAY_BETA_SYMP, // Original C code array index 28
    ABR_PEAK_BETA_SYMP, // Original C code array index 29
    ABR_END_BETA_SYMP, // Original C code array index 30
    INTRA_THORACIC_PRESSURE, // Original C code array index 31
    PV32, // Original C code array index 32
    PV33, // Original C code array index 33
    PV34, // Original C code array index 34
    PV35, // Original C code array index 35
    UBODY_VEN_COMPLIANCE, // Original C code array index 36
    RENAL_VEN_COMPLIANCE, // Original C code array index 37
    SPLAN_VEN_COMPLIANCE, // Original C code array index 38
    LBODY_VEN_COMPLIANCE, // Original C code array index 39
    ABDOM_VEN_COMPLIANCE, // Original C code array index 40
    IVC_COMPLIANCE, // Original C code array index 41
    SVC_COMPLIANCE, // Original C code array index 42
    RA_DIASTOLIC_COMPLIANCE, // Original C code array index 43
    RA_SYSTOLIC_COMPLIANCE, // Original C code array index 44
    RV_DIASTOLIC_COMPLIANCE, // Original C code array index 45
    RV_SYSTOLIC_COMPLIANCE, // Original C code array index 46
    PULM_ART_COMPLIANCE, // Original C code array index 47
    PULM_VEN_COMPLIANCE, // Original C code array index 48
    LA_DIASTOLIC_COMPLIANCE, // Original C code array index 49
    LA_SYSTOLIC_COMPLIANCE, // Original C code array index 50  
    LV_DIASTOLIC_COMPLIANCE, // Original C code array index 51
    LV_SYSTOLIC_COMPLIANCE, // Original C code array index 52
    UBODY_MICRO_RESISTANCE, // Original C code array index 53
    UBODY_VEN_RESISTANCE, // Original C code array index 54
    RENAL_MICRO_RESISTANCE, // Original C code array index 55
    RENAL_VEN_RESISTANCE, // Original C code array index 56
    SPLAN_MICRO_RESISTANCE, // Original C code array index 57
    SPLAN_VEN_RESISTANCE, // Original C code array index 58
    LBODY_MICRO_RESISTANCE, // Original C code array index 59
    LBODY_VEN_RESISTANCE, // Original C code array index 60
    ABDOM_VEN_RESISTANCE, // Original C code array index 61
    IVC_RESISTANCE, // Original C code array index 62
    SVC_RESISTANCE, // Original C code array index 63
    TRICUSPID_VALVE_RESISTANCE, // Original C code array index 64
    PUMONIC_VALVE_RESISTANCE, // Original C code array index 65
    PULM_MICRO_RESISTANCE, // Original C code array index 66
    PULM_VEN_RESISTANCE, // Original C code array index 67
    MITRAL_VALVE_RESISTANCE, // Original C code array index 68
    AORTIC_VALVE_RESISTANCE, // Original C code array index 69
    TOTAL_BLOOD_VOLUME, // Original C code array index 70
    MAX_INCREASE_IN_SPLAN_DISTENDING_VOL, // Original C code array index 71
    MAX_INCREASE_IN_LEG_DISTENDING_VOL, // Original C code array index 72
    MAX_INCREASE_IN_ABDOM_DISTENDING_VOL, // Original C code array index 73
    MAXIMAL_BLOOD_VOLUME_LOSS_DURINT_TILT, // Original C code array index 74
    PV75, // Original C code array index 75
    PV76, // Original C code array index 76
    UBODY_VEN_ZPFV, // Original C code array index 77
    RENAL_VEN_ZPFV, // Original C code array index 78
    SPLAN_VEN_ZPFV, // Original C code array index 79
    LBODY_VEN_ZPFV, // Original C code array index 80
    ABDOM_VEN_ZPFV, // Original C code array index 81
    IVC_ZPFV, // Original C code array index 82
    SVC_ZPFV, // Original C code array index 83
    RA_ZPFV, // Original C code array index 84
    RV_ZPFV, // Original C code array index 85
    PULM_ART_ZPFV, // Original C code array index 86
    PULN_VEN_ZPFV, // Original C code array index 87
    LA_ZPFV, // Original C code array index 88
    LV_ZPFV, // Original C code array index 89
    NOMINAL_HEART_RATE, // Original C code array index 90
    TILT_ANGLE, // Original C code array index 91
    TIME_TO_MAX_TILT_ANGLE, // Original C code array index 92
    TILT_ONSET_TIME, // Original C code array index 93
    DURATION_IN_UPRIGHT_POSTURE, // Original C code array index 94
    MAXIMAL_EXTERNAL_NEGATIVE_PRESSURE, // Original C code array index 95
    VOLUME_LOSS_DURING_LBNP, // Original C code array index 96
    ASCENDING_AORTA_COMPLIANCE, // Original C code array index 97
    BRACH_ART_COMPLIANCE, // Original C code array index 98
    THORACIC_AORTA_COMPLIANCE, // Original C code array index 99
    UBODY_ART_COMPLIANCE, // Original C code array index 100
    ABDOM_AORTA_COMPLIANCE, // Original C code array index 101
    RENAL_ART_COMPLIANCE, // Original C code array index 102
    SPLAN_ART_COMPLIANCE, // Original C code array index 103
    LBODY_ART_COMPLIANCE, // Original C code array index 104
    BRACH_ART_RESISTANCE, // Original C code array index 105
    UBODY_ART_RESISTANCE, // Original C code array index 106
    THORACIC_AORTA_RESISTANCE, // Original C code array index 107
    ABDOM_AORTA_RESISTANCE, // Original C code array index 108
    RENAL_ART_RESISTANCE, // Original C code array index 109
    SPLAN_ART_RESISTANCE, // Original C code array index 110
    LEG_ART_RESISTANCE, // Original C code array index 111
    ABR_ALPHA_SYMP_VEN_DELAY, // Original C code array index 112
    ABR_ALPHA_SYMP_VEN_PEAK, // Original C code array index 113
    ABR_ALPHA_SYMP_VEN_END, // Original C code array index 114
    ABR_ALPHA_SYMP_ART_DELAY, // Original C code array index 115
    ABR_ALPHA_SYMP_ART_PEAK, // Original C code array index 116
    ABR_ALPHA_SYMP_ART_END, // Original C code array index 117
    PR_INTERVAL, // Original C code array index 118
    ATRIAL_SYSTOLE_INTERVAL, // Original C code array index 119
    VENTRICULAR_SYSTOLE_INTERVAL, // Original C code array index 120
    ASCENDING_AORTA_HEIGHT, // Original C code array index 121
    BRACHIOCEPHAL_ART_HEIGHT, // Original C code array index 122
    UBODY_ART_HEIGHT, // Original C code array index 123
    UBODY_VEN_HEIGHT, // Original C code array index 124
    SVC_HEIGHT, // Original C code array index 125
    DESCENDING_AORTA_HEIGHT, // Original C code array index 126
    ABDOM_AORTA_HEIGHT, // Original C code array index 127
    RENAL_ART_HEIGHT, // Original C code array index 128
    RENAL_VEN_HEIGHT, // Original C code array index 129
    SPLAN_ART_HEIGHT, // Original C code array index 130
    SPLAN_VEIN_HEIGHT, // Original C code array index 131
    LEG_ART_HEIGHT, // Original C code array index 132
    LEG_VEN_HEIGHT, // Original C code array index 133
    ABDOM_IVC_HEIGHT, // Original C code array index 134
    THORACIC_IVC_HEIGHT, // Original C code array index 135
    ASCENDING_AORTA_VOLUME, // Original C code array index 136
    BRACH_ART_ZPFV, // Original C code array index 137
    THORACIC_AORTA_ZPFV, // Original C code array index 138
    UBODY_ART_ZPFV, // Original C code array index 139
    ABDOM_AORTA_ZPFV, // Original C code array index 140
    RENAL_ART_ZPFV, // Original C code array index 141
    SPLAN_ART_ZPFV, // Original C code array index 142
    LBODY_ART_ZPFV, // Original C code array index 143
    PV144, // Original C code array index 144
    PV145, // Original C code array index 145
    PV146, // Original C code array index 146
    ALPHA_CPV_DELAY, // Original C code array index 147
    ALPHA_CPV_PEAK, // Original C code array index 148
    ALPHA_CPV_END, // Original C code array index 149
    ALPHA_CPR_DELAY, // Original C code array index 150
    ALPHA_CPR_PEAK, // Original C code array index 151
    ALPHA_CPR_END;// Original C code array index 152

}
