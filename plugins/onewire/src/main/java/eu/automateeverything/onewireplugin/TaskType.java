package eu.automateeverything.onewireplugin;

enum TaskType {
    RefreshTemperature,
    ReadSwitchValue,
    ReadSensedSwitchValue,
    WriteSwitchValue,
    RefreshLoopFinished,
    ValidateIdentity,
    Continue,
    Break
}
