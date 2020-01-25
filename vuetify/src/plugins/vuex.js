import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const SET_ERROR = 'SET_ERROR' 
export const SET_PLUGINS = 'SET_PLUGINS' 
export const UPDATE_PLUGIN = 'UPDATE_PLUGIN' 

export default new Vuex.Store({
  state: {
    error: null,
    plugins: null,
    counter: 0
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
          console.log("We have you")
          element.enabled = plugin.enabled
        }
      });
    }
  }
})