package eu.geekhome.rest.automation

import kotlin.Throws
import eu.geekhome.rest.MappingException
import eu.geekhome.services.automation.EvaluationResult

class EvaluationResultDtoMapper {

    @Throws(MappingException::class)
    fun map(source: EvaluationResult): EvaluationResultDto {
        return EvaluationResultDto(
            source.interfaceValue,
            source.controlMode,
            source.isSignaled,
            source.descriptions
        )
    }
}

