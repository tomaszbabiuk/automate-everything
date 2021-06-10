package eu.geekhome

import eu.geekhome.rest.*
import org.glassfish.jersey.server.ResourceConfig

class AppSlowedDown : App() {
    init {
        register(SlowDownInterceptor())
    }
}