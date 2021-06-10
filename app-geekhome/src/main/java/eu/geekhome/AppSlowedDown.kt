package eu.geekhome

import eu.geekhome.rest.*

class AppSlowedDown : App() {
    init {
        register(SlowDownInterceptor())
    }
}