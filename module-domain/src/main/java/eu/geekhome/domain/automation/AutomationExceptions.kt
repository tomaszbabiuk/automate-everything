package eu.geekhome.domain.automation

import eu.geekhome.domain.localization.Language
import eu.geekhome.domain.localization.Resource
import java.lang.Exception

class AutomationErrorException(val localizedMessage: Resource, cause: Throwable) :
    Exception(localizedMessage.getValue(Language.EN), cause)