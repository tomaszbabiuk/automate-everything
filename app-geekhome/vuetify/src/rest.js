import axios from 'axios'
import vuetify from './plugins/vuetify'
import store from './plugins/vuex'
import { SET_ERROR, SET_PLUGINS, UPDATE_PLUGIN, SET_CONFIGURABLES, SET_INSTANCES } from './plugins/vuex'
import { CLEAR_TAGS, POST_TAG } from './plugins/vuex'

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
  getPlugins: function () {
    return axiosInstance
      .get("rest/plugins")
      .then(response => { store.commit(SET_PLUGINS, response.data) })
      .catch((innerException) => {
        var errorData = {
          message: "$vuetify.rest.error.getting_plugins",
          actionTitle: "$vuetify.common.retry",
          actionCallback: () => this.getPlugins(),
          innerException: innerException
        };

        store.commit(SET_ERROR, errorData)
      })
  },

  enablePlugin: function (pluginId, enable) {
    return axiosInstance
      .put("rest/plugins/" + pluginId + "/enabled", enable)
      .then(response => { store.commit(UPDATE_PLUGIN, response.data) })
      .catch((innerException) => {
        var errorData = {
          message: enable ? "$vuetify.rest.error.enabling_plugin" : "$vuetify.rest.error.disabling_plugin",
          actionTitle: "$vuetify.common.retry",
          actionCallback: () => this.enablePlugin(pluginId, enable),
          innerException: innerException
        };

        store.commit(SET_ERROR, errorData)
      })
  },

  postNewInstance: function (newInstance) {
    return axiosInstance
      .post("rest/instances", newInstance)
      .then(response => {
        console.log("Response from posting instance is" + response.data)
      })
      .catch((innerException) => {
        var errorData = {
          message: "$vuetify.rest.error.posting_instance",
          actionTitle: "$vuetify.common.retry",
          actionCallback: () => this.postNewInstance(newInstance),
          innerException: innerException
        };

        store.commit(SET_ERROR, errorData)
      })
  },

  getInstancesOfClazz: function (clazz) {
    return axiosInstance
      .get("rest/instances?class=" + clazz)
      .then(response => { store.commit(SET_INSTANCES, response.data) })
      .catch((innerException) => {
        var errorData = {
          message: "$vuetify.rest.error.getting_instances",
          actionTitle: "$vuetify.common.retry",
          actionCallback: () => this.getInstancesOfClazz(clazz),
          innerException: innerException
        };

        store.commit(SET_ERROR, errorData)
      })
  },

  getConfigurables: function () {
    return axiosInstance
      .get("rest/configurables")
      .then(response => { store.commit(SET_CONFIGURABLES, response.data) })
      .catch((innerException) => {
        var errorData = {
          message: "$vuetify.rest.error.getting_configurables",
          actionTitle: "$vuetify.common.retry",
          actionCallback: () => this.getConfigurables(),
          innerException: innerException
        };

        store.commit(SET_ERROR, errorData)
      })
  },

  getTags: function () {
    return axiosInstance
      .get("rest/tags")
      .then(response => {
        store.commit(CLEAR_TAGS)
        response.data.forEach(element => {
          store.commit(POST_TAG, element)
        })
      })
      .catch((innerException) => {
        var errorData = {
          message: "$vuetify.rest.error.getting_tags",
          actionTitle: "$vuetify.common.retry",
          actionCallback: () => this.getTags(),
          innerException: innerException
        };

        store.commit(SET_ERROR, errorData)
      })
  },

  postNewTag: function (newTag) {
    return axiosInstance
      .post("rest/tags", newTag)
      .then(response => {
        newTag.id = response.data
        store.commit(POST_TAG, newTag)
      })
      .catch((innerException) => {
        var errorData = {
          message: "$vuetify.rest.error.posting_tag",
          actionTitle: "$vuetify.common.retry",
          actionCallback: () => this.postNewTag(newTag),
          innerException: innerException
        };

        store.commit(SET_ERROR, errorData)
      })
  },

}