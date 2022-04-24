/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
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

package eu.automateeverything.rest

import eu.automateeverything.data.Repository
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.hardware.HardwareManager
import eu.automateeverything.domain.automation.AutomationConductor
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.inbox.Inbox
import jakarta.inject.Singleton
import eu.automateeverything.domain.automation.BlocklyParser
import eu.automateeverything.domain.dependencies.DependencyChecker
import eu.automateeverything.mappers.*
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.AbstractBinder

class DependencyInjectionBinder(
    private val eventBus: EventBus,
    private val repository: Repository,
    private val pluginsCoordinator: PluginsCoordinator,
    private val hardwareManager: HardwareManager,
    private val automationConductor: AutomationConductor,
    private val blockFactoriesCollector: BlockFactoriesCollector,
    private val inbox: Inbox
) : AbstractBinder() {

    override fun configure() {
        //mappers
        bind(FieldDefinitionDtoMapper::class.java)
            .to(FieldDefinitionDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(ConfigurableDtoMapper::class.java)
            .to(ConfigurableDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(PluginDtoMapper::class.java)
            .to(PluginDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(HardwareAdapterDtoMapper::class.java)
            .to(HardwareAdapterDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(AutomationUnitDtoMapper::class.java)
            .to(AutomationUnitDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(EvaluationResultDtoMapper::class.java)
            .to(EvaluationResultDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(PortDtoMapper::class.java)
            .to(PortDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(DiscoveryEventMapper::class.java)
            .to(DiscoveryEventMapper::class.java)
            .`in`(Singleton::class.java)

        bind(AutomationHistoryDtoMapper::class.java)
            .to(AutomationHistoryDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(SettingGroupDtoMapper::class.java)
            .to(SettingGroupDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(HeartbeatDtoMapper::class.java)
            .to(HeartbeatDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(InboxMessageDtoMapper::class.java)
            .to(InboxMessageDtoMapper::class.java)
            .`in`(Singleton::class.java)

        bind(LiveEventsMapper::class.java)
            .to(LiveEventsMapper::class.java)
            .`in`(Singleton::class.java)

        bind(BlocklyParser::class.java)
            .to(BlocklyParser::class.java)
            .`in`(Singleton::class.java)

        bind(DependencyChecker::class.java)
            .to(DependencyChecker::class.java)
            .`in`(Singleton::class.java)

        bind(DescriptionsUpdateDtoMapper::class.java)
            .to(DescriptionsUpdateDtoMapper::class.java)
            .`in`(Singleton::class.java)

        //factories for objects shared with the App
        bindFactory(SingletonFactory(hardwareManager))
            .to(HardwareManager::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(automationConductor))
            .to(AutomationConductor::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(pluginsCoordinator))
            .to(PluginsCoordinator::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(repository))
            .to(Repository::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(eventBus))
            .to(EventBus::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(blockFactoriesCollector))
            .to(BlockFactoriesCollector::class.java)
            .`in`(Singleton::class.java)

        bindFactory(SingletonFactory(inbox))
            .to(Inbox::class.java)
            .`in`(Singleton::class.java)

    }

    internal class SingletonFactory<T>(private val instance: T) : Factory<T> {
        override fun provide(): T {
            return instance
        }

        override fun dispose(instance: T) {}
    }
}