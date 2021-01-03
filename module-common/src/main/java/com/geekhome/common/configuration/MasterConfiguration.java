package com.geekhome.common.configuration;

import com.geekhome.common.*;
import com.geekhome.common.localization.ILocalizationProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MasterConfiguration extends Collector implements IMasterDependenciesChecker {
    private final MasterTextFilePersister _configurationProvider;
    private ILocalizationProvider _localizationProvider;
    private ArrayList<DependenciesCheckerModule> _dependenciesCheckers;
    private ArrayList<Collector> _collectors;
    private CollectorCollection<Floor> _floors;
    private CollectorCollection<MultistateCondition> _multistateConditions;
    private CollectorCollection<ValueCondition> _valueConditions;
    private CollectorCollection<DeltaCondition> _deltaConditions;
    private CollectorCollection<GroupCondition> _groupConditions;
    private CollectorCollection<TimeCondition> _timeConditions;
    private CollectorCollection<GeofenceCondition> _geofenceConditions;
    private CollectorCollection<ICondition> _calculatedConditions;
    private CollectorCollection<Mode> _modes;
    private CollectorCollection<Alert> _alerts;
    private CollectorCollection<KeySwitch> _keySwitches;
    private CollectorCollection<Block> _blocks;
    private CollectorCollection<DateCondition> _dateConditions;
    private CollectorCollection<ReadValueCommand> _readValueCommands;
    private CollectorCollection<ChangeStateCommand> _changeStateCommands;
    private CollectorCollection<Geofence> _geofences;

    private ArrayList<CollectorCollection<? extends IDevice>> _devicesCollectors;
    private ArrayList<CollectorCollection<? extends ICondition>> _conditionsCollectors;
    private ArrayList<IConfigurationValidator> _configurationValidators;
    private List<String> _validations;

    private IInvalidateCacheListener _masterInvalidateCacheListener;

    public void setMasterInvalidateCacheListener(IInvalidateCacheListener listener) {
        _masterInvalidateCacheListener = listener;
    }

    private void onMasterInvalidateCache(String what) {
        if (_masterInvalidateCacheListener != null) {
            _masterInvalidateCacheListener.invalidate(what);
        }
    }

    public boolean isAnyCollectionerModified() {
        for (Collector collector : _collectors) {
            if (collector.isModified()) {
                return true;
            }
        }

        return false;
    }

    @ConfigurationSaver(sectionName = "DateCondition", hasChildren = false)
    public CollectorCollection<DateCondition> getDateConditions() {
        return _dateConditions;
    }

    @ConfigurationSaver(sectionName = "Floor", hasChildren = true)
    public CollectorCollection<Floor> getFloors() {
        return _floors;
    }

    @ConfigurationSaver(sectionName = "MultistateCondition", hasChildren = false)
    public CollectorCollection<MultistateCondition> getMultistateConditions() {
        return _multistateConditions;
    }

    @ConfigurationSaver(sectionName = "ValueCondition", hasChildren = false)
    public CollectorCollection<ValueCondition> getValueConditions() {
        return _valueConditions;
    }

    @ConfigurationSaver(sectionName = "DeltaCondition", hasChildren = false)
    public CollectorCollection<DeltaCondition> getDeltaConditions() {
        return _deltaConditions;
    }

    @ConfigurationSaver(sectionName = "GroupCondition", hasChildren = false)
    public CollectorCollection<GroupCondition> getGroupConditions() {
        return _groupConditions;
    }

    @ConfigurationSaver(sectionName = "TimeCondition", hasChildren = false)
    public CollectorCollection<TimeCondition> getTimeConditions() {
        return _timeConditions;
    }

    @ConfigurationSaver(sectionName = "GeofenceCondition", hasChildren = false)
    public CollectorCollection<GeofenceCondition> getGeofenceConditions() {
        return _geofenceConditions;
    }

    @ConfigurationSaver(sectionName = "Mode", hasChildren = false)
    public CollectorCollection<Mode> getModes() {
        return _modes;
    }

    @ConfigurationSaver(sectionName = "Alert", hasChildren = false)
    public CollectorCollection<Alert> getAlerts() {
        return _alerts;
    }

    @ConfigurationSaver(sectionName = "KeySwitch", hasChildren = false)
    public CollectorCollection<KeySwitch> getKeySwitches() {
        return _keySwitches;
    }

    @ConfigurationSaver(sectionName = "Block", hasChildren = false)
    public CollectorCollection<Block> getBlocks() {
        return _blocks;
    }

    @ConfigurationSaver(sectionName = "ReadValueCommand", hasChildren = false)
    public CollectorCollection<ReadValueCommand> getReadValueCommands() {
        return _readValueCommands;
    }

    @ConfigurationSaver(sectionName = "ChangeStateCommand", hasChildren = false)
    public CollectorCollection<ChangeStateCommand> getChangeStateCommands() {
        return _changeStateCommands;
    }

    @ConfigurationSaver(sectionName = "Geofence", hasChildren = false)
    public CollectorCollection<Geofence> getGeofences() {
        return _geofences;
    }

    private CollectorCollection<ICondition> getCalculatedConditons() {
        return _calculatedConditions;
    }

    public ArrayList<CollectorCollection<? extends IDevice>> getDevicesCollectors() {
        return _devicesCollectors;
    }
    public ArrayList<CollectorCollection<? extends ICondition>> getConditionsCollectors() {
        return _conditionsCollectors;
    }

    @Override
    public void createDynamicObjects() {
        getCalculatedConditons().clear();
        AlwaysOnCondition alwaysOnCondition = new AlwaysOnCondition(_localizationProvider);
        getCalculatedConditons().add(alwaysOnCondition);
    }

    public List<String> getVallidations() {
        if (_validations == null) {
            refreshValidations();
        }
        return _validations;
    }

    private void refreshValidations() {
        _validations = validateConfiguration();
    }

    public MasterTextFilePersister getConfigurationProvider() {
        return _configurationProvider;
    }

    public MasterConfiguration(ILocalizationProvider localizationProvider) throws IOException {
        super(new IdPool());
        _localizationProvider = localizationProvider;
        _dependenciesCheckers = new ArrayList<>();
        _conditionsCollectors = new ArrayList<>();
        _devicesCollectors = new ArrayList<>();
        _configurationValidators = new ArrayList<>();
        _validations = null;

        _floors = new CollectorCollection<>();
        _multistateConditions = new CollectorCollection<>();
        _valueConditions = new CollectorCollection<>();
        _deltaConditions = new CollectorCollection<>();
        _groupConditions = new CollectorCollection<>();
        _timeConditions = new CollectorCollection<>();
        _geofenceConditions = new CollectorCollection<>();
        _dateConditions = new CollectorCollection<>();
        _modes = new CollectorCollection<>();
        _alerts = new CollectorCollection<>();
        _blocks = new CollectorCollection<>();
        _keySwitches = new CollectorCollection<>();
        _calculatedConditions = new CollectorCollection<>();
        _readValueCommands = new CollectorCollection<>();
        _changeStateCommands = new CollectorCollection<>();
        _geofences = new CollectorCollection<>();

        _configurationProvider = new MasterTextFilePersister(getPool());
    }

    @Override
    public void clear() {
        getFloors().clear();
        getMultistateConditions().clear();
        getValueConditions().clear();
        getDeltaConditions().clear();
        getGroupConditions().clear();
        getTimeConditions().clear();
        getGeofenceConditions().clear();
        getDateConditions().clear();
        getModes().clear();
        getBlocks().clear();
        getKeySwitches().clear();
        getCalculatedConditons().clear();
        getAlerts().clear();
        getReadValueCommands().clear();
        getChangeStateCommands().clear();
        getGeofences().clear();
    }

    @Override
    public String getPoolPrefix() {
        return "core";
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Floor", parentId = "null")
    public void modifyFloor(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String iconName = values.getValue("iconname");
        if (action == CrudAction.AddOrCreate) {
            addFloor(name, description, iconName, uniqueId);
        } else {
            editFloor(name, description, iconName, uniqueId);
        }
        onInvalidateCache("/CONFIG/FLOORS.JSON");
        onModified();
    }

    private void addFloor(String name, String description, String iconName, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName floorName = new DescriptiveName(name, uniqueId, description);
        Floor newFloor = new Floor(floorName, iconName);
        getFloors().add(newFloor);
    }

    private void editFloor(String name, String description, String iconName, String uniqueid) throws ObjectNotFoundException {
        Floor floor = getFloors().find(uniqueid);
        floor.getName().setName(name);
        floor.getName().setDescription(description);
        floor.setIconName(iconName);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Room/Floor", parentId = "floorid")
    public void modifyRoom(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String floorId = values.getValue("floorid");
        String iconname = values.getValue("iconname");
        if (action == CrudAction.AddOrCreate) {
            addRoom(floorId, name, description, iconname, uniqueId);
        } else {
            editRoom(name, description, iconname, uniqueId);
        }
        onInvalidateCache("/CONFIG/FLOORS.JSON");
        onModified();
    }

    private void addRoom(String floorId, String name, String description, String iconName, String uniqueId) throws ObjectNotFoundException {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        Floor floor = getFloors().find(floorId);
        DescriptiveName roomName = new DescriptiveName(name, uniqueId, description);
        Room newRoom = new Room(roomName, iconName);
        floor.getRooms().add(newRoom);
    }

    private void editRoom(String name, String description, String iconName, String uniqueId) throws ObjectNotFoundException {
        Room room = findRoom(uniqueId);
        room.getName().setName(name);
        room.getName().setDescription(description);
        room.setIconName(iconName);
    }

    private Room findRoom(String uniqueId, boolean throwObjectNotFoundExceptionIfNotFound) throws ObjectNotFoundException {
        for (Floor entry : getFloors().values()) {
            if (entry.getRooms().containsKey(uniqueId)) {
                return entry.getRooms().get(uniqueId);
            }
        }

        if (throwObjectNotFoundExceptionIfNotFound) {
            throw new ObjectNotFoundException(uniqueId);
        }

        return null;
    }

    private Room findRoom(String uniqueId) throws ObjectNotFoundException {
        return findRoom(uniqueId, true);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "KeySwitch", parentId = "")
    public void modifyKeySwitch(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String roomId = values.getValue("roomid");
        String portId = values.getValue("portid");
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));
        String createMultistateCondition = values.getValue("createmultistatecondition");

        if (action == CrudAction.AddOrCreate) {
            addKeySwitch(name, description, deviceCategoryParsed, portId, roomId, createMultistateCondition, uniqueId);
        } else {
            editKeySwitch(name, description, deviceCategoryParsed, portId, roomId, uniqueId);
        }
        onInvalidateCache("/CONFIG/KEYSWITCHES.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        onModified();
    }

    private void addKeySwitch(String name, String description, DeviceCategory deviceCategory, String portId, String roomId, String createMultistateCondition, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        KeySwitch device = new KeySwitch(deviceName, portId, roomId, deviceCategory);
        getKeySwitches().add(device);

        createMultistateConditionWhenRequested(name, uniqueId, createMultistateCondition);
    }

    private void editKeySwitch(String name, String description, DeviceCategory deviceCategory, String portId, String roomId, String uniqueId) throws ObjectNotFoundException {
        KeySwitch device = getKeySwitches().find(uniqueId);
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setRoomId(roomId);
        device.setPortId(portId);
        device.setDeviceCategory(deviceCategory);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "MultistateCondition", parentId = "")
    public void modifyMultistateCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String deviceId = values.getValue("deviceid");
        String statesIds = values.getValues("statesids");
        String delayTime = values.getValue("delaytime");
        if (action == CrudAction.AddOrCreate) {
            addMultistateCondition(name, description, deviceId, statesIds, delayTime, uniqueId);
        } else {
            editMultistateCondition(name, description, deviceId, statesIds, delayTime, uniqueId);
        }
        onInvalidateCache("/CONFIG/MULTISTATECONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addMultistateCondition(String name, String description, String deviceId, String statesIds, String delayTime, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        MultistateCondition condition = new MultistateCondition(conditionName, deviceId, statesIds, delayTime);
        getMultistateConditions().add(condition);
    }

    private void editMultistateCondition(String name, String description, String deviceId, String statesIds, String delayTime, String uniqueId) throws ObjectNotFoundException {
        MultistateCondition condition = getMultistateConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setStatesIds(statesIds);
        condition.setDeviceId(deviceId);
        condition.setDelayTime(delayTime);
    }

    public void createMultistateConditionWhenRequested(String name, String uniqueId, String createStateConditionOptions) throws Exception {
        if (createStateConditionOptions != null) {
            for (String state : createStateConditionOptions.split(",")) {
                if (state.equals("on")) {
                    addMultistateCondition(name + " - " + _localizationProvider.getValue("C:On"), "", uniqueId, "on", "00:00:00", "");
                }

                if (state.equals("off")) {
                    addMultistateCondition(name + " - " + _localizationProvider.getValue("C:Off"), "", uniqueId, "off", "00:00:00", "");
                }
            }
            onInvalidateCache("/CONFIG/MULTISTATECONDITIONS.JSON");
            onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        }
    }


    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "ValueCondition", parentId = "")
    public void modifyValueCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String deviceId = values.getValue("deviceid");
        String value = values.getValue("value");
        double valueParsed = Double.valueOf(value);
        String equalityOperator = values.getValue("operator");
        EqualityOperator equalityOperatorParsed = EqualityOperator.fromInt(Integer.parseInt(equalityOperator));
        String hysteresis = values.getValue("hysteresis");
        double hysteresisParsed = Double.parseDouble(hysteresis);
        if (action == CrudAction.AddOrCreate) {
            addValueCondition(name, description, equalityOperatorParsed, deviceId, valueParsed, hysteresisParsed, uniqueId);
        } else {
            editValueCondition(name, description, equalityOperatorParsed, deviceId, valueParsed, hysteresisParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/VALUECONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addValueCondition(String name, String description, EqualityOperator equalityOperator,
                                         String deviceId, double value, double hysteresis, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        ValueCondition condition = new ValueCondition(conditionName, deviceId, equalityOperator, value, hysteresis);
        getValueConditions().add(condition);
    }

    private void editValueCondition(String name, String description, EqualityOperator equalityOperator,
                                          String deviceId, double value, double hysteresis, String uniqueId) throws ObjectNotFoundException {
        ValueCondition condition =  getValueConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setOperator(equalityOperator);
        condition.setValue(value);
        condition.setDeviceId(deviceId);
        condition.setHysteresis(hysteresis);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "DeltaCondition", parentId = "")
    public void modifyDeltaCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String firstDeviceId = values.getValue("firstdeviceid");
        String secondDeviceId = values.getValue("seconddeviceid");
        String delta = values.getValue("delta");
        int deltaParsed = Integer.parseInt(delta);
        String hysteresis = values.getValue("hysteresis");
        double hysteresisParsed = Double.parseDouble(hysteresis);
        String equalityOperator = values.getValue("operator");
        EqualityOperator equalityOperatorParsed = EqualityOperator.fromInt(Integer.parseInt(equalityOperator));
        if (action == CrudAction.AddOrCreate) {
            addDeltaCondition(name, description, equalityOperatorParsed, deltaParsed, hysteresisParsed,
                    firstDeviceId, secondDeviceId, uniqueId);
        } else {
            editDeltaCondition(name, description, equalityOperatorParsed, deltaParsed, hysteresisParsed,
                    firstDeviceId, secondDeviceId, uniqueId);
        }
        onInvalidateCache("/CONFIG/DELTACONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addDeltaCondition(String name, String description, EqualityOperator equalityOperator,
                                                   int delta, double hysteresis, String firstDeviceId,
                                                   String secondDeviceId, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        DeltaCondition condition = new DeltaCondition(conditionName, equalityOperator,
                delta, hysteresis, firstDeviceId, secondDeviceId);
        getDeltaConditions().add(condition);
    }

    private void editDeltaCondition(String name, String description, EqualityOperator equalityOperator,
                                                    int delta, double hysteresis, String firstDeviceId,
                                                    String secondDeviceId, String uniqueId) throws ObjectNotFoundException {
        DeltaCondition condition = getDeltaConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setDelta(delta);
        condition.setHysteresis(hysteresis);
        condition.setFirstDeviceId(firstDeviceId);
        condition.setSecondDeviceId(secondDeviceId);
        condition.setOperator(equalityOperator);
    }
    
    
    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "GroupCondition", parentId = "")
    public void modifyGroupCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String conditionsIds = values.getValues("conditionsids");
        String match = values.getValue("match");
        GroupMatch matchParsed = GroupMatch.fromInt(Integer.parseInt(match));
        if (action == CrudAction.AddOrCreate) {
            addGroupCondition(name, description, conditionsIds, matchParsed, uniqueId);
        } else {
            editGroupCondition(name, description, conditionsIds, matchParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/GROUPCONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addGroupCondition(String name, String description, String conditionsIds, GroupMatch match, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        GroupCondition condition = new GroupCondition(conditionName, conditionsIds, match);
        getGroupConditions().add(condition);
    }

    private void editGroupCondition(String name, String description, String conditionsIds, GroupMatch match, String uniqueId) throws ObjectNotFoundException {
        GroupCondition condition = getGroupConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setConditionsIds(conditionsIds);
        condition.setMatch(match);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "TimeCondition", parentId = "")
    public void modifyTimeCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String startTime = values.getValue("starttime");
        String stopTime = values.getValue("stoptime");
        String daysIds = values.getValues("daysids");
        if (action == CrudAction.AddOrCreate) {
            addTimeCondition(name, description, startTime, stopTime, daysIds, uniqueId);
        } else {
            editTimeCondition(name, description, startTime, stopTime, daysIds, uniqueId);
        }
        onInvalidateCache("/CONFIG/TIMECONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addTimeCondition(String name, String description, String startTime, String stopTime, String daysIds, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        TimeCondition condition = new TimeCondition(conditionName, startTime, stopTime, daysIds);
        getTimeConditions().add(condition);
    }

    private void editTimeCondition(String name, String description, String startTime, String stopTime, String daysIds, String uniqueId) throws ObjectNotFoundException {
        TimeCondition condition = getTimeConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setStartTime(startTime);
        condition.setStopTime(stopTime);
        condition.setDaysIds(daysIds);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "DateCondition", parentId = "")
    public void modifyDateCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String startDate = values.getValue("startdate");
        String stopDate = values.getValue("stopdate");
        if (action == CrudAction.AddOrCreate) {
            addDateCondition(name, description, startDate, stopDate, uniqueId);
        } else {
            editDateCondition(name, description, startDate, stopDate, uniqueId);
        }
        onInvalidateCache("/CONFIG/DATECONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addDateCondition(String name, String description, String startDate, String stopDate, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        DateCondition condition = new DateCondition(conditionName, startDate, stopDate);
        getDateConditions().add(condition);
    }

    private void editDateCondition(String name, String description, String startDate, String stopDate, String uniqueId) throws ObjectNotFoundException {
        DateCondition condition = getDateConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setStartDate(startDate);
        condition.setStopDate(stopDate);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Block", parentId = "")
    public void modifyBlock(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String targetId = values.getValue("targetid");
        String conditionsIds = values.getValues("conditionsids");
        if (action == CrudAction.AddOrCreate) {
            addBlock(targetId, name, description, conditionsIds, uniqueId);
        } else {
            editBlock(name, description, conditionsIds, uniqueId);
        }
        onInvalidateCache("/CONFIG/BLOCKS.JSON");
        onModified();
    }

    private void addBlock(String targetId, String name, String description, String conditionsIds, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName blockName = new DescriptiveName(name, uniqueId, description);
        Block block = new Block(blockName, targetId, conditionsIds);
        getBlocks().add(block);
    }

    private void editBlock(String name, String description, String conditionsIds, String uniqueId) throws ObjectNotFoundException {
        Block block = getBlocks().find(uniqueId);
        block.getName().setName(name);
        block.getName().setDescription(description);
        block.setConditionsIds(conditionsIds);
    }

    public CollectorCollection<Block> findBlocksByTarget(String targetId) {
        CollectorCollection<Block> result = new CollectorCollection<>();
        for (Block block : getBlocks().values()) {
            String blockTargetId = block.getTargetId();
            if (blockTargetId.contains("_")) {
                blockTargetId = blockTargetId.substring(0, blockTargetId.indexOf("_"));
            }
            if (blockTargetId.equals(targetId)) {
                result.add(block);
            }
        }

        return result;
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Mode", parentId = "")
    public void modifyMode(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String priority = values.getValue("priority");
        int priorityParsed = Integer.parseInt(priority);
        if (action == CrudAction.AddOrCreate) {
            addMode(name, description, priorityParsed, uniqueId);
        } else {
            editMode(name, description, priorityParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/MODES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addMode(String name, String description, int priority, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName modeName = new DescriptiveName(name, uniqueId, description);
        Mode mode = new Mode(modeName, priority);
        getModes().add(mode);
    }

    private void editMode(String name, String description, int priority, String uniqueId) throws ObjectNotFoundException {
        Mode mode = getModes().find(uniqueId);
        mode.getName().setName(name);
        mode.getName().setDescription(description);
        mode.setPriority(priority);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Alert", parentId = "")
    public void modifyAlert(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String alertServicesIds = values.getValues("alertservicesids");
        if (action == CrudAction.AddOrCreate) {
            addAlert(name, description, alertServicesIds, uniqueId);
        } else {
            editAlert(name, description, alertServicesIds, uniqueId);
        }
        onInvalidateCache("/CONFIG/ALERTS.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addAlert(String name, String description, String alertServicesIds, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName modeName = new DescriptiveName(name, uniqueId, description);
        Alert alert = new Alert(modeName, alertServicesIds);
        getAlerts().add(alert);
    }

    private void editAlert(String name, String description, String alertServicesIds, String uniqueId) throws ObjectNotFoundException {
        Alert alert = getAlerts().find(uniqueId);
        alert.getName().setName(name);
        alert.getName().setDescription(description);
        alert.setAlertServicesIds(alertServicesIds);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "ReadValueCommand", parentId = "")
    public void modifyReadValueCommand(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String devicesIds = values.getValues("devicesids");
        if (action == CrudAction.AddOrCreate) {
            addReadValueCommand(name, description, devicesIds, uniqueId);
        } else {
            editReadValueCommand(name, description, devicesIds,uniqueId);
        }
        onInvalidateCache("/CONFIG/READVALUECOMMANDS.JSON");
        onModified();
    }

    private void addReadValueCommand(String name, String description, String devicesIds, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName modeName = new DescriptiveName(name, uniqueId, description);
        ReadValueCommand command = new ReadValueCommand(modeName, devicesIds);
        getReadValueCommands().add(command);
    }

    private void editReadValueCommand(String name, String description, String devicesIds, String uniqueId) throws ObjectNotFoundException {
        ReadValueCommand command = getReadValueCommands().find(uniqueId);
        command.getName().setName(name);
        command.getName().setDescription(description);
        command.setDevicesIds(devicesIds);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "ChangeStateCommand", parentId = "")
    public void modifyChangeStateCommand(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String deviceId = values.getValues("deviceid");
        String stateId = values.getValue("stateid");
        if (action == CrudAction.AddOrCreate) {
            addChangeStateCommand(name, description, deviceId, stateId, uniqueId);
        } else {
            editChangeStateCommand(name, description, deviceId, stateId, uniqueId);
        }
        onInvalidateCache("/CONFIG/CHANGESTATECOMMANDS.JSON");
        onModified();
    }

    private void addChangeStateCommand(String name, String description, String deviceId, String stateId, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName modeName = new DescriptiveName(name, uniqueId, description);
        ChangeStateCommand command = new ChangeStateCommand(modeName, deviceId, stateId);
        getChangeStateCommands().add(command);
    }

    private void editChangeStateCommand(String name, String description, String deviceId, String stateId, String uniqueId) throws ObjectNotFoundException {
        ChangeStateCommand command = getChangeStateCommands().find(uniqueId);
        command.getName().setName(name);
        command.getName().setDescription(description);
        command.setStateId(stateId);
        command.setDeviceId(deviceId);
    }


    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Geofence", parentId = "")
    public void modifyGeofence(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String iconName = values.getValue("iconname");
        String longitude = values.getValue("longitude");
        double longitudeParsed = Double.parseDouble(longitude);
        String latitude = values.getValue("latitude");
        double latitudeParsed = Double.parseDouble(latitude);
        String radius = values.getValue("radius");
        double radiusParsed = Double.parseDouble(radius);
        if (action == CrudAction.AddOrCreate) {
            addGeofence(name, description, iconName, longitudeParsed, latitudeParsed, radiusParsed, uniqueId);
        } else {
            editGeofence(name, description, iconName, longitudeParsed, latitudeParsed, radiusParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/GEOFENCES.JSON");
        onModified();
    }

    private void addGeofence(String name, String description, String iconName, double longitude, double latitude,
                                      double radius, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        Geofence geofence = new Geofence(conditionName, iconName, longitude, latitude, radius);
        getGeofences().add(geofence);
    }

    private void editGeofence(String name, String description, String iconName, double longitude, double latitude,
                                       double radius, String uniqueId) throws ObjectNotFoundException {
        Geofence condition = getGeofences().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setIconName(iconName);
        condition.setLatitude(latitude);
        condition.setLongitude(longitude);
        condition.setRadius(radius);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "GeofenceCondition", parentId = "")
    public void modifyGeofenceCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String geofencesIds = values.getValues("geofencesids");
        if (action == CrudAction.AddOrCreate) {
            addGeofenceCondition(name, description, geofencesIds, uniqueId);
        } else {
            editGeofenceCondition(name, description, geofencesIds, uniqueId);
        }
        onInvalidateCache("/CONFIG/GEOFENCECONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addGeofenceCondition(String name, String description, String geofencesIds, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        GeofenceCondition condition = new GeofenceCondition(conditionName, geofencesIds);
        getGeofenceConditions().add(condition);
    }

    private void editGeofenceCondition(String name, String description, String geofencesIds, String uniqueId) throws ObjectNotFoundException {
        GeofenceCondition condition = getGeofenceConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setGeofencesIds(geofencesIds);
    }

    @Override
    public void addDevicesCollectors(ArrayList<CollectorCollection<? extends IDevice>> devicesCollectors) {
        devicesCollectors.add(getKeySwitches());
    }

    @Override
    public void addConditionsCollectors(ArrayList<CollectorCollection<? extends ICondition>> conditionCollectors) {
        conditionCollectors.add(getMultistateConditions());
        conditionCollectors.add(getValueConditions());
        conditionCollectors.add(getDeltaConditions());
        conditionCollectors.add(getGroupConditions());
        conditionCollectors.add(getTimeConditions());
        conditionCollectors.add(getDateConditions());
        conditionCollectors.add(getCalculatedConditons());
        conditionCollectors.add(getGeofenceConditions());
    }

    @Override
    public ArrayList<CollectorCollection<? extends INamedObject>> buildAllCollections() {
        ArrayList<CollectorCollection<? extends INamedObject>> result = new ArrayList<>();
        result.add(getFloors());
        for (Floor floor : getFloors().values()) {
            result.add(floor.getRooms());
        }

        result.add(getMultistateConditions());
        result.add(getValueConditions());
        result.add(getDeltaConditions());
        result.add(getGroupConditions());
        result.add(getTimeConditions());
        result.add(getGeofenceConditions());
        result.add(getDateConditions());
        result.add(getModes());
        result.add(getAlerts());
        result.add(getBlocks());
        result.add(getKeySwitches());
        result.add(getCalculatedConditons());
        result.add(getReadValueCommands());
        result.add(getChangeStateCommands());
        result.add(getGeofences());

        return result;
    }

    private void startTrackModifications() {
        for (Collector collector : _collectors) {
            collector.setModifiedListener(this::refreshValidations);
        }
    }

    private void registerCollectors(ArrayList<Collector> collectors) {
        _collectors = collectors;

        for (Collector collector : collectors) {
            //refresh cache
            collector.setInvalidateCacheListener(this::onMasterInvalidateCache);

            collector.addDevicesCollectors(_devicesCollectors);
            collector.addConditionsCollectors(_conditionsCollectors);
        }
    }

    public void createDynamicObjectsForAllCollectors() {
        for (Collector collector : _collectors) {
            collector.createDynamicObjects();
        }
    }

    private void registerDependenciesCheckerModules(ArrayList<DependenciesCheckerModule> dependenciesCheckers) {
        _dependenciesCheckers = dependenciesCheckers;
    }

    private void registerConfigurationValidators(ArrayList<IConfigurationValidator> configurationValidators) {
        _configurationValidators = configurationValidators;
    }

    public List<Dependency> buildDependencies(String id) throws ObjectNotFoundException {
        List<Dependency> dependencies = new ArrayList<>();

        Object found = findAnyInAllCollectors(id);
        if (found != null) {
            checkDependencyInAllDependenciesCheckers(found, dependencies, 0);
            return dependencies;
        }

        throw new ObjectNotFoundException("Cannot find object: " + id);
    }

    private Object findAnyInAllCollectors(String id) {
        for (Collector collector : _collectors) {
            Object found = collector.findAny(id);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    public void checkDependencyInAllDependenciesCheckers(Object obj, List<Dependency> dependencies, int level) {
        for (DependenciesCheckerModule checker : _dependenciesCheckers) {
            checker.checkDependency(obj, dependencies, level);
        }
    }

    public List<String> validateConfiguration() {
        List<String> validations = new ArrayList<>();
        for (IConfigurationValidator configurationValidator : _configurationValidators) {
            configurationValidator.addValidations(validations);
        }

        return validations;
    }

    public void masterRemoveObjectWithItsDependencies(String id) throws Exception {
        List<Dependency> dependencies = buildDependencies(id);

        if (dependencies.size() > 0) {
            for (int i = dependencies.size() - 1; i >= 0; i--) {
                Dependency dependency = dependencies.get(i);
                masterRemoveSingleObject(dependency.getName().getUniqueId());
            }
        }
        masterRemoveSingleObject(id);

        onMasterInvalidateCache("all");
        refreshValidations();
    }

    private void masterRemoveSingleObject(String id) throws Exception {
        for (Collector collector : _collectors) {
            boolean removed = collector.removeSingleObject(id);
            if (removed) {
                break;
            }
        }
    }

    public CollectorCollection<IDevice> searchDevicesInRoom(String roomId, MatchDeviceDelegate match) {
        CollectorCollection<IDevice> result = new CollectorCollection<>();
        for (CollectorCollection collection : getDevicesCollectors()) {
            for (Object current : collection.values()) {
                if (current instanceof IRoomDevice) {
                    IRoomDevice device = (IRoomDevice) current;
                    if (device.getRoomId().equals(roomId)) {
                        addDevice(device, match, result);
                    }
                } else if (current instanceof IMultiroomDevice) {
                    IMultiroomDevice device = (IMultiroomDevice) current;
                    for (String iRoomId : device.getRoomsIds().split(",")) {
                        if (iRoomId.equals(roomId)) {
                            addDevice(device, match, result);
                        }
                    }
                }
            }
        }
        return result;
    }

    private static void addDevice(IDevice device, MatchDeviceDelegate match, CollectorCollection<IDevice> result) {
        if (match != null) {
            if (match.execute(device)) {
                result.put(device.getName().getUniqueId(), device);
            }
        } else {
            result.put(device.getName().getUniqueId(), device);
        }
    }

    public IDevice trySearchDevice(String id) throws ObjectNotFoundException {
        for (CollectorCollection collection : getDevicesCollectors()) {
            for (Object current : collection.values()) {
                if (current instanceof IDevice) {
                    IDevice device = (IDevice) current;
                    if (device.getName().getUniqueId().equals(id)) {
                        return device;
                    }
                }
            }
        }

        return null;
    }

    public IDevice searchDevice(String id) throws ObjectNotFoundException {
        IDevice found = trySearchDevice(id);
        if (found != null) {
            return found;
        }
        throw new ObjectNotFoundException("Cannot find device: " + id);
    }

    public void saveAll(String comment) throws Exception {
        incrementPoolNumber();
        _configurationProvider.save(_collectors, comment);

        clearModificationFlags();
    }

    private void clearModificationFlags() {
        for (Collector collector : _collectors) {
            collector.setIsModified(false);
        }
    }

    private void loadAll() throws Exception {
        _configurationProvider.load(_collectors, null);
        onLoadAll();
        clearModificationFlags();
    }

    private void onLoadAll() {
        recalculateAlerts();
    }

    private void clearAll() {
        getPool().reset();
        for (Collector collector : _collectors) {
            collector.clear();
        }
    }

    public void restore(String backupId) throws Exception {
        clearAll();
        ConfigurationMetadata metadata = _configurationProvider.load(_collectors, backupId);
        String comment = String.format(_localizationProvider.getValue("C:RestoredFromFormat"), metadata.getComment());
        saveAll(comment);
        onLoadAll();
        clearModificationFlags();
        onInvalidateCache("all");
    }

    public void rollbackAll() throws Exception {
        clearAll();
        clearModificationFlags();
        loadAll();
        onInvalidateCache("all");
        setIsModified(false);
    }


    public void initialize(ArrayList<DependenciesCheckerModule> dependenciesCheckers,
                           ArrayList<Collector> collectors,
                           ArrayList<IConfigurationValidator> configurationValidators) throws Exception {

        registerDependenciesCheckerModules(dependenciesCheckers);
        registerCollectors(collectors);
        registerConfigurationValidators(configurationValidators);
        loadAll();

        startTrackModifications();
    }

    private void recalculateAlerts() {
        for (Alert alert : getAlerts().values()) {
            ArrayList<String> relatedDevicesIds = new ArrayList<>();
            for (Block block : getBlocks().values()) {
                if (block.getTargetId().equals(alert.getName().getUniqueId())) {
                    for (String conditionId : block.getConditionsIds().replace("!","").split(",")) {
                        ICondition condition = (ICondition) findAnyInAllCollectors(conditionId);
                        if (condition != null) {
                            for (String deviceId : condition.getDevicesIds()) {
                                if (!relatedDevicesIds.contains(deviceId)) {
                                    relatedDevicesIds.add(deviceId);
                                }
                            }
                        }
                    }
                }
            }
            alert.setDevicesIds(relatedDevicesIds);
        }
    }

    private static <T> void addToCollectionIfNotNull(T obj, ArrayList<T> collection) {
        if (obj != null) {
            collection.add(obj);
        }
    }

    public int getVersion() {
        return getPool().getCurrentId();
    }

    public void systemInfoChanged() {
        refreshValidations();
    }
}