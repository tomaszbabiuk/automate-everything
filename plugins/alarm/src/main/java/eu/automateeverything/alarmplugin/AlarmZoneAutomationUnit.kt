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
    private val combinationLockIds: List<Long>
) : StateDeviceAutomationUnitBase(stateChangeReporter, instance, name, states, false) {

    override fun applyNewState(state: String) {
        when (state) {
            STATE_DISARMED -> {
                changeLeavingStateOfCodeLocks(false)
                disarmCodeLocks()
                disarmAlarmSensors()
            }
            STATE_ARMED -> {
                changeLeavingStateOfCodeLocks(false)
                armAlarmSensors()
                armCodeLocks()
                _sensorThatCausedTheAlarm = null
            }
            STATE_LEAVING -> {
                changeLeavingStateOfCodeLocks(true)
                armCodeLocks()
            }
            STATE_PREALARM -> {
                changeLeavingStateOfCodeLocks(false)
            }
            STATE_ALARM -> {
                changeLeavingStateOfCodeLocks(false)
            }
        }
    }

    private lateinit var combinationLockUnits: List<CombinationLockAutomationUnit>
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

        if (currentState.id !== STATE_DISARMED) {
            if (checkIfAnyCodeLockIsInState(false)) {
                changeState(STATE_DISARMED)
                return
            }
        }

        if (currentState.id === STATE_ARMED) {
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

        if (currentState.id === STATE_DISARMED) {
            if (checkIfAnyCodeLockIsInState(true)) {
                _leavingStartedAtTicks = now.timeInMillis
                changeState(STATE_LEAVING)
                return
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

    @Throws(java.lang.Exception::class)
    private fun changeLeavingStateOfCodeLocks(leaving: Boolean) {
        for (codeLock in combinationLockUnits) {
            codeLock.leaving = leaving
        }
    }

    override fun bind(automationUnitsCache: HashMap<Long, Pair<InstanceDto, DeviceAutomationUnit<*>>>) {
        alarmLineUnits = alarmLineIds.map { automationUnitsCache[it]!!.second as AlarmLineAutomationUnit }
        combinationLockUnits = combinationLockIds.map { automationUnitsCache[it]!!.second as CombinationLockAutomationUnit }
    }

    private fun checkIfAnyCodeLockIsInState(isArmed: Boolean): Boolean {
        var result = false
        for (codeLockAutomationUnit in combinationLockUnits) {
            if (codeLockAutomationUnit.isArmed == isArmed) {
                result = true
                break
            }
        }
        return result
    }

    @Throws(Exception::class)
    private fun armCodeLocks() {
        combinationLockUnits.forEach { it.arm() }
    }

    @Throws(Exception::class)
    private fun disarmCodeLocks() {
        combinationLockUnits.forEach { it.disarm() }
    }

    @Throws(Exception::class)
    private fun armAlarmSensors() {
        alarmLineUnits.forEach { it.arm() }
    }

    @Throws(Exception::class)
    private fun disarmAlarmSensors() {
        alarmLineUnits.forEach { it.disarm() }
    }
}
