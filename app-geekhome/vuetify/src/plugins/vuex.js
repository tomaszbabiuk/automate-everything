import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const SET_ERROR = 'SET_ERROR' 
export const SET_PLUGINS = 'SET_PLUGINS' 
export const SET_CONFIGURABLES= 'SET_CONFIGURABLES' 
export const UPDATE_PLUGIN = 'UPDATE_PLUGIN' 
export const NEW_INSTANCE = 'NEW_INSTANCE' 
export const RESET_INSTANCE = 'RESET_INSTANCE' 
export const UPDATE_INSTANCE = 'UPDATE_INSTANCE' 
export const SET_INSTANCES = 'SET_INSTANCES'

export const POST_TAG = 'POST_TAG'

export default new Vuex.Store({
  state: {
    error: null,
    plugins: [],
    configurables: [],
    instances: {},
    tags: [],
    newInstance: null,
    counter: 0,
  },
  mutations: {
    [SET_ERROR](state, error) {
      state.error = error
    },

    [SET_PLUGINS](state, plugins) {
      state.plugins = plugins
    },

    [SET_CONFIGURABLES](state, configurables) {
      state.configurables = configurables
    },

    [UPDATE_PLUGIN](state, plugin) {
      state.plugins.forEach(element => {
        if (element.id === plugin.id) {
          element.enabled = plugin.enabled
        }
      })
    },

    [NEW_INSTANCE](state, configurable) {
      var newInstance = {
        class : configurable.class,
        fields : {},
        parentId: null
      }

      state.newInstance = newInstance
    },

    [RESET_INSTANCE](state) {
      state.newInstance = null
    },

    [UPDATE_INSTANCE](state, payload) {
      /*
        payload should be { name: ..., value:... }
      */
      state.newInstance.fields[payload.name] = payload.value
    },

    [SET_INSTANCES](state, instances) {
      state.instances = instances
    },

    [POST_TAG](state, tagVM) {
      if (tagVM.parentId === null) {
        state.tags.push(tagVM)
      } else {
        state.tags.forEach(element => {
          if (element.id === tagVM.parentId) {

            if (!Object.prototype.hasOwnProperty.call(element, 'children')) {
              element['children'] = []
            }

            element.children.push(tagVM)
          }
        })

      }
    }
  }
})