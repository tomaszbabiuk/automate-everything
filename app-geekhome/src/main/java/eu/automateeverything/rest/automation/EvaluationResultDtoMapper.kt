package eu.automateeverything.rest.automation

import eu.automateeverything.data.automation.EvaluationResultDto
import kotlin.Throws
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.automation.EvaluationResult

class EvaluationResultDtoMapper {

    @Throws(MappingException::class)
    fun map(source: EvaluationResult<*>): EvaluationResultDto {
        return EvaluationResultDto(
            source.interfaceValue,
            source.isSignaled,
            source.descriptions.flatMap { it.split(System.lineSeparator()) },
            if (source.error != null) {
                source.error!!.toString()
            } else {
                null
            },
            source.nextStates
        )
    }
}

