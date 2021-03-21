package eu.geekhome

import eu.geekhome.services.events.EventsSink
import eu.geekhome.services.events.NumberedEventsSink

class LiveEventsBroadcaster {
    val sink = NumberedEventsSink<String>()
}