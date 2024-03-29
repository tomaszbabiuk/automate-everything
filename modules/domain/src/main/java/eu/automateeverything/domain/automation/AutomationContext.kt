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

package eu.automateeverything.domain.automation

import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.events.EventBus

class AutomationContext(
    val thisInstance: InstanceDto,
    val thisDevice: Configurable?,
    val automationUnitsCache: Map<Long, AutomationUnit<*>>,
    val evaluationUnitsCache: Map<Long, EvaluableAutomationUnit>,
    val factoriesCache: List<BlockFactory<*, *, *>>,
    val eventBus: EventBus
)
