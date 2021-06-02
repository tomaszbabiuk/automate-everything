import axios from 'axios'
import vuetify from './plugins/vuetify'
import store, { UPDATE_AUTOMATION_UNIT } from './plugins/vuex'
import { SET_ERROR, SET_PLUGINS, UPDATE_PLUGIN } from './plugins/vuex'
import { SET_CONFIGURABLES, SET_CONDITIONS, SET_FILTERS } from './plugins/vuex'
import { SET_INSTANCES, SET_INSTANCE_VALIDATION, REMOVE_INSTANCE } from './plugins/vuex'
import { CLEAR_TAGS, ADD_TAG, UPDATE_TAG, REMOVE_TAG } from './plugins/vuex'
import { CLEAR_ICON_CATEGORIES, ADD_ICON_CATEGORY, UPDATE_ICON_CATEGORY, REMOVE_ICON_CATEGORY } from './plugins/vuex'
import { ADD_ICON, UPDATE_ICON, REMOVE_ICON } from './plugins/vuex'
import { CLEAR_DISCOVERY_EVENTS, ADD_DISCOVERY_EVENT } from './plugins/vuex'
import { CLEAR_HARDWARE_ADAPTERS, ADD_HARDWARE_ADAPTER } from './plugins/vuex'
import { CLEAR_PORTS, ADD_PORT } from './plugins/vuex'
import { UPDATE_AUTOMATION, CLEAR_AUTOMATION_UNITS, ADD_AUTOMATION_UNIT } from './plugins/vuex'
import { ADD_AUTOMATION_HISTORY } from './plugins/vuex'

export const lang = vuetify.framework.lang

export const axiosInstance = axios.create({
  baseURL: 'http://localhost/'
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

  postNewInstance: async function (newInstance, callback) {
    await this.handleRestError(
      () => axiosInstance.post("rest/instances", JSON.stringify(newInstance)),
      (response) => {
        store.commit(SET_INSTANCE_VALIDATION, response.data)
        if (callback !== null) {
          callback(response.data)
        }
      }
    )
  },

  putInstance: async function (newInstance, callback) {
    await this.handleRestError(
      () => axiosInstance.put("rest/instances", JSON.stringify(newInstance)),
      (response) => {
        store.commit(SET_INSTANCE_VALIDATION, response.data)
        if (callback !== null) {
          callback(response.data)
        }
      }
    )
  },

  getInstancesOfClazz: async function (clazz) {
    await this.handleRestError(
      () => axiosInstance.get("rest/instances?class=" + clazz),
      (response) => store.commit(SET_INSTANCES, response.data)
    )
  },

  deleteInstance: async function (id) {
    await this.handleRestError(
      () => axiosInstance.delete("rest/instances/" + id),
      () => store.commit(REMOVE_INSTANCE, id)
    )
  },

  getConfigurables: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/configurables"),
      (response) => store.commit(SET_CONFIGURABLES, response.data)
    )
  },

  getFilters: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/instancebriefs/Filter"),
      (response) => store.commit(SET_FILTERS, response.data)
    )
  },

  getConditions: async function () {
    await this.handleRestError(
      () => axiosInstance.get("rest/instancebriefs/Condition"),
      (response) => store.commit(SET_CONDITIONS, response.data)
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
      () => axiosInstance.get("rest/icons/"+id +"/raw"),
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

  controlPort: async function (portId, controlValue) {
    await this.handleRestError(
      () => axiosInstance.put("rest/ports/" + portId + "/value", JSON.stringify(controlValue)),
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
      () => axiosInstance.put("rest/automationunits/" + instanceId + "/state", state),
      (response) => {
        store.commit(UPDATE_AUTOMATION_UNIT, response.data)
      }
    )
  },
}