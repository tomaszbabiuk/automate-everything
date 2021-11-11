package eu.automateeverything.data.fields

open class Reference(
    val clazz: Class<*>,
)

class PortReference(
    clazz: Class<*>,
    val type: PortReferenceType
) : Reference(clazz)

class InstanceReference(
    clazz: Class<*>,
    val type: InstanceReferenceType
) : Reference(clazz)