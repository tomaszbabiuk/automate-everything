import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export const SET_ERROR = 'SET_ERROR' 
export const SET_PLUGINS = 'SET_PLUGINS' 
export const SET_CONFIGURABLES= 'SET_CONFIGURABLES' 
export const UPDATE_PLUGIN = 'UPDATE_PLUGIN' 
export const NEW_INSTANCE = 'NEW_INSTANCE' 
export const RESET_INSTANCE = 'RESET_INSTANCE'
export const REMOVE_INSTANCE = 'REMOVE_INSTANCE' 
export const UPDATE_INSTANCE_FIELD = 'UPDATE_INSTANCE_FIELD' 
export const UPDATE_INSTANCE_ICON = 'UPDATE_INSTANCE_ICON' 
export const UPDATE_INSTANCE_ADD_TAG = 'UPDATE_INSTANCE_ADD_TAG' 
export const UPDATE_INSTANCE_REMOVE_TAG = 'UPDATE_INSTANCE_REMOVE_TAG' 
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
    validation: [],
    tags: [],
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
        id: null,
        class: configurable.class,
        fields: {},
        iconId: null,
        tagIds: [],
        parentId: null
      }

      state.newInstance = newInstance
    },

    [RESET_INSTANCE](state) {
      state.newInstance = null
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
    }
  }
})