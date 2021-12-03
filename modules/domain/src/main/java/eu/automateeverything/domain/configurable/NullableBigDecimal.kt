package eu.automateeverything.domain.configurable

import java.math.BigDecimal

open class NullableWrapper<V>(val wrapped: V?)

class NullableBigDecimal(wrapped: BigDecimal?) : NullableWrapper<BigDecimal>(wrapped)