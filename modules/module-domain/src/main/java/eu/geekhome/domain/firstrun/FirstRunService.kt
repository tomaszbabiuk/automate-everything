package eu.geekhome.domain.firstrun


interface FirstRunService {
    fun isFirstRun() : Boolean
    fun markFirstRunHappened()
}