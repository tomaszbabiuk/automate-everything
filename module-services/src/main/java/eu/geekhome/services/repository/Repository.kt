package eu.geekhome.services.repository

import org.pf4j.ExtensionPoint

interface Repository : ExtensionPoint {
    fun saveInstance(instanceDto: InstanceDto)
}