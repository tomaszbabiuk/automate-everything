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

export const CLEAR_TAGS = 'CLEAR_TAGS'
export const ADD_TAG = 'ADD_TAG'
export const UPDATE_TAG = 'UPDATE_TAG'
export const REMOVE_TAG = 'REMOVE_TAG'

export const CLEAR_ICONS = 'CLEAR_ICONS'
export const ADD_ICON = 'ADD_ICON'
export const UPDATE_ICON = 'UPDATE_ICON'
export const REMOVE_ICON = 'REMOVE_ICON'

export const CLEAR_ICON_CATEGORIES = 'CLEAR_ICON_CATEGORIES'
export const ADD_ICON_CATEGORY = 'ADD_ICON_CATEGORY'
export const UPDATE_ICON_CATEGORY = 'UPDATE_ICON_CATEGORY'
export const REMOVE_ICON_CATEGORY = 'REMOVE_ICON_CATEGORY'

function mapTagDtoToTagVM(tagDto) {
  var result = JSON.parse(JSON.stringify(tagDto))
  if (!Object.prototype.hasOwnProperty.call(result, 'children')) {
    result['children'] = []
  }
  return result
}

function mapIconCategoryDtoToIconCategoryVM(iconCategoryDto) {
  var result = JSON.parse(JSON.stringify(iconCategoryDto))
  if (!Object.prototype.hasOwnProperty.call(result, 'icons')) {
    result['icons'] = []
  }
  return result
}

export default new Vuex.Store({
  state: {
    error: null,
    plugins: [],
    configurables: [],
    instances: {},
    tags: [],
    icons: [],
    iconCategories: [],
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
      state.iconCategories.push(mapIconCategoryDtoToIconCategoryVM(iconCategoryDto))
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

    [CLEAR_ICONS](state) {
      state.icons = []
    },

    [ADD_ICON](state, iconDto) {
      state.icons.push(iconDto)
    },

    [UPDATE_ICON](state, iconDto) {
      state.icons.forEach( element => {
        if (element.id === iconDto.id) {
          element.raw = iconDto.raw
          element.iconCategoryId = iconDto.iconCategoryId
        }
      })
    },

    [REMOVE_ICON](state, id) {
      state.icons.forEach( (element, i) => {
        if (element.id === id) {
          Vue.delete(state.icons, i)
        }
      })
    }
  }
})