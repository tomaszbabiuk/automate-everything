package eu.geekhome.rest

import org.jvnet.hk2.annotations.Service
import java.lang.Exception
import javax.ws.rs.core.Application

@Service
open class HolderService<T: Any>(app: Application, type: Class<T>) {
    lateinit var instance: T

    init {
        for (singleton in app.singletons) {
            if (type.isAssignableFrom(singleton.javaClass)) {

                @Suppress("UNCHECKED_CAST")
                instance = singleton as T
                break
            }
        }

        @Suppress("SENSELESS_COMPARISON")
        if (instance == null) {
            throw Exception("There's no singleton of type " + type.name + " in the app singletons container")
        }
    }
}