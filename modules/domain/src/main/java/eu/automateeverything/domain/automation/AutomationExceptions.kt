package eu.automateeverything.domain.automation

import eu.automateeverything.domain.R
import eu.automateeverything.data.localization.Language
import eu.automateeverything.data.localization.Resource
import java.lang.Exception

open class AutomationErrorException(val localizedMessage: Resource, cause: Throwable? = null) :
    Exception(localizedMessage.getValue(Language.EN), cause)

class PortNotFoundException(portId: String, cause: Throwable? = null) : AutomationErrorException(R.error_port_not_found(portId), cause)
