package com.geekhome.common.configuration;

import com.geekhome.common.*;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class OnOffDeviceBase extends MultistateDevice implements IPortDrivenDevice, IRoomDevice, IBlocksTarget {
    private String _portId;
    private String _roomId;
    private YesNo _triggeredManually;

    @Persistable(name="PortId")
    public String getPortId() {
        return _portId;
    }

    public void setPortId(String value) {
        _portId = value;
    }

    @Persistable(name="RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    @Persistable(name="TriggeredManually")
    public YesNo getTriggeredManually() {
        return _triggeredManually;
    }

    public void setTriggeredManually(YesNo value) {
        _triggeredManually = value;
    }

    public OnOffDeviceBase(DescriptiveName name, String portId, String roomId, YesNo triggeredManually, String iconName,
                           DeviceCategory deviceCategory) {
        super(name, iconName, deviceCategory);
        setPortId(portId);
        setRoomId(roomId);
        setTriggeredManually(triggeredManually);
    }

    @Override
    protected ControlType calculateControlType() {
        return getTriggeredManually() == YesNo.Yes ? ControlType.Multistate : ControlType.ReadValue;
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:On"), "on",
                localizationProvider.getValue("C:SwitchOn")), StateType.NonSignaledAction, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"), "off",
                localizationProvider.getValue("C:SwitchOff")), StateType.SignaledAction, false));

        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }

    @Override
    public List<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        List<DescriptiveName> categories = new ArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("C:BlocksEnablingAutomaticControl"), "on"));
        return categories;
    }
}