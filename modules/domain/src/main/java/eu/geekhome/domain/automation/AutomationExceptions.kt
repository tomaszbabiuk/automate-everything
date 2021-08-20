package eu.geekhome.domain.automation

import eu.geekhome.domain.R
import eu.geekhome.data.localization.Language
import eu.geekhome.data.localization.Resource
import java.lang.Exception

open class AutomationErrorException(val localizedMessage: Resource, cause: Throwable? = null) :
    Exception(localizedMessage.getValue(Language.EN), cause)

class PortNotFoundException(portId: String, cause: Throwable? = null) : AutomationErrorException(R.error_port_not_found(portId), cause)
