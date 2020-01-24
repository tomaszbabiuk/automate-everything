import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const SET_ERROR = 'SET_ERROR' 
export const SET_PLUGINS = 'SET_PLUGINS' 

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
    }
  }
})