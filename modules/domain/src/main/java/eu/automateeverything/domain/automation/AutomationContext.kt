package eu.automateeverything.domain.automation

import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.data.instances.InstanceDto

class AutomationContext(
    val instanceDto: InstanceDto,
    val thisDevice: Configurable?,
    val automationUnitsCache: Map<Long, DeviceAutomationUnit<*>>,
    val evaluationUnitsCache: Map<Long, IEvaluableAutomationUnit>,
    val blocksCache: List<BlockFactory<*>>,
    val stateChangeReporter: StateChangeReporter
)