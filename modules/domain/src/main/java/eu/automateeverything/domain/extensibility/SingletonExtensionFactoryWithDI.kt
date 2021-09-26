package eu.automateeverything.domain.extensibility

import org.pf4j.DefaultExtensionFactory
import java.util.*

@Suppress("UNCHECKED_CAST")
class SingletonExtensionFactoryWithDI(private val injectionRegistry: InjectionRegistry) : DefaultExtensionFactory() {

    private val cache: MutableMap<ClassLoader, MutableMap<String, Any>> = WeakHashMap()

    override fun <T : Any> create(extensionClass: Class<T>): T {
        val extensionClassName = extensionClass.name
        val extensionClassLoader = extensionClass.classLoader

        if (!cache.containsKey(extensionClassLoader)) {
            cache[extensionClassLoader] = HashMap<String, Any>()
        }

        val classLoaderBucket = cache[extensionClassLoader]!!

        if (classLoaderBucket.containsKey(extensionClassName)) {
            return (classLoaderBucket[extensionClassName] as T)
        }

        val extension = createWithDI(extensionClass)
        classLoaderBucket[extensionClassName] = extension

        return extension
    }

    private fun <T> createWithDI(extensionClass: Class<T>): T {
        val constructors = extensionClass.constructors
        if (constructors.size == 1) {
            val primaryConstructor = constructors[0]
            val injectionParameters = primaryConstructor
                .parameters
                .map {
                    val constructedType = it.type
                    val obj = injectionRegistry.resolve(constructedType)
                        ?: throw CreationException("Cannot inject ${extensionClass.name}, the constructor contains parameter of unknown type $constructedType")
                    obj
                }.toTypedArray()

            val created = primaryConstructor.newInstance(*injectionParameters)
            return created as T
        }

        //no DI
        return super.create(extensionClass)
    }
}