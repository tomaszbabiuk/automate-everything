import axios from 'axios'
import vuetify from './plugins/vuetify'
import store from './plugins/vuex'
import {SET_ERROR, SET_PLUGINS} from './plugins/vuex'

export const lang = vuetify.framework.lang

export const axiosInstance = axios.create({
  baseURL: 'http://localhost'
})

var requestInterceptor = function(config) {
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
  getPlugins: function() {
    return axiosInstance
      .get("/rest/plugins")
      .then(response => { store.commit(SET_PLUGINS, response.data)})
      .catch(() => {
        var errorData = {
          message: "$vuetify.rest.error.loading_plugins",
          actionTitle: lang.t("$vuetify.common.retry"),
          actionCallback: () => this.getPlugins()
        };
        store.commit(SET_ERROR, errorData)

      })
  }
}