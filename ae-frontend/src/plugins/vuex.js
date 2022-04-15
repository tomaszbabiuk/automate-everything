import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const SET_ERROR = 'SET_ERROR' 

export const SET_PLUGINS = 'SET_PLUGINS' 
export const UPDATE_PLUGIN = 'UPDATE_PLUGIN' 

export const SET_CONFIGURABLES= 'SET_CONFIGURABLES' 
export const CLEAR_INSTANCES = 'CLEAR_INSTANCES'
export const ADD_INSTANCE = 'ADD_INSTANCE'
export const UPDATE_INSTANCE = 'UPDATE_INSTANCE' 
export const EDIT_INSTANCE = 'EDIT_INSTANCE' 
export const RESET_INSTANCE = 'RESET_INSTANCE'
export const REMOVE_INSTANCE = 'REMOVE_INSTANCE' 
export const UPDATE_INSTANCE_FIELD = 'UPDATE_INSTANCE_FIELD'
export const UPDATE_INSTANCE_ICON = 'UPDATE_INSTANCE_ICON'
export const UPDATE_INSTANCE_ADD_TAG = 'UPDATE_INSTANCE_ADD_TAG'
export const UPDATE_INSTANCE_REMOVE_TAG = 'UPDATE_INSTANCE_REMOVE_TAG'
export const UPDATE_INSTANCE_AUTOMATION = 'UPDATE_INSTANCE_AUTOMATION'
export const SET_INSTANCES = 'SET_INSTANCES'
export const SET_INSTANCE_VALIDATION = 'SET_INSTANCE_VALIDATION'
export const CLEAR_INSTANCE_VALIDATION = 'CLEAR_INSTANCE_VALIDATION'

export const CLEAR_TAGS = 'CLEAR_TAGS'
export const ADD_TAG = 'ADD_TAG'
export const UPDATE_TAG = 'UPDATE_TAG'
export const REMOVE_TAG = 'REMOVE_TAG'

export const ADD_ICON = 'ADD_ICON'
export const UPDATE_ICON = 'UPDATE_ICON'
export const REMOVE_ICON = 'REMOVE_ICON'

export const CLEAR_ICON_CATEGORIES = 'CLEAR_ICON_CATEGORIES'
export const ADD_ICON_CATEGORY = 'ADD_ICON_CATEGORY'
export const UPDATE_ICON_CATEGORY = 'UPDATE_ICON_CATEGORY'
export const REMOVE_ICON_CATEGORY = 'REMOVE_ICON_CATEGORY'

export const CLEAR_DISCOVERY_EVENTS = 'CLEAR_DISCOVERY_EVENTS'
export const ADD_DISCOVERY_EVENT = 'ADD_DISCOVERY_EVENT'

export const CLEAR_HARDWARE_ADAPTERS = 'CLEAR_HARDWARE_ADAPTERS'
export const ADD_HARDWARE_ADAPTER = 'ADD_HARDWARE_ADAPTER'

export const CLEAR_PORTS = 'CLEAR_PORTS'
export const ADD_PORT = 'ADD_PORT'
export const UPDATE_PORT = 'UPDATE_PORT'
export const REMOVE_PORT = 'REMOVE_PORT'

export const UPDATE_AUTOMATION = 'UPDATE_AUTOMATION'
export const CLEAR_AUTOMATION_UNITS = 'CLEAR_AUTOMATION_UNITS'
export const ADD_AUTOMATION_UNIT = 'ADD_AUTOMATION_UNIT'
export const UPDATE_AUTOMATION_UNIT = 'UPDATE_AUTOMATION_UNIT'
export const UPDATE_DESCRIPTIONS = 'UPDATE_DESCRIPTIONS'

export const CLEAR_AUTOMATION_HISTORY = 'CLEAR_AUTOMATION_HISTORY'
export const ADD_AUTOMATION_HISTORY = 'ADD_AUTOMATION_HISTORY'

export const SET_SETTINGS = 'SET_SETTINGS'
export const SET_SETTINGS_VALIDATION = 'SET_SETTINGS_VALIDATION'
export const CLEAR_SETTINGS_VALIDATION = 'CLEAR_SETTINGS_VALIDATION'
export const UPDATE_SETTINGS_FIELD = 'UPDATE_SETTINGS_FIELD' 

