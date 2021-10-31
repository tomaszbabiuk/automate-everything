package eu.automateeverything.domain.automation

import eu.automateeverything.data.automation.NextStatesDto
import eu.automateeverything.data.localization.Resource

class EvaluationResult<T> (
    val interfaceValue: Resource,
    val value: T? = null,
    val isSignaled: Boolean = false,
    val descriptions: List<Resource> = ArrayList(),
    val error: Exception? = null,
    val nextStates: NextStatesDto? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EvaluationResult<*>

        if (interfaceValue != other.interfaceValue) return false
        if (value != other.value) return false
        if (isSignaled != other.isSignaled) return false
        if (descriptions != other.descriptions) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = interfaceValue.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + isSignaled.hashCode()
        result = 31 * result + descriptions.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}