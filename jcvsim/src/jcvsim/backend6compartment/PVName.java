package jcvsim.backend6compartment;

/**
 * Attributes in the parameter vector
 *
 * We use certain abbreviations as prefixes. Most of these are set but not
 * used in the simulation.
 *
 * ART = Arterial
 * VEN = Venous
 * MICRO = Microcirculation
 * ZPFV = Zero point filling volume
 * 
 * ABDOM = Abdominal
 * LA_ = Left Atrium 
 * RA_ = Right Atrium 
 * LV_ = Left Ventricle 
 * RV_ = Right Ventricle
 * PULM = Pulmonary
 * UBODY_ = Upper Body
 * LBODY_ = Lower Body
 * SPLAN_ = Splanchnic
 *
 * @author Jason Leake
 */

public enum PVName {
    ABR_SET_POINT, // Original C code array index 0
    ABR_SCALING_FACTOR, // Original C code array index 1
    ABR_HR_SYMPATHETIC_GAIN, // Original C code array index 2
    ABR_HR_PARASYMPATHETIC_GAIN, // Original C code array index 3
    PV4, // Original C code array index 4
    PV5, // Original C code array index 5
    PV6, // Original C code array index 6
    PV7, // Original C code array index 7
    PV8, // Original C code array index 8
    PV9, // Original C code array index 9
    PV10, // Original C code array index 10
    PV11, // Original C code array index 11
    ABR_CONTRACT_RV_SYMP_GAIN, // Original C code array index 12
    ABR_CONTRACT_LV_SYMP_GAIN, // Original C code array index 13
    PV14, // Original C code array index 14
    CPR_SET_POINT, // Original C code array index 15
    CPR_SCALING_FACTOR, // Original C code array index 16
    PV17, // Original C code array index 17
    PV18, // Original C code array index 18
    PV19, // Original C code array index 19
    PV20, // Original C code array index 20
    PV21, // Original C code array index 21
    PV22, // Original C code array index 22
    PV23, // Original C code array index 23
    PV24, // Original C code array index 24
    PARASYMP_DELAY, // Original C code array index 25
    PARASYMP_PEAK, // Original C code array index 26
    PARASYMP_END, // Original C code array index 27
    ABR_DELAY_BETA_SYMP, // Original C code array index 28
    ABR_PEAK_BETA_SYMP, // Original C code array index 29
    ABR_END_BETA_SYMP, // Original C code array index 30
    INTRA_THORACIC_PRESSURE, // Original C code array index 31
    PV32, // Original C code array index 32
    PV33, // Original C code array index 33
    PV34, // Original C code array index 34
    PV35, // Original C code array index 35
    PV36, // Original C code array index 36
    PV37, // Original C code array index 37
    PV38, // Original C code array index 38
    PV39, // Original C code array index 39
    PV40, // Original C code array index 40
    PV41, // Original C code array index 41
    PV42, // Original C code array index 42
    PV43, // Original C code array index 43
    PV44, // Original C code array index 44
    RV_DIASTOLIC_COMPLIANCE, // Original C code array index 45
    RV_SYSTOLIC_COMPLIANCE, // Original C code array index 46
    PULM_ART_COMPLIANCE, // Original C code array index 47
    PULM_VEN_COMPLIANCE, // Original C code array index 48
    PV49, // Original C code array index 49
    PV50, // Original C code array index 50
    LV_DIASTOLIC_COMPLIANCE, // Original C code array index 51
    LV_SYSTOLIC_COMPLIANCE, // Original C code array index 52
    PV53, // Original C code array index 53
    PV54, // Original C code array index 54
    KIDNEY_RESISTANCE, // Original C code array index 55
    PV56, // Original C code array index 56
    SPLANCHNIC_RESISTANCE, // Original C code array index 57
    PV58, // Original C code array index 58
    LBODY_RESISTANCE, // Original C code array index 59
    PV60, // Original C code array index 60
    PV61, // Original C code array index 61
    PV62, // Original C code array index 62
    PV63, // Original C code array index 63
    PV64, // Original C code array index 64
    PULMONIC_VALVE_RESISTANCE, // Original C code array index 65
    PULM_MICRO_RESISTANCE, // Original C code array index 66
    PULM_VEN_RESISTANCE, // Original C code array index 67
    PV68, // Original C code array index 68
    LV_OUTFLOW_RESISTANCE, // Original C code array index 69
    TOTAL_BLOOD_VOLUME, // Original C code array index 70
    PV71, // Original C code array index 71
    PV72, // Original C code array index 72
    PV73, // Original C code array index 73
    PV74, // Original C code array index 74
    TOTAL_ZPFV, // Original C code array index 75
    PV76, // Original C code array index 76
    PV77, // Original C code array index 77
    KIDNEY_COMPARTMENT_ZPFV, // Original C code array index 78
    SPLAN_COMPARTMENT_ZPFV, // Original C code array index 79
    LBODY_COMPARTMENT_ZPFV, // Original C code array index 80
    ABDOM_VEN_ZPFV, // Original C code array index 81
    INFERIOR_VENA_CAVA_ZPFV, // Original C code array index 82
    SUPERIOR_VENA_CAVA_ZPFV, // Original C code array index 83
    PV84, // Original C code array index 84
    PV85, // Original C code array index 85
    PV86, // Original C code array index 86
    PV87, // Original C code array index 87
    PV88, // Original C code array index 88
    PV89, // Original C code array index 89
    NOMINAL_HEART_RATE, // Original C code array index 90
    PV91, // Original C code array index 91
    PV92, // Original C code array index 92
    PV93, // Original C code array index 93
    PV94, // Original C code array index 94
    PV95, // Original C code array index 95
    PV96, // Original C code array index 96
    PV97, // Original C code array index 97
    PV98, // Original C code array index 98
    PV99, // Original C code array index 99
    PV100, // Original C code array index 100
    PV101, // Original C code array index 101
    PV102, // Original C code array index 102
    PV103, // Original C code array index 103
    PV104, // Original C code array index 104
    PV105, // Original C code array index 105
    PV106, // Original C code array index 106
    PV107, // Original C code array index 107
    PV108, // Original C code array index 108
    PV109, // Original C code array index 109
    PV110, // Original C code array index 110
    PV111, // Original C code array index 111
    PV112, // Original C code array index 112
    PV113, // Original C code array index 113
    PV114, // Original C code array index 114
    PV115, // Original C code array index 115
    PV116, // Original C code array index 116
    PV117, // Original C code array index 117
    PV118, // Original C code array index 118
    PV119, // Original C code array index 119
    PV120, // Original C code array index 120
    PV121, // Original C code array index 121
    PV122, // Original C code array index 122
    PV123, // Original C code array index 123
    PV124, // Original C code array index 124
    PV125, // Original C code array index 125
    PV126, // Original C code array index 126
    PV127, // Original C code array index 127
    PV128, // Original C code array index 128
    PV129, // Original C code array index 129
    PV130, // Original C code array index 130
    PV131, // Original C code array index 131
    PV132, // Original C code array index 132
    PV133, // Original C code array index 133
    PV134, // Original C code array index 134
    PV135, // Original C code array index 135
    PV136, // Original C code array index 136
    PV137, // Original C code array index 137
    PV138, // Original C code array index 138
    PV139, // Original C code array index 139
    PV140, // Original C code array index 140
    PV141, // Original C code array index 141
    PV142, // Original C code array index 142
    PV143, // Original C code array index 143
    PV144, // Original C code array index 144
    PV145, // Original C code array index 145
    PV146, // Original C code array index 146
    CPR_VEN_DELAY_ALPHA, // Original C code array index 147
    CPR_VEN_PEAK_ALPHA, // Original C code array index 148
    CPR_VEN_END_ALPHA, // Original C code array index 149
    CPR_ART_DELAY_ALPHA, // Original C code array index 150
    CPR_ART_PEAK_ALPHA, // Original C code array index 151
    CPR_ART_END_ALPHA, // Original C code array index 152
    ART_COMPLIANCE, // Original C code array index 153
    VEN_COMPLIANCE, // Original C code array index 154
    AORTIC_VALVE_RESISTANCE, // Original C code array index 155
    TOTAL_PERIPHERAL_RESISTANCE, // Original C code array index 156
    VEN_RESISTANCE, // Original C code array index 157
    ABR_ART_RESISTANCE_SYMP_GAIN, // Original C code array index 158
    CPR_ART_RESISTANCE_SYMP_GAIN, // Original C code array index 159
    ABR_VEN_TONE_SYMPATHETIC_GAIN, // Original C code array index 160
    CPR_VEN_TONE_SYMP_GAIN, // Original C code array index 161
    LV_ZPFV, // Original C code array index 162
    SYSTEMIC_ART_ZPFV, // Original C code array index 163
    SYSTEMIC_VEN_ZPFV, // Original C code array index 164
    RV_ZPFV, // Original C code array index 165
    PULM_ART_ZPFV, // Original C code array index 166
    PULM_VEN_ZPFV, // Original C code array index 167

}
