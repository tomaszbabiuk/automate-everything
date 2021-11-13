package eu.automateeverything.alarmplugin

import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_ALARM
import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_ARMED
import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_DISARMED
import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_LEAVING
import eu.automateeverything.alarmplugin.AlarmZoneConfigurable.Companion.STATE_PREALARM
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.automation.DeviceAutomationUnit
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.StateDeviceAutomationUnitBase
import eu.automateeverything.domain.configurable.Duration
import eu.automateeverything.domain.configurable.StateDeviceConfigurable.Companion.STATE_UNKNOWN
import java.util.*

class AlarmZoneAutomationUnit(
    stateChangeReporter: StateChangeReporter,
    instance: InstanceDto,
    name: String,
    states: Map<String, State>,
    private val leavingTime: Duration,
    private val alarmLineIds: List<Long>,
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, states, false) {

    override fun applyNewState(state: String) {
        when (state) {
            STATE_DISARMED -> {
                disarmAlarmSensors()
            }
            STATE_ARMED -> {
                armAlarmSensors()
                _sensorThatCausedTheAlarm = null
            }
            STATE_LEAVING -> {
                _leavingStartedAtTicks = Calendar.getInstance().timeInMillis
            }
            STATE_PREALARM -> {
            }
            STATE_ALARM -> {
            }
        }
    }

    private lateinit var alarmLineUnits: List<AlarmLineAutomationUnit>

    private var _sensorThatCausedTheAlarm: AlarmLineAutomationUnit? = null
    private var _leavingStartedAtTicks: Long = Long.MAX_VALUE

    override val usedPortsIds: Array<String>
        get() = arrayOf()

    override val recalculateOnTimeChange = true
    override val recalculateOnPortUpdate = true

    override fun calculateInternal(now: Calendar) {
        if (currentState.id == STATE_UNKNOWN) {
            changeState(STATE_DISARMED)
            return
        }

        if (currentState.id == STATE_ARMED) {
            for (alarmSensorAutomationUnit in alarmLineUnits) {
                if (alarmSensorAutomationUnit.isAlarm()) {
                    _sensorThatCausedTheAlarm = alarmSensorAutomationUnit
                    changeState(STATE_ALARM)
                    return
                }
                if (alarmSensorAutomationUnit.isPrealarm()) {
                    changeState(STATE_PREALARM)
                    return
                }
            }
        }

        if (currentState.id === STATE_LEAVING) {
            if (now.timeInMillis > _leavingStartedAtTicks + leavingTime.milliseconds) {
                changeState(STATE_ARMED)
                return
            }
        }

        if (currentState.id === STATE_PREALARM) {
            for (alarmSensorAutomationUnit in alarmLineUnits) if (alarmSensorAutomationUnit.isAlarm()) {
                _sensorThatCausedTheAlarm = alarmSensorAutomationUnit
                changeState(STATE_ALARM)
                return
            }
        }
    }


    override fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, DeviceAutomationUnit<*>>>) {
        alarmLineUnits = alarmLineIds.map { automationUnitsCache[it]!!.second as AlarmLineAutomationUnit }
    }

    private fun armAlarmSensors() {
        alarmLineUnits.forEach { it.arm() }
    }

    private fun disarmAlarmSensors() {
        alarmLineUnits.forEach { it.disarm() }
    }
}
