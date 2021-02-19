package eu.geekhome.rest.blocks

import eu.geekhome.rest.PluginsCoordinator
import eu.geekhome.rest.RawJson
import eu.geekhome.rest.ResourceNotFoundException
import eu.geekhome.services.automation.State
import eu.geekhome.services.automation.StateType
import eu.geekhome.services.configurable.ConditionConfigurable
import eu.geekhome.services.configurable.StateDeviceConfigurable
import eu.geekhome.services.localization.Resource
import eu.geekhome.services.repository.InstanceBriefDto
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("blocks")
class BlocksController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator
) {
    @Throws(ResourceNotFoundException::class)
    @GET
    @Path("/{configurableClazz}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getToolbox(@PathParam("configurableClazz") configurableClazz : String): BlocklyToolboxWithBlocksDto {
        try {
            val configurable = pluginsCoordinator.configurables.first {
                it.javaClass.name.equals(configurableClazz)
            }

            val blockImplementations = ArrayList<RawJson>()
            val automationTriggersBlockDefinitions = ArrayList<BLocklyToolboxItemBlockDto>()
            val logicBlockDefinitions = ArrayList<BLocklyToolboxItemBlockDto>()
            val thisDeviceBlockDefinitions = ArrayList<BLocklyToolboxItemBlockDto>()
            val conditionsBlockDefinitions = ArrayList<BLocklyToolboxItemBlockDto>()

            //building "triggers" blocks
            addTimeloopTriggerBlock(automationTriggersBlockDefinitions, blockImplementations)

            //building "logic"
            addIfThanElseBlock(logicBlockDefinitions, blockImplementations)

            //building "this device" blocks
            if (configurable is StateDeviceConfigurable) {
                configurable
                    .states
                    .filter { it.value.type != StateType.ReadOnly }
                    .forEach {
                        addChangeStateBlock(it.value, thisDeviceBlockDefinitions, blockImplementations)
                    }
            }

            //building "conditions" blocks
            pluginsCoordinator
                .repository
                .getAllInstanceBriefs()
                .forEach { briefDto ->
                    addConditionBlock(briefDto, conditionsBlockDefinitions, blockImplementations)
                }

            val toolbox = BlocklyToolboxDto(
                contents = listOf(
                    BlocklyToolboxItemCategoryDto(name = R.category_name_triggers, contents = automationTriggersBlockDefinitions),
                    BlocklyToolboxItemCategoryDto(name = R.category_name_logic, contents = logicBlockDefinitions),
                    BlocklyToolboxItemCategoryDto(name = R.category_name_conditions, contents = conditionsBlockDefinitions),
                    BlocklyToolboxItemCategoryDto(name = R.category_name_this_device, contents = thisDeviceBlockDefinitions),
                )
            )

            return BlocklyToolboxWithBlocksDto(toolbox, blockImplementations)
        } catch (ex: NoSuchElementException) {
            throw ResourceNotFoundException()
        }
    }

    private fun addConditionBlock(
        briefDto: InstanceBriefDto,
        definitions: ArrayList<BLocklyToolboxItemBlockDto>,
        blocks: ArrayList<RawJson>
    ) {
        val instanceClass = briefDto.clazz
        val instanceConfigurable = pluginsCoordinator
            .configurables
            .firstOrNull {
                it.javaClass.name.equals(instanceClass)
            }

        if (instanceConfigurable is ConditionConfigurable) {
            val conditionInstances =
                pluginsCoordinator.repository.getInstancesOfClazz(instanceConfigurable.javaClass.name)
            conditionInstances.forEach {
                val type = "condition${it.id}"
                val blockDef = BLocklyToolboxItemBlockDto(type = type)
                definitions.add(blockDef)

                val blockImpl = BlockFactory.buildConditionStateBlock(
                    type = type,
                    label = Resource.createUniResource(it.fields["name"]!!)
                )
                blocks.add(blockImpl)
            }
        }
    }

    private fun addChangeStateBlock(
        state: State,
        definitions: ArrayList<BLocklyToolboxItemBlockDto>,
        blocks: ArrayList<RawJson>
    ) {
        val type = "change_state_${state.name.id}"
        val blockDef = BLocklyToolboxItemBlockDto(type = type)
        definitions.add(blockDef)

        val rawJson = BlockFactory.buildChangeStateBlock(type, state.name)
        blocks.add(rawJson)
    }

    private fun addTimeloopTriggerBlock(
        definitions: ArrayList<BLocklyToolboxItemBlockDto>,
        blocks: ArrayList<RawJson>
    ) {
        val type = "trigger_timeloop"
        val blockDef = BLocklyToolboxItemBlockDto(type = type)
        definitions.add(blockDef)

        val rawJson = BlockFactory.buildRepeatStateBlock(type)
        blocks.add(rawJson)
    }

    private fun addIfThanElseBlock(
        definitions: ArrayList<BLocklyToolboxItemBlockDto>,
        blocks: ArrayList<RawJson>
    ) {
        val type = "logic_if_than_else"
        val blockDef = BLocklyToolboxItemBlockDto(type = type)
        definitions.add(blockDef)

        val rawJson = BlockFactory.buildIfThanElseBlock(type)
        blocks.add(rawJson)
    }
}

