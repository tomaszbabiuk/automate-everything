package eu.automateeverything.domain.automation

import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.data.instances.InstanceDto

class AutomationContext(
    val instance: InstanceDto,
    val thisDevice: Configurable?,
    val automationUnitsCache: Map<Long, AutomationUnit<*>>,
    val evaluationUnitsCache: Map<Long, EvaluableAutomationUnit>,
    val blocksCache: List<BlockFactory<*>>,
    val stateChangeReporter: StateChangeReporter
)