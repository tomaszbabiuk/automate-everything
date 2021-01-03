package com.geekhome.common.configuration;

import java.util.List;

public class MasterDependenciesCheckerModule extends DependenciesCheckerModule {
    private MasterConfiguration _masterConfiguration;

    public MasterDependenciesCheckerModule(MasterConfiguration masterConfiguration) {
        super(masterConfiguration);
        _masterConfiguration = masterConfiguration;
    }

    @Override
    public void checkDependency(Object source, List<Dependency> dependencies, int level) {
        if (source instanceof IBlocksTarget) {
            level++;

            IBlocksTarget blocksTarget = (IBlocksTarget) source;
            for (Block block : _masterConfiguration.findBlocksByTarget(blocksTarget.getName().getUniqueId()).values()) {
                addDependency(dependencies, DependencyType.Block, block, level);
            }

        }

        if (source instanceof Floor) {
            level++;
            Floor floor = (Floor) source;
            for (Room room : floor.getRooms().values()) {
                addDependency(dependencies, DependencyType.Room, room, level);
            }
        }

        if (source instanceof Room) {
            level++;
            Room room = (Room) source;
            for (CollectorCollection collection : _masterConfiguration.getDevicesCollectors()) {
                for (Object current : collection.values()) {
                    if (current instanceof IRoomDevice) {
                        IRoomDevice device = (IRoomDevice) current;
                        if (device.getRoomId().equals(room.getName().getUniqueId())) {
                            addDependency(dependencies, DependencyType.Device, device, level);
                        }
                    }
                }
            }
        }

        if (source instanceof IDevice) {
            level++;
            IDevice device = (IDevice) source;
            if (device instanceof MultistateDevice) {
                for (MultistateCondition condition : _masterConfiguration.getMultistateConditions().values()) {
                    if (condition.getDeviceId().equals(device.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Condition, condition, level);
                    }
                }

                for (ChangeStateCommand changeStateCommand : _masterConfiguration.getChangeStateCommands().values()) {
                    if (changeStateCommand.getDeviceId().equals(device.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Command, changeStateCommand, level);
                    }
                }
            }

            if (device instanceof IValueDevice) {
                for (ValueCondition valueCondition : _masterConfiguration.getValueConditions().values()) {
                    if (valueCondition.getDeviceId().equals(device.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Condition, valueCondition, level);
                    }
                }

                for (DeltaCondition valueCondition : _masterConfiguration.getDeltaConditions().values()) {
                    if (valueCondition.getFirstDeviceId().equals(device.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Condition, valueCondition, level);
                    }
                    if (valueCondition.getSecondDeviceId().equals(device.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Condition, valueCondition, level);
                    }
                }
            }

            for (ReadValueCommand readValueCommand : _masterConfiguration.getReadValueCommands().values()) {
                for (String deviceId : readValueCommand.getDevicesIds().split(",")) {
                    if (deviceId.equals(device.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Command, readValueCommand, level);
                    }
                }
            }
        }

        if (source instanceof ICondition) {
            level++;
            ICondition condition = (ICondition) source;

            for (Block block : _masterConfiguration.getBlocks().values()) {
                for (String conditionId : block.getUniqueConditionsIds()) {
                    if (conditionId.equals(condition.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Block, block, level);
                    }
                }
            }

            for (GroupCondition groupCondition : _masterConfiguration.getGroupConditions().values()) {
                for (String conditionId : groupCondition.getConditionsIds().split(",")) {
                    if (conditionId.equals(condition.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Condition, groupCondition, level);
                    }
                }
            }
        }

        if (source instanceof Geofence) {
            level++;
            Geofence geofence = (Geofence) source;
            for (GeofenceCondition geofenceCondition : _masterConfiguration.getGeofenceConditions().values()) {
                for (String conditionId : geofenceCondition.getGeofencesIds().split(",")) {
                    if (conditionId.equals(geofence.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Condition, geofenceCondition, level);
                    }
                }
            }
        }
    }
}
