package eu.automateeverything.data.automation

import java.math.BigDecimal

data class ValueRangeDto(
    val min: BigDecimal,
    val max: BigDecimal,
    val step: BigDecimal
)
