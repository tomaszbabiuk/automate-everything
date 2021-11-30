import axios from 'axios'
import vuetify from './plugins/vuetify'
import store, { 
  UPDATE_AUTOMATION_UNIT,
  SET_ERROR, SET_PLUGINS, UPDATE_PLUGIN,
  SET_CONFIGURABLES,
  SET_INSTANCES, SET_INSTANCE_VALIDATION, REMOVE_INSTANCE,
  CLEAR_TAGS, ADD_TAG, UPDATE_TAG, REMOVE_TAG,
  CLEAR_ICON_CATEGORIES, ADD_ICON_CATEGORY, UPDATE_ICON_CATEGORY, REMOVE_ICON_CATEGORY,
  ADD_ICON, UPDATE_ICON, REMOVE_ICON,
  CLEAR_DISCOVERY_EVENTS, ADD_DISCOVERY_EVENT,
  CLEAR_HARDWARE_ADAPTERS, ADD_HARDWARE_ADAPTER,
  CLEAR_PORTS, ADD_PORT, REMOVE_PORT,
  UPDATE_AUTOMATION, CLEAR_AUTOMATION_UNITS, ADD_AUTOMATION_UNIT,
  ADD_AUTOMATION_HISTORY,
  SET_SETTINGS, SET_SETTINGS_VALIDATION,
  CLEAR_INBOX_MESSAGES, ADD_INBOX_MESSAGE, REMOVE_INBOX_MESSAGE, UPDATE_INBOX_MESSAGE, SET_INBOX_TOTAL_COUNT,
  SET_DEPENDENCIES
} from './plugins/vuex'

export const lang = vuetify.framework.lang

export const axiosInstance = axios.create({
  baseURL: '/'
})

var requestInterceptor = function (config) {
  config.headers['Accept-Language'] = lang.current
  return config
}

var requestErrorHandler = function (error) {
  return Promise.reject(error)
}

var responseInterceptor = function (response) {
  store.commit(SET_ERROR, null)
  return response;
}

var responseErrorHandler = function (error) {
  return Promise.reject(error);
}

axiosInstance.interceptors.request.use(requestInterceptor, requestErrorHandler)
axiosInstance.interceptors.response.use(responseInterceptor, responseErrorHandler);

