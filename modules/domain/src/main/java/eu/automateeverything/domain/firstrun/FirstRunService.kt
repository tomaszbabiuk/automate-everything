package eu.automateeverything.domain.firstrun


interface FirstRunService {
    fun isFirstRun() : Boolean
    fun markFirstRunHappened()
}