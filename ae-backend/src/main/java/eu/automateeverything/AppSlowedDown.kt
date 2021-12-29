package eu.automateeverything

import eu.automateeverything.rest.*

class AppSlowedDown : App() {
    init {
        register(SlowDownInterceptor())
    }
}