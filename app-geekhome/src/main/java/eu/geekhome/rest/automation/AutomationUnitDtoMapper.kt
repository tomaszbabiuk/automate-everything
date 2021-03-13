package eu.geekhome.rest.automation

import kotlin.Throws
import eu.geekhome.rest.MappingException
import eu.geekhome.automation.AutomationUnitWrapper
import javax.inject.Inject

class AutomationUnitDtoMapper @Inject constructor(
    private val evaluationResultDtoMapper: EvaluationResultDtoMapper
) {
    @Throws(MappingException::class)
    fun map(source: AutomationUnitWrapper<*>): AutomationUnitDto {
        return AutomationUnitDto(
            source.instance,
            source.state,
            source.error.toString(),
            evaluationResultDtoMapper.map(source.buildEvaluationResult())
        )
    }
}

