package eu.automateeverything.rest.automation

import eu.automateeverything.data.automation.EvaluationResultDto
import eu.automateeverything.data.hardware.PortValue
import kotlin.Throws
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.automation.EvaluationResult
import java.math.BigDecimal

class EvaluationResultDtoMapper {

    @Throws(MappingException::class)
    fun map(source: EvaluationResult<*>): EvaluationResultDto {
        return EvaluationResultDto(
            source.interfaceValue,
            extractBigDecimalValueIfPossible(source),
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

    private fun extractBigDecimalValueIfPossible(source: EvaluationResult<*>): BigDecimal? {
        if (source.value != null && source.value is PortValue) {
            return (source.value as PortValue).asDecimal()
        }

        return null
    }
}
