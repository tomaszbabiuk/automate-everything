package eu.geekhome.aforeplugin

import eu.geekhome.services.hardware.ConnectiblePort
import eu.geekhome.services.hardware.Wattage
import java.util.*

class AforeWattagePort(id: String, portOperator: AforeWattageReadPortOperator) :
    ConnectiblePort<Wattage>(id, Wattage::class.java, portOperator) {

    init {
        prolongValidity(Calendar.getInstance())
    }

    private fun prolongValidity(now: Calendar) {
        connectionValidUntil = now.timeInMillis + 1000 * 60 * 5 //now + 5 minutes
    }

    suspend fun refresh(now: Calendar): Boolean {
        try {
            val aforeOperator = readPortOperator as AforeWattageReadPortOperator
            val result = aforeOperator.refresh()
            prolongValidity(now)
            return result
        } catch (ex: Exception) {
            cancelValidity()
        }

        return false
    }
}