export const CLEAR_INBOX_MESSAGES = 'CLEAR_INBOX_MESSAGES'
export const ADD_INBOX_MESSAGE = 'ADD_INBOX_MESSAGE'
export const PREPEND_INBOX_MESSAGE = 'PREPEND_INBOX_MESSAGE'
export const REMOVE_INBOX_MESSAGE = 'REMOVE_INBOX_MESSAGE'
export const UPDATE_INBOX_MESSAGE = 'UPDATE_INBOX_MESSAGE'
export const SET_INBOX_UNREAD_COUNT = 'SET_INBOX_UNREAD_COUNT'
export const SET_INBOX_TOTAL_COUNT = 'SET_INBOX_TOTAL_COUNT'

export const SET_DEPENDENCIES = 'SET_DEPENDENCIES'
export const CLEAR_DEPENDENCIES = 'CLEAR_DEPENDENCIES'

function mapTagDtoToTagVM(tagDto) {
  var result = JSON.parse(JSON.stringify(tagDto))
  if (!Object.prototype.hasOwnProperty.call(result, 'children')) {
    result['children'] = []
  }
  return result
}

function mapIconCategoryDtoToIconCategoryVM(tagDto) {
  var result = JSON.parse(JSON.stringify(tagDto))
  result.refreshCounter = 0
  return result
}


