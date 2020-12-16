import axios from 'axios'
import vuetify from './plugins/vuetify'
import store from './plugins/vuex'
import { SET_ERROR, SET_PLUGINS, UPDATE_PLUGIN, SET_CONFIGURABLES, SET_INSTANCES } from './plugins/vuex'
import { CLEAR_TAGS, ADD_TAG, UPDATE_TAG, REMOVE_TAG } from './plugins/vuex'

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
      (response) => store.commit(UPDATE_PLUGIN, response.data)
    )
  },

  postNewInstance: async function (newInstance) {
    await this.handleRestError(
      () => axiosInstance.post("rest/instances", JSON.stringify(newInstance))
    )
  },

  getInstancesOfClazz: async function (clazz) {
    await this.handleRestError(
      () => axiosInstance.get("rest/instances?class=" + clazz),
      (response) => store.commit(SET_INSTANCES, response.data)
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
}