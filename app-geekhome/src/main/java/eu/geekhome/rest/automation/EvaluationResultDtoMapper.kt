package eu.geekhome.rest.automation

import eu.geekhome.data.automation.EvaluationResultDto
import kotlin.Throws
import eu.geekhome.rest.MappingException
import eu.geekhome.domain.automation.EvaluationResult

class EvaluationResultDtoMapper {

    @Throws(MappingException::class)
    fun map(source: EvaluationResult<*>): EvaluationResultDto {
        return EvaluationResultDto(
            source.interfaceValue,
            source.isSignaled,
            source.descriptions,
            if (source.error != null) {
                source.error!!.toString()
            } else {
                null
            },
            source.nextStates
        )
    }
}