export const client = {
  handleRestError: async function(axiosFunction, postResponseFunction) {
    try {
      const response = await axiosFunction()
      if (postResponseFunction !== null) {
        postResponseFunction(response)
      }
    } catch (innerException) {
      console.log(innerException)
      var errorData = {
        message: "$vuetify.rest.error",
        actionTitle: "$vuetify.common.retry",
        actionCallback: () => this.handleRestError(axiosFunction, postResponseFunction),
        innerException: innerException
      }

      store.commit(SET_ERROR, errorData)
    }
  },

  getPlugins: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/plugins"),
      (response) => store.commit(SET_PLUGINS, response.data)
    )
  },

  enablePlugin: async function (pluginId, enable) {
    await this.handleRestError(
      () => axiosInstance.put("rest/plugins/" + pluginId + "/enabled", JSON.stringify(enable)),
      (response) => {
        store.commit(UPDATE_PLUGIN, response.data)
      }
    )
  },

  setInstanceValidation: function(response, callback) {
    store.commit(SET_INSTANCE_VALIDATION, response.data)
    if (callback !== null) {
      callback(response.data)
    }
  },
  
  postNewInstance: async function (newInstance, callback) {
    await this.handleRestError(
      () => axiosInstance.post("rest/instances", JSON.stringify(newInstance)),
      (response) => this.setInstanceValidation(response, callback)
    )
  },

  putInstance: async function (updatedInstance, callback) {
    await this.handleRestError(
      () => axiosInstance.put("rest/instances", JSON.stringify(updatedInstance)),
      (response) => this.setInstanceValidation(response, callback)
    )
  },

  getInstancesOfClazz: async function (clazz) {
    await this.handleRestError(
      () => axiosInstance.get("rest/instances?class=" + clazz),
      (response) => store.commit(SET_INSTANCES, response.data)
    )
  },

  getAllInstances: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/instances"),
      (response) => store.commit(SET_INSTANCES, response.data)
    )
  },

  deleteInstance: async function (instanceIdToDelete) {
    await this.handleRestError(
      () => axiosInstance.delete("rest/instances/" + instanceIdToDelete),
      response => {
        response.data.forEach(removedInstanceId => {
          store.commit(REMOVE_INSTANCE, removedInstanceId)
        })
      }
    )
  },

  getConfigurables: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/configurables"),
      (response) => store.commit(SET_CONFIGURABLES, response.data)
    )
  },

  getTags: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/tags"),
      (response) => {
        store.commit(CLEAR_TAGS)
        response.data.forEach(element => {
          store.commit(ADD_TAG, element)
        })
      }
    )
  },

  postTag: async function(newTag) {
    await this.handleRestError(
      () => axiosInstance.post("rest/tags", JSON.stringify(newTag)),
      (response) => {
        newTag.id = response.data
        store.commit(ADD_TAG, newTag)
      }
    )
  },

  putTag: async function (newTag) {
    await this.handleRestError(
      () => axiosInstance.put("rest/tags", JSON.stringify(newTag)),
      () => store.commit(UPDATE_TAG, newTag)
    )
  },

  deleteTag: async function (id) {
    await this.handleRestError(
      () => axiosInstance.delete("rest/tags/" + id),
      () => store.commit(REMOVE_TAG, id)
    )
  },

  getIconCategories: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/iconcategories"),
      (response) => {
        store.commit(CLEAR_ICON_CATEGORIES)
        response.data.forEach(element => {
          store.commit(ADD_ICON_CATEGORY, element)
        })
      }
    )
  },

  postIconCategory: async function(iconCategoryDto) {
    await this.handleRestError(
      () => axiosInstance.post("rest/iconcategories", JSON.stringify(iconCategoryDto)),
      (response) => {
        iconCategoryDto.id = response.data
        store.commit(ADD_ICON_CATEGORY, iconCategoryDto)
      }
    )
  },

  putIconCategory: async function (iconCategoryDto) {
    await this.handleRestError(
      () => axiosInstance.put("rest/iconcategories", JSON.stringify(iconCategoryDto)),
      () => store.commit(UPDATE_ICON_CATEGORY, iconCategoryDto)
    )
  },

  deleteIconCategory: async function (id) {
    await this.handleRestError(
      () => axiosInstance.delete("rest/iconcategories/" + id),
      () => store.commit(REMOVE_ICON_CATEGORY, id)
    )
  },

  postIcon: async function(iconDto) {
    await this.handleRestError(
      () => axiosInstance.post("rest/icons", JSON.stringify(iconDto)),
      (response) => {
        iconDto.id = response.data
        store.commit(ADD_ICON, iconDto)
      }
    )
  },

  putIcon: async function (iconDto) {
    await this.handleRestError(
      () => axiosInstance.put("rest/icons", JSON.stringify(iconDto)),
      () => store.commit(UPDATE_ICON, iconDto)
    )
  },

  getIconRawWithCallback: async function(id, callback) {
    await this.handleRestError(
      () => axiosInstance.get("rest/icons/"+ id +"/raw"),
      (response) => callback(response.data)
    )
  },

  deleteIcon: async function (id) {
    await this.handleRestError(
      () => axiosInstance.delete("rest/icons/" + id),
      () => store.commit(REMOVE_ICON, id)
    )
  },

  getDiscoveryEvents: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/adapterevents"),
      (response) => {
        response.data.forEach(element => {
          store.commit(ADD_DISCOVERY_EVENT, element)
        })
      }
    )
  },

  clearDiscoveryEvents: function() {
    store.commit(CLEAR_DISCOVERY_EVENTS)
  },

  getAutomationHistory: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/automationhistory"),
      (response) => {
        response.data.forEach(element => {
          store.commit(ADD_AUTOMATION_HISTORY, element)
        })
      }
    )
  },

  getHardwareAdapters: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/hardwareadapters"),
      (response) => {
        store.commit(CLEAR_HARDWARE_ADAPTERS)
        response.data.forEach(element => {
          store.commit(ADD_HARDWARE_ADAPTER, element)
        })
      }
    )
  },

  discoverHardwareAdapters: async function(factoryId) {
    await this.handleRestError(
      () => axiosInstance.put("rest/hardwareadapters/" + factoryId + "/discover"),
      (response) => {
        console.log(response.data)
      }
    )
  },

  getPorts: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/ports"),
      (response) => {
        store.commit(CLEAR_PORTS)
        response.data.forEach(element => {
          store.commit(ADD_PORT, element)
        })
      }
    )
  },

  deletePort: async function (id) {
    await this.handleRestError(
      () => axiosInstance.delete("rest/ports/" + encodeURIComponent(id)),
      () => store.commit(REMOVE_PORT, id)
    )
  },


  controlPort: async function (portId, controlValue) {
    await this.handleRestError(
      () => axiosInstance.put("rest/ports/" + encodeURIComponent(portId) + "/value", JSON.stringify(controlValue)),
      (response) => {
        console.log(response.data)
      }
    )
  },

  getBlocklyToolboxWithCallback: async function(configurableClazz, callback) {
    await this.handleRestError(
      () => axiosInstance.get("rest/blocks/" + configurableClazz),
      (response) => callback(response.data)
    )
  },

  getAutomation: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/automation"),
      (response) => {
        store.commit(UPDATE_AUTOMATION, response.data)
      }
    )
  },

  getAutomationUnits: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/automationunits"),
      (response) => {
        store.commit(CLEAR_AUTOMATION_UNITS)
        response.data.forEach(element => {
          store.commit(ADD_AUTOMATION_UNIT, element)
        })
      }
    )
  },

  enableAutomation: async function (enable) {
    await this.handleRestError(
      () => axiosInstance.put("rest/automation/enabled", JSON.stringify(enable)),
      (response) => {
        store.commit(UPDATE_AUTOMATION, response.data)
      }
    )
  },

  changeState: async function (instanceId, state) {
    await this.handleRestError(
      () => axiosInstance.put("rest/automationunits/" + instanceId + "/state", JSON.stringify(state)),
      (response) => {
        store.commit(UPDATE_AUTOMATION_UNIT, response.data)
      }
    )
  },

  control: async function (instanceId, level) {
    await this.handleRestError(
      () => axiosInstance.put("rest/automationunits/" + instanceId + "/control", JSON.stringify(level)),
      (response) => {
        store.commit(UPDATE_AUTOMATION_UNIT, response.data)
      }
    )
  },

  getSettings: async function (pluginId) {
    await this.handleRestError(
      () => axiosInstance.get("rest/settings/" + pluginId),
      (response) => store.commit(SET_SETTINGS, response.data)
    )
  },
  
  putSettings: async function (pluginId, updatedSettings, callback) {
    await this.handleRestError(
      () => axiosInstance.put("rest/settings/" + pluginId, JSON.stringify(updatedSettings)),
      (response) => {
          store.commit(SET_SETTINGS_VALIDATION, response.data)
          if (callback !== null) {
            callback(response.data)
          }
      }
    )
  },

  getInboxMessages: async function (clear, limit, offset) {
    await this.handleRestError(
      () => axiosInstance.get("rest/inbox?limit=" + limit +"&offset=" + offset),
      (response) => {
        if (clear) {
          store.commit(CLEAR_INBOX_MESSAGES)
        }
        
        var totalCount = response.headers['x-total-count'];
        store.commit(SET_INBOX_TOTAL_COUNT, totalCount)

        response.data.forEach(element => {
          store.commit(ADD_INBOX_MESSAGE, element)
        })
      }
    )
  },

  deleteInboxMessage: async function (id) {
    await this.handleRestError(
      () => axiosInstance.delete("rest/inbox/" + id),
      () => store.commit(REMOVE_INBOX_MESSAGE, id)
    )
  },

  markInboxMessageAsRead: async function (messageId) {
    await this.handleRestError(
      () => axiosInstance.put("rest/inbox/" + messageId + "/read", true),
      (response) => {
        store.commit(UPDATE_INBOX_MESSAGE, response.data)
      }
    )
  },

  getDependencies: async function (instanceId) {
    await this.handleRestError(
      () => axiosInstance.get("rest/dependencies/" + instanceId),
      (response) => store.commit(SET_DEPENDENCIES, response.data)
    )
  },
}