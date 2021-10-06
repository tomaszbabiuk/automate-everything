package eu.automateeverything.data.localization

class LocalizedException(val localizedMessage: Resource) :
    Exception(localizedMessage.getValue(Language.EN))