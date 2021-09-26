package eu.automateeverything.domain.extensibility

class InjectionRegistry {
    private val objects = HashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T> resolve(type: Class<T>): T? {
        return objects[type] as T
    }

    fun <T> put(type: Class<*>, obj: T) {
        objects[type] = obj as Any
    }
}