export default new Vuex.Store({
  state: {
    error: null,
    plugins: [],
    configurables: [],
    instances: [],
    instanceValidation: [],
    tags: [],
    iconCategories: [],
    discoveryEvents: [],
    automationHistory: [],
    hardwareAdapters: [],
    ports: [],
    filters: [],
    conditions: [],
    dependencies: [],
    automation: {
      enabled: false
    },
    automationUnits: [],
    newInstance: {
      id: null,
      clazz: null,
      fields: {},
      iconId: null,
      automation: null,
      tagIds: [],
    },
    counter: 0,
    settingsValidation: [],
    settings: [],
    inboxMessages: [],
    inboxUnreadCount: 0,
    inboxTotalCount: 0
  },

  mutations: {
    [SET_ERROR](state, error) {
      state.error = error
    },

    [SET_PLUGINS](state, plugins) {
      state.plugins = plugins
    },

    [UPDATE_PLUGIN](state, plugin) {
      state.plugins.forEach(element => {
        if (element.id === plugin.id) {
          element.enabled = plugin.enabled
        }
      })
    },

    [SET_CONFIGURABLES](state, configurables) {
      state.configurables = configurables
    },

    [ADD_INSTANCE](state, instance) {
      state.instances.push(instance)
    },

    [UPDATE_INSTANCE](state, instanceDto) {
      state.instances.forEach( instanceElement => {
        if (instanceElement.id === instanceDto.id) {
          Vue.set(instanceElement, "fields", instanceDto.fields)
        }
      })
    },

    [CLEAR_INSTANCES](state) {
      while (state.instances.length > 0) {
        Vue.delete(state.instances, 0)
      }
    },

    [RESET_INSTANCE](state, configurable) {
      state.newInstance.id = null
      state.newInstance.clazz = configurable.clazz
      state.newInstance.iconId = null
      state.newInstance.tagIds = []
      state.newInstance.automation = null

      configurable.fields.forEach(element => {
        Vue.set(state.newInstance.fields, element.name, element.initialValue)
      })
    },

    [EDIT_INSTANCE](state, instance) {
      state.newInstance.id = instance.id
      state.newInstance.clazz = instance.clazz
      state.newInstance.iconId = instance.iconId
      state.newInstance.tagIds = instance.tagIds
      state.newInstance.automation = instance.automation

      Object.keys(instance.fields).forEach(element => {
        Vue.set(state.newInstance.fields, element, instance.fields[element])
      })
    },

    [REMOVE_INSTANCE](state, instanceId) {
      state.instances.forEach( (element, i) => {
        if (element.id === instanceId) {
          Vue.delete(state.instances, i)
        }
      })
    },

    [UPDATE_INSTANCE_FIELD](state, payload) {
      /*
        payload should be { name: ..., value:... }
      */
      state.newInstance.fields[payload.name] = payload.value
    },

    [UPDATE_INSTANCE_ICON](state, iconId) {
      state.newInstance.iconId = iconId
    },

    [UPDATE_INSTANCE_ADD_TAG](state, tagId) {
      state.newInstance.tagIds.push(tagId)
    },

    [UPDATE_INSTANCE_REMOVE_TAG](state, tagId) {
      state.newInstance.tagIds.forEach( (iTagId, index) => {
        if (iTagId === tagId) {
          Vue.delete(state.newInstance.tagIds, index);
        }
      })
    },

    [UPDATE_INSTANCE_AUTOMATION](state, xml) {
      state.newInstance.automation = xml
    },

    [SET_INSTANCES](state, instances) {
      state.instances = instances
    },

    [SET_INSTANCE_VALIDATION](state, validationData) {
      state.instanceValidation = validationData
    },

    [CLEAR_INSTANCE_VALIDATION](state) {
      state.instanceValidation = []
    },

    [CLEAR_TAGS](state) {
      state.tags = []
    },

    [ADD_TAG](state, tagDto) {
      if (tagDto.parentId === null) {
        state.tags.push(mapTagDtoToTagVM(tagDto))
      } else {
        state.tags.forEach(element => {
          if (element.id === tagDto.parentId) {
            element.children.push(mapTagDtoToTagVM(tagDto))
          }
        })
      }
    },

    [UPDATE_TAG](state, tagDto) {
      state.tags.forEach( parentLevelTag => {
        if (parentLevelTag.id === tagDto.id) {
          parentLevelTag.name = tagDto.name
          parentLevelTag.parentId = tagDto.parentId
        }

        parentLevelTag.children.forEach( childLevelTag => {
          if (childLevelTag.id === tagDto.id) {
            childLevelTag.name = tagDto.name
            childLevelTag.parentId = tagDto.parentId
          }
        })
      })
    },

    [REMOVE_TAG](state, id) {
      state.tags.forEach( (parentLevelTag, parentIndex) => {
        if (parentLevelTag.id === id) {
          Vue.delete(state.tags, parentIndex)
        } else {
          parentLevelTag.children.forEach((childLevelTag, childIndex) => {
            if (childLevelTag.id === id) {
              Vue.delete(parentLevelTag.children, childIndex);
            }
          })
        }
      })
    },

    [CLEAR_ICON_CATEGORIES](state) {
      state.iconCategories = []
    },

    [ADD_ICON_CATEGORY](state, iconCategoryDto) {
      var iconCategoryVM = mapIconCategoryDtoToIconCategoryVM(iconCategoryDto)
      state.iconCategories.push(iconCategoryVM)
    },

    [UPDATE_ICON_CATEGORY](state, iconCategoryDto) {
      state.iconCategories.forEach( element => {
        if (element.id === iconCategoryDto.id) {
          element.name = iconCategoryDto.name
        }
      })
    },

    [REMOVE_ICON_CATEGORY](state, id) {
      state.iconCategories.forEach( (element, i) => {
        if (element.id === id) {
          Vue.delete(state.iconCategories, i)
        }
      })
    },

    [ADD_ICON](state, iconDto) {
      state.iconCategories.forEach( (element) => {
        if (element.id === iconDto.iconCategoryId) {
          element.iconIds.push(iconDto.id)
        }
      })
    },

    [UPDATE_ICON](state, iconDto) {
      state.iconCategories.forEach( element => {
        if (element.id === iconDto.iconCategoryId) {
          element.refreshCounter++
        }
      })
    },

    [REMOVE_ICON](state, id) {
      state.iconCategories.forEach( (categoryElement) => {
        categoryElement.iconIds.forEach((elementId, i) => {
          if (elementId === id) {
            Vue.delete(categoryElement.iconIds, i)
          }
        })
      })
    },

    [CLEAR_DISCOVERY_EVENTS](state) {
      state.discoveryEvents = []
    },

    [ADD_DISCOVERY_EVENT](state, payload) {
      var alreadyAdded = false;

      state.discoveryEvents.forEach ( element => {
        if (element.no === payload.no) {
          alreadyAdded = true
        }
      })

      if (!alreadyAdded) {
        state.discoveryEvents.push(payload)
      }
    },

    [CLEAR_AUTOMATION_HISTORY](state) {
      state.automationHistory = []
    },

    [ADD_AUTOMATION_HISTORY](state, payload) {
      var alreadyAdded = false;

      state.automationHistory.forEach ( element => {
        if (element.no === payload.no) {
          alreadyAdded = true
        }
      })

      if (!alreadyAdded) {
        state.automationHistory.splice(0, 0, payload)
      }
    },

    [CLEAR_HARDWARE_ADAPTERS](state) {
      state.hardwareAdapters = []
    },

    [ADD_HARDWARE_ADAPTER](state, payload) {
      state.hardwareAdapters.push(payload)
    },

    [CLEAR_PORTS](state) {
      state.ports = []
    },

    [ADD_PORT](state, portDto) {
      state.ports.push(portDto)
    },

    [REMOVE_PORT](state, portId) {
      state.ports.forEach( (element, i) => {
        if (element.id === portId) {
          Vue.delete(state.ports, i)
        }
      })
    },

    [UPDATE_PORT](state, portDto) {
      var portFound = false;
      state.ports.forEach(element => {
        if (element.id === portDto.id) {
          element.decimalValue = portDto.decimalValue
          element.interfaceValue = portDto.interfaceValue
          element.lastSeenTimestamp = portDto.lastSeenTimestamp
          portFound = true
        }
      })

      if (!portFound) {
        state.ports.push(portDto)
      }
    },

    [UPDATE_AUTOMATION](state, automationDto) {
      state.automation = automationDto
    },

    [CLEAR_AUTOMATION_UNITS](state) {
      state.automationUnits = []
    },

    [ADD_AUTOMATION_UNIT](state, automationUnitDto) {
      automationUnitDto.show = false
      state.automationUnits.push(automationUnitDto)
    },

    [UPDATE_DESCRIPTIONS](state, descriptionsUpdateDto) {
      state.automationUnits.forEach( element => {
        if (element.instance.id === descriptionsUpdateDto.instanceId) {
          element.evaluationResult.descriptions = descriptionsUpdateDto.descriptions
        }
      })
    },

    [UPDATE_AUTOMATION_UNIT](state, automationUnitDto) {
      state.automationUnits.forEach( element => {
        if (element.instance.id === automationUnitDto.instance.id) {
          element.instance = automationUnitDto.instance
          element.evaluationResult = automationUnitDto.evaluationResult
        }
      })
    },

    [SET_SETTINGS_VALIDATION](state, validationData) {
      state.settingsValidation = validationData
    },

    [CLEAR_SETTINGS_VALIDATION](state) {
      state.settingsValidation = []
    },

    [UPDATE_SETTINGS_FIELD](state, payload) {
      var settingsGroup = state.settings[payload.clazz]
      if (settingsGroup != null) {
        settingsGroup[payload.name] = payload.value
      } else {
        var newSetting = {}
        newSetting[payload.name] = payload.value
        state.settings[payload.clazz] = newSetting
      }
    },

    [SET_SETTINGS](state, payload) {
      state.settings = payload
    },

    [CLEAR_INBOX_MESSAGES](state) {
      state.inboxMessages = []
    },

    [ADD_INBOX_MESSAGE](state, inboxMessageDto) {
      state.inboxMessages.push(inboxMessageDto)
    },

    [PREPEND_INBOX_MESSAGE](state, inboxMessageDto) {
      state.inboxMessages.unshift(inboxMessageDto)
    },

    [REMOVE_INBOX_MESSAGE](state, messageId) {
      state.inboxMessages.forEach( (element, i) => {
        if (element.id === messageId) {
          if (!element.read) {
            state.inboxUnreadCount--;
          }
          Vue.delete(state.inboxMessages, i)
        }
      })

      state.inboxTotalCount--
    },

    [UPDATE_INBOX_MESSAGE](state, message) {
      state.inboxMessages.forEach(element => {
        if (element.id === message.id) {
          element.read = message.read
        }
      })
    },

    [SET_INBOX_UNREAD_COUNT](state, count) {
      state.inboxUnreadCount = count
    },

    [SET_INBOX_TOTAL_COUNT](state, count) {
      state.inboxTotalCount = count
    },

    [SET_DEPENDENCIES](state, dependencies) {
      state.dependencies = dependencies
    },

    [CLEAR_DEPENDENCIES](state) {
      while (state.dependencies.length > 0) {
        Vue.delete(state.dependencies, 0)
      }
    },
  }
})