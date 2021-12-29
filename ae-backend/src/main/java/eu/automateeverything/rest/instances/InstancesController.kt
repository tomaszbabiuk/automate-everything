/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.rest.instances

import eu.automateeverything.data.Repository
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.rest.settings.ValidationResultMap
import eu.automateeverything.domain.configurable.ConfigurableWithFields
import eu.automateeverything.domain.configurable.FieldValidationResult
import eu.automateeverything.domain.dependencies.DependencyChecker
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import kotlin.collections.HashMap

@Path("instances")
class InstancesController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository,
    private val dependencyChecker: DependencyChecker
) {

    private fun findConfigurable(clazz: String): ConfigurableWithFields? {
        return pluginsCoordinator
            .configurables
            .filter { x -> x.javaClass.name.equals(clazz) }
            .filterIsInstance<ConfigurableWithFields>()
            .firstOrNull()
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Throws(Exception::class)
    fun postInstances(instanceDto: InstanceDto): ValidationResultMap {
        return validate(instanceDto) {
            repository.saveInstance(instanceDto)
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Throws(Exception::class)
    fun putInstances(instanceDto: InstanceDto): ValidationResultMap {
        return validate(instanceDto) {
            repository.updateInstance(instanceDto)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getInstancesById(@PathParam("id") id: Long): InstanceDto {
        return repository.getInstance(id)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getInstances(@QueryParam("class") clazz: String?): List<InstanceDto> {
        return if (clazz != null) {
            repository.getInstancesOfClazz(clazz)
        } else {
            return repository.getAllInstances()
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteInstance(@PathParam("id") id: Long): List<Long> {
        val dependencies = dependencyChecker.checkInstance(id)
        return if (dependencies.size > 0) {
            val toRemove = dependencies.values.map { it.id } + listOf(id)
            repository.deleteInstances(toRemove)
            toRemove
        } else {
            repository.deleteInstance(id)
            listOf(id)
        }
    }

    private fun validate(instanceDto: InstanceDto, onValidCallback: () -> Unit):
            MutableMap<String, FieldValidationResult> {
        val configurable = findConfigurable(instanceDto.clazz)
        return if (configurable != null) {
            val validationResult: MutableMap<String, FieldValidationResult> = HashMap()
            var isObjectValid = true
            for (fieldDefinition in configurable.fieldDefinitions.values) {
                val fieldValue = instanceDto.fields[fieldDefinition.name]
                val isValid = fieldDefinition.validate(fieldValue, instanceDto.fields)
                validationResult[fieldDefinition.name] = isValid
                if (!isValid.valid) {
                    isObjectValid = false
                }
            }
            if (isObjectValid) {
                onValidCallback()
            }
            validationResult
        } else {
            throw Exception("Unsupported configurable class")
        }
    }
}
