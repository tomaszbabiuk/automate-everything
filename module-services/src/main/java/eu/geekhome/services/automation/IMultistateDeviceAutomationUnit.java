package eu.geekhome.services.automation;

public interface IMultistateDeviceAutomationUnit {
    void changeState(String state, ControlMode controlMode, String code, String actor) throws Exception;
    void setControlMode(ControlMode value);
    ControlMode getControlMode();
}
